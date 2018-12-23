package cn.wlh.mapper;

import cn.wlh.model.SystemDict;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemDictMapper extends BaseMapper<SystemDict> {

    @Update("UPDATE system_dict SET state = #{state} WHERE id = #{id}")
    boolean updateState(@Param("id") Integer id, @Param("state") String state);
}
