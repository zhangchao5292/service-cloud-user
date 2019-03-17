//package com.xlhy.servicecenter.common.mail;
//
//import java.io.ByteArrayOutputStream;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import javax.annotation.PreDestroy;
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
//import com.xlhy.servicecenter.common.cfg.MailProperties;
//import com.xlhy.servicecenter.common.vo.ErrorResult;
//
///**
// * 邮件发送，异步
// *
// * @author liuqs
// *
// */
//public class MailService {
//	private static Logger logger = LoggerFactory.getLogger(MailService.class);
//
//	private ExecutorService threadPool = Executors.newFixedThreadPool(2);
//
//	private long lastSendTime = 0;
//
//	private long DELAY = 10000;
//
//	private int LIMIT = 10;
//
//	private int sendtime = 0;
//
//	private MailProperties prop;
//
//	private JavaMailSender mailSender;
//
//	public MailService(MailProperties prop) {
//		this.prop = prop;
//
//		logger.info("MailService init.");
//	}
//
//	public void setMailSender(JavaMailSender mailSender) {
//		this.mailSender = mailSender;
//	}
//
//	/**
//	 * 监听容器销毁
//	 */
//	@PreDestroy
//	public void destroy() {
//		threadPool.shutdown();
//
//		try {
//			threadPool.awaitTermination(5, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 发送邮件
//	 *
//	 * @param subject
//	 *            主题
//	 * @param content
//	 *            内容
//	 * @throws MessagingException
//	 */
//	public void send(String subject, String content) {
//		if (content == null || content.isEmpty()) {
//			logger.warn("Mail not send, because content is empty!");
//
//			return;
//		}
//
//		synchronized (this) {
//			long now = System.currentTimeMillis();
//
//			if (sendtime > LIMIT) {
//				if (now - lastSendTime < DELAY) {
//					return;
//				} else {
//					sendtime = 0;
//				}
//			} else {
//				if (now - lastSendTime < DELAY) {
//					sendtime++;
//				} else {
//					sendtime = 0;
//				}
//			}
//
//			lastSendTime = now;
//		}
//
//		threadPool.submit(new MailTask(subject, content));
//	}
//
//	/**
//	 * 发送邮件
//	 *
//	 * @param exception
//	 *            异常对象
//	 */
//	public void sendError(ErrorResult er) {
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("请求方法名：").append(er.getMethod()).append("\n");
//		sb.append("请求参数：").append(er.getParams()).append("\n");
//		sb.append("请求来源：").append(er.getRequestAddr()).append("\n");
//		sb.append("错误栈：\n").append(er.getStackTrace());
//
//		this.send("外部服务接口-日志预警", sb.toString());
//	}
//
//	/**
//	 * 发送邮件
//	 *
//	 * @param desc
//	 *            描述信息
//	 * @param exception
//	 *            异常对象
//	 */
//	public void sendError(String desc, Throwable exception) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("描述信息：").append(desc).append("\n");
//		sb.append("错误栈：\n").append(parseExceptionString(exception));
//
//		this.send("外部服务接口-日志预警", sb.toString());
//	}
//
//	/**
//	 * 解析错误栈信息
//	 *
//	 * @param e
//	 */
//	private String parseExceptionString(Throwable e) {
//		if (e == null) {
//			return "";
//		}
//
//		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//		PrintWriter writer = new PrintWriter(new OutputStreamWriter(byteOut));
//		e.printStackTrace(writer);
//
//		writer.flush();
//		return new String(byteOut.toByteArray());
//	}
//
//	/**
//	 * 邮件发送任务
//	 *
//	 * @author liuqs
//	 *
//	 */
//	class MailTask implements Runnable {
//		private String subject;
//		private String content;
//
//		public MailTask(String subject, String content) {
//			this.subject = subject;
//			this.content = content;
//		}
//
//		@Override
//		public void run() {
//			MimeMessage message = mailSender.createMimeMessage();
//
//			MimeMessageHelper helper = new MimeMessageHelper(message);
//			try {
//				helper.setFrom(prop.getFrom());
//				helper.setTo(prop.getTo());
//
//				String[] cc = prop.getCc();
//				if (cc != null) {
//					helper.setCc(cc);
//				}
//
//				helper.setSubject(subject);
//				helper.setText(content);
//
//				mailSender.send(message);
//
//				logger.info("Send mail successfully.");
//			} catch (Exception e) {
//				logger.error("Send mail error.", e);
//			}
//		}
//	}
//}
