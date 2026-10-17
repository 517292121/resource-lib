package cn.com.adtech.riss2.domain.text;

/**
 * 资源库分类
 * 
 * @author MaChao
 */

public class ResLibraryClassify implements java.io.Serializable {

    // Fields

    private static final long serialVersionUID = 1L;
    private Long rlclId;
    private Long rlclRtliId;
    private ResTextLibrary resTextLibrary;
    private Long rlclParentId;
    private String rlclName;
    private Long rlclIndex;

    // Constructors

    /** default constructor */
    public ResLibraryClassify() {
    }

    // Property accessors

    public Long getRlclId() {
        return this.rlclId;
    }

    public void setRlclId(Long rlclId) {
        this.rlclId = rlclId;
    }

    public ResTextLibrary getResTextLibrary() {
        return this.resTextLibrary;
    }

    public void setResTextLibrary(ResTextLibrary resTextLibrary) {
        this.resTextLibrary = resTextLibrary;
    }

    public Long getRlclParentId() {
        return this.rlclParentId;
    }

    public void setRlclParentId(Long rlclParentId) {
        this.rlclParentId = rlclParentId;
    }

    public String getRlclName() {
        return this.rlclName;
    }

    public void setRlclName(String rlclName) {
        this.rlclName = rlclName;
    }

    public Long getRlclIndex() {
        return this.rlclIndex;
    }

    public void setRlclIndex(Long rlclIndex) {
        this.rlclIndex = rlclIndex;
    }

    public Long getRlclRtliId() {
        return rlclRtliId;
    }

    public void setRlclRtliId(Long rlclRtliId) {
        this.rlclRtliId = rlclRtliId;
    }

}
