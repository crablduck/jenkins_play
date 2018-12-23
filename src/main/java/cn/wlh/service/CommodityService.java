package cn.wlh.service;

import cn.wlh.model.Commodity;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.Date;

public interface CommodityService extends IService<Commodity> {

    /**
     * 获取所有商品数据
     * @param page
     * @param commodity
     * @return
     */
    Object getCommodityList(Page<Commodity> page, Commodity commodity);

    /**
     * 添加或编辑商品
     * @param commodity
     * @return
     */
    Object addOrUpdateApp(Commodity commodity);

    /**
     * 添加或编辑商品状态
     * @param commodity
     * @return
     */
    Object updateState(Commodity commodity);

    /**
     * 通过公司id获取对于的商品数据
     * @param companyId
     * @return
     */
    Object getCommodityByCompanyId(Integer companyId);

    /**
     * 通过参数判断该商品是否存在
     * @param companyInfoJson
     * @return
     */
    boolean checkCommodity(JSONObject companyInfoJson);

    Commodity getAppNameByUserId(Integer userId);

    int insertAndSelectKey(Commodity commodity);

    String getBaseAppName(String app_name);
}
