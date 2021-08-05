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
package org.qifu.core.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbSysJreport;
import org.qifu.core.mapper.TbSysJreportMapper;
import org.qifu.core.service.ISysJreportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class SysJreportServiceImpl extends BaseService<TbSysJreport, String> implements ISysJreportService<TbSysJreport, String> {
	
	@Autowired
	TbSysJreportMapper sysJreportMapper;
	
	@Override
	protected IBaseMapper<TbSysJreport, String> getBaseMapper() {
		return this.sysJreportMapper;
	}

	/**
	 * no CONTENT field for query , because sometime no need use this field, maybe field byte is big
	 */
	@Override
	public DefaultResult<TbSysJreport> selectByPrimaryKeySimple(String oid) throws ServiceException, Exception {
		if (null == oid || StringUtils.isBlank(oid)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbSysJreport> result = new DefaultResult<TbSysJreport>();
		TbSysJreport value = this.sysJreportMapper.selectByPrimaryKeySimple(oid);
		if (value != null) {
			result.setValue(value);
		} else {
			result.setMessage(BaseSystemMessage.searchNoData());
		}
		return result;
	}
	
	/**
	 * no CONTENT field for query , because sometime no need use this field, maybe field byte is big
	 */
	@Override
	public DefaultResult<List<TbSysJreport>> selectListByParamsSimple(Map<String, Object> paramMap) throws ServiceException, Exception {
		if (MapUtils.isEmpty(paramMap)) {
			throw new ServiceException(BaseSystemMessage.parameterIncorrect());
		}
		DefaultResult<List<TbSysJreport>> result = new DefaultResult<List<TbSysJreport>>();
		List<TbSysJreport> value = this.sysJreportMapper.selectListByParamsSimple(paramMap);
		if (value != null) {
			result.setValue(value);
		} else {
			result.setMessage(BaseSystemMessage.searchNoData());
		}
		return result;
	}
	
}
