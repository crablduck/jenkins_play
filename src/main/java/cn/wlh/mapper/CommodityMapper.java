package cn.wlh.mapper;

import cn.wlh.model.Commodity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityMapper extends BaseMapper<Commodity> {

    @Insert("INSERT INTO commodity(commodity_number,commodity_name,commodity_secret,company_id, state,create_time)" +
            " VALUES(#{commodityNumber},#{commodityName},#{commoditySecret},#{companyId},1, NOW())")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insertBackKeyId(Commodity commodity);
}
