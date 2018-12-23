package cn.wlh.controller.user;

import cn.wlh.model.Commodity;
import cn.wlh.service.CommodityService;
import cn.wlh.vo.SimpleMsgVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("app")
public class CommodityController {

    @Resource
    CommodityService commodityServiceImpl;

    /**
     * 获取所有商品数据
     * @param page
     * @param commodity
     * @return
     */
    @RequestMapping("getCommodityList")
    public Object getCommodityList(Page<Commodity> page, Commodity commodity){
        try{
            return commodityServiceImpl.getCommodityList(page,commodity);
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 通过公司id获取对于的商品数据
     * @param companyId
     * @return
     */
    @RequestMapping("getCommodityById")
    public Object getCommodityById(Integer companyId){
        try{
            return SimpleMsgVO.getOk(commodityServiceImpl.getCommodityByCompanyId(companyId));
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 添加或编辑商品
     * @param commodity
     * @return
     */
    @RequestMapping("addOrUpdateApp")
    public Object addOrUpdateApp(Commodity commodity){
        try{
            return SimpleMsgVO.getOk(commodityServiceImpl.addOrUpdateApp(commodity));
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 添加或编辑商品状态
     * @param commodity
     * @return
     */
    @RequestMapping("updateState")
    public Object updateState(Commodity commodity){
        try{
            return SimpleMsgVO.getOk(commodityServiceImpl.updateState(commodity));
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 根据用户id获取应用名称
     */
    @RequestMapping("getAppNameByUserId/{userId}")
    public Object getAppByUserId(@PathVariable("userId") Integer userId) {

        Commodity appNameByUserId = commodityServiceImpl.getAppNameByUserId(userId);
        return SimpleMsgVO.getOk(appNameByUserId);

    }

}
