package com.jt.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)	//链式赋值
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITable {
	/*
	 * 	数据转化为json串时，调用属性的get方法。
	 * 	getTatal() ~~ get去掉 >>首字母小写，生成key
	 * 	value：利用get方法获取值
	 * 
	 * 	json转化为对象
	 * 	调用对象的set方法。
	 */
	private Integer total;
	private List<?> rows;
}
