package com.service.cloud.common.vo;

/**
 * 接口对统一返回错误对象模型类
 * 
 * @author liuqs
 *
 */
public class ResultResponse {
	private int code = 0;// 返回码，0表成功，非零表失败
	private String error = "success";// 错误信息，成功时错误信息为空
	private String data;// 均为json格式

	public ResultResponse(int code, String error) {
		super();
		this.code = code;
		this.error = error;
	}

	public ResultResponse(int code, String error, String data) {
		super();
		this.code = code;
		this.error = error;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public static ResultResponse success() {
		return new ResultResponse(0, "");
	}

	public static ResultResponse success(String data) {
		return new ResultResponse(0, "", data);
	}
}
