package cn.com.unionlib.parser.core.bean;

import org.apache.lucene.search.BooleanClause.Occur;

/**
 * 查询实体类
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-12
 */
public class Serach {
	
	//字段名
	private String coulmn;
	//关键词
	private String keyWord;
	//连接关系
	private Occur  occur = Occur.SHOULD;
	
	
	public Serach() {
		super();
	}

	public Serach(String coulmn, String keyWord) {
		this.coulmn = coulmn;
		this.keyWord = keyWord;
		this.occur = Occur.SHOULD;
	}
	
	public Serach(String coulmn, String keyWord, Occur occur) {
		this.coulmn = coulmn;
		this.keyWord = keyWord;
		this.occur = occur;
	}
	public String getCoulmn() {
		return coulmn;
	}
	public void setCoulmn(String coulmn) {
		this.coulmn = coulmn;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public Occur getOccur() {
		return occur;
	}
	public void setOccur(Occur occur) {
		this.occur = occur;
	}
	
	
	
}
