package com.service.cloud.common.web;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

	private static final String ERROR_PATH = "/exception";

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}
