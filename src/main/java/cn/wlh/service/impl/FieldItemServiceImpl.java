package cn.wlh.service.impl;

import cn.wlh.mapper.FieldItemMapper;
import cn.wlh.model.FieldItem;
import cn.wlh.service.IFieldItemService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldItemServiceImpl extends ServiceImpl<FieldItemMapper, FieldItem> implements IFieldItemService {
    @Override
    public List<FieldItem> getCompareRela(String fieldType) {
        Wrapper<FieldItem> fieldItemQueryWrapper = new EntityWrapper<>();
        fieldItemQueryWrapper.eq("field_type", fieldType);
        List<FieldItem> list = this.selectList(fieldItemQueryWrapper);

        return list;
    }
}
