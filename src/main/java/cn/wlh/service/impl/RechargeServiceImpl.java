package cn.wlh.service.impl;


import cn.wlh.mapper.RechargeMapper;
import cn.wlh.model.RechargeRecord;
import cn.wlh.service.IRechargeService;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service(value = "rechargeService")
public class RechargeServiceImpl extends ServiceImpl<RechargeMapper, RechargeRecord> implements IRechargeService {

    @Autowired
    private RechargeMapper rechargeMapper;

    @Override
    public Page getRechargeListById(Page<RechargeRecord> page, RechargeRecord rechargeRecord, Integer accountId) {

        Wrapper<RechargeRecord> wrapper = new EntityWrapper<>();

        wrapper.eq("account_id", accountId);

        return this.selectPage(page, wrapper);
    }

    @Override
    public List<HashMap<String, Object>> getRechargeSum(String beginDate, String nowDate) {
        return rechargeMapper.getRechargeSum(beginDate, nowDate);
    }

    @Override
    public List<HashMap<String, Object>> getRechargeCountByRange(String dateRange, String companyName) {

        String[] split = dateRange.split("~");
        String beginTime = split[0];
        String endTime = split[1];
        String[] split1 = endTime.split("-");
        Integer day = Integer.parseInt(split1[2])+1;
        endTime = split1[0] + "-"+ split1[1] +"-" + day.toString();
        List<HashMap<String, Object>> rechargeMaps = rechargeMapper.selectGroupByDay(beginTime, endTime,  companyName);
        return rechargeMaps;
    }
}
