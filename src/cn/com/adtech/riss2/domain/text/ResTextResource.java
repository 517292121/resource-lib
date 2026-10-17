package cn.com.adtech.riss2.domain.text;

import java.util.Date;

/**
 * 资源表
 * 
 * @author MaChao
 */

public class ResTextResource implements java.io.Serializable {

    // Fields
    
    private static final long serialVersionUID = 1L;
    private Long rtreId;
    private Long rtreRtliId;
    private Long rtreRlclId;
    private ResTextLibrary resTextLibrary;
    private ResLibraryClassify resLibraryClassify;
    private String rtreCover;
    private Long rtreRecommend;
    private Long rtreUsed;
    private Date rtreDate;
    private String rtreCol1;
    private String rtreCol2;
    private String rtreCol3;
    private String rtreCol4;
    private String rtreCol5;
    private String rtreCol6;
    private String rtreCol7;
    private String rtreCol8;
    private String rtreCol9;
    private String rtreCol10;
    private String rtreCol11;
    private String rtreCol12;
    private String rtreCol13;
    private String rtreCol14;
    private String rtreCol15;
    
    private String rtreTitle;
    private Long rtreAccess;
    
    //语种
    private String rtreLanguage;
    
    // Constructors

    public String getRtreTitle() {
        return rtreTitle;
    }

    public void setRtreTitle(String rtreTitle) {
        this.rtreTitle = rtreTitle;
    }

    /** default constructor */
    public ResTextResource() {
    }

    // Property accessors

    public Long getRtreId() {
        return this.rtreId;
    }

    public void setRtreId(Long rtreId) {
        this.rtreId = rtreId;
    }

    public ResTextLibrary getResTextLibrary() {
        return this.resTextLibrary;
    }

    public void setResTextLibrary(ResTextLibrary resTextLibrary) {
        this.resTextLibrary = resTextLibrary;
    }

    public ResLibraryClassify getResLibraryClassify() {
        return this.resLibraryClassify;
    }

    public void setResLibraryClassify(ResLibraryClassify resLibraryClassify) {
        this.resLibraryClassify = resLibraryClassify;
    }

    public String getRtreCover() {
        return this.rtreCover;
    }

    public void setRtreCover(String rtreCover) {
        this.rtreCover = rtreCover;
    }

    public Long getRtreRecommend() {
        return this.rtreRecommend;
    }

    public void setRtreRecommend(Long rtreRecommend) {
        this.rtreRecommend = rtreRecommend;
    }

    public Long getRtreUsed() {
        return this.rtreUsed;
    }

    public void setRtreUsed(Long rtreUsed) {
        this.rtreUsed = rtreUsed;
    }

    public Date getRtreDate() {
        return this.rtreDate;
    }

    public void setRtreDate(Date rtreDate) {
        this.rtreDate = rtreDate;
    }

    public String getRtreCol1() {
        return this.rtreCol1;
    }

    public void setRtreCol1(String rtreCol1) {
        this.rtreCol1 = rtreCol1;
    }

    public String getRtreCol2() {
        return this.rtreCol2;
    }

    public void setRtreCol2(String rtreCol2) {
        this.rtreCol2 = rtreCol2;
    }

    public String getRtreCol3() {
        return this.rtreCol3;
    }

    public void setRtreCol3(String rtreCol3) {
        this.rtreCol3 = rtreCol3;
    }

    public String getRtreCol4() {
        return this.rtreCol4;
    }

    public void setRtreCol4(String rtreCol4) {
        this.rtreCol4 = rtreCol4;
    }

    public String getRtreCol5() {
        return this.rtreCol5;
    }

    public void setRtreCol5(String rtreCol5) {
        this.rtreCol5 = rtreCol5;
    }

    public String getRtreCol6() {
        return this.rtreCol6;
    }

    public void setRtreCol6(String rtreCol6) {
        this.rtreCol6 = rtreCol6;
    }

    public String getRtreCol7() {
        return this.rtreCol7;
    }

    public void setRtreCol7(String rtreCol7) {
        this.rtreCol7 = rtreCol7;
    }

    public String getRtreCol8() {
        return this.rtreCol8;
    }

    public void setRtreCol8(String rtreCol8) {
        this.rtreCol8 = rtreCol8;
    }

    public String getRtreCol9() {
        return this.rtreCol9;
    }

    public void setRtreCol9(String rtreCol9) {
        this.rtreCol9 = rtreCol9;
    }

    public String getRtreCol10() {
        return this.rtreCol10;
    }

    public void setRtreCol10(String rtreCol10) {
        this.rtreCol10 = rtreCol10;
    }

    public String getRtreCol11() {
        return this.rtreCol11;
    }

    public void setRtreCol11(String rtreCol11) {
        this.rtreCol11 = rtreCol11;
    }

    public String getRtreCol12() {
        return this.rtreCol12;
    }

    public void setRtreCol12(String rtreCol12) {
        this.rtreCol12 = rtreCol12;
    }

    public String getRtreCol13() {
        return this.rtreCol13;
    }

    public void setRtreCol13(String rtreCol13) {
        this.rtreCol13 = rtreCol13;
    }

    public String getRtreCol14() {
        return this.rtreCol14;
    }

    public void setRtreCol14(String rtreCol14) {
        this.rtreCol14 = rtreCol14;
    }

    public String getRtreCol15() {
        return this.rtreCol15;
    }

    public void setRtreCol15(String rtreCol15) {
        this.rtreCol15 = rtreCol15;
    }

    public Long getRtreRtliId() {
        return rtreRtliId;
    }

    public void setRtreRtliId(Long rtreRtliId) {
        this.rtreRtliId = rtreRtliId;
    }

    public Long getRtreRlclId() {
        return rtreRlclId;
    }

    public void setRtreRlclId(Long rtreRlclId) {
        this.rtreRlclId = rtreRlclId;
    }

    public Long getRtreAccess() {
        return rtreAccess;
    }

    public void setRtreAccess(Long rtreAccess) {
        this.rtreAccess = rtreAccess;
    }

	public String getRtreLanguage() {
		return rtreLanguage;
	}

	public void setRtreLanguage(String rtreLanguage) {
		this.rtreLanguage = rtreLanguage;
	}

}
