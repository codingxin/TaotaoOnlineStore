package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCategoryList(long parentid);
	
	
	
}
