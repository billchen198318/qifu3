package org.qifu.core.vo;

import org.qifu.base.model.YesNo;

public class ResponseResult implements java.io.Serializable {
	private static final long serialVersionUID = -635281624341584427L;
	
	private String flag = YesNo.NO;
	private String message = "";
	
	public static ResponseResult build() {
		return new ResponseResult();
	}
	
	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
