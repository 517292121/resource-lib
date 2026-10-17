/***************************************************************************************
 	*	 
 	* 该接口已测试过
 	*
 	* @see (Dictionary.java、TibetFilter.java、LanguageUtil.java)
 	* 
 *
 * 开发公司: 重庆联图科技有限责任公司
 * 
 * 版权:本文件版权归属重庆联图科技有限责任公司
 * 
 	* @author  王林
 	* 
 	* @since 该文件所支持的JDK版本号: jdk1.6 
 	*
 	* @date  (开发日期): 2016-09-27
 	* 
 	* @version 1.2
 	* 
 *
 * 最后更新日期: 
 *
 * 更改人: 
 * 
 * 更改内容:
 * 
 *   
 * 复审人:   
 * 
 * 复审时间： 
 * 
 * 复审结果:
 *
 **************************************************************************************/
package org.apache.lucene.analysis.sun.core;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.sun.dic.Dictionary;
import org.apache.lucene.analysis.sun.util.LanguageUtil;
import org.apache.lucene.analysis.sun.util.LanguageUtil.CharType;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * 
 * 藏文分词器，根据藏文词典main2016.dic与停用词典stop.dic实现藏文分词
 * 本类主要实现对藏文、中文、英文、数字几种语言类型实现了切分
 * 
 * 藏文：当一个单独的藏文文字，如果其本身在词库中不为一个词，且与它之后的一个
 * 至多字都不能组成词语，则视该文字单独作为一个词。否则不单独切分字为词
 * 中文：只实现了文字的拆分，即一个字为一个词
 * 英文：以字母开始，至非字母的字符结束作为一个词
 * 数字：以数字开始，至非数字的字符结束作为一个词
 * 
 * 除以上四种字符外的所有字符匀已过滤，其中除空格以外一个字符作为一个词语间隔符
 * 
 *
 * @author wanglin
 * 
 * @version 1.0
 *
 * @date Sep 27, 20162:29:53 PM
 *
 */
public final class SunSimpleTokenizer extends Tokenizer{
	
	
	//存放待分词的字符数组做为分词列队,由reader读入其中
	private static int BUFF_SIZE = 2048; //默认缓冲区大小
	private static int MAX_TIBETAN_WORD_CHAR = 200; //藏文词语最大遍历字符数
	private static int MAX_CHINESE_WORD_CHAR = 20;  //中文词语最大遍历字符数
	//用于记录ioBuffer上一批次的字符数量个数，以词语最大遍历数为准，再约为加一点
	private static int TEMP_OFFSET_SIZE = 0;
	
	
	//当开发商用分词器时，必须实现lucene词元的以下几个属性对象
	final private CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	final private OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	final private PositionIncrementAttribute position = addAttribute(PositionIncrementAttribute.class);
	final private TypeAttribute type = addAttribute(TypeAttribute.class);
	
	/** 是否只切出词典中存在的词语 */
	private boolean isMust = false;
	
	/** 词典管理对象 */
	private Dictionary dic = null;
	
	/** 当前批次读取的字符流 */
	private char[] ioBuff = null;
	
	/** 当前批次加上一批次最后一段缓存的字符流 */
	private char[] tempBuff = null;
	
	/** 当前词语语种类型,根据第一个字符判断 */
	private CharType wordType = null;
	
	/** 当前词语在内容中的起始下标 （全文） */
	private int start = 0;
	
	/** 记录词语字符长度 （当前词语）*/
	private int fontLen;
	
	/** 字符总长度 （当前批次）*/
	private int dataLen = -1;
	
	/** 字符读取指针 （当前批次）*/
	private int batchIndex = 0;
	
	/** 已拆分到内容的总位置下标 （全文）*/
	private int countIndex = 0;
	
	/** 本轮遍历第一个“字”在本批字符中的结束下标；用于记录下轮遍历的起始下标 */
	private int wordEndTemp = 0;
	
	/** 本轮遍历一共处理了多少个字符数 */
	private int wordPushLen = 0;
	
	/** 本轮遍历第一个“字”的字符长度 */
	private int wordFirstLen = -1;
	
	/** 本轮遍历是否切分出过词语 */
	private boolean haveWord = false;
	
	/** 当前字符语种是否等于本轮最初字符语种 */
	private boolean lanEq = true;
	
	/** 是否为其它语种字符（即中文、藏文、英文、数字以外的字符） */
	private boolean otherLan = false;
	
	/** the current char type */
	private CharType t = null;
	
