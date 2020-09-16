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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.qifu.base.controller.BaseControllerSupport;
import org.qifu.base.controller.IPageNamespaceProvide;
import org.qifu.base.exception.AuthorityException;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.CheckControllerFieldHandler;
import org.qifu.base.model.DefaultControllerJsonResultObj;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.QueryControllerJsonResultObj;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysBeanHelp;
import org.qifu.core.entity.TbSysBeanHelpExpr;
import org.qifu.core.entity.TbSysBeanHelpExprMap;
import org.qifu.core.entity.TbSysExpression;
import org.qifu.core.logic.ISystemBeanHelpLogicService;
import org.qifu.core.model.ScriptExpressionRunType;
import org.qifu.core.service.ISysBeanHelpExprMapService;
import org.qifu.core.service.ISysBeanHelpExprService;
import org.qifu.core.service.ISysBeanHelpService;
import org.qifu.core.service.ISysExpressionService;
import org.qifu.core.service.ISysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysBeanSupportController extends BaseControllerSupport implements IPageNamespaceProvide {
	
	@Autowired
	ISysBeanHelpService<TbSysBeanHelp, String> sysBeanHelpService;
	
	@Autowired
	ISysService<TbSys, String> sysService;
	
	@Autowired
	ISysExpressionService<TbSysExpression, String> sysExpressionService;
	
	@Autowired
	ISysBeanHelpExprService<TbSysBeanHelpExpr, String> sysBeanHelpExprService;
	
	@Autowired
	ISysBeanHelpExprMapService<TbSysBeanHelpExprMap, String> sysBeanHelpExprMapService;
	
	@Autowired
	ISystemBeanHelpLogicService systemBeanHelpLogicService;
	
	@Override
	public String viewPageNamespace() {
		return "sys_beansup";
	}	
	
	private void init(String type, ModelMap mm) throws AuthorityException, ControllerException, ServiceException, Exception {
		if ("mainPage".equals(type) || "createPage".equals(type) || "editPage".equals(type)) {
			mm.put( "sysMap", sysService.findSysMap(true) );
		}
		if ("editExpressionPage".equals(type)) {
			mm.put( "runTypeMap", ScriptExpressionRunType.getRunTypeMap(true) );
			mm.put( "expressionMap", sysExpressionService.findExpressionMap(true) );
		}
	}
	
	private void fetch(String sysBeanHelpOid, ModelMap mm) throws ServiceException, ControllerException, Exception {
		TbSysBeanHelp sysBeanHelp = this.sysBeanHelpService.selectByPrimaryKey(sysBeanHelpOid).getValueEmptyThrowMessage();
		mm.put("sysBeanHelp", sysBeanHelp);
		TbSys sys = new TbSys();
		sys.setSysId( sysBeanHelp.getSystem() );
		sys = this.sysService.selectByUniqueKey(sys).getValueEmptyThrowMessage();
		mm.put("systemOid", sys.getOid());
	}
	
	private void fetchExpr(String sysBeanHelpExprOid, ModelMap mm) throws ServiceException, ControllerException, Exception {
		TbSysBeanHelpExpr sysBeanHelpExpr = this.sysBeanHelpExprService.selectByPrimaryKey(sysBeanHelpExprOid).getValueEmptyThrowMessage();
		mm.put("sysBeanHelpExpr", sysBeanHelpExpr);
	}
	
	//CORE_PROG003D0003Q
	@RequestMapping(value = "/sysBeanSupportPage")	
	public String mainPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewMainPage();
		this.getDefaultModelMap(mm, "CORE_PROG003D0003Q");
		try {
			this.init("mainPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
	//CORE_PROG003D0003Q
	@RequestMapping(value = "/sysBeanSupportQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<TbSysBeanHelp> > queryGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<TbSysBeanHelp> > result = this.getQueryJsonResult("CORE_PROG003D0003Q");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<TbSysBeanHelp> > queryResult = this.sysBeanHelpService.findPage(
					this.queryParameter(searchValue).selectOption("systemOid").fullEquals("beanId").value(), 
					pageOf.orderBy("SYSTEM,BEAN_ID").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	//CORE_PROG003D0003A
	@RequestMapping(value = "/sysBeanSupportCreatePage")
	public String createPage(ModelMap mm, HttpServletRequest request) {
		String viewName = this.viewCreatePage();
		this.getDefaultModelMap(mm, "CORE_PROG003D0003A");
		try {
			this.init("createPage", mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}
		return viewName;
	}	
	
	//CORE_PROG003D0003E
	@RequestMapping(value = "/sysBeanSupportEditPage")
	public String editPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewEditPage();
		this.getDefaultModelMap(mm, "CORE_PROG003D0003E");
		try {
			this.init("editPage", mm);
			this.fetch(oid, mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}
	
	//CORE_PROG003D0003S01Q
	@RequestMapping(value = "/sysBeanSupportExpressionPage")
	public String editExpressionPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewPageWithNamespace("expr");
		this.getDefaultModelMap(mm, "CORE_PROG003D0003S01Q");
		try {
			this.init("editExpressionPage", mm);
			this.fetch(oid, mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}	
	
	//CORE_PROG003D0003S01Q
	@RequestMapping(value = "/sysBeanSupportExpressionQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<TbSysBeanHelpExpr> > queryExpressionGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<TbSysBeanHelpExpr> > result = this.getQueryJsonResult("CORE_PROG003D0003S01Q");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<TbSysBeanHelpExpr> > queryResult = this.sysBeanHelpExprService.findPage(
					this.queryParameter(searchValue).addField("helpOid", true).value(), 
					pageOf.orderBy("EXPR_ID,EXPR_SEQ").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	//CORE_PROG003D0003S02Q
	@RequestMapping(value = "/sysBeanSupportExpressionParamPage")
	public String editExpressionParamPage(ModelMap mm, HttpServletRequest request, @RequestParam(name="oid") String oid) {
		String viewName = this.viewPageWithNamespace("expr-map");
		this.getDefaultModelMap(mm, "CORE_PROG003D0003S02Q");
		try {
			this.init("editExpressionParamPage", mm);
			this.fetchExpr(oid, mm);
		} catch (AuthorityException e) {
			viewName = this.getAuthorityExceptionPage(e, mm);
		} catch (ControllerException | ServiceException e) {
			viewName = this.getServiceOrControllerExceptionPage(e, mm);
		} catch (Exception e) {
			viewName = this.getExceptionPage(e, mm);
		}	
		return viewName;
	}	
	
	//CORE_PROG003D0003S02Q
	@RequestMapping(value = "/sysBeanSupportExpressionParamQueryGridJson", produces = MediaType.APPLICATION_JSON_VALUE)	
	public @ResponseBody QueryControllerJsonResultObj< List<TbSysBeanHelpExprMap> > queryExpressionParamGrid(SearchValue searchValue, PageOf pageOf) {
		QueryControllerJsonResultObj< List<TbSysBeanHelpExprMap> > result = this.getQueryJsonResult("CORE_PROG003D0003S02Q");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			QueryResult< List<TbSysBeanHelpExprMap> > queryResult = this.sysBeanHelpExprMapService.findPage(
					this.queryParameter(searchValue).addField("helpExprOid", true).value(), 
					pageOf.orderBy("METHOD_RESULT_FLAG, METHOD_PARAM_INDEX").sortTypeAsc());
			this.setQueryGridJsonResult(result, queryResult, pageOf);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	private void checkFields(DefaultControllerJsonResultObj<TbSysBeanHelp> result, TbSysBeanHelp sysBeanHelp, String systemOid) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("systemOid", ( PleaseSelect.noSelect(systemOid) ), "Please select system!")
		.testField("beanId", sysBeanHelp, "@org.apache.commons.lang3.StringUtils@isBlank( beanId )", "Bean Id is blank!")
		.testField("beanId", sysBeanHelp, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09( beanId.replaceAll(\"[.]\", \"\") )", "Bean Id error!")
		.testField("method", sysBeanHelp, "@org.apache.commons.lang3.StringUtils@isBlank( method )", "Method is blank!")
		.testField("method", sysBeanHelp, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09( method.replaceAll(\"_\", \"\") )", "Method name error!")
		.throwMessage();
	}
	
	private void checkFieldsForExpression(DefaultControllerJsonResultObj<TbSysBeanHelpExpr> result, TbSysBeanHelpExpr sysBeanHelpExpr, String sysBeanHelpOid, String expressionOid) throws ControllerException, Exception {
		this.getCheckControllerFieldHandler(result)
		.testField("expressionOid", ( PleaseSelect.noSelect(expressionOid) ), "Please select expression!")
		.testField("exprSeq", sysBeanHelpExpr, "@org.apache.commons.lang3.StringUtils@isBlank( exprSeq )", "Seq is blank!")
		.testField("exprSeq", sysBeanHelpExpr, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09( exprSeq )", "Seq only normal character!")
		.testField("runType", ( PleaseSelect.noSelect(sysBeanHelpExpr.getRunType()) ), "Please select process type!")
		.throwMessage();		
	}
	
	private void checkFieldsForExpressionMap(DefaultControllerJsonResultObj<TbSysBeanHelpExprMap> result, TbSysBeanHelpExprMap sysBeanHelpExprMap, String sysBeanHelpExprOid) throws ControllerException, Exception {
		CheckControllerFieldHandler< TbSysBeanHelpExprMap > checkHandler = this.getCheckControllerFieldHandler(result)
		.testField("varName", sysBeanHelpExprMap, "@org.apache.commons.lang3.StringUtils@isBlank( varName )", "Variable name is blank!")
		.testField("varName", sysBeanHelpExprMap, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09( varName )", "Variable name only normal character!");
		if (!YES.equals(sysBeanHelpExprMap.getMethodResultFlag())) {
			checkHandler
			.testField("methodParamClass", sysBeanHelpExprMap, "@org.apache.commons.lang3.StringUtils@isBlank( methodParamClass )", "Method class name is blank!")
			.testField("methodParamClass", sysBeanHelpExprMap, "!@org.qifu.util.SimpleUtils@checkBeTrueOf_azAZ09( methodParamClass.replaceAll(\"[.]\", \"\") )", "Method class name error!")
			.testField("methodParamIndex", sysBeanHelpExprMap, "methodParamIndex < 0", "Method parameter index error!");
		}
		checkHandler.throwMessage();
	}
	
	private void save(DefaultControllerJsonResultObj<TbSysBeanHelp> result, TbSysBeanHelp sysBeanHelp, String systemOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysBeanHelp, systemOid);
		DefaultResult<TbSysBeanHelp> cResult = this.systemBeanHelpLogicService.create(sysBeanHelp, systemOid);
		if ( cResult.getValue() != null ) {
			result.setValue( cResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( cResult.getMessage());		
	}	
	
	private void update(DefaultControllerJsonResultObj<TbSysBeanHelp> result, TbSysBeanHelp sysBeanHelp, String systemOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFields(result, sysBeanHelp, systemOid);
		DefaultResult<TbSysBeanHelp> uResult = this.systemBeanHelpLogicService.update(sysBeanHelp, systemOid);
		if ( uResult.getValue() != null ) {
			result.setValue( uResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( uResult.getMessage() );		
	}	
	
	private void delete(DefaultControllerJsonResultObj<Boolean> result, TbSysBeanHelp sysBeanHelp) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.systemBeanHelpLogicService.delete(sysBeanHelp);
		if ( dResult.getValue() != null && dResult.getValue() ) {
			result.setValue( dResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( dResult.getMessage() );		
	}	 
	
	private void saveExpression(DefaultControllerJsonResultObj<TbSysBeanHelpExpr> result, TbSysBeanHelpExpr sysBeanHelpExpr, String sysBeanHelpOid, String expressionOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFieldsForExpression(result, sysBeanHelpExpr, sysBeanHelpOid, expressionOid);
		DefaultResult<TbSysBeanHelpExpr> cResult = this.systemBeanHelpLogicService.createExpr(sysBeanHelpExpr, sysBeanHelpOid, expressionOid);
		if ( cResult.getValue() != null ) {
			result.setValue( cResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( cResult.getMessage() );			
	}
	
	private void deleteExpression(DefaultControllerJsonResultObj<Boolean> result, TbSysBeanHelpExpr sysBeanHelpExpr) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.systemBeanHelpLogicService.deleteExpr(sysBeanHelpExpr);
		if ( dResult.getValue() != null && dResult.getValue() ) {
			result.setValue( dResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( dResult.getMessage() );			
	}
	
	private void saveExpressionMap(DefaultControllerJsonResultObj<TbSysBeanHelpExprMap> result, TbSysBeanHelpExprMap sysBeanHelpExprMap, String sysBeanHelpExprOid) throws AuthorityException, ControllerException, ServiceException, Exception {
		this.checkFieldsForExpressionMap(result, sysBeanHelpExprMap, sysBeanHelpExprOid);
		DefaultResult<TbSysBeanHelpExprMap> cResult = this.systemBeanHelpLogicService.createExprMap(sysBeanHelpExprMap, sysBeanHelpExprOid);
		if ( cResult.getValue() != null ) {
			result.setValue( cResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( cResult.getMessage() );			
	}	
	
	private void deleteExpressionMap(DefaultControllerJsonResultObj<Boolean> result, TbSysBeanHelpExprMap sysBeanHelpExprMap) throws AuthorityException, ControllerException, ServiceException, Exception {
		DefaultResult<Boolean> dResult = this.systemBeanHelpLogicService.deleteExprMap(sysBeanHelpExprMap);
		if ( dResult.getValue() != null && dResult.getValue() ) {
			result.setValue( dResult.getValue() );
			result.setSuccess( YES );
		}
		result.setMessage( dResult.getMessage() );			
	}	
	
	//CORE_PROG003D0003A
	@RequestMapping(value = "/sysBeanSupportSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysBeanHelp> doSave(TbSysBeanHelp sysBeanHelp, @RequestParam("systemOid") String systemOid) {
		DefaultControllerJsonResultObj<TbSysBeanHelp> result = this.getDefaultJsonResult("CORE_PROG003D0003A");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.save(result, sysBeanHelp, systemOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	//CORE_PROG003D0003E
	@RequestMapping(value = "/sysBeanSupportUpdateJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysBeanHelp> doUpdate(TbSysBeanHelp sysBeanHelp, @RequestParam("systemOid") String systemOid) {
		DefaultControllerJsonResultObj<TbSysBeanHelp> result = this.getDefaultJsonResult("CORE_PROG003D0003E");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.update(result, sysBeanHelp, systemOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);		
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	//CORE_PROG003D0003D
	@RequestMapping(value = "/sysBeanSupportDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDelete(TbSysBeanHelp sysBeanHelp) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult("CORE_PROG003D0003D");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.delete(result, sysBeanHelp);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}	
	
	//CORE_PROG003D0003S01A
	@RequestMapping(value = "/sysBeanSupportExpressionSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysBeanHelpExpr> doSaveExpression(TbSysBeanHelpExpr sysBeanHelpExpr, @RequestParam("sysBeanHelpOid") String sysBeanHelpOid, @RequestParam("expressionOid") String expressionOid) {
		DefaultControllerJsonResultObj<TbSysBeanHelpExpr> result = this.getDefaultJsonResult("CORE_PROG003D0003S01A");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.saveExpression(result, sysBeanHelpExpr, sysBeanHelpOid, expressionOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	//CORE_PROG003D0003S01D
	@RequestMapping(value = "/sysBeanSupportExpressionDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDeleteExpression(TbSysBeanHelpExpr sysBeanHelpExpr) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult("CORE_PROG003D0003S01D");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.deleteExpression(result, sysBeanHelpExpr);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}		
	
	//CORE_PROG003D0003S02A
	@RequestMapping(value = "/sysBeanSupportExpressionParamSaveJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<TbSysBeanHelpExprMap> doSaveExpressionMap(TbSysBeanHelpExprMap sysBeanHelpExprMap, @RequestParam("sysBeanHelpExprOid") String sysBeanHelpExprOid) {
		DefaultControllerJsonResultObj<TbSysBeanHelpExprMap> result = this.getDefaultJsonResult("CORE_PROG003D0003S02A");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.saveExpressionMap(result, sysBeanHelpExprMap, sysBeanHelpExprOid);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);	
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
	//CORE_PROG003D0003S02D
	@RequestMapping(value = "/sysBeanSupportExpressionParamDeleteJson", produces = MediaType.APPLICATION_JSON_VALUE)		
	public @ResponseBody DefaultControllerJsonResultObj<Boolean> doDeleteExpressionMap(TbSysBeanHelpExprMap sysBeanHelpExprMap) {
		DefaultControllerJsonResultObj<Boolean> result = this.getDefaultJsonResult("CORE_PROG003D0003S02D");
		if (!this.isAuthorizeAndLoginFromControllerJsonResult(result)) {
			return result;
		}
		try {
			this.deleteExpressionMap(result, sysBeanHelpExprMap);
		} catch (AuthorityException | ServiceException | ControllerException e) {
			this.baseExceptionResult(result, e);
		} catch (Exception e) {
			this.exceptionResult(result, e);
		}
		return result;
	}
	
}