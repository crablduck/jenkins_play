package cn.wlh.mapper;

import cn.wlh.model.SystemCompany;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemCompanyMapper extends BaseMapper<SystemCompany> {


//    @SelectKey(statement = "SELECT max(company_id) FROM system_company", keyProperty = "companyId", resultType = int.class, before = false)
//    int insertAndSelectKey(@Param("companyName")String companyName, @Param("companySecret")String companySecret);
    @Insert("INSERT INTO system_company(company_name,company_secret,create_time) VALUES(#{companyName},#{companySecret},NOW())")
    @Options(useGeneratedKeys = true,keyProperty = "companyId",keyColumn = "company_id")
    int insertAndSelectKey(SystemCompany systemCompany);
}

