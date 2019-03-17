package com.service.cloud.common.handler;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.cloud.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.xlhy.servicecenter.common.constants.ErrorCode;
import com.xlhy.servicecenter.common.exception.ServiceException;
import com.xlhy.servicecenter.common.vo.ResultResponse;
import com.xlhy.servicecenter.common.vo.ErrorResult;

/**
 * 异常处理类
 * 
 * @author liuqs
 *
 */
@Component
public class ExceptionResolverHandler implements HandlerExceptionResolver {
	private static Logger logger = LoggerFactory.getLogger(ExceptionResolverHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) {
		if (response.isCommitted()) {
			return null;
		}

		logger.error("ExceptionResolverHandler", e);

		ResultResponse error = new ResultResponse(ErrorCode.UNKOWN_ERROR, "Service unavailable, please contact the service provider to solve!");
		if (e instanceof ServiceException) {
			ServiceException gse = (ServiceException) e;
			error.setCode(gse.code());
			error.setError(gse.msg());
		}

		ErrorResult er = new ErrorResult(error);

		if (obj instanceof HandlerMethod) {
			String name = ((HandlerMethod) obj).getMethod().getName();
			er.setMethod(name);
		}

		er.setParams(parseParams(request));
		er.setRequestAddr(request.getRemoteAddr());
		er.setStackTrace(parseExceptionStackTrace(e));
		request.setAttribute("errorret", er);

		return new ModelAndView("forward:/exception");
	}

	/**
	 * 解析参数
	 * @param request
	 * @return
	 */
	private String parseParams(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterMap == null || parameterMap.isEmpty()) {
			return "";
		}

		Iterator<Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();
		StringBuilder sb = new StringBuilder("{");

		while (iterator.hasNext()) {
			Entry<String, String[]> entry = iterator.next();

			sb.append(entry.getKey()).append(" = ");
			String[] value = entry.getValue();
			if (value != null && value.length > 0) {
				sb.append(value[0]).append("; ");
			}
		}
		sb.append("}");

		return sb.toString();
	}
	
	/**
	 * 解析错误栈信息
	 * 
	 * @param e
	 */
	private String parseExceptionStackTrace(Exception e) {
		if (e == null) {
			return "";
		}

		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(byteOut));
		e.printStackTrace(writer);

		writer.flush();
		return new String(byteOut.toByteArray());
	}
}
