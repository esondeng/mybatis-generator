package com.sankuai.meituan.waimai.agent.template.mapper;

import com.sankuai.meituan.waimai.agent.template.domain.Test;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {
    Test getById(Integer id);

    int update(Test record);

    int updateSelective(Test record);

    int deleteById(Integer id);

    int insert(Test record);

    int batchInsert(@Param("items") List<Test> items);
}