package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
		String orderId = ""+order.getUserId()+new Date().getTime();
		//2.1首先入库订单
		Date date = new Date();		//生成统一日期
		order.setOrderId(orderId).setStatus(1).setCreated(date).setUpdated(date);
		orderMapper.insert(order);
		//2.2入库订单物流信息
		OrderShipping orderShipping = order.getOrderShipping();	
		orderShipping.setOrderId(orderId).setCreated(date).setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		//2.3入库订单商品
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId).setCreated(date).setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		return order.getOrderId();
	}
	@Override
	public Order findOrderById(String id) {
//		Order order = orderMapper.selectById(id);
//		OrderShipping orderShipping = orderShippingMapper.selectById(id);
//		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq("order_id", id);
//		List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
//		order.setOrderShipping(orderShipping);
//		order.setOrderItems(orderItems);
		Order order = orderMapper.findOrderById(id);
		return order;
	}
	
}
