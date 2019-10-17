package com.jt.service;

import java.util.List;

import com.jt.pojo.Cart;

public interface DubboCartService {
	
	
	/**
	 * 根据用户ID查询所有的购物记录
	 * @param userId
	 * @return
	 */
	List<Cart> findCartListByUserId(Long userId);
	
	/**
	 * 更新购物车数量信息
	 * @param cart
	 */
	void updateCartNum(Cart cart);

}
