package org.qifu.core.ws.impl;

import javax.jws.WebService;

import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.YesNo;
import org.qifu.core.vo.ResponseResult;
import org.qifu.core.ws.ITestService;
import org.springframework.stereotype.Component;

@WebService(
		serviceName = "testService", 
		targetNamespace = "http://webservice.test.qifu.org", 
		endpointInterface = "org.qifu.core.ws.ITestService")
@Component
public class TestServiceImpl implements ITestService {

	@Override
	public ResponseResult test(String name) {
		ResponseResult result = ResponseResult.build();
		result.setFlag( YesNo.YES );
		result.setMessage( BaseSystemMessage.updateSuccess() );
		return result;
	}
	
}
