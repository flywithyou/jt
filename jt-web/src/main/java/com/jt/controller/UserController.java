package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.service.DubboUserSerivce;

@Controller
@RequestMapping("/user")
public class UserController {
	@Reference(check = false)
	private DubboUserSerivce dubboUserSerivce;
	@RequestMapping("/{moduleName}")
	public String module(@PathVariable String moduleName) {
		return moduleName;
	}
}
