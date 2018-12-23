package cn.wlh.service.impl;

import cn.wlh.mapper.CommodityMapper;
import cn.wlh.model.Commodity;
import cn.wlh.model.SystemCompany;
import cn.wlh.model.SystemUser;
import cn.wlh.service.CommodityService;
import cn.wlh.service.SystemCompanyService;
import cn.wlh.util.SessionUtil;
import cn.wlh.vo.TableMsgVO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    @Resource
    SessionUtil sessionUtil;

    @Resource
    SystemCompanyService companyServiceImpl;

    @Autowired
    private CommodityMapper commodityMapper;

    @Autowired
    private SystemCompanyService systemCompanyService;
    /**
     * 获取所有商品数据
     *
     * @param page
     * @param commodity
     * @return
     */
    @Override
    public Object getCommodityList(Page<Commodity> page, Commodity commodity) {
        Integer state = commodity.getState();
        String commodityName = null;
        String commodityNumber = null;
        if (commodity.getCommodityName() != null) {
            commodityNumber = commodity.getCommodityNumber().trim();
        }
        if (commodity.getCommodityNumber() != null) {
            commodityName = commodity.getCommodityName().trim();
        }
        SystemUser systemUser = sessionUtil.getUserInfo();
        if (null != systemUser) {
            Wrapper<Commodity> wrapper = new EntityWrapper<>();
            if(null != systemUser.getCompanyId() && !"".equals(systemUser.getCompanyId())){
                wrapper.eq("company_id", systemUser.getCompanyId());
            }
            if (null != state) {
                wrapper.eq("state", state);
            }
            if (null != commodityNumber && !"".equalsIgnoreCase(commodityNumber)) {
                wrapper.andNew();
                wrapper.like("commodity_number", commodityNumber);
            }
            if (null != commodityName && !"".equalsIgnoreCase(commodityName)) {
                wrapper.or();
                wrapper.like("commodity_name", commodityName);
            }
            if (systemUser.getUserId() == 1) {
                page = this.selectPage(page, wrapper);
            } else {
                page = this.selectPage(page, wrapper);
            }
        }
        return TableMsgVO.getOk(page.getTotal(), page.getRecords());
    }

    /**
     * 添加或编辑商品
     *
     * @param commodity
     * @return
     */
    @Override
    public Object addOrUpdateApp(Commodity commodity) {
        SystemUser systemUser = sessionUtil.getUserInfo();
        if (null != systemUser) {
            if (null == commodity.getId()) {//添加
                SystemCompany systemCompany = companyServiceImpl.getCompanyById(systemUser.getCompanyId());
                if (null != systemCompany) {
                    commodity.setCommoditySecret(systemCompany.getCompanySecret());
                    commodity.setCompanyId(systemCompany.getCompanyId());
                    return this.insert(commodity);
                }
            } else {//修改
                return this.updateById(commodity);
            }

        }
        return null;
    }

    /**
     * 添加或编辑商品状态
     *
     * @param commodity
     * @return
     */
    @Override
    public Object updateState(Commodity commodity) {
        return this.updateById(commodity);
    }

    /**
     * 通过公司id获取对于的商品数据
     *
     * @param companyId
     * @return
     */
    @Override
    public Object getCommodityByCompanyId(Integer companyId) {
        Wrapper<Commodity> wrapper = new EntityWrapper<>();
        wrapper.eq("company_id", companyId);
        return this.selectList(wrapper);
    }

    /**
     * 通过参数判断该商品是否存在
     *
     * @param companyInfoJson
     * @return
     */
    @Override
    public boolean checkCommodity(JSONObject companyInfoJson) {
        Wrapper<Commodity> wrapper = new EntityWrapper<>();
        wrapper.eq("commodity_number", companyInfoJson.getString("app_code"));
        wrapper.eq("commodity_name", companyInfoJson.getString("app_name"));
        wrapper.eq("commodity_secret", companyInfoJson.getString("company_secret"));
        Commodity commodity = this.selectOne(wrapper);
        if (null == commodity) {
            return false;
        }
        return true;
    }

    @Override
    public Commodity getAppNameByUserId(Integer userId) {

        return this.selectById(userId);
    }

    @Override
    public int insertAndSelectKey(Commodity commodity) {
        return commodityMapper.insertBackKeyId(commodity);
    }

    @Override
    public String getBaseAppName(String app_name) {
        EntityWrapper<Commodity> commodityEntityWrapper = new EntityWrapper<>();
        commodityEntityWrapper.eq("commodity_name", app_name);

        Commodity commodity = this.selectOne(commodityEntityWrapper);
        Integer companyId = commodity.getCompanyId();
        SystemCompany systemCompany = systemCompanyService.selectById(companyId);
        Integer baseAppId = systemCompany.getBaseAppId();


        Commodity commodity1 = this.selectById(baseAppId);
        return commodity1.getCommodityName();

    }
}
