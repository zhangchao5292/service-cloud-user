//package com.xlhy.servicecenter.common.cfg;
//
//import com.xlhy.servicecenter.common.mail.MailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.autoconfigure.mail.MailProperties;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//
//@RefreshScope
//@Configuration
//@ConditionalOnProperty(name = "custom.mail.enable", matchIfMissing = false)
////@EnableConfigurationProperties(MailProperties.class)
//public class MailServiceAutoConfiguration {
//	@Autowired
//	private MailProperties prop;
//
//	@Autowired
//	private JavaMailSender mailSender;
//
//	@Bean
//	public MailService createMailService(){
//		MailService mailService = new MailService(prop);
//		mailService.setMailSender(mailSender);
//
//		return mailService;
//	}
//}
