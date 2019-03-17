package com.service.cloud.common.web;

import com.xlhy.servicecenter.common.constants.ErrorCode;
import com.xlhy.servicecenter.common.vo.ErrorResult;
import com.xlhy.servicecenter.common.vo.ResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 索引处理
 * 
 * @author liuqs
 *
 */
@RestController
public class IndexController {
//
//	@Autowired(required = false)
//	private MailService mailService;

	private String uptime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	
	/**
	 * 默认信息
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public ResponseEntity<String> home() {
		String msg = "外部服务接口<br> 上线时间：" + uptime;
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}

	/**
	 * 错误页面
	 * 
	 * @return
	 */
	@RequestMapping("/404")
	public ResponseEntity<ResultResponse> notfound() {
		return new ResponseEntity<ResultResponse>(new ResultResponse(404, "resource not found!"), HttpStatus.NOT_FOUND);
	}

	/**
	 * 错误响应处理
	 * 
	 *            错误描述信息
	 *            异常类
	 * @return
	 */
	@RequestMapping("/exception")
	public ResponseEntity<ResultResponse> responseError(@RequestAttribute ErrorResult errorret) {
		if (errorret == null || errorret.getError() == null) {
			// 错误为null，默认返回空结果
			return new ResponseEntity<ResultResponse>(ResultResponse.success(), HttpStatus.FORBIDDEN);
		}

		ResultResponse error = errorret.getError();

		if (error.getCode() > 0xf000) {
//			if (mailService != null) {
//				// 发送预警邮件,异步发送
//				mailService.sendError(errorret);
//			}

			if (ErrorCode.DS_INVALID == error.getCode()) {
				// 数据源异常返回503，供调用方区别处理
				return new ResponseEntity<ResultResponse>(error, HttpStatus.SERVICE_UNAVAILABLE);
			}

			// 所有大于0xf000的错误码均表示服务端问题
			return new ResponseEntity<ResultResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<ResultResponse>(error, HttpStatus.BAD_REQUEST);
	}
}