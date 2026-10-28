package cn.com.adtech.riss2.upload;

import cn.com.adtech.riss2.core.file.ResourceDirectoryFiles;
import net.sf.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** Upload directories to the configured local resource root without replacing existing books. */
public final class ResourceDirectoryUpload {
    private static final int MAX_FILES = 10000;
    private final Path resourceRoot;
    private final long sourceLimit;
    private final long coverLimit;
    private long written;
    private int count;

    public ResourceDirectoryUpload(File resourceRoot, long sourceLimit, long coverLimit) throws IOException {
        this.resourceRoot = resourceRoot.getCanonicalFile().toPath();
        this.sourceLimit = sourceLimit;
        this.coverLimit = coverLimit;
    }

    public File save(MultipartHttpServletRequest request) throws IOException {
        written = 0;
        count = 0;
        String mode = request.getParameter("directoryMode");
        MultipartFile cover = request.getFile("directoryCover");
        validateCover(cover);
        if (mode == null || mode.isEmpty() || "server".equals(mode)) {
            String path = request.getParameter("imgDir");
            if (path == null || path.trim().isEmpty()) throw new IOException("请输入服务器资源目录，或选择 ZIP / 本地文件夹上传。");
            File existing = new File(path.trim()).getCanonicalFile();
            if (!existing.isDirectory()) throw new IOException("服务器资源目录不存在。");
            validateContents(existing);
            // Existing legacy paths can still be read; uploaded covers must stay in the resource root.
            if (present(cover)) {
                if (!existing.toPath().startsWith(resourceRoot)) throw new IOException("上传封面的资源目录必须位于系统资源根目录下。");
                saveCover(cover, existing.toPath());
            }
            return existing;
        }
        if (!"zip".equals(mode) && !"folder".equals(mode)) throw new IOException("资源目录上传方式无效。");
        Files.createDirectories(resourceRoot);
        Path staging = Files.createTempDirectory(resourceRoot, ".upload-");
        Path container = resourceRoot.resolve("book-" + UUID.randomUUID().toString());
        boolean committed = false;
        try {
            if ("zip".equals(mode)) extract(request.getFile("resourceArchive"), staging);
            else copyFolder(request, staging);
            Path book = unwrap(staging);
            validateContents(book.toFile());
            if (present(cover)) saveCover(cover, book);
            String name = book.getFileName().toString();
            if (book.equals(staging) && "zip".equals(mode)) {
                String archiveName = request.getFile("resourceArchive").getOriginalFilename();
                name = archiveName.replace('\\', '/');
                name = name.substring(name.lastIndexOf('/') + 1, name.length() - 4);
            } else if (book.equals(staging)) name = "resource";
            Path destination = safeTarget(container, name);
            Files.createDirectory(container);
            Files.move(book, destination);
            committed = true;
            return destination.toFile();
        } finally {
            // This path was created by this upload, never a user-selected directory.
            removeStaging(staging);
            if (!committed && Files.exists(container)) removeStaging(container);
        }
    }

    private void extract(MultipartFile archive, Path staging) throws IOException {
        if (!present(archive)) throw new IOException("请选择 ZIP 压缩包。");
        if (!archive.getOriginalFilename().toLowerCase(Locale.ROOT).endsWith(".zip")) throw new IOException("压缩包请使用 ZIP 格式。");
        if (archive.getSize() > sourceLimit) throw new IOException("压缩包超过源文件上传大小限制。");
        Path temporary = Files.createTempFile(resourceRoot, ".archive-", ".zip");
        try {
            try (InputStream in = archive.getInputStream()) { Files.copy(in, temporary, StandardCopyOption.REPLACE_EXISTING); }
            ZipFile zip;
            try { zip = openZip(temporary.toFile(), Charset.forName("UTF-8")); }
            catch (IllegalArgumentException | java.util.zip.ZipException e) { zip = openZip(temporary.toFile(), Charset.forName("GB18030")); }
            try (ZipFile entries = zip) {
                Enumeration<? extends ZipEntry> iterator = entries.entries();
                while (iterator.hasMoreElements()) {
                    ZipEntry entry = iterator.nextElement();
                    if (++count > MAX_FILES) throw new IOException("目录文件数不能超过 " + MAX_FILES + "。");
                    Path target = safeTarget(staging, entry.getName());
                    if (ignored(entry.getName())) continue;
                    if (entry.isDirectory()) { Files.createDirectories(target); continue; }
                    validateExtension(target);
                    try (InputStream in = entries.getInputStream(entry)) { copy(in, target); }
                }
            }
        } finally { Files.deleteIfExists(temporary); }
    }

    private ZipFile openZip(File archive, Charset charset) throws IOException {
        ZipFile zip = new ZipFile(archive, charset);
        try {
            // JDK 8 decodes ZIP names lazily; detect legacy encodings before extracting any file.
            Enumeration<? extends ZipEntry> entries = zip.entries();
            int number = 0;
            while (entries.hasMoreElements()) {
                if (++number > MAX_FILES) throw new IOException("目录文件数不能超过 " + MAX_FILES + "。");
                if (entries.nextElement().getName().length() > 4096) throw new IOException("资源文件路径过长。");
            }
            return zip;
        } catch (IOException | RuntimeException e) { zip.close(); throw e; }
    }

