package cn.wlh.mapper;

import cn.wlh.model.SystemUser;
import cn.wlh.vo.UserInfoReq;
import cn.wlh.vo.UserInfoRes;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemUserMapper extends BaseMapper<SystemUser> {


    @Insert("INSERT INTO system_user(user_account,username,password,user_phone,creater_id,company_id,commodity_id,state,descr,create_time) VALUES(#{userAccount},#{username},#{password},#{userPhone},#{createrId},#{companyId},#{commodityId},#{state},#{descr},NOW())")
    @Options(useGeneratedKeys = true,keyProperty = "userId",keyColumn = "user_id")
    int insertAndSelectKey(SystemUser systemUser);

    List<UserInfoRes> selectUserPages(Pagination page, @Param("userInfoReq") UserInfoReq userInfoReq);

    List<UserInfoRes> selectCompanyUserPages(Pagination page, @Param("userInfoReq") UserInfoReq userInfoReq);

    //更新当前用户子账户个数
    @Update("UPDATE system_user SET sub_account_num = sub_account_num + 1 WHERE user_id = #{userId}")
    int updateSubSumByUserId(Integer userId);

    /**
     * 获取公司级用户下所有子账户
     * @return
     */
    List<UserInfoRes> selectSubUserPages(Pagination page, @Param("userInfoReq") UserInfoReq userInfoReq);
}
