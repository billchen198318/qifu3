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
package org.qifu.base.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.CreateField;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.UpdateField;
import org.qifu.base.util.EntityParameterGenerateUtil;
import org.qifu.base.util.UserLocalUtils;
import org.qifu.util.SimpleUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ognl.DefaultMemberAccess;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

/**
 * @param <T>	MyBatis entity
 * @param <K>	PK屬性
 */
public abstract class BaseService<T extends java.io.Serializable, K extends java.io.Serializable> implements IBaseService<T, K> {
	
	protected abstract IBaseMapper<T, K> getBaseMapper();
	
	// 自訂義主鍵值生成abstract , 移動到 IBaseServiceCustomPrimaryKeyProvide
	//protected abstract K generateCustomPrimaryKey();
	
	private OgnlContext ognlContext = new OgnlContext(null,null,new DefaultMemberAccess(true));
	private boolean foundCustomPrimaryKeyProvide = false;
	
	public BaseService() {
		super();
		if ( this instanceof IBaseServiceCustomPrimaryKeyProvide ) { // check實作類是否有 implements IBaseServiceCustomPrimaryKeyProvide, 自定義 PK生成method.
			foundCustomPrimaryKeyProvide = true;
		}
	}	
	
	@SuppressWarnings("unchecked")
	private void setEntityPrimaryKey(T entity) {
		EntityPK primaryKeyField = EntityParameterGenerateUtil.getPrimaryKeyField(entity);
		if ( null == primaryKeyField || StringUtils.isBlank(primaryKeyField.name()) ) {
			return;
		}
		Object value = null;
		if (primaryKeyField.autoUUID()) {
			value = SimpleUtils.getUUIDStr();
		} /* else {
			value = this.generateCustomPrimaryKey();
		} */
		if (foundCustomPrimaryKeyProvide) {
			value = ( (IBaseServiceCustomPrimaryKeyProvide<T, K>) this ).generateCustomPrimaryKey();
		}
		if (null == value) {
			return;
		}		
		try {
			Ognl.setValue(primaryKeyField.name(), this.ognlContext, entity, value);
		} catch (OgnlException e) {
			e.printStackTrace();
		}
	}
	
	private void setEntityCreateUserField(T entity) {
		CreateField field = EntityParameterGenerateUtil.getCreateField(entity);
		if (null == field) {
			return;
		}
		if ( field.getCreateUserField() != null && !StringUtils.isBlank(field.getCreateUserField().name()) ) {
			try {
				// FIXME: 要改 UserLocalUtils 為 Apache-shiro 或別的登入session管理元件
				Ognl.setValue(field.getCreateUserField().name(), this.ognlContext, entity, UserLocalUtils.getUserInfo().getUserId());
			} catch (OgnlException oe) {
				oe.printStackTrace();
			}			
		}
		if ( field.getCreateDateField() != null && !StringUtils.isBlank(field.getCreateDateField().name()) ) {
			try {
				Ognl.setValue(field.getCreateDateField().name(), this.ognlContext, entity, new Date());
			} catch (OgnlException oe) {
				oe.printStackTrace();
			}
		}		
	}
	
	private void setEntityUpdateField(T entity) {
		UpdateField field = EntityParameterGenerateUtil.getUpdateField(entity);
		if (null == field) {
			return;
		}
		if ( field.getUpdateUserField() != null && !StringUtils.isBlank(field.getUpdateUserField().name()) ) {
			try {
				// FIXME: 要改 UserLocalUtils 為 Apache-shiro 或別的登入session管理元件
				Ognl.setValue(field.getUpdateUserField().name(), this.ognlContext, entity, UserLocalUtils.getUserInfo().getUserId());
			} catch (OgnlException oe) {
				oe.printStackTrace();
			}
		}
		if ( field.getUpdateDateField() != null && !StringUtils.isBlank(field.getUpdateDateField().name()) ) {
			try {
				Ognl.setValue(field.getUpdateDateField().name(), this.ognlContext, entity, new Date());
			} catch (OgnlException oe) {
				oe.printStackTrace();
			}
		}		
	}
	
	public String defaultString(String source) {
		return StringUtils.defaultString(source);
	}
	
