package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		//1.获取商品记录总数
		Integer total = itemMapper.selectCount(null);
		//2.偶去分页后的数据
		int start = (page-1)*rows;
		List<Item> itemList = itemMapper.findItemByPage(start,rows);
		return new EasyUITable(total, itemList);
	}
	
	@Transactional	//事务控制
	@Override
	public void saveItem(Item item,ItemDesc itemDesc) {
		item.setStatus(1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		
		//当数据入库之后才有主键
		//mybatis特性：数据库操作中主键自增之后都会回填主键
		//新增商品详情
		itemDesc.setItemId(item.getId()).setCreated(item.getCreated()).setUpdated(item.getUpdated());
		itemDescMapper.insert(itemDesc);
	}
	@Transactional
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		System.out.println("修改详情");
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		itemDesc.setItemId(item.getId()).setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}
	@Transactional
	@Override
	public void deleteItems(Long[] ids) {
		System.out.println("批量删除");
		//将数组转化为list集合，在执行批量删除
		itemMapper.deleteBatchIds(Arrays.asList(ids));
		
		itemDescMapper.deleteBatchIds(Arrays.asList(ids));
	}
	
	/**
	 * 修改操作一般单独修改
	 */
	@Override
	@Transactional
	public void updateItemsStatus(int status, Long[] ids) {
		itemMapper.updateItemsStatusById(ids,status,new Date());
//		Item item = new Item();
//		for (Long id : ids) {
//			item.setId(id).setStatus(status).setUpdated(new Date());
//			itemMapper.updateById(item);
//		}
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

	@Override
	public Item findItemById(Long id) {
		Item item = itemMapper.selectById(id);
		return item;
	}

}
