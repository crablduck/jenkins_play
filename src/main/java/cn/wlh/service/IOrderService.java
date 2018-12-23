package cn.wlh.service;

import cn.wlh.model.Person;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.HashMap;
import java.util.List;

public interface IOrderService extends IService<Person> {
    Page<Person> getOrderList(Page<Person> page, Person person);

    Person getOrderById(Integer orderId);

    boolean addOrder(Person person);

    List<Person> getListOrderByStatus(Integer status);

    List<Person> getAllOrderList();

    Page<Person> getOrderListByUserId(Page<Person> page, Person person);

    HashMap<String, Object> getOrderByOid(Person person);

    List<String> getDescriptionById(Integer order_id);

    List<HashMap<String, Object>> getOrderCountByType(String beginDate, String nowDate);

    List<HashMap<String, Object>> getOrderCountByRange(String dateRange, String companyName);

    List<Person> getOrderByOverDay(Integer overDay);

    Page<Person> getOrderListByUserCompanyId(Page<Person> page, Person person);
}
