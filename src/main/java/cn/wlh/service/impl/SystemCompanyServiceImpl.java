package cn.wlh.service.impl;

import cn.wlh.mapper.SystemCompanyMapper;
import cn.wlh.model.SystemCompany;
import cn.wlh.service.SystemCompanyService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SystemCompanyServiceImpl extends ServiceImpl<SystemCompanyMapper,SystemCompany> implements SystemCompanyService {


    /**
     * 根据id获取商品信息
     * @param id
     * @return
     */
    @Override
    public SystemCompany getCompanyById(Integer id) {
        return this.selectById(id);
    }

    /**
     * 根据公司秘钥检查该公司是否存在
     * @param company_secret
     * @return
     */
    @Override
    public SystemCompany checkCompanyBySecret(String company_secret) {
        Wrapper<SystemCompany> wrapper = new EntityWrapper<>();
        wrapper.eq("company_secret",company_secret);
        SystemCompany systemCompany = this.selectOne(wrapper);
        return systemCompany;
    }

    @Override
    public boolean addBaseAppId(SystemCompany systemCompany) {
        return this.updateById(systemCompany);
    }
}
