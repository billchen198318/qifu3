/* 
 * Copyright 2012-2017 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.qifu.ui.impl.ToolBar;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ToolBarTag extends TagSupport {
	
	private String id="";
	private String createNewEnable="";
	private String saveEnabel="";
	private String refreshEnable="";
	private String cancelEnable="";
	private String exportEnable="";
	private String importEnable="";	
	private String createNewJsMethod="";
	private String saveJsMethod="";
	private String refreshJsMethod="";
	private String cancelJsMethod="";
	private String exportJsMethod="";
	private String importJsMethod="";	
	private String programName="";
	private String programId="";
	private String description="";
	
	private ToolBar handler() {
		ToolBar toolBar = new ToolBar();
		toolBar.setServletRequestAttributes((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
		toolBar.setId(this.id);
		toolBar.setCreateNewEnable(this.createNewEnable);
		toolBar.setSaveEnabel(this.saveEnabel);
		toolBar.setRefreshEnable(this.refreshEnable);
		toolBar.setCancelEnable(this.cancelEnable);
		toolBar.setExportEnable(this.exportEnable);
		toolBar.setImportEnable(this.importEnable);
		toolBar.setCreateNewJsMethod(this.createNewJsMethod);
		toolBar.setSaveJsMethod(this.saveJsMethod);
		toolBar.setRefreshJsMethod(this.refreshJsMethod);
		toolBar.setCancelJsMethod(this.cancelJsMethod);
		toolBar.setExportJsMethod(this.exportJsMethod);
		toolBar.setImportJsMethod(this.importJsMethod);
		toolBar.setProgramName(this.programName);
		toolBar.setProgramId(this.programId);
		toolBar.setDescription(this.description);
		return toolBar;
	}
	
	@Override
	public int doEndTag() throws JspException {
		ToolBar toolBar = this.handler();
		try {
			this.pageContext.getOut().write( toolBar.getHtml() );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		toolBar = null;
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {
		return 0;
	}
	
	@Override
	public void release() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateNewEnable() {
		return createNewEnable;
	}

	public void setCreateNewEnable(String createNewEnable) {
		this.createNewEnable = createNewEnable;
	}

	public String getSaveEnabel() {
		return saveEnabel;
	}

	public void setSaveEnabel(String saveEnabel) {
		this.saveEnabel = saveEnabel;
	}

	public String getRefreshEnable() {
		return refreshEnable;
	}

	public void setRefreshEnable(String refreshEnable) {
		this.refreshEnable = refreshEnable;
	}

	public String getCancelEnable() {
		return cancelEnable;
	}

	public void setCancelEnable(String cancelEnable) {
		this.cancelEnable = cancelEnable;
	}

	public String getExportEnable() {
		return exportEnable;
	}

	public void setExportEnable(String exportEnable) {
		this.exportEnable = exportEnable;
	}

	public String getImportEnable() {
		return importEnable;
	}

	public void setImportEnable(String importEnable) {
		this.importEnable = importEnable;
	}

	public String getCreateNewJsMethod() {
		return createNewJsMethod;
	}

	public void setCreateNewJsMethod(String createNewJsMethod) {
		this.createNewJsMethod = createNewJsMethod;
	}

	public String getSaveJsMethod() {
		return saveJsMethod;
	}

	public void setSaveJsMethod(String saveJsMethod) {
		this.saveJsMethod = saveJsMethod;
	}

	public String getRefreshJsMethod() {
		return refreshJsMethod;
	}

	public void setRefreshJsMethod(String refreshJsMethod) {
		this.refreshJsMethod = refreshJsMethod;
	}

	public String getCancelJsMethod() {
		return cancelJsMethod;
	}

	public void setCancelJsMethod(String cancelJsMethod) {
		this.cancelJsMethod = cancelJsMethod;
	}

	public String getExportJsMethod() {
		return exportJsMethod;
	}

	public void setExportJsMethod(String exportJsMethod) {
		this.exportJsMethod = exportJsMethod;
	}

	public String getImportJsMethod() {
		return importJsMethod;
	}

	public void setImportJsMethod(String importJsMethod) {
		this.importJsMethod = importJsMethod;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
