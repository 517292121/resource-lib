package cn.com.adtech.riss2.fileservice;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.adtech.riss2.wsClient.FileConvertServicePortType;

/** In-process replacement for the old XFire service. No second Tomcat or SOAP loopback. */
public class LocalFileConvertService implements FileConvertServicePortType, AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(LocalFileConvertService.class);
    // Printer tools are not reentrant. A bounded, managed worker also limits media-conversion load.
    private final ThreadPoolExecutor worker = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(200), runnable -> {
                Thread thread = new Thread(runnable, "MCProject-file-converter");
                thread.setDaemon(true);
                return thread;
            });

    @Override
    public void documentConvert(String input, String output) {
        submit("document", input, output, "");
    }

    @Override
    public void audioConvert(String input, String output) {
        submit("audio", input, output, "");
    }

    @Override
    public void videoConvert(String input, String output, String time) {
        submit("video", input, output, time);
    }

    @Override
    public void photoConvertFromVideo(String input, String output, String time) {
        submit("photo", input, output, time);
    }

    private void submit(String kind, String input, String output, String time) {
        try {
            final Path source = FileServiceSettings.resolve(FileServiceSettings.root(), input);
            final Path target = FileServiceSettings.resolve(FileServiceSettings.root(), output);
            if (source.equals(target)) {
                return; // Already mp3/flv/swf; never overwrite an input with itself.
            }
            if (!Files.isRegularFile(source)) {
                LOG.warn("File conversion skipped: input does not exist: {}", source);
                return;
            }
            final List<String> command = command(kind, source, target, time);
            if (command == null) {
                return;
            }
            worker.execute(() -> {
                try {
                    convert(command, target);
                    LOG.info("File conversion completed: {}", target);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOG.warn("File conversion interrupted: {}", source);
                } catch (Exception e) {
                    LOG.error("File conversion failed: " + source, e);
                }
            });
        } catch (IOException | RejectedExecutionException e) {
            // As with the original async service, a failed preview must not roll back the uploaded resource.
            LOG.error("Cannot queue file conversion: " + input, e);
        }
    }

    private List<String> command(String kind, Path source, Path target, String time) {
        List<String> args = new ArrayList<String>();
        if ("document".equals(kind)) {
            String tool = FileServiceSettings.property("file.service.document-tool",
                    FileServiceSettings.property("convertTool", "print2flash"));
            String key = "flashpaper".equalsIgnoreCase(tool)
                    ? "file.service.flashpaper" : "file.service.print2flash";
            String executable = FileServiceSettings.property(key, "print2flash".equalsIgnoreCase(tool)
                    ? FileServiceSettings.property("p2fServerPath", "C:/Print2Flash3/p2fServer.exe") : "");
            if (!available(executable, key)) {
                return null;
            }
            args.add(executable);
            args.add(source.toString());
            if ("flashpaper".equalsIgnoreCase(tool)) {
                args.add("-o");
            }
            args.add(target.toString());
        } else {
            String executable = FileServiceSettings.property("file.service.ffmpeg", "file-tools/ffmpeg/ffmpeg.exe");
            if (!available(executable, "file.service.ffmpeg")) {
                return null;
            }
            args.addAll(Arrays.asList(executable, "-y", "-i", source.toString()));
            if ("audio".equals(kind)) {
                args.addAll(Arrays.asList("-vn", "-acodec", FileServiceSettings.property("file.service.mp3-codec", "mp3")));
            } else if ("photo".equals(kind)) {
                if (time != null && !time.trim().isEmpty()) {
                    args.addAll(Arrays.asList("-ss", time.trim()));
                }
                args.addAll(Arrays.asList("-vframes", "1", "-an", "-s", "100x120", "-f", "image2"));
            } else {
                if (time != null && !time.trim().isEmpty()) {
                    args.addAll(Arrays.asList("-t", time.trim()));
                }
                args.addAll(Arrays.asList("-vcodec", "flv", "-acodec", FileServiceSettings.property("file.service.mp3-codec", "mp3"), "-ar", "22050"));
            }
            args.add(target.toString());
        }
        return args;
    }

    private boolean available(String executable, String key) {
        if (!executable.isEmpty() && new File(executable).isFile()) {
            return true;
        }
        LOG.warn("Preview conversion skipped: {} is unavailable ({}). Upload and original-file reading remain available.",
                key, executable);
        return false;
    }

    private void convert(List<String> args, Path target) throws IOException, InterruptedException {
        Files.createDirectories(target.getParent());
        String name = target.getFileName().toString();
        String suffix = name.lastIndexOf('.') >= 0 ? name.substring(name.lastIndexOf('.')) : ".tmp";
        Path pending = Files.createTempFile(target.getParent(), ".converting-", suffix);
        Path log = Files.createTempFile("MCProject-convert-", ".log");
        Process process = null;
        try {
            List<String> command = new ArrayList<String>(args);
            command.set(command.size() - 1, pending.toString());
            int seconds = Math.max(1, Integer.parseInt(FileServiceSettings.property("file.service.timeout-seconds", "600")));
            process = new ProcessBuilder(command).redirectErrorStream(true).redirectOutput(log.toFile()).start();
            if (!process.waitFor(seconds, TimeUnit.SECONDS)) {
                throw new IOException("Conversion timed out after " + seconds + " seconds");
            }
            if (process.exitValue() != 0) {
                throw new IOException("Converter exited with " + process.exitValue() + "; log: " + log);
            }
            if (!Files.isRegularFile(pending) || Files.size(pending) == 0) {
                throw new IOException("Converter did not produce an output; log: " + log);
            }
            Files.move(pending, target, StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(log);
        } finally {
            if (process != null && process.isAlive()) {
                process.destroyForcibly();
                process.waitFor(5, TimeUnit.SECONDS);
            }
            Files.deleteIfExists(pending);
        }
    }

    @Override
    public void photoConvertFromDocument(String input, String output) {
        // The caller's boolean means the cover really exists, so PDF rendering is synchronous.
        try {
            Path source = FileServiceSettings.resolve(FileServiceSettings.root(), input);
            Path target = FileServiceSettings.resolve(FileServiceSettings.root(), output);
            if (source.equals(target)) {
                throw new IllegalArgumentException("Cover cannot overwrite its source");
            }
            PDDocument pdf = PDDocument.load(source.toFile());
            try {
                if (pdf.getNumberOfPages() == 0) {
                    throw new IOException("PDF has no pages");
                }
                BufferedImage page = ((PDPage) pdf.getDocumentCatalog().getAllPages().get(0))
                        .convertToImage(BufferedImage.TYPE_INT_RGB, 96);
                BufferedImage cover = new BufferedImage(100, 120, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = cover.createGraphics();
                try {
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(0, 0, 100, 120);
                    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    double scale = Math.min(100.0 / page.getWidth(), 120.0 / page.getHeight());
                    int width = Math.max(1, (int) (page.getWidth() * scale));
                    int height = Math.max(1, (int) (page.getHeight() * scale));
                    graphics.drawImage(page, (100 - width) / 2, (120 - height) / 2, width, height, null);
                } finally {
                    graphics.dispose();
                }
                Files.createDirectories(target.getParent());
                String name = target.getFileName().toString().toLowerCase(Locale.ROOT);
                String format = name.endsWith(".png") ? "png" : "jpeg";
                Path pending = Files.createTempFile(target.getParent(), ".cover-", "." + format);
                try {
                    if (!ImageIO.write(cover, format, pending.toFile())) {
                        throw new IOException("No cover image writer");
                    }
                    Files.move(pending, target, StandardCopyOption.REPLACE_EXISTING);
                } finally {
                    Files.deleteIfExists(pending);
                }
            } finally {
                pdf.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create PDF cover: " + input, e);
        }
    }

    @Override
    public void close() {
        int discarded = worker.shutdownNow().size();
        if (discarded > 0) {
            LOG.warn("Discarded {} pending file-conversion tasks during shutdown", discarded);
        }
    }
}
