package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

public interface ContentCategoryService {
    //获取内容分类列表
	List<EasyUITreeNode> getContentCategoryList(long parentid);
	//添加内容，注意参数名称要与content-category.jsp页面指定的参数名一致
	TaotaoResult addContentCategory(Long parentId,String name);
	//修改内容，注意参数名称要与content-category.jsp页面指定的参数名一致
	TaotaoResult updateContentCategory(long id,String name);
	//删除内容，注意参数名称要与content-category.jsp页面指定的参数名一致
	TaotaoResult deleContentCategory(long id);
	
}
