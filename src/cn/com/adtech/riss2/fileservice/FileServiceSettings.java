package cn.com.adtech.riss2.fileservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import cn.com.adtech.riss2.core.CommonUtil;

/** Shared by the embedded service and the legacy upload/URL adapters. */
public final class FileServiceSettings {
    private FileServiceSettings() { }

    public static String property(String key, String fallback) {
        String value = System.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            value = CommonUtil.getProperties("/config.properties", key);
        }
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    public static boolean embedded() {
        return !"remote".equalsIgnoreCase(property("file.service.mode", "embedded"));
    }

    public static Path root() throws IOException {
        String directory = property("file.dir", "");
        if (directory.isEmpty()) {
            throw new IOException("file.dir is not configured");
        }
        return new File(directory).getCanonicalFile().toPath();
    }

    public static Path resolve(Path root, String relative) throws IOException {
        if (relative == null || relative.trim().isEmpty()) {
            throw new IllegalArgumentException("File path is empty");
        }
        String normalized = relative.replace('\\', '/');
        // Legacy downloadPath is relative to file.dir, never an arbitrary local path.
        if (normalized.startsWith("/") || normalized.indexOf(':') >= 0) {
            throw new SecurityException("Absolute file paths are not allowed");
        }
        Path base = root.toFile().getCanonicalFile().toPath();
        Path file = base.resolve(normalized).toFile().getCanonicalFile().toPath();
        if (file.equals(base) || !file.startsWith(base)) {
            throw new SecurityException("File path is outside file.dir");
        }
        return file;
    }
}
