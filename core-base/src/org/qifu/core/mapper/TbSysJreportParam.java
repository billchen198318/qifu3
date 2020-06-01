package org.qifu.core.mapper;

import java.util.Date;

import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;

public class TbSysJreportParam implements java.io.Serializable {
	private static final long serialVersionUID = 3958361031577724660L;
	
	private String oid;
	private String reportId;
	private String urlParam;
	private String rptParam;
	private String cuserid;
	private Date cdate;
	private String uuserid;
	private Date udate;
	
	@EntityPK(name = "oid", autoUUID = true)
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	@EntityUK(name = "reportId")
	public String getReportId() {
		return reportId;
	}
	
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	public String getUrlParam() {
		return urlParam;
	}
	
	public void setUrlParam(String urlParam) {
		this.urlParam = urlParam;
	}
	
	@EntityUK(name = "rptParam")
	public String getRptParam() {
		return rptParam;
	}
	
	public void setRptParam(String rptParam) {
		this.rptParam = rptParam;
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