    private void copyFolder(MultipartHttpServletRequest request, Path staging) throws IOException {
        List<MultipartFile> files = request.getFiles("directoryFiles");
        JSONArray paths;
        try { paths = JSONArray.fromObject(request.getParameter("directoryPaths")); }
        catch (RuntimeException e) { throw new IOException("无法读取文件夹路径，请重新选择文件夹。", e); }
        if (files.isEmpty() || files.size() != paths.size()) throw new IOException("请选择完整的资源文件夹。");
        if (files.size() > MAX_FILES) throw new IOException("目录文件数不能超过 " + MAX_FILES + "。");
        for (int i = 0; i < files.size(); i++) {
            String relative = paths.getString(i);
            Path target = safeTarget(staging, relative);
            if (ignored(relative)) continue;
            validateExtension(target);
            try (InputStream in = files.get(i).getInputStream()) { copy(in, target); }
        }
    }

    private Path safeTarget(Path root, String entry) throws IOException {
        String name = entry.replace('\\', '/');
        if (name.startsWith("/") || name.contains(":") || name.indexOf('\0') >= 0) throw new IOException("资源包含无效文件路径。");
        Path target = root.resolve(name).normalize();
        if (!target.startsWith(root) || target.equals(root)) throw new IOException("资源包含越界文件路径。");
        for (String segment : name.split("/")) if ("..".equals(segment)) throw new IOException("资源包含越界文件路径。");
        return target;
    }

    private boolean ignored(String path) {
        String name = path.replace('\\', '/');
        return name.startsWith("__MACOSX/") || name.contains("/__MACOSX/") || name.endsWith(".DS_Store") || name.toLowerCase(Locale.ROOT).endsWith("thumbs.db");
    }

    private void validateExtension(Path path) throws IOException {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        if (!name.matches(".+\\.(txt|jpg|jpeg|png|gif|bmp|webp|pdf|xls|xlsx|xml)"))
            throw new IOException("目录中包含不支持的文件：" + path.getFileName() + "（支持 TXT、图片、PDF、Excel 目录和 XML 切片配置）。");
    }

    private void copy(InputStream in, Path target) throws IOException {
        Files.createDirectories(target.getParent());
        try (OutputStream out = Files.newOutputStream(target, StandardOpenOption.CREATE_NEW)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = in.read(buffer)) != -1) {
                written += length;
                if (written > Math.min(1024L * 1024 * 1024, Math.max(sourceLimit, sourceLimit * 20)))
                    throw new IOException("资源目录解压 / 上传后的总大小超过限制。");
                out.write(buffer, 0, length);
            }
        }
    }

    private Path unwrap(Path root) throws IOException {
        Path current = root;
        for (int depth = 0; depth < 32; depth++) {
            File[] children = current.toFile().listFiles();
            if (children == null || children.length != 1 || !children[0].isDirectory()) return current;
            current = children[0].toPath();
        }
        throw new IOException("资源目录层级过深。");
    }

    private void validateContents(File directory) throws IOException {
        if (ResourceDirectoryFiles.textFiles(directory.getPath()).isEmpty() && !ResourceDirectoryFiles.hasImagePages(directory.getPath()))
            throw new IOException("资源目录中没有 TXT 章节或可阅读的页面图片。");
    }

    private boolean present(MultipartFile file) { return file != null && !file.isEmpty(); }

    private void validateCover(MultipartFile cover) throws IOException {
        if (!present(cover)) return;
        if (cover.getSize() > coverLimit) throw new IOException("封面超过系统封面上传大小限制。");
        String name = cover.getOriginalFilename().toLowerCase(Locale.ROOT);
        if (!name.matches(".+\\.(jpg|jpeg|png|gif|bmp)")) throw new IOException("封面请选择 JPG、PNG、GIF 或 BMP 图片。");
    }

    private void saveCover(MultipartFile cover, Path book) throws IOException {
        BufferedImage source;
        try (InputStream in = cover.getInputStream()) { source = ImageIO.read(in); }
        if (source == null) throw new IOException("封面不是有效的图片。");
        BufferedImage rgb = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = rgb.createGraphics();
        try { graphics.setColor(Color.WHITE); graphics.fillRect(0, 0, rgb.getWidth(), rgb.getHeight()); graphics.drawImage(source, 0, 0, null); }
        finally { graphics.dispose(); }
        Path temporary = Files.createTempFile(book, ".cover-", ".jpg");
        try {
            if (!ImageIO.write(rgb, "jpg", temporary.toFile())) throw new IOException("无法保存封面图片。");
            Files.move(temporary, book.resolve("cover.jpg"), StandardCopyOption.REPLACE_EXISTING);
        } finally { Files.deleteIfExists(temporary); }
    }

    private void removeStaging(Path path) throws IOException {
        Path absolute = path.toAbsolutePath().normalize();
        if (!absolute.startsWith(resourceRoot) || absolute.equals(resourceRoot)) throw new IOException("临时资源目录无效。");
        if (!Files.exists(path)) return;
        List<Path> files = new ArrayList<Path>();
        try (java.util.stream.Stream<Path> stream = Files.walk(path)) { stream.forEach(files::add); }
        files.sort(java.util.Comparator.reverseOrder());
        for (Path file : files) Files.deleteIfExists(file);
    }
}
