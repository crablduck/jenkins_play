package cn.wlh.controller.order;

import cn.wlh.model.Person;
import cn.wlh.model.SystemCompany;
import cn.wlh.model.SystemUser;
import cn.wlh.service.IOrderService;
import cn.wlh.service.SystemCompanyService;
import cn.wlh.service.SystemUserService;
import cn.wlh.util.DateUtil;
import cn.wlh.util.StringUtil;
import cn.wlh.vo.SimpleMsgVO;
import cn.wlh.vo.TableMsgVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    SystemUserService systemUserServiceImpl;

    @Autowired
    SystemCompanyService systemCompanyServiceImpl;

    /*根据角色等级获取订单*/
    @RequestMapping("/getOrderList/{roleLevel}")
    public Object getOrderList(Page<Person> page, Person person
            , @PathVariable("roleLevel") Integer roleLevel) {
        try {
            if (roleLevel == 1) {
                page = orderService.getOrderList(page, person);

            } else if (roleLevel == 2){
                page = orderService.getOrderListByUserCompanyId(page, person);
            } else if (roleLevel == 3) {
                page = orderService.getOrderListByUserId(page, person);
            }
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return TableMsgVO.getOk(page.getTotal(), page.getRecords());
    }

    @RequestMapping("/getOrderById/{orderId}")
    public Object getOrderById(@PathVariable("orderId") Integer orderId) {
        Person person = null;
        try {
            person = orderService.getOrderById(orderId);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return new TableMsgVO("0", 1, person);
    }

    @RequestMapping("/getOrderByStatus/{orderStatus}")
    public Object getOrderByStatus(@PathVariable("orderStatus") Integer orderStatus) {
        List<Person> person = null;
//        try {
        person = orderService.getListOrderByStatus(orderStatus);
//        } catch (Exception e) {
//            logger.info("服务器调用失败");
//            return SimpleMsgVO.get500Fail();
//        }
        logger.info("调用成功");
        return new TableMsgVO("0", 1, person);
    }


    @RequestMapping("getDescription/{order_id}")
    public Object getOrderDetail(@PathVariable("order_id") Integer order_id) {

        List<String> descriptionById = orderService.getDescriptionById(order_id);

        return SimpleMsgVO.getOk(descriptionById);
    }

    /**
     * @param dateType： * type == 1 ： 7days
     *                  * type == 2 ： 7days
     *                  * type == 3 ： 30days
     *                  * type == 4 ： 90days
     * @return
     */
    @RequestMapping("getOrderCountByType/{dateType}")
    public Object getOrderCountByType(@PathVariable("dateType") Integer dateType) {

        Integer dateNum = switchDateByType(dateType);
        //获取当前的时间
        String beginDate = DateUtil.getDateString(dateNum);
        String nowDate = DateUtil.getDateString(0);

        List<HashMap<String, Object>> orderCountByType = orderService.getOrderCountByType(beginDate, nowDate);
        return SimpleMsgVO.getOk(orderCountByType);
    }

    @RequestMapping("getOrderCountByRange")
    public Object getOrderCountByRange( Person person) {


        String dateRange = person.getRangeTime();

        Integer user_id = person.getUser_id();
        SystemUser userByUserId = systemUserServiceImpl.getUserByUserId(user_id);
        Integer companyId = userByUserId.getCompanyId();
        SystemCompany companyById = systemCompanyServiceImpl.getCompanyById(companyId);
        String companyName = companyById.getCompanyName();

        List<HashMap<String, Object>> orderCountByType = orderService.getOrderCountByRange(dateRange, companyName);
        return SimpleMsgVO.getOk(orderCountByType);
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
