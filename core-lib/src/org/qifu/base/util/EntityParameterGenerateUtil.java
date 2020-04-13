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
package org.qifu.base.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityParameterGenerate;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class EntityParameterGenerateUtil implements EntityParameterGenerate {
	
	public static Map<String, Object> createParamMap(String paramName, String value) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(paramName, value);
		return paramMap;
	}
	
	public static Map<String, Object> getUKParameter(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		Map<String, Object> ukMap=new HashMap<String, Object>();
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof EntityUK) {
					if (methods[ix].getName().indexOf("get")==0) {
						for (int nx=0; nx<annotations.length; nx++) {
							if (annotations[nx] instanceof EntityUK) {
								try {
									ukMap.put(((EntityUK)annotations[nx]).name(), methods[ix].invoke(entityObject));
									nx=annotations.length;
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}								
							}
						}					
					}					
				}
			}
		}		
		return ukMap;
	}	
	
	public static Map<String, Object> getPKParameter(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		Map<String, Object> ukMap=new HashMap<String, Object>();
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof EntityPK) {
					if (methods[ix].getName().indexOf("get")==0) {
						for (int nx=0; nx<annotations.length; nx++) {
							if (annotations[nx] instanceof EntityPK) {
								try {
									ukMap.put(((EntityPK)annotations[nx]).name(), methods[ix].invoke(entityObject));
									nx=annotations.length;
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}								
							}							
						}					
					}					
				}
			}
		}		
		return ukMap;
	}	
	
	public static EntityPK getPrimaryKeyField(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		EntityPK field = null;
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof EntityPK) {
					field = ((EntityPK)annotation);			
				}
			}
		}
		return field;
	}		
	
	public static String getUpdateUserFieldName(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		String fieldName = "";
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof UpdateUserField) {
					fieldName = ((UpdateUserField)annotation).name();					
				}
			}
		}
		return fieldName;
	}	
	
	public static String getUpdateDateFieldName(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		String fieldName = "";
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof UpdateDateField) {
					fieldName = ((UpdateDateField)annotation).name();					
				}
			}
		}
		return fieldName;
	}
	
	public static String getCreateUserFieldName(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		String fieldName = "";
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof CreateUserField) {
					fieldName = ((CreateUserField)annotation).name();					
				}
			}
		}
		return fieldName;
	}	
	
	public static String getCreateDateFieldName(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		String fieldName = "";
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof CreateDateField) {
					fieldName = ((CreateDateField)annotation).name();					
				}
			}
		}
		return fieldName;
	}
	
}
