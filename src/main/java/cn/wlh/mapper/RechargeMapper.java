package cn.wlh.mapper;

import cn.wlh.model.RechargeRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;


public interface RechargeMapper extends BaseMapper<RechargeRecord> {

    @Select("SELECT SUM(recharge_amount) as recharge_amount, company_name FROM recharge_record WHERE add_time BETWEEN #{beginDate} AND #{nowDate} GROUP BY company_name")
    List<HashMap<String, Object>> getRechargeSum(@Param("beginDate") String beginDate, @Param("nowDate") String nowDate);

    @Select("SELECT DATE_FORMAT( add_time, \"%Y-%m-%d\" ) as bill_time , COUNT( * ) as recharge_count\n" +
            "FROM recharge_record\n" +
            "WHERE (add_time BETWEEN #{beginTime} AND #{endTime} )AND company_name = #{companyName}\n" +
            "GROUP BY DATE_FORMAT( add_time, \"%Y-%m-%d\" )")
    List<HashMap<String, Object>> selectGroupByDay(@Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("companyName") String companyName);
}
