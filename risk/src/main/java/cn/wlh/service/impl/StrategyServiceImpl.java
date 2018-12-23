package cn.wlh.service.impl;

import cn.wlh.mapper.StrategyMapper;
import cn.wlh.model.Strategy;
import cn.wlh.model.StrategyManage;
import cn.wlh.service.IStrategyManageService;
import cn.wlh.service.IStrategyService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements IStrategyService {

    @Autowired
    private IStrategyManageService strategyManageService;

    @Autowired
    private StrategyMapper strategyMapper;

    @Override
    public Page getStrategies(Page<Strategy> page, Strategy strategy) {
        Wrapper<Strategy> wrapper = new EntityWrapper<>();
        Integer strategiesId = strategy.getStrategiesId();
        Integer status = strategy.getStatus();
        String strate = strategy.getStrategy();
        if(null != status){
            wrapper.eq("status", status);
        }
        if(null != strategiesId){
            wrapper.eq("strategies_id", strategiesId);
        }
        if(null != strate && !"".equalsIgnoreCase(strate)){
            wrapper.like("strategy", strate);
        }
        return this.selectPage(page, wrapper);
    }

    @Override
    public Strategy getStrateById(Integer strateId) {
        return this.selectById(strateId);
    }

    @Override
    public boolean deleteStrategy(Integer strategyId) {
        Strategy strategy = this.selectById(strategyId);
        StrategyManage strategyManage = strategyManageService.selectById(strategy.getStrategiesId());
        Integer strategyNum = strategyManage.getStrategyNum() - 1;
        strategyManage.setStrategyNum(strategyNum);
        strategyManageService.updateById(strategyManage);
        return this.deleteById(strategyId);
    }

    @Override
    public boolean updateStrategyStatus(Strategy strategy) {
        return this.updateById(strategy);
    }

    @Override
    public boolean addOrUpdateStrategy(Strategy strategy) {
        Integer strategyId = strategy.getStrategyId();
        if (strategyId == null) {
            strategy.setRuleNum(0);

            //策略增加策略集合策略数目+1
            StrategyManage strategyManage = strategyManageService.selectById(strategy.getStrategiesId());
            Integer strategyNum = strategyManage.getStrategyNum() + 1;
            strategyManage.setStrategyNum(strategyNum);
            strategyManageService.updateById(strategyManage);
        }
        return insertOrUpdate(strategy);
    }

    @Override
    public List<Strategy> getStrateByIdStatus(Integer integer, int i) {
        EntityWrapper<Strategy> strategyEntityWrapper = new EntityWrapper<>();
        strategyEntityWrapper.eq("strategies_id", integer);
        strategyEntityWrapper.eq("status", 1);
        return this.selectList(strategyEntityWrapper);
    }
}
