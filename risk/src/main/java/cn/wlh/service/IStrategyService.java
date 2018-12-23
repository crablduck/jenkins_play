package cn.wlh.service;

import cn.wlh.model.Strategy;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface IStrategyService extends IService<Strategy> {
    Page getStrategies(Page<Strategy> page, Strategy strategy);

    Strategy getStrateById(Integer strateId);

    boolean deleteStrategy(Integer strategyId);

    boolean updateStrategyStatus(Strategy strategy);

    boolean addOrUpdateStrategy(Strategy strategy);

    List<Strategy> getStrateByIdStatus(Integer integer, int i);
}
