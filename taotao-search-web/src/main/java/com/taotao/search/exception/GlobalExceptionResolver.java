package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception e) {
		logger.info("进入全局异常处理器..........");
		logger.debug("测试handler类型："+handler.getClass());
		// 控制台打印异常
		e.printStackTrace();
		// 向日志文件中写入异常
		logger.error("系统发生异常",e);
		// 发邮件
		//jmail
		// 发短信
		//Jmail
		// 展示错误页面
      ModelAndView modelAndView=new ModelAndView();
      modelAndView.addObject("message","您的电脑有问题，稍后重试");
      //error/exception.jsp
      modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
