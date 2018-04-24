package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.util.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.BigPicture;


@Controller
public class IndexController {
	@Value("${PICTURE1_CATEGORY_ID}")
	private Long PICTURE1_CATEGORY_ID;

	@Value("${PICTURE1_WIDTH}")
	private Integer PICTURE1_WIDTH;

	@Value("${PICTURE1_WIDTH_b}")
	private Integer PICTURE1_WIDTH_b;

	@Value("${PICTURE1_HEIGHT}")
	private Integer PICTURE1_HEIGHT;

	@Value("${PICTURE1_HEIGHT_B}")
	private Integer PICTURE1_HEIGHT_B;
	@Autowired
	private ContentService ContentService;

	@RequestMapping("/index")
	public String showIndex(Model model) {
		// 根据cid查询内容列表轮播图
		List<TbContent> contentList = ContentService.getContentByid(PICTURE1_CATEGORY_ID);
		// 把列表转换为BigPicture列表
		List<BigPicture> pictures = new ArrayList<>();
		for (TbContent tbContent : contentList) {
			BigPicture node = new BigPicture();
			node.setAlt(tbContent.getTitle());
			node.setHeight(PICTURE1_HEIGHT);
			node.setHeightB(PICTURE1_HEIGHT_B);
			node.setWidth(PICTURE1_WIDTH);
			node.setWidthB(PICTURE1_HEIGHT_B);
			// 从数据库中读取
			node.setSrc(tbContent.getPic());
			node.setSrcB(tbContent.getPic2());
			node.setHref(tbContent.getUrl());
			// 添加节点到列表
			pictures.add(node);

		}
		// 把列表转换为json数据
		String objectToJson = JsonUtils.objectToJson(pictures);
	
		// 把json数据传递给页面
       model.addAttribute("ad1", objectToJson);
		
		// return web-INF/jsp/index.jsp
		return "index";

	}

}
