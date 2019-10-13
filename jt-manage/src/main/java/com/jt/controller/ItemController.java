package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController //返回数据都是json
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		return itemService.findItemByPage(page,rows);
	}

	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		itemService.saveItem(item,itemDesc);
		return SysResult.Success();
	}
	/**
	 * 修改商品信息，一般按照id修改
	 * @param item
	 * @return
	 */
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		itemService.updateItem(item,itemDesc);
		return SysResult.Success();
	}
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids) {
		itemService.deleteItems(ids);
		return SysResult.Success();
	}
	@RequestMapping("/instock")
	public SysResult instock(Long[] ids) {
		/*表示下架*/
		int status =2;
		itemService.updateItemsStatus(status,ids);
		return SysResult.Success();
	}
	@RequestMapping("/reshelf")
	public SysResult reshelf(Long[] ids) {
		/*表示下架*/
		int status =1;
		itemService.updateItemsStatus(status,ids);
		return SysResult.Success();
	}
	/**
	 * 商品详情查询
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {
		ItemDesc itemDesc = itemService.findItemDescById(itemId);
		//将数据回传给页面
		return SysResult.Success(itemDesc);
	}
}
