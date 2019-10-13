package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {
	/**
	 * 	分页查询商品信息
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUITable findItemByPage(Integer page, Integer rows);

	void saveItem(Item item, ItemDesc itemDesc);

	void updateItem(Item item, ItemDesc itemDesc);

	void deleteItems(Long[] ids);

	void updateItemsStatus(int status, Long[] ids);

	ItemDesc findItemDescById(Long itemId);
	
}
