package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;

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

	/*
	 * mybatis的逆向工程中会生成实例及实例对应的example，example用于添加条件，相当where后面的部分 xxxExample example
	 * = new xxxExample(); Criteria criteria = new Example().createCriteria();
	 */
	@Override
	public List<TbContent> getContentByid(long cid) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		//执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		return list;
	}

}
