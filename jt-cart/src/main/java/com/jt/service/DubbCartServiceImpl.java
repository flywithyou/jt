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
		//mybatis plus 根据对象中部位null的属性进行操作
		Cart cartTemp = new Cart();
		cartTemp.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
		updateWrapper.eq("user_id", cart.getUserId()).eq("item_id",cart.getItemId());		
		int result = cartMapper.update(cartTemp, updateWrapper);
		System.out.println(result);
	}

	@Override
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("item_id", cart.getItemId()).eq("user_id", cart.getUserId());
		int rows = cartMapper.delete(queryWrapper);
		System.out.println(rows);
	}

	@Override
	public void addCart(Cart cart) {
		//1.首先查询数据库中是否已存在相同的商品，如果存在则更新数据，否则新增购物车信息
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("item_id", cart.getItemId()).eq("user_id", cart.getUserId());
		//数据库中的记录
		Cart result = cartMapper.selectOne(queryWrapper);
		if (null == result) {
			cart.setCreated(new Date()).setUpdated(cart.getCreated());
			cartMapper.insert(cart);
			return;
		}else {
			//优化更新操作，通过主键id仅更新num、updated属性
			int num = cart.getNum()+result.getNum();
			Cart tempCart = new Cart();
			tempCart.setId(result.getId()).setNum(num).setUpdated(new Date());
			cartMapper.updateById(tempCart);
		}
	}
	
}
