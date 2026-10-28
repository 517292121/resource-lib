package cn.com.adtech.riss2.core.file;

import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.fileservice.FileServiceSettings;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import cn.com.adtech.riss2.annotation.TextAnchor;

/** Read-only single TXT preview, using only the stored source path under file.dir. */
public final class TextFilePreview {
    public static final String CHAPTER = "fulltext";
    private TextFilePreview() { }
    public static boolean supports(ResSourceData resource) {
        return resource!=null && (resource.getRsdaSourceType()==null || resource.getRsdaSourceType()==0)
                && (textExtension(resource.getRsdaFileType()) || textExtension(resource.getRsdaFileName()) || textExtension(resource.getRsdaFileLocalName()));
    }
    private static boolean textExtension(String value) {
        if(value==null)return false;
        value=value.trim().toLowerCase(Locale.ROOT);
        return value.equals("txt") || value.endsWith(".txt");
    }
    public static String read(ResSourceData resource) throws IOException {
        if(!supports(resource))throw new IOException("该资源不是单个 TXT 文本文件。");
        String relative=resource.getRsdaFileLocalName();
        if(relative==null || relative.trim().isEmpty())throw new IOException("该资源没有可预览的文本源文件。");
        Path file=FileServiceSettings.resolve(FileServiceSettings.root(),relative);
        if(!Files.isRegularFile(file))throw new IOException("文本源文件不存在。");
        if(Files.size(file)>10L*1024*1024)throw new IOException("文本文件超过10MB，暂不支持在线预览。");
        return ResourceDirectoryFiles.readText(file.toFile());
    }
    /** Adapt one file to the existing book reader's chapter model; layout and pagination are unchanged. */
    public static Map<String,Object> readerModel(ResSourceData resource) throws IOException {
        String text=TextAnchor.normalize(read(resource));
        String title=resource.getRsdaFileName();
        if(title==null || title.trim().isEmpty())title=resource.getRsdaTitle();
        if(title==null)title="";
        title=title.replace('\\','/');title=title.substring(title.lastIndexOf('/')+1).replaceFirst("(?i)\\.txt$","");
        Map<String,String> chapter=new LinkedHashMap<String,String>();
        chapter.put("path",CHAPTER);chapter.put("title",title);chapter.put("text",text);chapter.put("hash",TextAnchor.hash(text));
        Map<String,Object> model=new LinkedHashMap<String,Object>();
        model.put("textChapters",Collections.singletonList(chapter));model.put("currentChapter",0);
        model.put("chapterTitle",title);model.put("chapterText",text);
        return model;
    }
}
