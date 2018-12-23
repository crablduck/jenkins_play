package cn.wlh.service.impl;

import cn.wlh.mapper.FieldMapper;
import cn.wlh.model.Field;
import cn.wlh.service.IFieldService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;


@Service(value = "userService")
public class FieldServiceImpl extends ServiceImpl<FieldMapper, Field> implements IFieldService {


    @Override
    public int getFieldCount(Field field) {
        return 0;
    }

    @Override
    public Page getField(Page<Field> page, Field field) {
        String fieldType = field.getFieldType();
        String fieldName = field.getFieldName();
        Wrapper<Field> wrapper = new EntityWrapper<>();

        if(null != fieldType && !"".equalsIgnoreCase(fieldType)){
            wrapper.eq("field_type", fieldType);
        }
        if(null != fieldName && !"".equalsIgnoreCase(fieldName)){
            wrapper.andNew().like("field_name", fieldName)
                    .or().like("display_name", fieldName);
        }

        return this.selectPage(page, wrapper);
    }

    @Override
    public String getFieldType(String fieldName) {
        Wrapper<Field> fieldQueryWrapper = new EntityWrapper<>();

        fieldQueryWrapper.eq("field_name", fieldName);
        Field one =  this.selectOne(fieldQueryWrapper);
        return one.getFieldType();
    }

    @Override
    public List<Field> getField() {
        return this.selectList(new EntityWrapper<>());
    }
}
