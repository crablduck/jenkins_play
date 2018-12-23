package cn.wlh.controller.order;

import cn.wlh.model.Commodity;
import cn.wlh.model.StrategyManage;
import cn.wlh.model.SystemUser;
import cn.wlh.service.CommodityService;
import cn.wlh.service.IStrategyManageService;
import cn.wlh.service.SystemUserService;
import cn.wlh.vo.SimpleMsgVO;
import cn.wlh.vo.TableMsgVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rule")
public class StrategyManageController extends BaseController{


    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private IStrategyManageService strategyManageService;

    @RequestMapping("/getStrategyManageList")
    public Object getStrategyManageList(Page<StrategyManage> page, StrategyManage strategyManage) {
        try {
            page  = strategyManageService.getStrategyManages(page, strategyManage);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return TableMsgVO.getOk(page.getTotal(),page.getRecords());
    }

    @RequestMapping("/addOrUpdateStrategyManage/{userId}")
    public Object addOrUpdateStrategyManage(StrategyManage strategyManage
                                    ,@PathVariable("userId") Integer userId){



        SystemUser systemUser = systemUserService.selectById(userId);
        Commodity commodity = commodityService.selectById(systemUser.getCommodityId());
        String commodityName = commodity.getCommodityName();

        strategyManage.setUserId(userId);
        strategyManage.setAppName(commodityName);
        boolean addStatus = false;
        try {
            addStatus  = strategyManageService.addStrategyManage(strategyManage);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(addStatus);
    }

    @RequestMapping("/getStrategyById/{strategyManageId}")
    public Object getStrategyById(@PathVariable("strategyManageId") Integer strategyManageId){

        StrategyManage strategyManage;
        try {
            strategyManage  = strategyManageService.getStrategyById(strategyManageId);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(strategyManage);
    }

    @RequestMapping("/deleteStrategyManage/{strategyManageId}")
    public Object deleteStrategyManage(@PathVariable("strategyManageId") Integer strategyManageId){

        boolean delStatus = false;
        try {
            delStatus  = strategyManageService.deleteStrategyManage(strategyManageId);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(delStatus);
    }

    @RequestMapping("updateStrategyMStatus")
    public Object updateStrategyMStatus(StrategyManage strategyManage){

        boolean delStatus = false;
        try {
            delStatus  = strategyManageService.updateStrategyMStatus(strategyManage);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(delStatus);
    }


}
