/*
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
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
package org.qifu.directive;

import java.io.IOException;
import java.util.Map;

import org.qifu.ui.PageUiDirectiveUtils;
import org.qifu.ui.impl.ToolBar;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ToolBarDirective implements TemplateDirectiveModel {
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
    public void execute(Environment env, Map paramMap, TemplateModel[] models, TemplateDirectiveBody body) throws TemplateException, IOException {
    	ToolBar comp = this.handler();
    	PageUiDirectiveUtils.write(env, paramMap, comp);
        comp = null;    	
    }	
    
}