	@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)		
	public DefaultResult<T> selectByPrimaryKey(K pk) throws ServiceException, Exception {
		if (null == pk || StringUtils.isBlank(String.valueOf(pk))) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<T> result = new DefaultResult<T>();
		T value = this.getBaseMapper().selectByPrimaryKey(pk);
		if (value != null) {
			result.setValue(value);
		} else {
			result.setMessage(BaseSystemMessage.searchNoData());
		}
		return result;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
	public DefaultResult<List<T>> selectListByParams(Map<String, Object> paramMap) throws ServiceException, Exception {
		if (null == paramMap || paramMap.size() < 1) {
			throw new ServiceException(BaseSystemMessage.parameterIncorrect());
		}
		DefaultResult<List<T>> result = new DefaultResult<List<T>>();
		List<T> value = (List<T>) this.getBaseMapper().selectListByParams(paramMap);
		if (value != null) {
			result.setValue(value);
		} else {
			result.setMessage(BaseSystemMessage.searchNoData());
		}
		return result;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
	public DefaultResult<T> selectByUniqueKey(T mapperObj) throws ServiceException, Exception {
		if (null == mapperObj) {
			throw new ServiceException(BaseSystemMessage.objectNull());
		}
		Map<String, Object> paramMap = EntityParameterGenerateUtil.getUKParameter(mapperObj);
		if (null == paramMap || paramMap.size() < 1) {
			throw new ServiceException(BaseSystemMessage.parameterIncorrect() + " , please set @EntityUK.");
		}
		List<T> searchList = (List<T>) this.getBaseMapper().selectListByParams(paramMap);
		if (null != searchList && searchList.size() > 0) {
			throw new ServiceException(BaseSystemMessage.dataErrors());
		}
		T value = null;
		if (searchList.size() == 1) {
			value = searchList.get(0);
		}
		DefaultResult<T> result = new DefaultResult<T>();
		if (value != null) {
			result.setValue(value);
		} else {
			result.setMessage(BaseSystemMessage.searchNoData());
		}
		return result;
	}
	
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	public DefaultResult<T> insert(T mapperObj) throws ServiceException, Exception {
		if (null == mapperObj) {
			throw new ServiceException(BaseSystemMessage.objectNull());
		}
		DefaultResult<T> result = new DefaultResult<T>();
		if (this.countByUK(mapperObj) > 0) {
			throw new ServiceException(BaseSystemMessage.dataIsExist());
		}
		this.setEntityPrimaryKey(mapperObj);
		this.setEntityCreateUserField(mapperObj);
		if (this.getBaseMapper().insert(mapperObj) < 1) {
			throw new ServiceException(BaseSystemMessage.insertFail());
		}
		result.setValue(mapperObj);
		result.setMessage(BaseSystemMessage.insertSuccess());
		return result;
	}
	
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	public DefaultResult<T> insertIgnoreUK(T mapperObj) throws ServiceException, Exception {
		if (null == mapperObj) {
			throw new ServiceException(BaseSystemMessage.objectNull());
		}
		DefaultResult<T> result = new DefaultResult<T>();
		this.setEntityPrimaryKey(mapperObj);
		this.setEntityCreateUserField(mapperObj);
		if (this.getBaseMapper().insert(mapperObj) < 1) {
			throw new ServiceException(BaseSystemMessage.insertFail());
		}
		result.setValue(mapperObj);
		result.setMessage(BaseSystemMessage.insertSuccess());
		return result;
	}	
	
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	public DefaultResult<T> update(T mapperObj) throws ServiceException, Exception {
		if (null == mapperObj) {
			throw new ServiceException(BaseSystemMessage.objectNull());
		}
		DefaultResult<T> result = new DefaultResult<T>();
		this.setEntityUpdateField(mapperObj);
		if (this.getBaseMapper().update(mapperObj) < 1) {
			throw new ServiceException(BaseSystemMessage.updateFail());
		}
		result.setValue(mapperObj);
		result.setMessage(BaseSystemMessage.updateSuccess());
		return result;
	}
	
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	public DefaultResult<Boolean> delete(T mapperObj) throws ServiceException, Exception {
		if (null == mapperObj) {
			throw new ServiceException(BaseSystemMessage.objectNull());
		}
		DefaultResult<Boolean> result = new DefaultResult<Boolean>();
		if (!this.getBaseMapper().delete(mapperObj)) {
			throw new ServiceException(BaseSystemMessage.deleteFail());
		}
		result.setValue(true);
		result.setMessage(BaseSystemMessage.deleteSuccess());
		return result;
	}
	
	public Long count(Map<String, Object> paramMap) throws ServiceException, Exception {
		return this.getBaseMapper().count(paramMap);
	}
	
	public Long countByUK(T mapperObj) throws ServiceException, Exception {
		if (null == mapperObj) {
			throw new ServiceException(BaseSystemMessage.objectNull());
		}
		Map<String, Object> paramMap = EntityParameterGenerateUtil.getUKParameter(mapperObj);
		if (null == paramMap || paramMap.size() < 1) {
			throw new ServiceException(BaseSystemMessage.parameterIncorrect() + " , please set @EntityUK.");
		}
		return this.getBaseMapper().count(paramMap);
	}	
	
	@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
	public <VO> QueryResult<List<VO>> findPage(Map<String, Object> paramMap, PageOf pageOf) throws ServiceException, Exception {
		if (null == paramMap || null == pageOf) {
			throw new ServiceException(BaseSystemMessage.objectNull());
		}
		pageOf.setQueryOrderSortParameter(paramMap);
		QueryResult<List<VO>> result = new QueryResult<List<VO>>();
		Long countSize = this.getBaseMapper().count(paramMap);
		if (countSize > 0) {
			this.fillPageOfAndfindPageParam(paramMap, pageOf, countSize);
			@SuppressWarnings("unchecked")
			List<VO> searchList = (List<VO>) this.getBaseMapper().findPage(paramMap);
			result.setValue(searchList);
		} else {
			result.setMessage(BaseSystemMessage.searchNoData());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
	public <VO> QueryResult<List<VO>> findPage(String mapperCountMethodName, String mapperQueryMethodName, Map<String, Object> paramMap, PageOf pageOf) throws ServiceException, Exception {
		if (null == paramMap || null == pageOf) {
			throw new ServiceException(BaseSystemMessage.objectNull());
		}
		pageOf.setQueryOrderSortParameter(paramMap);	
		QueryResult<List<VO>> result = new QueryResult<List<VO>>();
		OgnlContext ognlContext = new OgnlContext(null,null,new DefaultMemberAccess(true));
		ognlContext.put("paramMap", paramMap);
		Object countSizeObj = Ognl.getValue(mapperCountMethodName+"(#paramMap)", ognlContext, this.getBaseMapper());
		if (!(countSizeObj instanceof Long)) {
			throw new Exception("count method:" + mapperCountMethodName + " return value not accept!");
		}
		Long countSize = Long.parseLong( String.valueOf(countSizeObj) );
		if (countSize > 0) {
			this.fillPageOfAndfindPageParam(paramMap, pageOf, countSize);
			Object searchListObj = Ognl.getValue(mapperQueryMethodName+"(#paramMap)", ognlContext, this.getBaseMapper());
			if (!(searchListObj instanceof List)) {
				throw new Exception("findPage method:" + mapperQueryMethodName + " return value not accept!");
			}
			result.setValue( (List<VO>) searchListObj );
		} else {
			result.setMessage(BaseSystemMessage.searchNoData());
		}
		return result;
	}	
	
	private void fillPageOfAndfindPageParam(Map<String, Object> paramMap, PageOf pageOf, Long countSize) {
		long startRow = (pageOf.getIntegerValue( pageOf.getSelect() )-1) * pageOf.getIntegerValue( pageOf.getShowRow() );
		long endRow = pageOf.getIntegerValue( pageOf.getSelect() ) * pageOf.getIntegerValue( pageOf.getShowRow() );
		if (startRow < 0) {
			startRow = 0;
		}
		if (startRow > 0) {
			startRow = startRow + 1;
		}
		if (endRow <= startRow) {
			endRow = startRow + pageOf.getIntegerValue( pageOf.getShowRow() );
		}
		if (startRow > countSize) {
			startRow = 0;
			endRow = 1 * pageOf.getIntegerValue( pageOf.getShowRow() );
		}			
		if (endRow > countSize) {
			endRow = countSize;
		}
		paramMap.put("startRow", startRow); // 資料庫要放 Long
		paramMap.put("endRow", endRow); // 資料庫要放 Long
		
		pageOf.setCountSize(String.valueOf(countSize));
		//pageOf.toCalculateSize(); // 2019-09-10
		pageOf.toCalculateSize((int)startRow); // 2019-09-10
	}
	
}
