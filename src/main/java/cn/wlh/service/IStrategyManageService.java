package cn.wlh.service;

import cn.wlh.model.StrategyManage;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface IStrategyManageService extends IService<StrategyManage> {
    Page getStrategyManages(Page<StrategyManage> page, StrategyManage strategyManage);

    boolean addStrategyManage(StrategyManage strategyManage);

    StrategyManage getStrategyById(Integer strategyManageId);

    boolean deleteStrategyManage(Integer strategyManageId);

    boolean updateStrategyMStatus(StrategyManage strategyManage);

    List<StrategyManage> getByAppNameStatus(String app_name, Integer status);
}
