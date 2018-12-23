package cn.wlh.service;

import cn.wlh.model.SystemDict;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

public interface SystemDictService extends IService<SystemDict> {

    /**
     * 获取所有字典数据
     * @param page
     * @param systemDict
     * @return
     */
    Object getDictList(Page<SystemDict> page, SystemDict systemDict);

    /**
     * 修改字典数据状态
     * @param id
     * @param state
     * @return
     */
    Object updateState(Integer id, String state);
}
