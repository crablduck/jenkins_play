package cn.wlh.mapper;

import cn.wlh.model.Strategy;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface StrategyMapper extends BaseMapper<Strategy> {

//    @Insert("INSERT INTO strategy(strategy, rule_num, description, status, strategies_id) VALUES(#{userAccount},#{username},#{password},#{userPhone},#{createrId},#{companyId},#{commodityId},#{state},#{descr},NOW())")
//    @Options(useGeneratedKeys = true,keyProperty = "userId",keyColumn = "user_id")
//    int insertBackId(Strategy strategy);
}
