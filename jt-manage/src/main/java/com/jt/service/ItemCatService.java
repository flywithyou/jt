package com.jt.service;

import java.util.List;

import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;

public interface ItemCatService {
	/**
	 * 	根据id查询对象
	 * @param itemCatId
	 * @return
	 */
	ItemCat findItemCatById(Long itemCatId);

	List<EasyUITree> findEasyUITree(Long parentId);

	List<EasyUITree> findItemCache(Long parentId);

}
