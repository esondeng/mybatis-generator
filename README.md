# mybatis-generator
## 定制化mybatis 代码生成插件，生成简化的文件
### Mapper 注解， 生成下面简化风格的
@Mapper
public interface TabMapper {
    Tab getById(Integer id);

    int update(Tab record);

    int updateSelective(Tab record);

    int deleteById(Integer id);

    int insert(Tab record);

    int batchInsert(@Param("items") List<Tab> items);
}
### Domain 带有Getter，Setter注解
