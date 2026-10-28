package cn.com.adtech.riss2.core.file;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/** Shared chapter discovery for directory imports, full indexing and reading. */
public final class ResourceDirectoryFiles {
    private ResourceDirectoryFiles() { }

    public static final Comparator<String> NATURAL_ORDER = new Comparator<String>() {
        public int compare(String a, String b) {
            int i = 0, j = 0;
            while (i < a.length() && j < b.length()) {
                char x = a.charAt(i), y = b.charAt(j);
                if (x >= '0' && x <= '9' && y >= '0' && y <= '9') {
                    int ai = i, bj = j;
                    while (i < a.length() && Character.isDigit(a.charAt(i))) i++;
                    while (j < b.length() && Character.isDigit(b.charAt(j))) j++;
                    String an = a.substring(ai, i).replaceFirst("^0+(?!$)", "");
                    String bn = b.substring(bj, j).replaceFirst("^0+(?!$)", "");
                    int c = Integer.compare(an.length(), bn.length());
                    if (c == 0) c = an.compareTo(bn);
                    if (c != 0) return c;
                } else {
                    int c = Character.compare(Character.toLowerCase(x), Character.toLowerCase(y));
                    if (c != 0) return c;
                    i++; j++;
                }
            }
            int c = Integer.compare(a.length() - i, b.length() - j);
            return c == 0 ? a.compareTo(b) : c;
        }
    };

    public static List<File> textFiles(String directory) {
        final List<File> result = new ArrayList<File>();
        if (directory == null || directory.trim().isEmpty()) return result;
        final Path root = new File(directory).toPath();
        if (!Files.isDirectory(root)) return result;
        // Do not follow symbolic links into unrelated resource directories.
        try (Stream<Path> files = Files.walk(root, 32)) {
            files.filter(p -> Files.isRegularFile(p) && !Files.isSymbolicLink(p)
                    && p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".txt"))
                 .forEach(p -> result.add(p.toFile()));
        } catch (IOException e) {
            throw new IllegalStateException("无法扫描资源目录文本文件：" + directory, e);
        }
        Collections.sort(result, (a, b) -> NATURAL_ORDER.compare(relativeTitle(root.toFile(), a), relativeTitle(root.toFile(), b)));
        return result;
    }

    public static String relativeTitle(File root, File file) {
        String name = root.toPath().toAbsolutePath().normalize().relativize(file.toPath().toAbsolutePath().normalize()).toString().replace('\\', '/');
        return name.substring(0, name.lastIndexOf('.'));
    }

    public static String readText(File file) throws IOException {
        if (file.length() > 50L * 1024 * 1024) throw new IOException("单个文本文件不能超过 50MB：" + file.getName());
        byte[] bytes = Files.readAllBytes(file.toPath());
        int offset = 0;
        Charset charset = StandardCharsets.UTF_8;
        if (bytes.length >= 3 && (bytes[0] & 255) == 239 && (bytes[1] & 255) == 187 && (bytes[2] & 255) == 191) offset = 3;
        else if (bytes.length >= 2 && (bytes[0] & 255) == 255 && (bytes[1] & 255) == 254) { charset = StandardCharsets.UTF_16LE; offset = 2; }
        else if (bytes.length >= 2 && (bytes[0] & 255) == 254 && (bytes[1] & 255) == 255) { charset = StandardCharsets.UTF_16BE; offset = 2; }
        try {
            return charset.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes, offset, bytes.length - offset)).toString();
        } catch (CharacterCodingException e) {
            if (!StandardCharsets.UTF_8.equals(charset)) throw e;
            return Charset.forName("GB18030").newDecoder().onMalformedInput(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes)).toString();
        }
    }

    public static File cover(String directory) {
        if (directory == null) return null;
        File[] files = new File(directory).listFiles();
        if (files == null) return null;
        for (String name : new String[]{"cover.jpg", "cover.jpeg", "cover.png", "cover.gif", "cover.bmp", "cover.webp"}) {
            for (File file : files) if (file.isFile() && file.getName().equalsIgnoreCase(name)) return file;
        }
        return null;
    }

    public static boolean hasImagePages(String directory) {
        List<String> pages = MyFileUtil.getPageNameList(directory);
        return pages != null && !pages.isEmpty();
    }
}
