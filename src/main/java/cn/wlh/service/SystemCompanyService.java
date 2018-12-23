package cn.wlh.service;

import cn.wlh.model.SystemCompany;
import com.baomidou.mybatisplus.service.IService;

public interface SystemCompanyService extends IService<SystemCompany> {

    /**
     * 根据id获取商品信息
     * @param id
     * @return
     */
    SystemCompany getCompanyById(Integer id);

    /**
     * 根据公司秘钥检查该公司是否存在
     * @param company_secret
     * @return
     */
    SystemCompany checkCompanyBySecret(String company_secret);

    boolean addBaseAppId(SystemCompany systemCompany);
}
