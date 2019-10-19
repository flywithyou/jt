package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class DubbOrderServiceImpl implements DubboOrderService{
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Override
	public String saveOrdeer(Order order) {
		//1.根据时间生成订单id
		String orderId = "8"+new Date().getTime();
		//2.1首先入库订单
		order.setOrderId(orderId).setCreated(new Date()).setUpdated(order.getCreated());
		orderMapper.insert(order);
		//2.2入库订单配送信息
		OrderShipping orderShipping = order.getOrderShipping();	
		orderShipping.setOrderId(orderId).setCreated(order.getCreated()).setUpdated(order.getUpdated());
		orderShippingMapper.insert(orderShipping);
		//2.3入库订单商品
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId).setCreated(order.getCreated()).setUpdated(order.getUpdated());
			orderItemMapper.insert(orderItem);
		}
		return order.getOrderId();
	}
	@Override
	public Order findOrderById(String id) {
		Order order = orderMapper.selectById(id);
		return order;
	}
	
}
