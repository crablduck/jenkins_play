package cn.wlh.controller.order;

import cn.wlh.model.Strategy;
import cn.wlh.service.IStrategyService;
import cn.wlh.vo.SimpleMsgVO;
import cn.wlh.vo.TableMsgVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rule")
public class StrategyController extends BaseController{

    @Autowired
    private IStrategyService strategyService;

    @RequestMapping("/getStrategy")
    public Object getStrategy(Page<Strategy> page, Strategy strategy) {
        try {
            page  = strategyService.getStrategies(page, strategy);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return TableMsgVO.getOk(page.getTotal(),page.getRecords());
    }

    @RequestMapping("addOrUpdateStrategy")
    public Object addOrUpdateStrategy(Strategy strategy){

        boolean addStatus = false;

        try {
            addStatus  = strategyService.addOrUpdateStrategy(strategy);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(addStatus);
    }

    @RequestMapping("/getStrateById/{strateId}")
    public Object getStrateById(@PathVariable("strateId") Integer strateId){

        Strategy strategy;
        try {
            strategy  = strategyService.getStrateById(strateId);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(strategy);
    }

    @RequestMapping("/deleteStrategy/{strategyId}")
    public Object deleteStrategy(@PathVariable("strategyId") Integer strategyId){

        boolean delStatus = false;
        try {
            delStatus  = strategyService.deleteStrategy(strategyId);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(delStatus);
    }

    @RequestMapping("updateStrategyStatus")
    public Object updateStrategyStatus(Strategy strategy){

        boolean delStatus = false;
        try {
            delStatus  = strategyService.updateStrategyStatus(strategy);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(delStatus);
    }


}
