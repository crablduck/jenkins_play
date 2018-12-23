package cn.wlh.service.impl;

import cn.wlh.mapper.StrategyManageMapper;
import cn.wlh.model.Person;
import cn.wlh.model.StrategyManage;
import cn.wlh.service.IStrategyManageService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyManageServiceImpl extends ServiceImpl<StrategyManageMapper, StrategyManage> implements IStrategyManageService {
    @Override
    public Page getStrategyManages(Page<StrategyManage> page, StrategyManage strategyManage) {

        String strategy = strategyManage.getStrategy();
        Integer status = strategyManage.getStatus();
        Integer userId = strategyManage.getUserId();
        Wrapper<StrategyManage> wrapper = new EntityWrapper<>();
        if(null != status){
            wrapper.eq("status", status);
        }
        if(null != userId){
            wrapper.eq("user_id", userId);
        }
        if(null != strategy && !"".equalsIgnoreCase(strategy)){
            wrapper.like("strategy", strategy);
        }
        return this.selectPage(page, wrapper);
    }

    @Override
    public boolean addStrategyManage(StrategyManage strategyManage) {
        Integer strategyManageId = strategyManage.getStrategyManageId();
        if (strategyManageId == null) {
            strategyManage.setRuleNum(0);
            strategyManage.setStrategyNum(0);
        }
        return this.insertOrUpdate(strategyManage);
    }

    @Override
    public StrategyManage getStrategyById(Integer strategyManageId) {
        return this.selectById(strategyManageId);
    }

    @Override
    public boolean deleteStrategyManage(Integer strategyManageId) {
        return this.deleteById(strategyManageId);
    }

    @Override
    public boolean updateStrategyMStatus(StrategyManage strategyManage) {
        return this.updateById(strategyManage);
    }

    @Override
    public List<StrategyManage> getByAppNameStatus(String app_name, Integer status) {
        EntityWrapper<StrategyManage> personEntityWrapper = new EntityWrapper<>();
        personEntityWrapper.eq("status", 1);
        personEntityWrapper.eq("app_name", app_name);
        return this.selectList(personEntityWrapper);
    }

}
