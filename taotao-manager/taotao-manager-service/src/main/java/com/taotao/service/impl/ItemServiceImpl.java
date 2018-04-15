package com.taotao.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.dao.TbItemDao;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

//默认生成对象首字母小写itemServiceImpl
@Service
public class ItemServiceImpl implements ItemService{
  
	//表名+mapper
	@Autowired
    private TbItemDao itemMapper;
	
	
	/*
	 * 
	 * 
	 * 商品管理Service
	 * @see com.taotao.service.ItemService#getItemById(long)
	 */
	@Override
	public TbItem getItemById(long itemId) {
		TbItem item=itemMapper.selectByPrimaryKey(itemId);
		return item;
	}

	
	
	
	
}
