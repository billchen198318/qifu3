package org.qifu.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CoreAppConstants {
	
	private static final String _CONFIG_ExcludePathPatterns = "excludePathPatterns.json";
	
	private static Map<String, Object> excludePathPatternsMap = null;
	
	private static String _excludePathPatternsDatas = " { } ";	
	
	private static String excludePathPatternsArray[] = null;
	
	static {
		try {
			InputStream is = Constants.class.getClassLoader().getResource( _CONFIG_ExcludePathPatterns ).openStream();
			_excludePathPatternsDatas = IOUtils.toString(is, Constants.BASE_ENCODING);
			is.close();
			is = null;
			excludePathPatternsMap = loadDatas();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null==excludePathPatternsMap) {
				excludePathPatternsMap = new HashMap<String, Object>();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> loadDatas() {
		Map<String, Object> datas = null;
		try {
			datas = (Map<String, Object>)new ObjectMapper().readValue( _excludePathPatternsDatas, LinkedHashMap.class );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}	
	
	public static final String [] WebConfig_resource = {
			"/webjars/**",
            "/bootbox/**",
            "/bootstrap-vali/**",
            "/css/**",
            "/font-awesome/**",
            "/icons/**",
            "/images/**",
            "/jquery/**",
            "/js/**",
            "/popper-js/**",
            "/tether/**",
            "/toastr/**",
            "swagger-ui.html",
            "/frontend/**" // frontend static html/js resource
	};
	
	public static final String[] WebConfig_resourceLocations = {
            "classpath:/META-INF/resources/webjars/",
            "classpath:/static/bootbox/",
            "classpath:/static/bootstrap-vali/",
            "classpath:/static/css/",
            "classpath:/static/font-awesome/",
            "classpath:/static/icons/",
            "classpath:/static/images/",
            "classpath:/static/jquery/",
            "classpath:/static/js/",
            "classpath:/static/popper-js/",
            "classpath:/static/tether/",
            "classpath:/static/toastr/",
            "classpath:/META-INF/resources/",
            "classpath:/static/frontend/" // put my webpack out frontend static html/js resource file
	};
	
	public static final String [] WebConfig_interceptorExcludePathPatterns = {
			"/webjars/**", 
			"/bootbox/**", 
			"/bootstrap-vali/**", 
			"/css/**", 
			"/font-awesome/**", 
			"/icons/**", 
			"/images/**",
			"/jquery/**", 
			"/js/**", 
			"/popper-js/**", 
			"/tether/**", 
			"/toastr/**",
			"/error**",
			"/swagger-resources/**",
			"/swagger-ui.html",
			"/swagger-ui.html/**",
			"/swagger**",
			"/api-docs**",
			"/api-docs/**",
			"/springfox.js",
			"/springfox.js/**",
			"/csrf",
			"/csrf/**",
			"/token4sinosoft/**", 
			"/token4oldcoresystem/**",
			"/frontend/**" // frontend static html/js resource
	};	
	
	public static String[] getWebConfiginterceptorExcludePathPatterns() {
		if (excludePathPatternsArray != null) {
			return excludePathPatternsArray;
		}
		System.out.println("init Constants getWebConfiginterceptorExcludePathPatterns...");
		@SuppressWarnings("unchecked")
		List<String> excludePathPatterns = (List<String>) excludePathPatternsMap.get("excludePathPatterns");
		if ( null == excludePathPatterns ) {
			excludePathPatterns = new ArrayList<String>();
		}
		Object[] arr = Stream.concat( Arrays.stream(WebConfig_interceptorExcludePathPatterns), Arrays.stream( excludePathPatterns.toArray()) ).toArray();
		return ( excludePathPatternsArray = Arrays.copyOf(arr, arr.length, String[].class) );
	}	
	
}
