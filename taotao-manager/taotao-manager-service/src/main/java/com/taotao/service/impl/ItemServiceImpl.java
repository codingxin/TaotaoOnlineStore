package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.IDUtils;
import com.taotao.common.util.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

/**
 * 商品管理Service
 * <p>
 * Title: ItemServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.itcast.cn
 * </p>
 * 
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper descmapper;
	@Autowired
	private JmsTemplate JmsTemplate;
	@Resource(name = "itemAddtopic")
	private Destination Destination;
	@Autowired
	private JedisClient JedisClient;

	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${ITEM_EXPIRE}")
	private Integer ITEM_EXPIRE;

	// 加缓存
	@Override
	public TbItem getItemById(long itemId) {
		// 查询数据库之前 ，先查询缓存
		try {
			String json=JedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
			if(StringUtils.isNotBlank(json))
			{//把json数据转换成pojo
				TbItem tbItem=JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 缓存中没有查询数据库
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
      
		try {// 那字符转化为JSON, 把查询结果添加到缓存
			JedisClient.set(ITEM_INFO + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
			// 设置过期时间，提高缓存利用率
			JedisClient.expire(ITEM_INFO + ":" + itemId + ":BASE", ITEM_EXPIRE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return item;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		// 取查询结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		// 返回结果
		return result;
	}

	// 添加商品时用activemq消息队列，发送队列消息
	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		// 生成商品ID
		long itemId = IDUtils.genItemId();
		// 补全item属性
		item.setId(itemId);
		// 商品状态：1-正常2-下架3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		itemMapper.insert(item);
		// 创建一个商品描述表所对应的pojo
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全pojo的属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(new Date());
		itemDesc.setCreated(new Date());
		// 向商品描述表插入数据
		descmapper.insert(itemDesc);
		// 向Activemq发送商品添加消息
		JmsTemplate.send(Destination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// 发送商品ID
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		// 返回结果
		return TaotaoResult.ok();

	}

	@Override
	public TbItemDesc geTbItemDescById(long itemId) {
	// 查询数据库之前 ，先查询缓存
				try {
					String json=JedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
					if(StringUtils.isNotBlank(json))
					{//把json数据转换成pojo
						TbItemDesc tbItemDesc=JsonUtils.jsonToPojo(json, TbItemDesc.class);
						return tbItemDesc;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 缓存中没有查询数据库
				TbItemDesc itemDesc = descmapper.selectByPrimaryKey(itemId);
		      
				try {// 那字符转化为JSON, 把查询结果添加到缓存
					JedisClient.set(ITEM_INFO + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
					// 设置过期时间，提高缓存利用率
					JedisClient.expire(ITEM_INFO + ":" + itemId + ":DESC", ITEM_EXPIRE);

				} catch (Exception e) {
					e.printStackTrace();
				}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return itemDesc;
	}

}
