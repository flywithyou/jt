package com.jt.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;

public interface ItemMapper extends BaseMapper<Item>{
	/**
	 * 	分页查询商品信息
	 * @param start
	 * @param rows
	 * @return
	 */
	@Select("select * from tb_item order by updated desc limit #{start},#{rows}")
	List<Item> findItemByPage(@Param("start")Integer start,@Param("rows") Integer rows);

	void updateItemsStatusById(Long[] ids, int status, Date date);
	
}
