package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;

/*
 * 
 * 索引库维护Controller
 */
@Controller
public class IndexlManagerController {
	@Autowired
	private SearchItemService SearchItemService;

	@RequestMapping("/index/import")
	@ResponseBody
	public TaotaoResult importIndex() {
		TaotaoResult taotaoResult = SearchItemService.importItemsToIdex();
		return taotaoResult;
	}
}
