package cn.com.adtech.riss2.domain;



/**
 * SysOnlineConsultants entity. @author MaChao
 */

public class SysOnlineConsultants  implements java.io.Serializable {


    // Fields    

     private Long socoSuinId;
     private SysUserinfo sysUserinfo;


    // Constructors

    /** default constructor */
    public SysOnlineConsultants() {
    }

    
    /** full constructor */
    public SysOnlineConsultants(SysUserinfo sysUserinfo) {
        this.sysUserinfo = sysUserinfo;
    }

   
    // Property accessors

    public Long getSocoSuinId() {
        return this.socoSuinId;
    }
    
    public void setSocoSuinId(Long socoSuinId) {
        this.socoSuinId = socoSuinId;
    }

    public SysUserinfo getSysUserinfo() {
        return this.sysUserinfo;
    }
    
    public void setSysUserinfo(SysUserinfo sysUserinfo) {
        this.sysUserinfo = sysUserinfo;
    }
   








}
