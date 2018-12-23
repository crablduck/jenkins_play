package cn.wlh.service.impl;

import cn.wlh.mapper.OrderMapper;
import cn.wlh.model.Commodity;
import cn.wlh.model.Person;
import cn.wlh.model.SystemUser;
import cn.wlh.service.CommodityService;
import cn.wlh.service.IOrderService;
import cn.wlh.service.SystemUserService;
import cn.wlh.util.StringUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Person> implements IOrderService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private CommodityService commodityService;

    @Override
    public Page<Person> getOrderList(Page<Person> page, Person person) {

        String userRealName = person.getUser_real_name();

        Wrapper<Person> wrapper = new EntityWrapper<>();


        if (null != userRealName && !"".equalsIgnoreCase(userRealName)) {

            wrapper.andNew().like("user_real_name", userRealName)
                    .or().like("phone", userRealName)
                    .or().like("order_no", userRealName);
        }
        wrapper.orderDesc(Collections.singleton("add_time"));
        return this.selectPage(page, wrapper);
    }

    @Override
    public Person getOrderById(Integer orderId) {

        return this.selectById(orderId);
    }

    @Override
    public boolean addOrder(Person person) {
        return this.insert(person);
    }

    @Override
    public List<Person> getListOrderByStatus(Integer status) {

//        return orderMapper.getListOrderByStatus(1);

        ArrayList<Person> people = new ArrayList<>();
        Person person = new Person();
        Person person1 = new Person();
        person1.setIdcard("431");


        Person person2 = new Person();
        person.setIdcard("545234234");
        person.setPhone("1235");
        person.setUser_id(23);
        person2.setIdcard("545234234");

        people.add(person);
        people.add(person1);
        people.add(person2);
        return people;
    }

    @Override
    public List<Person> getAllOrderList() {
        EntityWrapper<Person> personEntityWrapper = new EntityWrapper<>();
        return this.selectList(personEntityWrapper);
    }

    @Override
    public Page<Person> getOrderListByUserId(Page<Person> page, Person person) {
        String userRealName = person.getUser_real_name();
        Integer user_id = person.getUser_id();
        SystemUser systemUser = systemUserService.selectById(user_id);
        Integer commodityId = systemUser.getCommodityId();
        Commodity commodity = commodityService.selectById(commodityId);
        String commodityName = commodity.getCommodityName();


        Wrapper<Person> wrapper = new EntityWrapper<>();
        wrapper.eq("app_name", commodityName);

        if (null != userRealName && !"".equalsIgnoreCase(userRealName)) {

            wrapper.andNew().like("user_real_name", userRealName)
                    .or().like("phone", userRealName)
                    .or().like("order_no", userRealName);
        }



        if (person.getRangeTime() != null && !"".equals(person.getRangeTime())) {
            String rangeTime = person.getRangeTime();
            String[] split = rangeTime.split("-");

            String beginDate = StringUtil.getTimeString(split, 0, 3);
            String endDate = StringUtil.getTimeString(split, 3 ,6);
            wrapper.between("add_time", beginDate, endDate);
        }
        wrapper.orderDesc(Collections.singleton("add_time"));
        return this.selectPage(page, wrapper);
    }

    @Override
    public HashMap<String, Object> getOrderByOid(Person person) {

        //得到描述字符长串
        EntityWrapper<Person> personEntityWrapper = new EntityWrapper<>();
        personEntityWrapper.eq("apply_oid", person.getApply_oid());
        Person result = this.selectOne(personEntityWrapper);
        String description_array = result.getDescription_array();
        List<String> descriptionList = new ArrayList<String>();
        if (description_array != null) {

            descriptionList = getDescriptionList(description_array);
        }
        Integer order_status = result.getOrder_status();
        Integer totalScore = result.getTotalScore();

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("descriptionList", descriptionList);
        stringObjectHashMap.put("order_status", order_status);
        stringObjectHashMap.put("total_score", totalScore);
        return stringObjectHashMap;
    }

    @Override
    public List<String> getDescriptionById(Integer order_id) {
        Person person = this.selectById(order_id);
        Integer order_status = person.getOrder_status();


        List<String> strings = null;
        if (order_status == 1) {
            strings.add("该用户完全符合要求");
        } else {
            String[] split = person.getDescription_array().split(",");
            strings = Arrays.asList(split);
        }

        return strings;
    }

    @Override
    public List<HashMap<String, Object>> getOrderCountByType(String beginDate, String nowDate) {


        return  orderMapper.getOrderCountByType(beginDate, nowDate);
    }

    @Override
    public List<HashMap<String, Object>> getOrderCountByRange(String dateRange, String companyName) {

        String[] split = dateRange.split("~");
        String beginTime = split[0];
        String endTime = split[1];
        String[] split1 = endTime.split("-");
        Integer day = Integer.parseInt(split1[2])+1;
        endTime = split1[0] + "-"+ split1[1] +"-" + day.toString();

//        List<String> rangeDatePoint = StringUtil.getRangeDatePoint(dateRange);

//        ArrayList<HashMap<String, Object>> orderCountByDate = new ArrayList<>();
//        for (int i = 0; i < rangeDatePoint.size()-1; i++) {
//            HashMap<String, Object> point = new HashMap<>();
//            EntityWrapper<Person> personEntityWrapper = new EntityWrapper<>();
//            String beginDate = rangeDatePoint.get(i);
//            String endDate = rangeDatePoint.get(i + 1);
//
//
//            HashMap<String, Object> orderByPoint = orderMapper.getOrderByPoint(beginDate, endDate, companyName);
//
//            orderByPoint.put("order_time", beginDate);
//
//            orderCountByDate.add(orderByPoint);
//        }

        List<HashMap<String, Object>> hashMaps = orderMapper.selectGroupByDay(beginTime, endTime, 1);

        return hashMaps;

    }

    @Override
    public List<Person> getOrderByOverDay(Integer overDay) {
        EntityWrapper<Person> personEntityWrapper = new EntityWrapper<>();
        personEntityWrapper.gt("over_day", overDay);
        return this.selectList(personEntityWrapper);
    }

    @Override
    public Page<Person> getOrderListByUserCompanyId(Page<Person> page, Person person) {

        String userRealName = person.getUser_real_name();
        //根据companyid得到应用名字根据应用名字然后去查找订单
        Integer user_id = person.getUser_id();
        SystemUser systemUser = systemUserService.selectById(user_id);
        Integer companyId = systemUser.getCompanyId();

        EntityWrapper<Commodity> commodityWrapper = new EntityWrapper<>();
        commodityWrapper.eq("company_id", companyId);
        List<Commodity> commodities = commodityService.selectList(commodityWrapper);



        List<Person> orders = new ArrayList<>();


            Wrapper<Person> wrapper = new EntityWrapper<>();


            if (null != userRealName && !"".equalsIgnoreCase(userRealName)) {

                wrapper.andNew().like("user_real_name", userRealName)
                        .or().like("phone", userRealName)
                        .or().like("order_no", userRealName);
            }



            if (person.getRangeTime() != null && !"".equals(person.getRangeTime())) {
                String rangeTime = person.getRangeTime();
                String[] split = rangeTime.split("-");

                String beginDate = StringUtil.getTimeString(split, 0, 3);
                String endDate = StringUtil.getTimeString(split, 3 ,6);
                wrapper.between("add_time", beginDate, endDate);
            }
        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < commodities.size(); i++) {

            strings.add(commodities.get(i).getCommodityName());

        }
        wrapper.in("app_name", strings);

        wrapper.orderDesc(Collections.singleton("add_time"));
        return this.selectPage(page, wrapper);

    }

    private List<String> getDescriptionList(String Descriptions){

        String[] split = Descriptions.split(",");
        List<String> strings = Arrays.asList(split);

        return strings;
    }


}
