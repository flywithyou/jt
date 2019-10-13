package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SysResult {
	private Integer status ;	//定义状态码 200正常 201失败
	private String msg;			//服务器返回客户端消息
	private Object data;		//服务器返回客户端数据

	public static SysResult Success() {
		return new SysResult().setStatus(200);
	}
	public static SysResult Success(Object object) {
		return new SysResult().setStatus(200).setData(object);
	}
	public static SysResult Success(String msg,Object data) {
		return new SysResult(200, msg, data);
	}
	
	public static SysResult fail() {
		return new SysResult().setStatus(201);
	}
	public static SysResult fail(String msg) {
		return new SysResult().setStatus(201).setMsg(msg);
	}
}
