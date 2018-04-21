package com.taotao.content.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;

	@Override
	public TaotaoResult addContent(TbContent content) {
		// 补全对象
		content.setCreated(new Date());
		content.setUpdated(new Date());
		// 插入到内容表
		contentMapper.insert(content);

		return TaotaoResult.ok();
	}

}
