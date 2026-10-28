package cn.com.adtech.riss2.controller.platform;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import cn.com.adtech.riss2.fileservice.FileServiceSettings;
import cn.com.adtech.riss2.fileservice.FileTextReader;

/** Compatible URLs for resources previously served by the separate fileService webapp. */
@Controller
public class EmbeddedFileServiceController {
    @RequestMapping(value = {"/fileService/FileDownload", "/FileDownload"},
            method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST})
    public ResponseEntity<Resource> download(@RequestParam("downloadPath") String downloadPath,
            @RequestParam(value = "fileName", required = false) String encodedName,
            @RequestParam(value = "isOnLine", defaultValue = "false") boolean online) throws IOException {
        Path file = existingFile(downloadPath);
        String name = encodedName == null || encodedName.isEmpty() ? file.getFileName().toString() : decode(encodedName);
        // A display filename must not inject headers or contain directory components.
        name = name.replaceAll("[\\r\\n\\\\/]", "_");
        String mime = Files.probeContentType(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mime == null ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(mime));
        headers.setContentDisposition(ContentDisposition.builder(online ? "inline" : "attachment")
                .filename(name, StandardCharsets.UTF_8).build());
        headers.setContentLength(Files.size(file));
        headers.setLastModified(Files.getLastModifiedTime(file).toMillis());
        headers.setCacheControl("private");
        headers.set("X-Content-Type-Options", "nosniff");
        if (mime != null && (mime.contains("html") || mime.contains("svg"))) {
            // Downloads now share the application's origin. Do not let uploaded active content read its cookies.
            headers.set("Content-Security-Policy", "sandbox; default-src 'none'; style-src 'unsafe-inline'; img-src data:");
        }
        // Spring's Resource converter supplies byte ranges/206/416 and HEAD for PDF/audio/video clients.
        return new ResponseEntity<Resource>(new FileSystemResource(file.toFile()), headers, HttpStatus.OK);
    }

    @RequestMapping(value = {"/fileService/ReadFileServlet", "/ReadFileServlet"},
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> read(@RequestParam("filePath") String filePath) throws IOException {
        Path file = existingFile(filePath);
        long maxBytes = Long.parseLong(FileServiceSettings.property("file.service.text-max-bytes", "52428800"));
        if (Files.size(file) > maxBytes) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large for text extraction");
        }
        try {
            return ResponseEntity.ok().contentType(new MediaType("text", "plain", StandardCharsets.UTF_8))
                    .header("X-Content-Type-Options", "nosniff").body(FileTextReader.read(file));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
        }
    }

    private Path existingFile(String encoded) throws IOException {
        try {
            Path file = FileServiceSettings.resolve(FileServiceSettings.root(), decode(encoded));
            if (!Files.isRegularFile(file)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
            }
            return file;
        } catch (SecurityException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "File is outside the resource directory");
        }
    }

    private String decode(String encoded) {
        try {
            // Legacy format: Base64(URLEncoder(UTF-8 path)); '+' may arrive as a query-space.
            String escaped = new String(Base64.getDecoder().decode(encoded.replace(' ', '+')), StandardCharsets.UTF_8);
            return URLDecoder.decode(escaped, "UTF-8");
        } catch (IOException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid encoded file path");
        }
    }
}
