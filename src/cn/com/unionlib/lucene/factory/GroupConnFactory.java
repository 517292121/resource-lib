package cn.com.unionlib.lucene.factory;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import cn.com.adtech.riss2.cache.SysConfigInfo;

/**
 * 
 * 资源聚类统计索引连接管理工厂
 *
 * @author MaChao
 *
 * @date：Jan 29, 2018
 */
public class GroupConnFactory {
	
	/**定义全局表态对象实例***/
	private static GroupConnFactory self = null;
	
	//索引目录文件
	public File INDEX_DIR = null;
	
	//索引目录对象
	private Directory directory = null;
	
	//索引写入对象
	private IndexWriter writer = null;
	
	
	private TaxonomyReader taxonomyReader = null;
	private DirectoryTaxonomyWriter taxoWriter = null;
	

	//定义私有构造函数防止实例化
	private GroupConnFactory(){
		if(INDEX_DIR == null){
			String indexDir = SysConfigInfo.INDEX_GROUP_DIR;
			INDEX_DIR = new File(indexDir);
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
	public static GroupConnFactory instance(){
		if(self == null){
			self = new GroupConnFactory();
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
		File file = INDEX_DIR;
		//如果索引的目录不存在，则自动创建
		if(file.exists() == false){
			file.mkdirs();
		}
		try {
			directory = FSDirectory.open(file);
		} catch (IOException e) {
			System.out.println("无法打开索引目录：" + file.getPath());
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
		try {
			IndexWriter.unlock(directory);
		} catch (IOException e) {
			System.out.println("writer解锁失败：" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取聚类查询索引
	 * @author MaChao
	 * @date 2017-9-4
	 */
	public TaxonomyReader getTaxonomyReader(){
		if(directory==null){
			getDirectory();
		}
		try {
			if(taxonomyReader==null){
				taxonomyReader = new DirectoryTaxonomyReader(directory);
			}else{
				TaxonomyReader tr = DirectoryTaxonomyReader.openIfChanged(taxonomyReader);	
				if (tr != null) {
					taxonomyReader = tr;
//					taxonomyReader = new DirectoryTaxonomyReader(directory);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return taxonomyReader;
	}
	
	/**
	 * 获取聚类写索引
	 * @author MaChao
	 * @date 2017-9-4
	 */
	public DirectoryTaxonomyWriter getTaxonomyWriter(){
		if(directory==null){
			getDirectory();
		}
		if(taxoWriter==null){
			try {
				taxoWriter = new DirectoryTaxonomyWriter(directory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return taxoWriter;
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
		File f = new File(INDEX_DIR, "write.lock");
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
}
