package com.jt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class TestJson {
	public static final ObjectMapper MAPPER = new ObjectMapper();
	@Test
	public void toJson() throws IOException {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemDesc("商品详情").
				setItemId(100L).setCreated(new Date()).setUpdated(itemDesc.getCreated());
		String json = MAPPER.writeValueAsString(itemDesc);
		System.out.println(json);
		
		itemDesc = MAPPER.readValue(json, ItemDesc.class);
		System.out.println(itemDesc.toString());
	}
	@Test
	public void testList() throws IOException {
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemDesc("商品详情").
				setItemId(100L).setCreated(new Date()).setUpdated(itemDesc1.getCreated());
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemDesc("商品详情").
				setItemId(100L).setCreated(new Date()).setUpdated(itemDesc2.getCreated());
		List<ItemDesc> list = new ArrayList<>();
		list.add(itemDesc1);
		list.add(itemDesc2);
		String json = MAPPER.writeValueAsString(list);
		System.out.println(json);
		
		@SuppressWarnings("unchecked")
		List<ItemDesc> list1  = MAPPER.readValue(json, list.getClass());
		System.out.println(list1);
	}
}
