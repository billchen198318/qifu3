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

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.TbAccount;
import org.qifu.core.entity.TbRolePermission;
import org.qifu.core.entity.TbUserRole;
import org.qifu.core.model.User;
import org.qifu.core.service.IAccountService;
import org.qifu.core.service.IRolePermissionService;
import org.qifu.core.service.IUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
@Transactional(propagation= Propagation.REQUIRED, timeout=300, readOnly=true)
public class BaseUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IAccountService<TbAccount, String> accountService;

    @Autowired
    IUserRoleService<TbUserRole, String> userRoleService;
    
    @Autowired
    IRolePermissionService<TbRolePermission, String> rolePermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("login account: {}", username);
        if (StringUtils.isBlank(username)) {
        	logger.warn("account value blank.");
        	throw new UsernameNotFoundException( BaseSystemMessage.parameterBlank() );
        }
        TbAccount accObj = new TbAccount();
        accObj.setAccount(username);
        DefaultResult<TbAccount> result = null;
        try {
            result = this.accountService.selectByUniqueKey(accObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null || result.getValue() == null) {
            throw new UsernameNotFoundException( result.getMessage() );
        }
        accObj = result.getValue();
        //String encodePwd = new BCryptPasswordEncoder().encode(accObj.getPassword());
        return new User(accObj.getOid(), accObj.getAccount(), accObj.getPassword(), accObj.getOnJob(), this.findUserRoleList(username));
    }

    private List<TbUserRole> findUserRoleList(String username) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("account", username);
        DefaultResult<List<TbUserRole>> result = null;
        try {
            result = userRoleService.selectListByParams(paramMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<TbUserRole> roleList = result.getValue();
        for (int i = 0; roleList != null && i < roleList.size(); i++) {
        	TbUserRole userRole = roleList.get(i);
        	paramMap.clear();
        	paramMap.put("role", userRole.getRole());
        	try {
				DefaultResult<List<TbRolePermission>> permResult = rolePermissionService.selectListByParams(paramMap);
				userRole.setRolePermission( permResult.getValue() );
			} catch (Exception e) {
				e.printStackTrace();
			}
        	if (userRole.getRolePermission() == null) {
        		userRole.setRolePermission( new ArrayList<TbRolePermission>() );
        	}
        }
        paramMap.clear();
        paramMap = null;
        return roleList;
    }
    
}
