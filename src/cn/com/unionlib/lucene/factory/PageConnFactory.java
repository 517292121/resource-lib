package cn.com.unionlib.lucene.factory;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.exception.ErrorMessageException;

/**
 * 
 * 资源单独每一子页或目录索引读写连接相关对象单态工厂 <br/>
 * 该索引主要用于图文对照的独立页或者目录树格式预览时的目录索引
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date 2018.08
 *
 */
public class PageConnFactory {
	
	/**定义全局表态对象实例***/
	private static PageConnFactory self = null;
	
	public File PAGE_INDEX_DIR = null;
	public IndexWriterConfig WRITER_CONFIG = null; 
	
	//索引目录对象
	private Directory directory = null;
	
	//索引写入对象
	private IndexWriter writer = null;
	
	//lucene读取对象实例
	private DirectoryReader reader = null;
	
	//索引查询器
	private IndexSearcher searcher = null;
	
	//索引使用次数
	private int writer_usenum = 0;
	

	//定义私有构造函数防止实例化
	private PageConnFactory(){
		if(PAGE_INDEX_DIR == null){
			String indexDir = SysConfigInfo.INDEX_PAGE_DIR;
			PAGE_INDEX_DIR = new File(indexDir);
		}
		if(WRITER_CONFIG == null){
			WRITER_CONFIG = new IndexWriterConfig(Version.LUCENE_46, AnalyzerFactory.instance().getNewAnalyzer(false, false));
		}
	}
	
	
	/**
	 * 获取自身实例
	 *
	 * @author MaChao
	 * @date Apr 20, 2016
	 *
	 * @return
	 */
	public static PageConnFactory instance(){
		if(self == null){
			self = new PageConnFactory();
		}
		return self;
	}
	
	/**
	 * 获取索引目录对象
	 *
	 * @author MaChao
	 * @date May 30, 2016
	 *
	 * @return
	 */
	private Directory getDirectory(){
		File file = PAGE_INDEX_DIR;
		//如果索引的目录不存在，则自动创建
		if(file.exists() == false){
			file.mkdirs();
		}
		try {
			directory = FSDirectory.open(file);
		} catch (IOException e) {
			System.out.println("无法打开子页索引目录：" + file.getPath());
			e.printStackTrace();
		}
		return directory;
	}
	
	/**
	 * 获取索引写入对象<br>
	 * 经过测试发现，在同一个应用中；当打开一个writer后，lucene会写入一个write.lock文件到索引目录中<br>
	 * 当这个writer没有关闭close()方法之前，这个write.lock文件会一直驻留在目录中，当再次创建writer时就会报错<br>
	 * 如果write.lock文件驻留在目录中时，只有重新打开应用(web项目即重启tomcat)后第一次方可再次创建索引<br>
	 * 但是如果再次创建writer时write.lock还在的话，便无法再次打开writer了。<br>
	 * 所以这就是经常会看到的，索引只会写入一次，第二次开始便报错了，因为第一次无论有没有write.lock文件writer都可以打开，<br>
	 * 但是如果没有关闭writer的话就无法再次打开writer了;因此一定要控制好writer的关闭管理<br>
	 *
	 * @author MaChao
	 * @date Apr 20, 2016
	 *
	 * @return
	 */
	public IndexWriter getWriter(){
		if(writer == null){
			againOpenWriter();
		}
		//当writer对象使用达到100次后，重新创建一次writer对象
		//经测试，由于平台共用一个writer对象；当多个任务运行时，可能会出现其它任务正在使用writer，
		//而本次任务重新创建wirter时会关闭一段时间writer，因此会造成其它任务创建索引失败；所以暂时取消该操作
//		if(writer_usenum >= 10){
//			System.out.println("writer使用次数达到100次，重新创建writer");
//			WebLogger.instance().setLog("writer使用次数达到100次，重新创建writer");
//			againOpenWriter();
//		}else{
//			writer_usenum++;
//		}
		return writer;
	}
	
