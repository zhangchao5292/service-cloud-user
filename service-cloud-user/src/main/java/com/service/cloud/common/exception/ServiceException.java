package com.service.cloud.common.exception;

/**
 * 自定义异常类，抛出此异常时，框架会返回给调用方错误信息
 * 
 * @author liuqs
 *
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = -6169216464178969177L;

	private int code;
	private String msg;

	public ServiceException() {
		this("");
	}

	public ServiceException(String msg) {
		this(-1, msg);
	}

	public ServiceException(int code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

	public ServiceException(int code, String msg, Throwable e) {
		super(msg, e);
		this.code = code;
		this.msg = msg;
	}

	public int code() {
		return code;
	}

	public String msg() {
		return msg;
	}
}
