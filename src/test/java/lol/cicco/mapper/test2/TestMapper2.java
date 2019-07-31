package lol.cicco.mapper.test2;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestMapper2 {

    void saveTest2(@Param("name")String name);

    int findCount();

    void deleteData();
}
