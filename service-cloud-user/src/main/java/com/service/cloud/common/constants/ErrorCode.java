package com.service.cloud.common.constants;

/**
 * 系统错误码
 * 
 * @author liuqs
 *
 */
public class ErrorCode {
	// 游戏ID非法
	public static final int GAME_NOT_SUPPORT = 0x1001;
	// 区服代码非法
	public static final int SERVERCODE_NOT_SUPPORT = 0x1002;
	public static final int CONFIG_NOT_FOUND = 0x1003;

	// 参数未找到
	public static final int PARAM_NOT_FOUND = 0x2001;
	// 参数格式错误
	public static final int PARAM_FORMAT_ILLEGAL = 0x2002;
	// 参数不合法
	public static final int PARAM_ILLEGAL = 0x2003;

	// 非法操作
	public static final int OPT_ILLEGAL = 0xf001;
	// 配置错误
	public static final int CONFIG_ILLEGAL = 0xf002;
	// 远程服务不可用
	public static final int REMOTE_INVOKE_ERROR = 0xf003;
	// 远程服务异常
	public static final int REMOTE_SERVICE_ERROR = 0xf004;

	// 数据源不可用
	public static final int DS_INVALID = 0xff01;

	// 未知错误
	public static final int UNKOWN_ERROR = 0xffff;

}
