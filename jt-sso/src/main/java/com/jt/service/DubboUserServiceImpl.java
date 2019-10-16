package com.jt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.UserMapper;

@Service
public class DubboUserServiceImpl implements DubboUserSerivce {
	@Autowired
	private UserMapper userMapper;
}
