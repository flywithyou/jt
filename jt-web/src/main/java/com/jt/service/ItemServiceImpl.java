package com.jt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.pojo.Item;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private HttpClientService httpClient;
	@Override
	public Item findItemById(Long itemId) {
		String url  = "http://manage.jt.com/web/item/findItemById?id=562379";
		Map<String, String> parms = new HashMap<>();
		parms.put("id", itemId+"");
		String result = httpClient.doGet(url,parms);
		Item item = ObjectMapperUtil.toObject(result, Item.class);
		return item;
	}

}
