package cn.wlh.service;

import cn.wlh.model.FieldItem;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface IFieldItemService extends IService<FieldItem> {
    List<FieldItem> getCompareRela(String fieldType);
}
