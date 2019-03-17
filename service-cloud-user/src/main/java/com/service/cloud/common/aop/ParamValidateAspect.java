package com.service.cloud.common.aop;

import com.service.cloud.common.annotation.ValidateNullParam;
import com.service.cloud.common.constants.ErrorCode;
import com.service.cloud.common.exception.ServiceException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;


/**
 * 
 * @author liuqs
 *
 */
@Aspect
@Component
public class ParamValidateAspect {
	@Pointcut("execution(* com.service.cloud.controller..*(..))")
	public void validatePointcut() {
	}

	/**
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Before("validatePointcut()")
	public void doInterceptor(JoinPoint pjp) throws Throwable {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();

		if (method.isAnnotationPresent(ValidateNullParam.class)) {
			ValidateNullParam annotation = method.getAnnotation(ValidateNullParam.class);
			String[] values = annotation.value();
			
			for (String name : values) {
				String[] parameterNames = signature.getParameterNames();
				
				for (int i = 0; i < parameterNames.length; i++) {
					if (name.equals(parameterNames[i])) {
						Object obj = pjp.getArgs()[i];
						
						if (obj == null || ((obj instanceof String) && ((String) obj).trim().isEmpty()) || ((obj instanceof Collection) && ((Collection<?>) obj).isEmpty()) || ((obj instanceof Map) && ((Map<?, ?>) obj).isEmpty())) {
							throw new ServiceException(ErrorCode.PARAM_NOT_FOUND, "Parameter [" + parameterNames[i] + "] required!");
						}
					}
				}
			}
		}
	}
}
