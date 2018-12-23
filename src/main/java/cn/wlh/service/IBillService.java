package cn.wlh.service;

import cn.wlh.model.Bill;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.HashMap;
import java.util.List;

public interface IBillService extends IService<Bill> {

    Page getBills(Page<Bill> page, Bill field);

    Bill getBillById(Integer billId);

    List<HashMap<String, Object>> getBillCountByType(String beginDate, String nowDate);

    Page getBillByCompanyId(Page<Bill> page, Integer companyId);

    boolean rechargeAndUpdate(Bill bill);

    List<HashMap<String, Object>> getBillCountByRange(String dateRange, String companyName);
}
