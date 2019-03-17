package com.service.cloud.common.cfg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import com.service.cloud.common.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;
import com.xlhy.servicecenter.common.constants.ErrorCode;
import com.xlhy.servicecenter.common.exception.ServiceException;
import com.xlhy.servicecenter.common.util.Util;

/**
 * 程序配置类
 * 
 * @author liuqs
 *
 */
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "github.config")
public class SchemaMappingConfigProperties {
	private static Logger logger = LoggerFactory.getLogger(SchemaMappingConfigProperties.class);

	/**
	 * 原始程序配置
	 */
	private Map<String, String> servercode;

	/**
	 * 初始化后区服配置
	 */
	private Map<String, SchemaConfig> schemaConfigs = new HashMap<String, SchemaConfig>();

	/**
	 * 实例化对象后初始化内部数据
	 */
	@PostConstruct
	public void init() {
		if (servercode == null) {
			throw new IllegalStateException("程序配置未找到，请检查xlhy.config的前缀配置！");
		}

		Iterator<Entry<String, String>> iterator = servercode.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String serverCode = entry.getKey();

			SchemaConfig cm = convertSchemaConfig(entry.getValue());

			schemaConfigs.put(serverCode, cm);
		}

		logger.info("SchemaMappingConfigProperties inited.");
	}

	public Map<String, String> getServercode() {
		return servercode;
	}

	public void setServercode(Map<String, String> servercode) {
		this.servercode = servercode;
	}

	private SchemaConfig convertSchemaConfig(String json) {
		SchemaConfig schemaConfig = null;
		try {
			schemaConfig = JSON.parseObject(json, SchemaConfig.class);
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.PARAM_FORMAT_ILLEGAL, "\"xlhy.config.serverCode\" formart error, text=" + json, e);
		}

		Util.validateEmpty("schema", schemaConfig.getSchema());
		Util.validateEmpty("gameIds", schemaConfig.getGameIds());
		Util.validateEmpty("location", schemaConfig.getLocation());

		return schemaConfig;
	}

	/**
	 * 根据游戏ID获取数据库方案名称
	 * 
	 * @param gameId
	 * @return
	 */
	public String getSchemaByServerCode(String serverCode) {
		return tryGetSchemaConfig(serverCode).getSchema();
	}

	/**
	 * 通过游戏ID获取区服代码
	 * 
	 * @param gameId
	 * @return
	 */
	public String getServerCodeByGameId(Integer gameId) {
		Iterator<Entry<String, SchemaConfig>> iterator = schemaConfigs.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, SchemaConfig> item = iterator.next();

			if (item.getValue().getGameIds().contains(gameId)) {
				return item.getKey();
			}
		}

		return null;
	}

	/**
	 * 根据servercode获取游戏id
	 * 
	 * @param serverCode
	 *            区服代码
	 * @return
	 */
	public List<Integer> getGameIdsByServerCode(String serverCode) {
		List<Integer> gameIds = tryGetSchemaConfig(serverCode).getGameIds();

		if (gameIds == null || gameIds.isEmpty()) {
			throw new ServiceException(ErrorCode.SERVERCODE_NOT_SUPPORT, "找不到对应的游戏id, serverCode  = " + serverCode);
		}

		return gameIds;
	}

	/**
	 * 是否拆包
	 * 
	 * @param serverCode
	 * @return
	 */
	public boolean isUnpackServerCode(String serverCode) {
		return tryGetSchemaConfig(serverCode).isUnpack();
	}

	/**
	 * 扩展配置
	 * 
	 * @param serverCode
	 * @return
	 */
	public Map<String, String> getConfigs(String serverCode) {
		return tryGetSchemaConfig(serverCode).getConfigs();
	}

	/**
	 * 逻辑校验字段
	 * 
	 * @param serverCode
	 * @return
	 */
	public List<String> getLogicItem(String serverCode) {
		return tryGetSchemaConfig(serverCode).getLogicItems();
	}

	private SchemaConfig tryGetSchemaConfig(String serverCode) {
		if (serverCode == null) {
			throw new ServiceException(ErrorCode.PARAM_NOT_FOUND, "找不到区服代码，请确认参数是否传递！");
		}

		SchemaConfig schemaConfig = schemaConfigs.get(serverCode);

		if (schemaConfig == null) {
			throw new ServiceException(ErrorCode.SERVERCODE_NOT_SUPPORT, "不支持的区服代码, serverCode  = " + serverCode);
		}

		return schemaConfig;
	}

	static class SchemaConfig {
		private String schema;
		private boolean unpack;
		private List<Integer> gameIds;
		private String location;

		private Map<String, String> configs;
		private List<String> logicItems;

		public String getSchema() {
			return schema;
		}

		public void setSchema(String schema) {
			this.schema = schema;
		}

		public boolean isUnpack() {
			return unpack;
		}

		public void setUnpack(boolean unpack) {
			this.unpack = unpack;
		}

		public List<Integer> getGameIds() {
			return gameIds;
		}

		public void setGameIds(List<Integer> gameIds) {
			this.gameIds = gameIds;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public List<String> getLogicItems() {
			return logicItems;
		}

		public void setLogicItems(List<String> logicItems) {
			this.logicItems = logicItems;
		}

		public Map<String, String> getConfigs() {
			return configs;
		}

		public void setConfigs(Map<String, String> configs) {
			this.configs = configs;
		}
	}
}
