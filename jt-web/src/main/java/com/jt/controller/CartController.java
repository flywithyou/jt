package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Reference(check = false)
	private DubboCartService cartService;

	/**
	 * 查询用户的全部购物记录信息
	 * @return
	 */
	@RequestMapping("/show")
	public String show(Model model) {
		Long userId = 7L;
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("cartList",cartList);
		return "cart";
	}

	@RequestMapping("/update/num/{itemId}/{num}")
	public String updateCartNum(Cart cart) {
		Long userId = 7L;
		cart.setUserId(userId);
		cartService.updateCartNum(cart);
		System.err.println("进来了");
		return "redirect:/cart/show";
	}
	@RequestMapping("/delete/{itemId}")
	public String  deleteCart(Cart cart) {
		Long userId = 7L;
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		System.err.println("进来了");
		return "redirect:/cart/show";
	}
	@RequestMapping("/add/{itemId}")
	public String addCart(Cart cart) {
		Long userId = 7L;
		cart.setUserId(userId);
		cartService.addCart(cart);
		System.err.println("进来了");
		return "redirect:/cart/show";
	}
}
