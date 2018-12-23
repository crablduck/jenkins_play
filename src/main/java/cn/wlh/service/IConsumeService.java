package cn.wlh.service;

import cn.wlh.model.ConsumeRecord;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.HashMap;
import java.util.List;

public interface IConsumeService extends IService<ConsumeRecord> {

    Page getConsumeListById(Page<ConsumeRecord> page, ConsumeRecord consumeRecord, Integer accountId);

    List<HashMap<String, Object>> getConsumeSum(String beginDate, String nowDate);

    List<HashMap<String, Object>> getConsumeCountByRange(String dateRange, String companyName);
}
