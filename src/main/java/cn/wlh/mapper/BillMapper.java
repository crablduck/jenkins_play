package cn.wlh.mapper;

import cn.wlh.model.Bill;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

public interface BillMapper extends BaseMapper<Bill> {


    @Select("SELECT SUM(c.consume_amount) as consume_amount, SUM(r.recharge_amount) as recharge_amount, c.company_name " +
            "FROM consume_record c LEFT JOIN recharge_record r ON r.company_name=c.company_name  WHERE c.add_time BETWEEN #{beginDate} AND #{nowDate} GROUP BY c.company_name")
    List<HashMap<String, Object>> getBillCountByType(@Param("beginDate") String beginDate,@Param("nowDate")  String nowDate);

    @Select("SELECT DATE_FORMAT( add_time, \"%Y-%m-%d\" ) as bill_time , COUNT( * ) as bill_count\n" +
            "FROM bill_manage\n" +
            "WHERE (add_time BETWEEN #{beginTime} AND #{endTime} )AND company_name = #{companyName}\n" +
            "GROUP BY DATE_FORMAT( add_time, \"%Y-%m-%d\" ) ")
    List<HashMap<String, Object>> selectGroupByDay(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("companyName") String companyName);
}