	/** 用于缓存本轮切分中，记录已切分过的词元 */
	private StringBuffer wordTextTemp = new StringBuffer();
	
	
	//实例化本对象时同时创建词典管理对象和初始化各个参数默认值
	protected SunSimpleTokenizer(Reader input, boolean isMust) {
		super(input);
		this.isMust = isMust;
		dic = Dictionary.getSingleton();
		
		MAX_CHINESE_WORD_CHAR = dic.getMaxChineseWordChar();
		MAX_TIBETAN_WORD_CHAR = dic.getMaxTibetanWordChar();
		if(MAX_TIBETAN_WORD_CHAR >= BUFF_SIZE){
			BUFF_SIZE = BUFF_SIZE + MAX_TIBETAN_WORD_CHAR;
		}
		
		TEMP_OFFSET_SIZE = MAX_TIBETAN_WORD_CHAR;
		tempBuff = new char[BUFF_SIZE + TEMP_OFFSET_SIZE];
		
		this.batchIndex = TEMP_OFFSET_SIZE;
		this.ioBuff = new char[BUFF_SIZE]; 
	}
	protected SunSimpleTokenizer(AttributeFactory factory, Reader input) {
		super(factory, input);
	}
	
	
	@Override
	public boolean incrementToken() throws IOException {
		clearAttributes();
		this.fontLen = 0;
		this.start = this.countIndex;
		if(wordTextTemp.length() > 0){
			this.termAtt.append(wordTextTemp);
		}
		while(true){
			//当下标已走完当前批次中字符总个数，再次读取一批字符到数组中
			if((batchIndex - TEMP_OFFSET_SIZE) >= dataLen){
				
				//（最后一批进入该程序）
				//如果不是第一次进来，且当上一批次的长度不足定义批次总长度时，说明已是最后一批
				//最后一批时，不再读取reader中的字符，直接从上次切分的位置继续切分，切完结束
				if(dataLen > -1 && dataLen < BUFF_SIZE){
					if(isTibetWord()){ 
						this.haveWord = true;
						this.wordFirstLen = wordPushLen;
						this.wordEndTemp = batchIndex;
						return push();
					}
					
					initBatch();
					if(wordFirstLen < 0){ 
						//第一个字的长度为-1，说明此次遍历未望到分割符，由此判断当前已是最后一个文字
						this.batchIndex = TEMP_OFFSET_SIZE + dataLen;
						return push();
					}
					if(haveWord || isMust){
						initNext();
						continue;
					}else {
						return returnFirst();
					}
				}
				
				//将上一批字符最后一段复制到缓存数组的前面
				System.arraycopy(ioBuff, (BUFF_SIZE - TEMP_OFFSET_SIZE), tempBuff, 0, TEMP_OFFSET_SIZE);
				this.dataLen = this.input.read(this.ioBuff);
				
				if(this.dataLen < 1){ //如果读取之后还为0，则直接返回
					dataLen = 0;
				}
				
				//将新读取的字符全部复制到缓存数组后面，形成上次的结尾一段与本次字符正确连接
				System.arraycopy(ioBuff, 0, tempBuff, TEMP_OFFSET_SIZE, dataLen);
				
				//当字符动态指针移动最本批次最前面
				this.batchIndex = TEMP_OFFSET_SIZE;
				
				//如果读取新批次之前已定位了下次起始位置的指针，由于此次读取后已将上一批次末尾缓
				//存的字符全部移动到本批次前面的缓存空间，所以要将下次起始位置移动到年批次长度之前
				if(wordEndTemp > 0){
					this.wordEndTemp = wordEndTemp - BUFF_SIZE;
				}
			}
			
			char c = tempBuff[batchIndex];
			this.t = LanguageUtil.checkType(c);
			if(this.wordPushLen < 1){
				this.wordType = t;
			}
			this.lanEq = (t == wordType);
			this.otherLan = (t == CharType.OTHER);
			boolean isSingle = (isSingleCharWord(c) && fontLen < 1);
			
			//相同语种字符时移动指定下标
			if(lanEq){
				wordTextTemp.append(c);
				this.wordPushLen++;
				this.batchIndex++;
			}
			
			//中文处理
			if(wordType == CharType.CHINESE){
				this.termAtt.append(c);
				this.fontLen++;
				
				if(wordEndTemp < 1){
					this.wordEndTemp = batchIndex;
				}
				if(this.wordFirstLen < 0){
					this.wordFirstLen = fontLen;
				}
				if(isChinaWord()){
					this.haveWord = true;
					
					//粗略分词，当出现词语后，下一次从本次词语之后再开始切分，而不从下一个字开始
					this.wordFirstLen = wordPushLen;
					this.wordEndTemp = batchIndex;
					return push();
				}
				
				if(!lanEq || wordPushLen > MAX_CHINESE_WORD_CHAR){
					initBatch();
					if(haveWord || isMust){
						initNext();
						continue;
					}else {
						return returnFirst();
					}
				}
				continue;
			}
			
			
			
			/*
			 * 进入内容切分，切分时机包括：
			 * 1、当前字符是藏文分割符
			 * 2、当前字符语种与之前语种不一样
			 * 3、当前字符为单个字符切分类型，且当前字符为此次切分的第一个字符
			 */
			if(LanguageUtil.isSplit(c) || !lanEq || isSingle){
				//第一个字符就进入切分程序中的。根据if中的条件能在第一个字符就进入切分的字符
				//只有藏文分割符或单个字符词语，因此该字符需要直接返回
				boolean filterChar = false; //首字符过滤
				if(fontLen < 1){ 
					this.termAtt.append(c);
					this.fontLen++;
					filterChar = true;
				}
				if(wordEndTemp < 1){
					this.wordEndTemp = batchIndex;
				}
				if(this.wordFirstLen < 0){
					this.wordFirstLen = fontLen;
				}
				
				/*
				 * 结束本轮切分，进入下一轮词语切分，结束时机包括：
				 * 1、本轮切分的字符数已达到定义的词语最大字符长度
				 * 2、当前字符语种已不为之前切分的字符语种，即已切分到其它语种时
				 * 3、当前字符语种类型为其它(即非中文、藏文、英文、数字)
				 * 4、当前字符为本轮切分的第一个字符
				 */
				if(wordPushLen > MAX_TIBETAN_WORD_CHAR || !lanEq || otherLan || filterChar){
					initBatch();
					if(haveWord || isMust){
						initNext();
						continue;
					}else {
						return returnFirst();
					}
				}
				
				//王林：2019.08.12 将此判断调整至上面（结束本轮切分下面）
				//调整原因：当词典中有藏文加数字等字符也算一个词时会出现死循环
				//该调整未经过测试
				if(isTibetWord()){ 
					this.haveWord = true;
					this.wordFirstLen = wordPushLen;
					this.wordEndTemp = batchIndex;
					return push();
				}
			}
			this.termAtt.append(c);
			this.fontLen++;
		}
	}
	
