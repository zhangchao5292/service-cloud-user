package com.service.cloud.common.aop;

import com.service.cloud.common.annotation.Proxy;
import com.service.cloud.common.cfg.SchemaMappingConfigProperties;
import com.service.cloud.common.cfg.SupportProperties;
import com.service.cloud.common.constants.ErrorCode;
import com.service.cloud.common.exception.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;


/**
 * 
 * @author liuqs
 *
 */
@Aspect
@Component
public class AutoProxyAspect implements ApplicationContextAware {
	@Autowired
	private SupportProperties supportProperties;

	@Autowired
	private SchemaMappingConfigProperties schemaMappingConfigProperties;

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 */
	@Pointcut("execution(* com.xlhy.servicecenter.service.impl..*(..))")
	public void doAspect() {
	}

	/**
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("doAspect()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		Object proxyBean = findParamAndInitProxyBean(pjp);

		if (proxyBean != null) {
			try {
				Method method = ((MethodSignature) pjp.getSignature()).getMethod();
				return proxyBean.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(proxyBean, pjp.getArgs());
			} catch (Exception e) {
				throw e.getCause();
			}
		}

		return pjp.proceed();
	}

	/**
	 * 判断注解配置，获取代理bean
	 * 
	 * @param pjp
	 * @return
	 */
	private Object findParamAndInitProxyBean(ProceedingJoinPoint pjp) {
		ProxyConfig proxyConfig = findProxyEnableConfig(pjp);

		String supportKey = proxyConfig.getSupportKey();

		String serverCode = findServerCodeParam(proxyConfig.getParamIndex(), pjp);
		if (supportKey != null && serverCode != null) {
			// 调用外部接口
			if (!supportProperties.isUnSupport(supportKey, serverCode)) {
				return applicationContext.getBean(proxyConfig.getProxyClass());
			}
		}

		return null;
	}

	/**
	 * 通过参数获取服务代码
	 * 
	 * @param pjp
	 * @return
	 */
	private String findServerCodeParam(int index, ProceedingJoinPoint pjp) {
		Object[] args = pjp.getArgs();

		if (args.length > index) {
			if (args[index] == null) {
				throw new ServiceException(ErrorCode.PARAM_NOT_FOUND, "Parameter [gameId] not found.");
			}

			if (args[index] instanceof Integer) {
				return schemaMappingConfigProperties.getServerCodeByGameId((Integer) args[index]);
			}
		}

		return null;
	}

	/**
	 * 获取AutoSchema注解配置
	 * 
	 * @param pjp
	 * @return
	 */
	private ProxyConfig findProxyEnableConfig(ProceedingJoinPoint pjp) {
		ProxyConfig proxyConfig = new ProxyConfig();

		Class<?> targetCls = pjp.getTarget().getClass();
		if (targetCls.isAnnotationPresent(Proxy.class)) {
			Proxy annotation = targetCls.getAnnotation(Proxy.class);
			proxyConfig.setSupportKey(annotation.supportKey());
			proxyConfig.setProxyClass(annotation.proxyClass());
			proxyConfig.setParamIndex(annotation.paramIndex());
		}

		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		if (method.isAnnotationPresent(Proxy.class)) {
			Proxy annotation = method.getAnnotation(Proxy.class);
			proxyConfig.setSupportKey(annotation.supportKey());
			proxyConfig.setProxyClass(annotation.proxyClass());
			proxyConfig.setParamIndex(annotation.paramIndex());
		}

		return proxyConfig;
	}

	static class ProxyConfig {
		private String supportKey;
		private Class<?> proxyClass;
		private int paramIndex;

		public String getSupportKey() {
			return supportKey;
		}

		public void setSupportKey(String supportKey) {
			this.supportKey = supportKey;
		}

		public Class<?> getProxyClass() {
			return proxyClass;
		}

		public void setProxyClass(Class<?> proxyClass) {
			this.proxyClass = proxyClass;
		}

		public int getParamIndex() {
			return paramIndex;
		}

		public void setParamIndex(int paramIndex) {
			this.paramIndex = paramIndex;
		}
	}
}
