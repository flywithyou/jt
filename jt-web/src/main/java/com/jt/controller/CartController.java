package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.ThreadLocalUtil;
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
	public String show(Model model,HttpServletRequest request) {
//		User user = (User) request.getAttribute("JTUSER");
		Long userId = ThreadLocalUtil.get().getId();
		List<Cart> cartList = cartService.findCartListByUserId(userId);
		model.addAttribute("cartList",cartList);
		return "cart";
	}
	
	@ResponseBody
	@RequestMapping("/update/num/{itemId}/{num}")
	public SysResult updateCartNum(Cart cart) {
		Long userId = ThreadLocalUtil.get().getId();
		cart.setUserId(userId);
		cartService.updateCartNum(cart);
		return SysResult.Success();
	}
	@RequestMapping("/delete/{itemId}")
	public String  deleteCart(Cart cart) {
		Long userId = ThreadLocalUtil.get().getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		return "redirect:/cart/show";
	}
	@RequestMapping("/add/{itemId}")
	public String addCart(Cart cart) {
		Long userId = ThreadLocalUtil.get().getId();
		cart.setUserId(userId);
		cartService.addCart(cart);
		return "redirect:/cart/show";
	}
}
