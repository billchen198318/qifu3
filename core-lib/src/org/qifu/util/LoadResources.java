/* 
 * Copyright 2010-2022 qifu of copyright Chen Xin Nien
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
package org.qifu.util;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.qifu.base.Constants;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LoadResources {
	
	public static <T> T objectMapperReadValue(String fileRes, Class<T> resourceJson2HashMapOrListClazz, Class<?> resourceClazz) throws Exception {
		return (T) new ObjectMapper().readValue( read( fileRes, resourceClazz), resourceJson2HashMapOrListClazz );
	}
	
	public static List<String> readLine(String fileRes, Class<?> resourceClazz) throws Exception {
		List<String> strList = null;
		InputStream is = null;
		try {
			is = resourceClazz.getClassLoader().getResource( fileRes ).openStream();			
			strList = IOUtils.readLines(is, Constants.BASE_ENCODING);			
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			is = null;
		}		
		return strList;
	}	
	
	public static String read(String fileRes, Class<?> resourceClazz) throws Exception {
		String str = "";
		InputStream is = null;
		try {
			is = resourceClazz.getClassLoader().getResource( fileRes ).openStream();
			str = IOUtils.toString(is, Constants.BASE_ENCODING);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			is = null;
		}		
		return str;
	}
	
}
