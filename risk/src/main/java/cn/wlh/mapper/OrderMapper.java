package cn.wlh.mapper;

import cn.wlh.model.Person;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface OrderMapper extends BaseMapper<Person> {


    @Select("select * from person where order_status = #{orderStatus}")
    List<Person> getListOrderByStatus(Integer orderStatus);

    @Select("SELECT COUNT(1) as order_count, company_name FROM person WHERE add_time  BETWEEN #{beginDate} AND #{endDate} GROUP BY company_name ")
    List<HashMap<String, Object>> getOrderCountByType(@Param("beginDate") String beginDate, @Param("endDate") String endDate);


    @Select(" SELECT\n" +
            "            COUNT( 1 ) as order_count\n" +
            "        FROM\n" +
            "            person\n" +
            "        WHERE\n" +
            "            (add_time BETWEEN #{beginDate} AND #{endDate}) AND company_name =  #{companyName}")
    HashMap<String, Object> getOrderByPoint(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("companyName") String companyName);

    @Select("SELECT DATE_FORMAT( add_time, \"%Y-%m-%d\" ) as order_time , COUNT( * ) as order_count\n" +
            "FROM person\n" +
            "WHERE (add_time BETWEEN #{beginTime} AND #{endTime} )AND order_status = #{status}\n" +
            "GROUP BY DATE_FORMAT( add_time, \"%Y-%m-%d\" ) ")
    List<HashMap<String, Object>> selectGroupByDay(@Param("beginTime") String beginTime,  @Param("endTime") String endTime, @Param("status")Integer status);
}
