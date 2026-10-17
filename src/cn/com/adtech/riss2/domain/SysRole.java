package cn.com.adtech.riss2.domain;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author MaChao */
@SuppressWarnings("serial")
public class SysRole implements Serializable
{
	/**数字图书管理员*/
	public static final Long ID_DIGITALBOOK_WORKER=new Long(20);
	
    // 已登录用户

    public static final Long ID_LOGIN_USER = new Long(14);

	
	/** identifier field */
    private Long srolId;

    /** persistent field */
    private String srolName;

    /** persistent field */
    private String srolCode;

    /** nullable persistent field */
    private String srolDesc;

    /** persistent field */
    private Set sysRoleFunctions;
    
    //是否系统角色
    private Boolean srolIsSysRole;

    /** full constructor */
    public SysRole(Long srolId, String srolName, String srolCode,
            String srolDesc, Set sysRoleFunctions)
    {
        this.srolId = srolId;
        this.srolName = srolName;
        this.srolCode = srolCode;
        this.srolDesc = srolDesc;
        this.sysRoleFunctions = sysRoleFunctions;
    }

    /** default constructor */
    public SysRole()
    {
    }

    /** minimal constructor */
    public SysRole(Long srolId, String srolName, String srolCode,
            Set sysRoleFunctions)
    {
        this.srolId = srolId;
        this.srolName = srolName;
        this.srolCode = srolCode;
        this.sysRoleFunctions = sysRoleFunctions;
    }

    public Long getSrolId()
    {
        return this.srolId;
    }

    public void setSrolId(Long srolId)
    {
        this.srolId = srolId;
    }

    public String getSrolName()
    {
        return this.srolName;
    }

    public void setSrolName(String srolName)
    {
        this.srolName = srolName;
    }

    public String getSrolCode()
    {
        return this.srolCode;
    }

    public void setSrolCode(String srolCode)
    {
        this.srolCode = srolCode;
    }

    public String getSrolDesc()
    {
        return this.srolDesc;
    }

    public void setSrolDesc(String srolDesc)
    {
        this.srolDesc = srolDesc;
    }

    public Set getSysRoleFunctions()
    {
        return this.sysRoleFunctions;
    }

    public void setSysRoleFunctions(Set sysRoleFunctions)
    {
        this.sysRoleFunctions = sysRoleFunctions;
    }

    public String toString()
    {
        return new ToStringBuilder(this).append("srolId", getSrolId())
                .toString();
    }

    public Boolean getSrolIsSysRole()
    {
        return srolIsSysRole;
    }

    public void setSrolIsSysRole(Boolean srolIsSysRole)
    {
        this.srolIsSysRole = srolIsSysRole;
    }
    
    /**
     * added by liliang
     */
    @Override
    public boolean equals(Object obj) {
    	// TODO Auto-generated method stub
    	SysRole role = (SysRole)obj;
    	return this.srolId.longValue() == role.getSrolId().longValue();
    }

}
