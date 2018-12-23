package cn.wlh.mapper;

import cn.wlh.model.ConsumeRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;

public interface ConsumeMapper extends BaseMapper<ConsumeRecord> {

    @Select("SELECT SUM(consume_amount) as consume_amount, company_name FROM consume_record WHERE add_time BETWEEN #{beginDate} AND #{nowDate}  GROUP BY company_name")
    List<HashMap<String, Object>> getConsumeSum(@Param("beginDate") String beginDate, @Param("nowDate")String nowDate);

    @Select("SELECT DATE_FORMAT( add_time, \"%Y-%m-%d\" ) as bill_time , COUNT( * ) as consume_count\n" +
            "FROM consume_record\n" +
            "WHERE (add_time BETWEEN #{beginTime} AND #{endTime} )AND company_name = #{companyName}\n" +
            "GROUP BY DATE_FORMAT( add_time, \"%Y-%m-%d\" )")
    List<HashMap<String, Object>> selectGroupByDay(@Param("beginTime") String beginTime, @Param("endTime") String endTime,@Param("companyName") String companyName);
}
