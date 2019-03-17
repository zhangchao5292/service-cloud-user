package com.service.cloud.common.vo;

/**
 * 接口对统一返回错误对象模型类
 * 
 * @author liuqs
 *
 */
public class ErrorResult {
	private ResultResponse error;
	private String stackTrace;
	private String method;
	private String params;

	private String requestAddr;

	public ErrorResult(ResultResponse error) {
		super();
		this.error = error;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public ResultResponse getError() {
		return error;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getMethod() {
		return method;
	}

	public String getParams() {
		return params;
	}

	public String getRequestAddr() {
		return requestAddr;
	}

	public void setRequestAddr(String requestAddr) {
		this.requestAddr = requestAddr;
	}
}
