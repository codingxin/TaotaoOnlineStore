package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {

	TaotaoResult addContent(TbContent content);
	//大广告轮播图
    List<TbContent> getContentByid(long cid);
}
