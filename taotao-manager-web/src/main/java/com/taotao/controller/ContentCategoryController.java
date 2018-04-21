package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
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
    //参数parentId与id名称不一致，因此需要@RequestParam
    public List<EasyUITreeNode> getContentCategoryList(
    		@RequestParam(value="id",defaultValue="0")Long parentid)
    {
    	List<EasyUITreeNode> list=ContentCategoryService.getContentCategoryList(parentid);
    	return list;
    }
    
    
    //{parentId:node.parentId,id:node.id}
    @RequestMapping("/content/category/create")
    @ResponseBody
    public  TaotaoResult addContentCategory(Long parentId,String name)
    {
    	TaotaoResult result=ContentCategoryService.addContentCategory(parentId, name);
    	return result;
    }
    
    @RequestMapping("/content/category/update")    
    @ResponseBody    
    public TaotaoResult updateContentCategory(Long id,String name){    
        TaotaoResult taotaoResult = ContentCategoryService.updateContentCategory(id, name);    
        return taotaoResult;    
    }    
        
    
    @RequestMapping("/content/category/delete/")    
    @ResponseBody    
    public TaotaoResult deleteContentCategory(Long id){    
        TaotaoResult taotaoResult = ContentCategoryService.deleContentCategory(id);    
        return taotaoResult;    
    }    
    
    
    

}
