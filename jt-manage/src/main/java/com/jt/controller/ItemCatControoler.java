package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatControoler {
	@Autowired
	private ItemCatService itemCatService;

	/**
	 * 	根据商品分类id查询名称
	 */
	@RequestMapping("/queryItemName")
	public String findItemCatById(Long itemCatId){
		ItemCat itemCat = itemCatService.findItemCatById(itemCatId);
		return itemCat.getName();
	}
	/**
	 * url:/item/cat/list
	 * 	返回结果：List<EasyUITree>
	 * 	业务思想：
	 * 		只查询一级商品分类信息
	 * 		parent_id=0
	 * springmvc 动态接收数据
	 * 	参数名：id
	 * 	目的：id当做parentId使用
	 * 	要求：初始化为0
	 * 	@RequestParam注解说明
	 * 		name/value:接收用户提交参数
	 * 		defaultValue：设定默认值
	 * 		required：改参数是否必传 true/false
	 * @return
	 */
	@RequestMapping("/list")
	public List<EasyUITree> findEasyUITree(@RequestParam(value = "id",defaultValue = "0",required = true)Long parentId){
		return itemCatService.findEasyUITree(parentId);
//		return itemCatService.findItemCache(parentId);
	}
}
