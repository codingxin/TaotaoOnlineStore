package com.taotao.dao;

import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbItemCatDao {
    int countByExample(TbItemCatQuery example);

    int deleteByExample(TbItemCatQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(TbItemCat record);

    int insertSelective(TbItemCat record);

    List<TbItemCat> selectByExample(TbItemCatQuery example);

    TbItemCat selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbItemCat record, @Param("example") TbItemCatQuery example);

    int updateByExample(@Param("record") TbItemCat record, @Param("example") TbItemCatQuery example);

    int updateByPrimaryKeySelective(TbItemCat record);

    int updateByPrimaryKey(TbItemCat record);
}