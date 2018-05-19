package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemAddMessageListener implements MessageListener {
	@Autowired
	private ItemService ItemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;

	@Override
	public void onMessage(Message message) {
		try {
			// 从消息中取商品ID
			TextMessage textMessage = (TextMessage) message;
			String strId = textMessage.getText();
			Long itemId = Long.parseLong(strId);
			//等待事务提交
			Thread.sleep(1000);
			// 根据商品Id查询商品信息及商品描述
			TbItem tbItem = ItemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = ItemService.geTbItemDescById(itemId);
			// 使用Freemarker生成静态页面
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			// 1.创建模板
			// 2.加载模板对象
			Template template = configuration.getTemplate("item.ftl");
			// 3.准备模板需要的数据
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			// 4.指定输出的目录及文件名
			Writer out = new FileWriter(new File(HTML_OUT_PATH + strId + ".html"));
			// 5.生成静态页面
			template.process(data, out);
			// 6.关闭流
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
