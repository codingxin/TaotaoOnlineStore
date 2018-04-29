package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;

import redis.clients.jedis.JedisCluster;

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private JedisClient JedisClient;
	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;

	@Override
	public TaotaoResult addContent(TbContent content) {
		// 补全对象
		content.setCreated(new Date());
		content.setUpdated(new Date());
		// 插入到内容表
		contentMapper.insert(content);
		// 同步缓存
		// 删除对应缓存信息
		JedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());

		return TaotaoResult.ok();
	}

	/*
	 * mybatis的逆向工程中会生成实例及实例对应的example，example用于添加条件，相当where后面的部分 xxxExample example
	 * = new xxxExample(); Criteria criteria = new Example().createCriteria();
	 */
	@Override
	public List<TbContent> getContentByid(long cid) {
		// 先查询缓存
		// 添加缓存不能影响正常业务逻辑
		try {
			// 查询缓存
			String json = JedisClient.hget(INDEX_CONTENT, cid + "");
			// 查询到结果，把Json转换成List返回
			if (StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存中没有命中，需要查询数据库

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		// 执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		// 把结果添加到缓存,list转化为字符串objectToJson
		try {
			JedisClient.hset("INDEX_CONTENT", cid + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回结果

		return list;
	}

}
