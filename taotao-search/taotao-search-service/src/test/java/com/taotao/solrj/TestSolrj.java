package com.taotao.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrj {

	@Test
	public void testAddDocument() throws Exception {
		// 创建一个SolrServer对象，创建一个HttpSolrServer对象
		// 需要指定solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://122.152.226.53:8080/solr/collection1");
		// 创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		// 向文档中添加域，必须有id域，域的名称必须在schema.xml定义
		document.addField("id", "test001");
		document.addField("item_title", "测试商品1");
		document.addField("item_price", 1000);
		// 把文档对象写入索引库
		solrServer.add(document);
		// 提交
		solrServer.commit();
	}

	public void deleteDocumentById() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://www.xilixili.me:8080/solr/collection1");
		solrServer.deleteById("test001");
		// 提交
		solrServer.commit();
	}

	public void deleteDocumentByQuery() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://www.xilixili.me:8080/solr/collection1");
		solrServer.deleteByQuery("item_title:测试商品1");
		solrServer.commit();
	}

	@Test
	public void searchDocument() throws Exception {
		// 创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://122.152.226.53:8080/solr/collection1");
		// 创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		// 设置查询条件，过滤条件，分页调教，排序调教，高亮
		query.setQuery("手机");
		// 分页
		query.setStart(0);
		query.setRows(10);
		// 设置默认查询条件
		query.set("df", "item_keywords");
		// 设置高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<div>");
		query.setHighlightSimplePre("</div>");
		// 执行查询，得到一个Response对象
		QueryResponse response = solrServer.query(query);
        //查询结果
		SolrDocumentList solrDocumentList = response.getResults();
		// 取查询结果总记录数
		System.out.println("查询结果总记录数" + solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			String itemTitle = "";
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");

			if (list != null && list.size() > 0) {
				itemTitle = list.get(0);
			} else {
				itemTitle = (String) solrDocument.get("item_title");

			}
			System.out.println(itemTitle);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));

		}

	}

}
