package org.qifu.core.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.qifu.core.vo.ResponseResult;

@WebService(targetNamespace = "http://webservice.test.qifu.org")
public interface ITestService {
	
	@WebMethod
	ResponseResult test(@WebParam(name = "name") String name);
	
}
