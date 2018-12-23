package cn.wlh.service;

import cn.wlh.model.Field;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.List;

public interface IFieldService extends IService<Field> {
    int getFieldCount(Field field);

    Page getField(Page<Field> page, Field field);

    String getFieldType(String fieldName);

    List<Field> getField();
}
