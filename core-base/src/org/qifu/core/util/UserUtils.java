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
package org.qifu.core.util;

import org.qifu.base.Constants;
import org.qifu.core.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
	
	public static User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth.getPrincipal() instanceof User)) {
			return null;
		}
		return (User) auth.getPrincipal();
	}
	
	public static boolean isAdmin() {
		User user = getCurrentUser();
		boolean isAdm = false;
		if (user != null && user.getRoles() != null) {
			for (int i = 0; i < user.getRoles().size() && !isAdm; i++) {
				if (Constants.SUPER_ROLE_ADMIN.equals(user.getRoles().get(i).getRole()) || Constants.SUPER_ROLE_ALL.equals(user.getRoles().get(i).getRole())) {
					isAdm = true;
				}
			}
		}
		return isAdm;
	}
	
}
