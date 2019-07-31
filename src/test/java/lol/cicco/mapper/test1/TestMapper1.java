package lol.cicco.mapper.test1;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper1 {

    void saveTest1(@Param("name")String name);

    int findCount();

    void deleteData();
}
