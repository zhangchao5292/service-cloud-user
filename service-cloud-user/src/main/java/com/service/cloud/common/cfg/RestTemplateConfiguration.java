package com.service.cloud.common.cfg;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.xlhy.servicecenter.common.constants.ErrorCode;
import com.xlhy.servicecenter.common.exception.ServiceException;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfiguration {

	@Value("${github.resttemplate.retryCount:3}")
	private int retryCount;

	@Value("${github.resttemplate.retryable:true}")
	private boolean retryable;

	@Bean
	@ConfigurationProperties(prefix = "github.resttemplate.conn.pool")
	public HttpClientConnectionManager poolingConnectionManager() {
		return new PoolingHttpClientConnectionManager();
	}

	@Bean
	@ConfigurationProperties(prefix = "github.resttemplate.conn.request")
	public ClientHttpRequestFactory clientHttpRequestFactory(HttpClientConnectionManager connManager) {
		HttpClientBuilder custom = HttpClients.custom();

		custom.setConnectionManager(connManager);
		custom.setRetryHandler(new StandardHttpRequestRetryHandler(retryCount, retryable));

		HttpClient httpClient = custom.build();

		return new HttpComponentsClientHttpRequestFactory(httpClient);
	}

	@Bean
	public ResponseErrorHandler responseErrorHandler() {
		return new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return response.getRawStatusCode() != 200;
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				InputStream body = response.getBody();
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

				byte[] buf = new byte[512];
				int i = -1;
				while ((i = body.read(buf)) != -1) {
					byteOut.write(buf, 0, i);
				}

				throw new ServiceException(ErrorCode.REMOTE_INVOKE_ERROR, new String(byteOut.toByteArray()));
			}
		};
	}

	@Bean("converters")
	public List<HttpMessageConverter<?>> messageConverters() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

		List<MediaType> list = new ArrayList<MediaType>();
		list.add(MediaType.ALL);

		converter.setSupportedMediaTypes(list);

		messageConverters.add(converter);

		return messageConverters;
	}

	@Bean
	public RestTemplate createRestTemplate(ClientHttpRequestFactory requestFactory,@Qualifier("converters") List<HttpMessageConverter<?>> messageConverters, ResponseErrorHandler errorHandler) {
		RestTemplate restTemplate = new RestTemplate();

		restTemplate.setRequestFactory(requestFactory);
		restTemplate.setMessageConverters(messageConverters);
		restTemplate.setErrorHandler(errorHandler);

		return restTemplate;
	}
}
