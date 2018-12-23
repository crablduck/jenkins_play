package cn.wlh.service;

import cn.wlh.model.RechargeRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.HashMap;
import java.util.List;

public interface IRechargeService  extends IService<RechargeRecord> {

    Page getRechargeListById(Page<RechargeRecord> page, RechargeRecord rechargeRecord, Integer accountId);

    List<HashMap<String, Object>> getRechargeSum(String beginDate, String nowDate);

    List<HashMap<String, Object>> getRechargeCountByRange(String dateRange, String companyName);
}
