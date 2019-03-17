package com.service.cloud.common.cfg;

import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 程序配置类
 *
 * @author liuqs
 *
 */
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "github")
public class SupportProperties {
	private Map<String, Set<String>> support;

	public Map<String, Set<String>> getSupport() {
		return support;
	}

	public void setSupport(Map<String, Set<String>> support) {
		this.support = support;
	}

	/**
	 * 根据servercode获取游戏id
	 *
	 * 区服代码
	 * @return
	 */
	public boolean isSupport(String type, String schemaName) {
		if (support == null) {
			return true;
		}

		Set<String> sets = support.get(type);
		if(sets == null || sets.isEmpty()) {
			return true;
		}

		return sets.contains(schemaName);
	}

	/**
	 *  根据判断是否执行配置，默认为不支持
	 *
	 * 区服代码
	 * @return
	 */
	public boolean isUnSupport(String type, String schemaName) {
		if (support == null) {
			return true;
		}

		Set<String> sets = support.get(type);
		if(sets == null || sets.isEmpty()) {
			return true;
		}

		return !sets.contains(schemaName);
	}
}
