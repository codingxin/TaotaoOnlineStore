package com.taotao.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

/**
 * 商品详情页面
 * @author Person
 *
 */
@Controller
public class ItemController {
	@Autowired
	private ItemService ItemService;
	
	@RequestMapping("/item/{itemId}")
	public String showItem(@PathVariable Long itemId,Model model) {
		//取商品基本信息
		TbItem tbItem=ItemService.getItemById(itemId);
		Item item=new Item(tbItem);
		//取商品详情
		TbItemDesc tbItemDesc=ItemService.geTbItemDescById(itemId);
		model.addAttribute("item",item);
		model.addAttribute("itemDesc", tbItemDesc);
		return "item";
	}
	
	
	
	
	
	
	
	
	
	
	

}
