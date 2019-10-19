package com.jt.service;

import org.springframework.transaction.annotation.Transactional;

import com.jt.pojo.Order;

public interface DubboOrderService {
	/**
	 * 订单入库，并返回订单id
	 * @param order
	 * @return
	 */
	@Transactional
	String saveOrdeer(Order order);
	
	/**
	 * 根据orderId查询订单信息
	 * @param id
	 * @return
	 */
	Order findOrderById(String id);

}
