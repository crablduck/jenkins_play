package cn.wlh.mapper;

import cn.wlh.model.Field;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

public interface FieldMapper extends BaseMapper<Field> {
    List<Field> getField(Field field);
}
