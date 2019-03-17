package com.service.cloud.common.http;

import com.service.cloud.common.constants.ErrorCode;
import com.service.cloud.common.exception.ServiceException;
import com.service.cloud.vo.RemoteMsg;
import com.service.cloud.vo.ResponseMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.xlhy.servicecenter.common.constants.ErrorCode;
import com.xlhy.servicecenter.common.exception.ServiceException;
import com.xlhy.servicecenter.vo.RemoteMsg;
import com.xlhy.servicecenter.vo.ResponseMsg;

@Component
public class RemoteServiceInvocation {
	private static Logger logger = LoggerFactory.getLogger(RemoteServiceInvocation.class);
	
	@Autowired
	private RestTemplate restTemplate;

	public ResponseMsg reqGet(String url) {
		return restTemplate.getForObject(url, ResponseMsg.class);
	}
	
	/**
	 * 调用远程get 方法
	 * 
	 * @param url
	 *            查询路径
	 */
	public final <T> RemoteMsg<T> reqGetForResult(String url) {
		try {
			logger.info("Req: {}", url);

			ResponseEntity<RemoteMsg<T>> exchange = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<RemoteMsg<T>>() {
			});

			RemoteMsg<T> body = exchange.getBody();

			logger.info("Res: {}", body.toString());
			
			return body;
		} catch (Exception e) {
			String str = String.format("Invoke remote service network error, url = %s", url);
			throw new ServiceException(ErrorCode.REMOTE_INVOKE_ERROR, str, e);
		}
	}
}
