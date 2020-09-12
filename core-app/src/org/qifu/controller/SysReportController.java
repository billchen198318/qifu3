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
package org.qifu.controller;

import java.io.IOException;

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.TbSysJreport;
import org.qifu.core.entity.TbSysJreportParam;
import org.qifu.core.entity.TbSysUpload;
import org.qifu.core.logic.ISystemJreportLogicService;
import org.qifu.core.service.ISysJreportParamService;
import org.qifu.core.service.ISysJreportService;
import org.qifu.core.service.ISysUploadService;
import org.qifu.util.SimpleUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysReportController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	ISysJreportService<TbSysJreport, String> sysJreportService;
	
	ISysJreportParamService<TbSysJreportParam, String> sysJreportParamService;
	
	ISysUploadService<TbSysUpload, String> sysUploadService;
	
	ISystemJreportLogicService systemJreportLogicService;	
	
	@Override
	public String viewPageNamespace() {
		return "sys_rpt";
	}
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		
	}
	
	private void fetch(String oid, ModelMap mm) throws ServiceException, ControllerException, Exception {
		DefaultResult<TbSysJreport> result = this.sysJreportService.selectByPrimaryKeySimple(oid);
		if ( result.getValue() == null ) {
			throw new ControllerException( result.getMessage() );
		}
		TbSysJreport sysJreport = result.getValue();
		mm.put("sysJreport", sysJreport);		
	}
	
	@RequestMapping(value = "/sysReportPage")	
	public ModelAndView queryPage(HttpServletRequest request) {
		String viewName = PAGE_SYS_ERROR;
		ModelAndView mv = this.getDefaultModelAndView("CORE_PROG001D0005Q");
		try {
			this.init("queryPage", request, mv);
			viewName = "sys-report/sys-report-management";
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, request);
		} catch (ServiceException | ControllerException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, request);
		} catch (Exception e) {
			this.getExceptionPage(e, request);
		}
		mv.setViewName(viewName);
		return mv;
	}
	
	@RequestMapping(value = "/core.sysReportQueryGridJson.do", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<SysJreportVO>>  queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<SysJreportVO> > result = this.getQueryJsonResult("CORE_PROG001D0005Q");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<SysJreportVO> > queryResult = this.sysJreportService.findGridResult(searchValue, pageOf);
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@RequestMapping(value = "/core.sysReportCreate.do")
	public ModelAndView createPage(HttpServletRequest request) {
		String viewName = PAGE_SYS_ERROR;
		ModelAndView mv = this.getDefaultModelAndView("CORE_PROG001D0005A");
		try {
			this.init("createPage", request, mv);
			viewName = "sys-report/sys-report-create";
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, request);
		} catch (ServiceException | ControllerException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, request);
		} catch (Exception e) {
			this.getExceptionPage(e, request);
		}
		mv.setViewName(viewName);
		return mv;
	}	
	
	@RequestMapping(value = "/core.sysReportEdit.do")
	public ModelAndView editPage(HttpServletRequest request, SysJreportVO sysJreport) {
		String viewName = PAGE_SYS_ERROR;
		ModelAndView mv = this.getDefaultModelAndView("CORE_PROG001D0005E");
		try {
			this.init("editPage", request, mv);
			this.fetchData(sysJreport, mv);
			viewName = "sys-report/sys-report-edit";
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, request);
		} catch (ServiceException | ControllerException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, request);
		} catch (Exception e) {
			this.getExceptionMessage(e);
		}
		mv.setViewName(viewName);
		return mv;
	}	
	
	@RequestMapping(value = "/core.sysReportParam.do")
	public ModelAndView paramPage(HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = PAGE_SYS_ERROR;
		ModelAndView mv = this.getDefaultModelAndView("CORE_PROG001D0005S01Q");
		try {
			SysJreportVO sysJreport = new SysJreportVO();
			sysJreport.setOid(oid);
			this.init("editParamPage", request, mv);
			this.fetchData(sysJreport, mv);
			viewName = "sys-report/sys-report-param";
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, request);
		} catch (ServiceException | ControllerException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, request);
		} catch (Exception e) {
			this.getExceptionPage(e, request);
		}
		mv.setViewName(viewName);
		return mv;
	}		
	
	@RequestMapping(value = "/core.sysJreportParamQueryGridJson.do", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<SysJreportParamVO>>  paramQueryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<SysJreportParamVO> > result = this.getQueryJsonResult("CORE_PROG001D0005S01Q");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<SysJreportParamVO> > queryResult = this.sysJreportParamService.findGridResult(searchValue, pageOf);
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@RequestMapping(value = "/core.sysReportPreview.do")
	public ModelAndView previewPage(HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = PAGE_SYS_ERROR;
		ModelAndView mv = this.getDefaultModelAndView("CORE_PROG001D0005S02Q");
		try {
			SysJreportVO sysJreport = new SysJreportVO();
			sysJreport.setOid(oid);
			this.init("previewPage", request, mv);
			this.fetchData(sysJreport, mv);
			
			sysJreport = (SysJreportVO) mv.getModel().get("sysJreport");
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("reportId", sysJreport.getReportId());
			List<TbSysJreportParam> paramList = this.sysJreportParamService.findListByParams( paramMap );
			mv.addObject("paramList", paramList);
			
			viewName = "sys-report/sys-report-preview";
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, request);
		} catch (ServiceException | ControllerException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, request);
		} catch (Exception e) {
			this.getExceptionPage(e, request);
		}
		mv.setViewName(viewName);
		return mv;
	}		
	
	private void checkFields(DefaultControllerJsonResultObj<TbSysJreport> result, TbSysJreport sysJreport) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("reportId", sysJreport, "@org.apache.commons.lang3.StringUtils@isBlank(reportId)", "Id is blank!")
		.testField("reportId", ( !SimpleUtils.checkBeTrueOf_azAZ09(super.defaultString(sysJreport.getReportId()).replaceAll("-", "").replaceAll("_", "")) ), "Id only normal character!")
		.testField("reportId", ( this.noSelect(sysJreport.getReportId()) ), "Please change Id value!") // 不能用  "all" 這個下拉值
		.throwMessage();
	}
	
	private void checkFieldsForParam(DefaultControllerJsonResultObj<TbSysJreportParam> result, TbSysJreportParam param) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("urlParam", param, "@org.apache.commons.lang3.StringUtils@isBlank(urlParam)", "URL parameter is blank!")
		.testField("rptParam", param, "@org.apache.commons.lang3.StringUtils@isBlank(rptParam)", "Report variable is blank!")
		.testField("urlParam", param, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09(urlParam)", "URL parameter only normal character!")
		.testField("rptParam", param, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09( @org.apache.commons.lang3.StringUtils@defaultString(rptParam).replaceAll(\"_\", \"\") )", "Report variable only normal character!")
		.throwMessage();
	}	
	
	private void fillUploadFileContent(DefaultControllerJsonResultObj<TbSysJreport> result, TbSysJreport sysJreport, String uploadOid) throws ServiceException, IOException, Exception {
		DefaultResult<TbSysUpload> uResult = this.sysUploadService.selectByPrimaryKeySimple(uploadOid);
		if ( uResult.getValue() == null ) {
			throw new ServiceException( uResult.getMessage() );
		}
		String fileNameHead = uResult.getValue().getShowName().split("[.]")[0];
		this.getCheckControllerFieldHandler(result).testField("reportId", ( !fileNameHead.equals(sysJreport.getReportId()) ), "Please change Id value as " + fileNameHead).throwMessage();
		sysJreport.setContent( UploadSupportUtils.getDataBytes(uploadOid) );		
	}
	
	private void save(DefaultControllerJsonResultObj<SysJreportVO> result, SysJreportVO sysJreport, String uploadOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysJreport);
		if (StringUtils.isBlank(uploadOid)) {
			throw new ControllerException("Please upload report file!");
		}		
		JReportUtils.selfTestDecompress4Upload(uploadOid);
		this.fillUploadFileContent(result, sysJreport, uploadOid);
		DefaultResult<SysJreportVO> rResult = this.systemJreportLogicService.create(sysJreport);
		if ( rResult.getValue() != null ) {
			JReportUtils.deployReport( rResult.getValue() );
			rResult.getValue().setContent( null ); // 不傳回 content byte[] 內容
			result.setValue( rResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( rResult.getSystemMessage().getValue() );
	}	
	
	private void update(DefaultControllerJsonResultObj<SysJreportVO> result, SysJreportVO sysJreport, String uploadOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysJreport);
		if (!StringUtils.isBlank(uploadOid)) {
			JReportUtils.selfTestDecompress4Upload(uploadOid);
			this.fillUploadFileContent(result, sysJreport, uploadOid);
		}
		DefaultResult<SysJreportVO> rResult = this.systemJreportLogicService.update(sysJreport);
		if ( rResult.getValue() != null ) {
			if (!StringUtils.isBlank(uploadOid)) { // 由於 content 內容改變了(有重新上傳報表), 所以重新部屬
				JReportUtils.deployReport( rResult.getValue() );
			}
			rResult.getValue().setContent( null ); // 不傳回 content byte[] 內容
			result.setValue( rResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( rResult.getSystemMessage().getValue() );		
	}
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, SysJreportVO sysJreport) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> tResult = this.systemJreportLogicService.delete(sysJreport);
		if ( tResult.getValue() != null && tResult.getValue() ) {
			result.setValue( Boolean.TRUE );
			result.setSuccess( YES );
		}
		result.setMessage( tResult.getSystemMessage().getValue() );
	}	
	
	private void saveParam(DefaultControllerJsonResultObj<SysJreportParamVO> result, SysJreportParamVO sysJreportParam, String reportOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFieldsForParam(result, sysJreportParam);
		DefaultResult<SysJreportParamVO> pResult = this.systemJreportLogicService.createParam(sysJreportParam, reportOid);
		if ( pResult.getValue() != null ) {
			result.setValue( pResult.getValue() );
			result.setSuccess(YES);
		}
		result.setMessage( pResult.getSystemMessage().getValue() );
	}
	
	private void deleteParam(DefaultControllerJsonResultObj<Boolean> result, SysJreportParamVO sysJreportParam) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> pResult = this.systemJreportLogicService.deleteParam(sysJreportParam);
		if ( pResult.getValue() != null && pResult.getValue() ) {
			result.setValue( Boolean.TRUE );
			result.setSuccess( YES );
		}
		result.setMessage( pResult.getSystemMessage().getValue() );
	}
	
	@RequestMapping(value = "/core.sysReportSaveJson.do", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<SysJreportVO> doSave(SysJreportVO sysJreport, @RequestParam("uploadOid") String uploadOid) {
		DefaultControllerJsonResultObj<SysJreportVO> result = this.getDefaultJsonResult("CORE_PROG001D0005A");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, sysJreport, uploadOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	@RequestMapping(value = "/core.sysReportUpdateJson.do", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DefaultControllerJsonResultObj<SysJreportVO> doUpdate(SysJreportVO sysJreport, @RequestParam("uploadOid") String uploadOid) {
		DefaultControllerJsonResultObj<SysJreportVO> result = this.getDefaultJsonResult("CORE_PROG001D0005E");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, sysJreport, uploadOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@RequestMapping(value = "/core.sysReportDeleteJson.do", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(SysJreportVO sysJreport) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult("CORE_PROG001D0005D");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, sysJreport);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@RequestMapping(value = "/core.sysReportDownloadContentJson.do", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<String> doDownloadContent(SysJreportVO sysJreport) {
		DefaultControllerJsonResultObj<String> result = this.getDefaultJsonResult("CORE_PROG001D0005Q");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			DefaultResult<SysJreportVO> rResult = this.sysJreportService.findObjectByOid(sysJreport);
			if ( rResult.getValue() == null ) {
				throw new ControllerException( rResult.getSystemMessage().getValue() );
			}
			sysJreport = rResult.getValue();
			result.setValue( UploadSupportUtils.create(Constants.getSystem(), UploadTypes.IS_TEMP, true, sysJreport.getContent(), sysJreport.getReportId()+".zip") );
			result.setSuccess( YES );
			result.setMessage( SysMessageUtil.get(SysMsgConstants.INSERT_SUCCESS) );
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@RequestMapping(value = "/core.sysJreportParamSaveJson.do", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<SysJreportParamVO> doParamSave(SysJreportParamVO sysJreportParam, @RequestParam("reportOid") String reportOid) {
		DefaultControllerJsonResultObj<SysJreportParamVO> result = this.getDefaultJsonResult("CORE_PROG001D0005S01A");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.saveParam(result, sysJreportParam, reportOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	@RequestMapping(value = "/sysJreportParamDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doParamDelete(TbSysJreportParam sysJreportParam) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult("CORE_PROG001D0005S01D");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.deleteParam(result, sysJreportParam);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			result.setMessage( e.getMessage().toString() );			
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
}
