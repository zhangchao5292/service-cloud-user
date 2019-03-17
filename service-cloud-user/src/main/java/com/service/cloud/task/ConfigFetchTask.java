package com.service.cloud.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xlhy.servicecenter.common.cfg.UrlConfigProperties;
import com.xlhy.servicecenter.common.thread.FixedIntervalThread;
import com.xlhy.servicecenter.common.thread.FixedIntervalThread.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @date 2017年9月1日 上午10:19:00
 */
@Component
public class ConfigFetchTask {
	private static Logger logger = LoggerFactory.getLogger(ConfigFetchTask.class);

	@Value("${http.rest.game.inner.uri:http://localhost}")
	private String url;

	@Value("${xlhy.config.fetch.interval:300000}")
	private long interval;

	@Autowired
	private RestTemplate redisTemplate;

//	@Autowired(required = false)
//	private MailService mailService;

	@Autowired
	private UrlConfigProperties configProperties;

	private FixedIntervalThread fixedIntervalThread;

	private String configUrl;

	/**
	 * 构造函数
	 * 
	 */
	public ConfigFetchTask() {
	}

	/**
	 * 实例化对象后初始化内部数据
	 */
//	@PostConstruct
	public void init() {
		String finalUrl = url;

		if (!url.endsWith("/")) {
			finalUrl = url + "/";
		}

		configUrl = finalUrl + "config/getAllServerCodesInfoWithGame";

		fetchConfig();

		initFixedIntervalThread();
	}

	private void initFixedIntervalThread() {
		fixedIntervalThread = new FixedIntervalThread(interval, new Runnable() {
			@Override
			public void run() {
				fetchConfig();
			}
		});

		fixedIntervalThread.setExceptionHandler(new ExceptionHandler() {
			@Override
			public void handle(Throwable e) {
//				if (mailService != null) {
					String msg = "获配置失败，发送预警邮件！";
					logger.info(msg);
//
//					mailService.sendError(msg, e);
//				}
			}
		});

		fixedIntervalThread.start();
	}

	@PreDestroy
	public void destroy() {
		fixedIntervalThread.stop();
	}

	public void fetchConfig() {
		logger.info("Fetching config from " + configUrl);

		String jsonString = redisTemplate.getForObject(configUrl, String.class);

		JSONArray jsonArray = JSON.parseArray(jsonString);

		Map<Integer, String> gameIdData = new HashMap<Integer, String>(jsonArray.size() * 2);
		Map<String, String> serverCodeData = new HashMap<String, String>(jsonArray.size() * 2);

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String serverCode = jsonObject.getString("serverCode");
			if (serverCode == null || serverCode.trim().isEmpty()) {
				throw new RuntimeException("区服代码为空!");
			}

			String remoteUrl = jsonObject.getString("remoteUrl");
			if (remoteUrl == null || remoteUrl.trim().isEmpty()) {
				throw new RuntimeException(serverCode + " 的remoteUrl为空!");
			}

			JSONArray gameInfos = jsonObject.getJSONArray("gameInfo");
			if (gameInfos == null || gameInfos.size() == 0) {
				throw new RuntimeException(serverCode + " 的游戏配置为空!");
			}

			String finalRemoteUrl = remoteUrl.trim();

			serverCodeData.put(serverCode, finalRemoteUrl);

			for (int k = 0; k < gameInfos.size(); k++) {
				JSONObject jsonObjectgame = gameInfos.getJSONObject(k);

				Integer gameId = jsonObjectgame.getInteger("gameId");
				if (gameId == null) {
					throw new RuntimeException(serverCode + " 游戏ID不存在!");
				}

				gameIdData.put(gameId, finalRemoteUrl);
			}
		}

		configProperties.init(gameIdData, serverCodeData);
	}
}
