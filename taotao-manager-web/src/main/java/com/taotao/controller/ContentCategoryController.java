package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.content.service.ContentCategoryService;

/*
 * 
 * 内容分类管理Controller
 */
@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService ContentCategoryService;
	
	
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(
    		@RequestParam(value="id",defaultValue="0")long parentid)
    {
    	List<EasyUITreeNode> list=ContentCategoryService.getContentCategoryList(parentid);
    	return list;
    }

}
