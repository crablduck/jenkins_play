package cn.wlh.controller.order;

import cn.wlh.model.*;
import cn.wlh.service.*;
import cn.wlh.util.DateUtil;
import cn.wlh.vo.SimpleMsgVO;
import cn.wlh.vo.TableMsgVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("bill")
public class BillController extends BaseController{

    @Autowired
    private IBillService billService;

    @Autowired
    private IRechargeService rechargeService;

    @Autowired
    private IConsumeService consumeService;

    @Autowired
    private SystemUserService userService;

    @Autowired
    private SystemCompanyService companyService;

    /**
     * 账单管理列表
     * @param page
     * @param bill
     * @return
     */
    @RequestMapping("/getBillList/{roleLeve}")
    public Object getBillList(Page<Bill> page, Bill bill,
                    @PathVariable("roleLeve") Integer roleLeve) {
        Page bills = null;
        Integer userId = bill.getUserId();
        SystemUser systemUser = userService.selectById(userId);
        Integer companyId = systemUser.getCompanyId();
        try {
            if (roleLeve == 1) {

                bills = billService.getBills(page, bill);
            } else {
                bills = billService.getBillByCompanyId(page, companyId);
            }
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return TableMsgVO.getOk(page.getTotal(),page.getRecords());
    }

    /**
     *  获得单个账单
     * @param billId
     * @return
     */
    @RequestMapping("/getBillById/{billId}")
    public Object getBillById(@PathVariable("billId") Integer billId){
        Bill bill= null;
        try {
            bill = billService.getBillById(billId);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        return SimpleMsgVO.getOk(bill);
    }

    @RequestMapping("/getRechargeList/{billId}")
    public Object getRechargeList(Page<RechargeRecord> page, RechargeRecord rechargeRecord, @PathVariable("billId") Integer billId){


        Page<RechargeRecord> rechargeListById = null;
//        try {

         rechargeListById = rechargeService.getRechargeListById(page, rechargeRecord, billId);
//        } catch (Exception e) {
//            logger.info("服务器调用失败");
//            return SimpleMsgVO.get500Fail();
//        }
        logger.info("调用成功");
        return TableMsgVO.getOk(rechargeListById.getTotal(),rechargeListById.getRecords());
    }

    @RequestMapping("/getConsumeList/{billId}")
    public Object getConsumeList(Page<ConsumeRecord> page, ConsumeRecord consumeRecord, @PathVariable("billId") Integer billId){
        Page<ConsumeRecord> consumeListById = null;
//        try {
            consumeListById = consumeService.getConsumeListById(page, consumeRecord, billId);
//        } catch (Exception e) {
//            logger.info("服务器调用失败");
//            return SimpleMsgVO.get500Fail();
//        }
        logger.info("调用成功");
        return TableMsgVO.getOk(consumeListById.getTotal(),consumeListById.getRecords());

    }

    /*账单走势根据类型*/
    @RequestMapping("getBillCountByType/{dateType}")
    public Object getBillCountByType(@PathVariable("dateType") Integer dateType){
        Integer dateNum = switchDateByType(dateType);
        //获取当前的时间
        String beginDate = DateUtil.getDateString(dateNum);
        String nowDate = DateUtil.getDateString(0);


        List<HashMap<String, Object>> BillCounts = billService.getBillCountByType(beginDate, nowDate);


        return SimpleMsgVO.getOk(BillCounts);
    }    /*账单走势根据类型*/

    /*账单根据时间范围*/
    @RequestMapping("getBillCountByRange")
    public Object getBillCountByRange( Bill bill) {


        String dateRange = bill.getRangeTime();

        Integer user_id = bill.getUserId();
        SystemUser userByUserId = userService.getUserByUserId(user_id);
        Integer companyId = userByUserId.getCompanyId();
        SystemCompany companyById = companyService.getCompanyById(companyId);
        String companyName = companyById.getCompanyName();

        List<HashMap<String, Object>> allCountByRange = new ArrayList<>();
        List<HashMap<String, Object>>  consumeCountByRange = consumeService.getConsumeCountByRange(dateRange, companyName);
        List<HashMap<String, Object>> rechargeCountByRange = rechargeService.getRechargeCountByRange(dateRange, companyName);
        allCountByRange.addAll(consumeCountByRange);
        allCountByRange.addAll(rechargeCountByRange);
        return SimpleMsgVO.getOk(allCountByRange);

    }

    @RequestMapping("addRecharge")
    public Object addRecharge(Bill bill){




        boolean b = billService.rechargeAndUpdate(bill);


        return SimpleMsgVO.getOk(b);
    }

    private Integer switchDateByType(Integer dateType) {

        Integer dateNum = null;
        switch (dateType) {
            case 1:
                dateNum = -7;
                break;
            case 2:
                dateNum = -14;
                break;
            case 3:
                dateNum = -30;
                break;
            case 4:
                dateNum = -90;
                break;
            default:
                dateNum = 0;
                break;
        }
        return dateNum;
    }

}
