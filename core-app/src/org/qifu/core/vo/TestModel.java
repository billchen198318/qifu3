/**
 * test
 */
package org.qifu.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TestModel", description = "測試模組")
public class TestModel implements java.io.Serializable {
	private static final long serialVersionUID = -5289888544046385634L;
	
	@ApiModelProperty(name = "key", value = "編號", required = true, dataType = "string", position = 1)
	private String key;
	
	@ApiModelProperty(name = "msg", value = "訊息資料", required = true, dataType = "string", position = 2)
	private String msg;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
