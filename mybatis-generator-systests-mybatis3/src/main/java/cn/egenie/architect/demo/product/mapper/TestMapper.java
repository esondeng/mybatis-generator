package cn.egenie.architect.demo.product.mapper;

import cn.egenie.architect.demo.product.domain.Test;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper {
    Test getById(Integer id);

    List<Test> getByIds(@Param("ids") List<Integer> ids);

    int update(Test record);

    int updateSelective(Test record);

    int softDeleteById(Integer id);

    int softDeleteByIds(@Param("ids") List<Integer> ids);

    int insert(Test record);

    int batchInsert(@Param("items") List<Test> items);
}