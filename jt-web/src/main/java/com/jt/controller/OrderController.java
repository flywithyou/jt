package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Reference(check = false)
	private DubboCartService cartService;
	@Reference(check = false)
	private DubboOrderService orderService;
	
	
	@RequestMapping("/create")
	public String create(Model model) {
		User user = ThreadLocalUtil.get();
		List<Cart> carts = cartService.findCartListByUserId(user.getId());
		model.addAttribute("carts",carts);
		return "order-cart";
	}
	
	/**
	 * 业务说明：
	 * 	完成订单入库操作，并且返回orderId
	 * 	自动生成orderId
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/submit")
	public SysResult saveOrder(Order order) {
		//1.订单入库
		String orderId = orderService.saveOrdeer(order);
		//2.将商品信息从购物车中移除
		User user = ThreadLocalUtil.get();
		for (OrderItem orderItem : order.getOrderItems()) {
			Cart cart = new Cart().setUserId(user.getId()).setItemId(Long.valueOf(orderItem.getItemId()));
			cartService.deleteCart(cart);
		}
		return SysResult.Success(orderId);
	}
	
	//根据orderID查询数据库，3张表
	@RequestMapping("/success")
	public String successOrder(String id,Model model) {
		Order order =  orderService.findOrderById(id);
		model.addAttribute("order",order);
		return "success";
	}
}
