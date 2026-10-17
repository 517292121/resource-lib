package cn.com.adtech.riss2.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author MaChao */
public class SysUserRole implements Serializable
{

    /** identifier field */
    private Long suroId;
    /** persistent field */
    private SysManager user;
    private Long sysUserId;

    /** nullable persistent field */
    private SysRole role;
    
    /** default constructor */
    public SysUserRole()
    {
    }

	public Long getSuroId()
    {
        return this.suroId;
    }

    public void setSuroId(Long suroId)
    {
        this.suroId = suroId;
    }

    public SysRole getRole()
    {
        return this.role;
    }

    public void setRole(SysRole sysRole)
    {
        this.role = sysRole;
    }

    public String toString()
    {
        return new ToStringBuilder(this).append("suroId", getSuroId())
                .toString();
    }

    public SysManager getUser()
    {
        return user;
    }

    public void setUser(SysManager user)
    {
        this.user = user;
    }
    public Long getSysUserId(){
    	return sysUserId;
    }
    public void setSysUserId(Long sysUserId){
    	this.sysUserId=sysUserId;
    }

}
