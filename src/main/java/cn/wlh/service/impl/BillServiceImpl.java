package cn.wlh.service.impl;

import cn.wlh.mapper.BillMapper;
import cn.wlh.model.Bill;
import cn.wlh.model.RechargeRecord;
import cn.wlh.service.IBillService;
import cn.wlh.service.IRechargeService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service(value = "billService")
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private IRechargeService rechargeService;

    @Override
    public Page getBills(Page<Bill> page, Bill bill) {

        Integer company_id = bill.getCompany_id();
        String account = bill.getAccount();

        Wrapper<Bill> wrapper = new EntityWrapper<>();
        if(null != company_id){
            wrapper.eq("company_id", company_id);
        }
        if(null != account && !"".equalsIgnoreCase(account)){
            wrapper.andNew().like("account", account)
                    .or().like("phone_number", account);
        }

        return this.selectPage(page, wrapper);
    }

    @Override
    public Bill getBillById(Integer billId) {
        return this.selectById(billId);
    }

    @Override
    public List<HashMap<String, Object>> getBillCountByType(String beginDate, String nowDate) {
        return billMapper.getBillCountByType(beginDate, nowDate);
    }

    @Override
    public Page getBillByCompanyId(Page<Bill> page, Integer companyId) {
        EntityWrapper<Bill> billEntityWrapper = new EntityWrapper<>();
        billEntityWrapper.eq("company_id", companyId);
        return this.selectPage(page, billEntityWrapper);
    }

    @Override
    public boolean rechargeAndUpdate(Bill bill) {
        Float rechargeNum = bill.getRechargeNum();

        //更改账单剩余金额
        Float preAmount = bill.getBalance();
        Float afterAmount = preAmount + rechargeNum;
        bill.setBalance(afterAmount);


        //添加到充值账单里面去
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setCurrentAmount(preAmount);
        rechargeRecord.setAfterAmount(afterAmount);
        rechargeRecord.setAccountId(bill.getBillId());
        Date date = new Date();
        Timestamp timeStamp = new Timestamp(date.getTime());
        rechargeRecord.setAddTime(timeStamp);

        boolean insert = rechargeService.insert(rechargeRecord);
        insert = this.updateById(bill);

        return insert;
    }


    @Override
    public List<HashMap<String, Object>> getBillCountByRange(String dateRange, String companyName) {

        String[] split = dateRange.split("~");
        String beginTime = split[0];
        String endTime = split[1];
        String[] split1 = endTime.split("-");
        Integer day = Integer.parseInt(split1[2])+1;
        endTime = split1[0] + split1[1] + day.toString();

        List<HashMap<String, Object>> hashMaps = billMapper.selectGroupByDay(beginTime, endTime,  companyName);
        return hashMaps;
    }


}
