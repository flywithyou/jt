package com.jt.thro;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice	//对controller层的异常有效
@Slf4j					//引入日志
public class SystemException {
	//支队运行时异常有效
	@ExceptionHandler(RuntimeException.class)
	public SysResult exception(Throwable throwable) {
		throwable.printStackTrace();
		log.info(throwable.getMessage());
		return SysResult.fail("服务器调用失败！");
	}
}