	/**重置本轮词语结束后的参数值*/
	private void initBatch(){
		this.batchIndex = wordEndTemp;  // - BUFF_SIZE
		this.wordEndTemp = 0;
		this.countIndex = countIndex + wordFirstLen;
		if(wordPushLen > wordFirstLen && wordType != CharType.CHINESE && !haveWord){
			//只有当此轮遍历的个数大于第一个单词长度时，说明之前遇到过分割符，才需要在总指针上加一
			this.countIndex++; 
		}
		this.wordPushLen = 0;
		this.wordTextTemp = new StringBuffer();
	}
	
	/**重置下一个字开始切分前的参数值***/
	private void initNext(){
		this.fontLen = 0;
		this.haveWord = false;
		this.wordFirstLen = -1;
		this.start = countIndex;
		termAtt.setEmpty();
	}
	
	/***将第一个字作为词返回***/
	private boolean returnFirst(){
		this.haveWord = false;
		termAtt.setLength(this.wordFirstLen);
		this.wordFirstLen = -1;
		return push();
	}
	
	
	//返回装载中的词元
	private boolean push(){
		if(this.fontLen > 0){
//			termAtt.setLength(termAtt.length());
			offsetAtt.setOffset(start, start + termAtt.length());
			type.setType(wordType.toString());
			position.setPositionIncrement(1);
			return true;
		}else{
			if((batchIndex - TEMP_OFFSET_SIZE) < dataLen){
				return true;
			}else{
				//当返回false时，说明此次分词结束；重置所有全局变量。(此方法很重要)
				resetDef();  
				return false;
			}
		}
	}
	
	/**
	 * 重置所有参数
	 *
	 * @author wanglin
	 * @date Sep 28, 2016
	 *
	 */
	private void resetDef(){
		this.ioBuff = new char[BUFF_SIZE];
		this.tempBuff = new char[BUFF_SIZE + TEMP_OFFSET_SIZE];
		this.wordType = null;
		this.start = 0;
		this.fontLen = 0;
		this.dataLen = -1;
		this.batchIndex = TEMP_OFFSET_SIZE;
		this.countIndex = 0;
		this.wordEndTemp = 0;
		this.wordPushLen = 0;
		this.wordFirstLen = -1;
		this.haveWord = false;
		this.lanEq = true;
		this.otherLan = false;
		this.t = null;
		this.wordTextTemp = new StringBuffer();
	}
	
	
	/**
	 * 验证token中的内容是否为词典中的词
	 *
	 * @author wanglin
	 * @date Sep 21, 2016
	 *
	 * @return
	 */
	private boolean isTibetWord(){
		if(termAtt.length() < 1){
			return false;
		}
		if(wordType != CharType.TIBETAN){
			return false;
		}
		boolean b = dic.matchInTibetDict(termAtt.buffer(), termAtt.length());
		return b;
	}
	private boolean isChinaWord(){
		if(termAtt.length() < 1){
			return false;
		}
		boolean b = dic.matchInChinaDict(termAtt.buffer(), termAtt.length());
		return b;
	}
	
	/**
	 * 判断当前字符是否作为单个字符切分
	 * 作为单个切分的类型包括中文、其它语种
	 *
	 * @author wanglin
	 * @date Sep 26, 2016
	 *
	 * @param c
	 * @return
	 */
	private boolean isSingleCharWord(char c){
//		if(t == CharType.CHINESE){
//			return true;
//		}
		if(c == ' '){
			return false;
		}
		if(t == CharType.OTHER){
			return true;
		}
		return false;
	}
	
	@Override
	public void reset() throws IOException {
		super.reset();
		this.resetDef();
	}
}
