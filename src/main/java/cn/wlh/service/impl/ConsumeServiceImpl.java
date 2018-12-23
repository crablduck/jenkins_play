package cn.wlh.service.impl;


import cn.wlh.mapper.ConsumeMapper;
import cn.wlh.model.ConsumeRecord;
import cn.wlh.service.IConsumeService;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service(value = "consumeService")
public class ConsumeServiceImpl extends ServiceImpl<ConsumeMapper, ConsumeRecord> implements IConsumeService {

    @Autowired
    private ConsumeMapper consumeMapper;

    @Override
    public Page getConsumeListById(Page<ConsumeRecord> page, ConsumeRecord consumeRecord, Integer accountId) {
        Wrapper<ConsumeRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("account_id", accountId);
        return this.selectPage(page, wrapper);
    }

    @Override
    public List<HashMap<String, Object>> getConsumeSum(String beginDate, String nowDate) {
        return consumeMapper.getConsumeSum(beginDate, nowDate);
    }

    @Override
    public List<HashMap<String, Object>> getConsumeCountByRange(String dateRange, String companyName) {

        String[] split = dateRange.split("~");
        String beginTime = split[0];
        String endTime = split[1];
        String[] split1 = endTime.split("-");
        Integer day = Integer.parseInt(split1[2])+1;
        endTime = split1[0] + "-"+ split1[1] +"-" + day.toString();

        List<HashMap<String, Object>> consumeMaps = consumeMapper.selectGroupByDay(beginTime, endTime,  companyName);
        return consumeMaps;
    }

}
