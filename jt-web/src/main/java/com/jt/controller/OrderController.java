package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.ThreadLocalUtil;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Reference(check = false)
	private DubboCartService cartService;
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
		
		return SysResult.Success();
	}
	//根据orderID查询数据库，3张表
}
