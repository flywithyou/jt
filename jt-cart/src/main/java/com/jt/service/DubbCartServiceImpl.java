package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service
public class DubbCartServiceImpl implements DubboCartService {
	@Autowired
	private CartMapper cartMapper;
	
	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id",userId);		
		return cartMapper.selectList(queryWrapper);
	}

	@Override
	public void updateCartNum(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
		updateWrapper.eq("user_id", cart.getUserId()).eq("item_id",cart.getItemId());		
		int result = cartMapper.update(cartTemp, updateWrapper);
		System.out.println(result);
	}
	
}
