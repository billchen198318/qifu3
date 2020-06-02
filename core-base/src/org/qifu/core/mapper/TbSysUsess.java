package org.qifu.core.mapper;

import java.util.Date;

import org.qifu.base.model.EntityUK;

public class TbSysUsess extends TbSysUsessKey {
	private static final long serialVersionUID = 4699630657747152165L;
	
	private String account;
	private String currentId;
	private String cuserid;
	private Date cdate;
	private String uuserid;
	private Date udate;
	
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	@EntityUK(name = "currentId")
	public String getCurrentId() {
		return currentId;
	}
	
	public void setCurrentId(String currentId) {
		this.currentId = currentId;
	}
	
	public String getCuserid() {
		return cuserid;
	}
	
	public void setCuserid(String cuserid) {
		this.cuserid = cuserid;
	}
	
	public Date getCdate() {
		return cdate;
	}
	
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	
	public String getUuserid() {
		return uuserid;
	}
	
	public void setUuserid(String uuserid) {
		this.uuserid = uuserid;
	}
	
	public Date getUdate() {
		return udate;
	}
	
	public void setUdate(Date udate) {
		this.udate = udate;
	}
	
}