	/**
	 * 重新打开IndexWriter
	 * 可开放定时或手工重新打开IndexWriter的操作
	 *
	 * @author MaChao
	 * @date Apr 21, 2016
	 *
	 */
	public synchronized  void againOpenWriter(){
		if(directory == null){
			getDirectory();
		}
		try {
			if(writer != null){
				//关闭前先将删除在栈内的数据清空；
//				writer.forceMergeDeletes(); 
				writer.close();
			}
			IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_46, AnalyzerFactory.instance().getNewAnalyzer(false, false));
			writer = new IndexWriter(directory, writerConfig);
			writer_usenum = 1; //重建后将使用次数重置为一
		} catch (IOException e) {
			System.out.println("索引器IndexWriter创建失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭并将directory和writer置为空，下次获取writer时将重新打开这两个对象
	 * 次方法慎用，最好不要使用太频繁
	 *
	 * @author MaChao
	 * @date Jun 15, 2016
	 *
	 */
	public void closeWriter(){
		if(writer != null){
			try {
				//关闭前先将删除在栈内的数据清空；
//				writer.forceMergeDeletes(); 
				writer.close();
				writer = null;
			} catch (IOException e) {
				System.out.println("writer关闭失败：" + e.getMessage());
				e.printStackTrace();
				writer = null;
			}
		}
	}
	public void unlockWriter(){
		try{
			if (directory != null && IndexWriter.isLocked(directory)) {
				IndexWriter.unlock(directory);
			}
		}catch (IOException e) {
			System.out.println("writer解锁失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 获取索引读取器
	 *
	 * @author MaChao
	 * @date Apr 20, 2016
	 *
	 * @return
	 */
	private DirectoryReader getReader(){
		try {
			if (directory == null) {
				getDirectory();
			} 
			reader = DirectoryReader.open(directory);
		} catch (IndexNotFoundException e) {
			System.out.println("索引目录为空：");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("getIndexReader发生异常：");
			e.printStackTrace();
		}
		return reader;
	}
	
	/**
	 * 获取索引查询器
	 *
	 * @author MaChao
	 * @date Apr 20, 2016
	 *
	 * @return
	 */
	public IndexSearcher getSearcher(){
		if(searcher == null){
			getReader();
			searcher = new IndexSearcher(reader);
		}else{
			DirectoryReader ir;
			try {
				//当索引发生变化后，则重新打开读取流
				ir = DirectoryReader.openIfChanged(reader);
				if (ir != null) {
//					reader.close();
					reader = ir;
					searcher = new IndexSearcher(reader);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(searcher.getIndexReader() == null){
			System.out.println("searcher的Reader为空了，需要重新创建");
			getNewSearcher();
		}
		if(searcher.getIndexReader().getRefCount() < 1){
			System.out.println("searcher的Reader的RefCount小于1，重新创建searcher");
			getNewSearcher();
		}
//		System.out.println("RefCount：" + searcher.getIndexReader().getRefCount());
		return searcher;
	}
	
	/**
	 * 重新打开一个新的IndexSearcher，由于是重新创建
	 * 这个过程也会对DirectoryReader重新创建一次
	 *
	 * @author MaChao
	 * @date Jun 7, 2016
	 *
	 * @return
	 */
	public IndexSearcher getNewSearcher(){
		try {
			directory = getDirectory();
			reader = DirectoryReader.open(directory);
			searcher = new IndexSearcher(reader);
			return searcher;
		} catch (IOException e) {
			e.printStackTrace();
			throw new ErrorMessageException("重新打开searcher异常：" + e.getMessage());
		}
	}
	
	/**
	 * 获取IndexWriter是否为打开状态
	 *
	 * @author MaChao
	 * @date Jul 14, 2016
	 *
	 * @return
	 */
	public boolean writeIsOpen(){
		if(writer != null){
			return true;
		}
		//如果writer为空，即未打开的状态下。查看一下索引目录中是否有write.lock文件
		File f = new File(PAGE_INDEX_DIR, "write.lock");
		if(f.exists()){
			//如果write.lock文件存在，则将其删除。虽然很有可能删除不了；
			try {
				f.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public int getWriterUsenum(){
		return writer_usenum;
	}
	
}
