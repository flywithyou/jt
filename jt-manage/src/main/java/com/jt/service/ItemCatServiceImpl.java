package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.annotion.Cache_Find;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;
@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private ItemCatMapper itemCatMapper;
//	@Autowired
	private Jedis jedis;

	@Override
	public ItemCat findItemCatById(Long itemCatId) {
		return itemCatMapper.selectById(itemCatId);
	}

	/**
	 * pojo 转 vo
	 * 1.根据parentId查询list信息
	 * 2.遍历list集合 pojo转换为 vo
	 * 3.重新封装volist
	 * 
	 */
	@Cache_Find()
	@Override
	public List<EasyUITree> findEasyUITree(Long parentId) {
		List<ItemCat> itemCatList = findItemCatByParentId(parentId);
		List<EasyUITree> easyUITrees=new ArrayList<EasyUITree>();
		for (ItemCat itemCat : itemCatList) {
			//如果是父级则closed，子级则open
			String state = itemCat.getIsParent()?"closed":"open";
			EasyUITree easyUITree = new EasyUITree(itemCat.getId(), itemCat.getName(), state);
			easyUITrees.add(easyUITree);
		}
		System.out.println("走数据库！");
		return easyUITrees;
	}
	/**
	 * 	根据父id查询分类信息
	 * @param parentId
	 * @return
	 */
	private List<ItemCat> findItemCatByParentId(Long parentId) {
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);
		return itemCatList;
	}

	@Override
	public List<EasyUITree> findItemCache(Long parentId) {
		//获取key
		String key = "ITEM_CAT_"+parentId;
		//先从Redis缓存中根据parentId获取值
		String json = jedis.get(key);
		//假如值为null，则说明缓存中还没有存储有响应的数据，需要从数据库中查询
		if (StringUtils.isEmpty(json)) {
			//从数据库中根据parentId查询
			List<EasyUITree> listTree = findEasyUITree(parentId);
			//将查询到的对象转化为json串，并存入Redis缓存中
			json = ObjectMapperUtil.toJSON(listTree);
			jedis.set(key, json);
			return listTree;
		}
		//将json串转化为对象并返回
		return ObjectMapperUtil.toObject(json, List.class);
	}
}
