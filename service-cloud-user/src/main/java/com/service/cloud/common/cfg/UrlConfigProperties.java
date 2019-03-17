package com.service.cloud.common.cfg;

import java.util.HashMap;
import java.util.Map;

import com.service.cloud.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.xlhy.servicecenter.common.constants.ErrorCode;
import com.xlhy.servicecenter.common.exception.ServiceException;

/**
 * 程序配置类
 * 
 * @author liuqs
 *
 */
@Configuration
public class UrlConfigProperties {
	private static Logger logger = LoggerFactory.getLogger(UrlConfigProperties.class);

	/**
	 * 程序配置
	 */

	/**
	 * 游戏id和转卡地址关系
	 */
	private Map<Integer, String> gameIdMapper = new HashMap<Integer, String>();
	/**
	 * 区服代码与转卡地址关系
	 */
	private Map<String, String> serverCodeMapper = new HashMap<String, String>();

	/**
	 * 实例化对象后初始化内部数据
	 */
	public void init(Map<Integer, String> gameIdMapper, Map<String, String> serverCodeMapper) {
		this.gameIdMapper = gameIdMapper;
		this.serverCodeMapper = serverCodeMapper;

		logger.info("UrlConfigProperties inited.");
	}

	/**
	 * 根据游戏ID获取服务地址
	 * 
	 * @param gameId
	 * @return
	 */
	public String getServiceUrl(Integer gameId) {
		if (gameId == null) {
			throw new ServiceException(ErrorCode.PARAM_NOT_FOUND, "找不到游戏ID，请确认参数是否传递！");
		}

		String url = gameIdMapper.get(gameId);

		if (url == null) {
			throw new ServiceException(ErrorCode.GAME_NOT_SUPPORT, "不支持的游戏ID, gameId  = " + gameId);
		}

		return url;
	}

	/**
	 * 根据区服代码获取服务地址
	 * 
	 * @param serverCode
	 * @return
	 */
	public String getServiceUrl(String serverCode) {
		if (serverCode == null) {
			throw new ServiceException(ErrorCode.PARAM_NOT_FOUND, "找不到区服代码，请确认参数是否传递！");
		}

		String url = serverCodeMapper.get(serverCode);

		if (url == null) {
			throw new ServiceException(ErrorCode.SERVERCODE_NOT_SUPPORT, "不支持的区服代码, serverCode  = " + serverCode);
		}

		return url;
	}
}
