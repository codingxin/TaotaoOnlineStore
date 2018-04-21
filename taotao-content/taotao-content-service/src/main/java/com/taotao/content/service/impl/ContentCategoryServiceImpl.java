package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

/*
 * 
 * 内容分类管理
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	@Autowired
	private TbContentCategoryMapper ContentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		// 根据parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		// 设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbContentCategory> list = ContentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			resultList.add(node);
		}

		return resultList;

	}

	@Override
	public TaotaoResult addContentCategory(Long parentId, String name) {
		// 创建一个pojo对象
		TbContentCategory contentCategory = new TbContentCategory();
		// 补全对象的属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		// 状态：1正常2删除
		contentCategory.setStatus(1);
		// 排序，默认为1
		contentCategory.setSortOrder(1);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		// 插入到数据库
		ContentCategoryMapper.insert(contentCategory);
		// 判断父节点状态
		TbContentCategory parent = ContentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			// 如果父节点为叶子节点应该改为父节点
			parent.setIsParent(true);
			// 更新父节点
			ContentCategoryMapper.updateByPrimaryKey(parent);
		}

		// 返回结果
		return TaotaoResult.ok(contentCategory);
	}

	@Override
	public TaotaoResult updateContentCategory(long id, String name) {
		// 通过ID查询节点对象
		TbContentCategory contentCategory = ContentCategoryMapper.selectByPrimaryKey(id);
		// 判断新的name是否和原来的值相同，如果相同则不用更新
		if (name != null && name.equals(contentCategory.getName())) {
			return TaotaoResult.ok();

		}
		contentCategory.setName(name);
		contentCategory.setUpdated(new Date());
		// 更新数据库
		ContentCategoryMapper.updateByPrimaryKey(contentCategory);
		// 返回结果
		return TaotaoResult.ok();
	}

	// 通过父节点id来查询所有节点，这是抽离出来的公共方法
	private List<TbContentCategory> getContentCategoryListByParentId(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = ContentCategoryMapper.selectByExample(example);
		return list;
	}


	//递归删除节点  
    private void deleteNode(long parentId){  
        List<TbContentCategory> list = getContentCategoryListByParentId(parentId);  
        for(TbContentCategory contentCategory : list){  
            contentCategory.setStatus(2);  
            ContentCategoryMapper.updateByPrimaryKey(contentCategory);  
            if(contentCategory.getIsParent()){  
                deleteNode(contentCategory.getId());  
            }  
        }  
    }  
	@Override
	public TaotaoResult deleContentCategory(long id) {
		// 删除分类，就是改节点的状态为2
		TbContentCategory contentCategory = ContentCategoryMapper.selectByPrimaryKey(id);
		// 状态。可选值:1(正常),2(删除)
		contentCategory.setStatus(2);
		ContentCategoryMapper.updateByPrimaryKey(contentCategory);
		// 我们还需要判断一下要删除的这个节点是否是父节点，如果是父节点，那么就级联
		// 删除这个父节点下的所有子节点（采用递归的方式删除）
		if (contentCategory.getIsParent()) {
			deleteNode(contentCategory.getId());
		}
		// 需要判断父节点当前还有没有子节点，如果有子节点就不用做修改
		// 如果父节点没有子节点了，那么要修改父节点的isParent属性为false即变为叶子节点
		TbContentCategory parent = ContentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
		List<TbContentCategory> list = getContentCategoryListByParentId(parent.getId());
		// 判断父节点是否有子节点是判断这个父节点下的所有子节点的状态，如果状态都是2就说明
		// 没有子节点了，否则就是有子节点。
		boolean flag = false;
		for (TbContentCategory tbContentCategory : list) {
			if (tbContentCategory.getStatus() == 0) {
				flag = true;
				break;
			}
		}
		// 如果没有子节点了
		if (!flag) {
			parent.setIsParent(false);
			ContentCategoryMapper.updateByPrimaryKey(parent);
		}
		// 返回结果
		return TaotaoResult.ok();
	}

}