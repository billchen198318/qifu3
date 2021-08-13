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

import java.util.Locale;

import org.qifu.base.AppContext;
import org.springframework.beans.BeansException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class LocaleMessageSourceUtils {
	
    private static MessageSource messageSource;
    
    public static String getMessage(String code) {
    	return getMessage(code, null);
    }
    
    public static String getMessage(String code, Object[] args){
    	return getMessage(code, args, "");
    }
    
    public static String getMessage(String code, Object[] args, String defaultMessage){
    	if (null == messageSource) {
    		try {
				messageSource = (MessageSource) AppContext.getBean(MessageSource.class);
			} catch (BeansException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }
    
}
