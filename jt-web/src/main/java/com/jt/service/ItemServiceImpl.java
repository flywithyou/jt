package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private HttpClientService httpClient;
	@Override
	public Item findItemById(Long itemId) {
		String url  = "http://manage.jt.com/web/item/findItemById";
		Map<String, String> parms = new HashMap<>();
		parms.put("id", itemId+"");
		String result = httpClient.doGet(url,parms);
		System.out.println(result);
		Item item = ObjectMapperUtil.toObject(result, Item.class);
		return item;
	}
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String url  = "http://manage.jt.com/web/item/findItemDescById";
		Map<String, String> parms = new HashMap<>();
		parms.put("id", itemId+"");
		String result = httpClient.doGet(url,parms);
		return ObjectMapperUtil.toObject(result, ItemDesc.class);
	}
}
