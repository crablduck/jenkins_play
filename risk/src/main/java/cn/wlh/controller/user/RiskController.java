package cn.wlh.controller.user;

import cn.wlh.model.SystemCompany;
import cn.wlh.service.CommodityService;
import cn.wlh.service.SystemCompanyService;
import cn.wlh.vo.SimpleMsgVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("risk")
public class RiskController {

    @Resource
    SystemCompanyService systemCompanyServiceImpl;

    @Resource
    CommodityService commodityServiceImpl;

    /**
     * 检查公司三要素
     * @return
     */
    @RequestMapping("check3Ele")
    public Object check3Ele(@RequestBody String companyInfo){
        JSONObject data= new JSONObject();
        JSONObject companyInfoJson = null;
        //判断json格式
        try{
            companyInfoJson = JSON.parseObject(companyInfo);
        }catch(Exception e){
            return SimpleMsgVO.get407Fail();
        }
        //非空校验
        String validateMsg = validate(companyInfoJson);
        if(null != validateMsg){
            return SimpleMsgVO.get406Fail(validateMsg);
        }
        //判断该公司是否存在
        SystemCompany systemCompany = systemCompanyServiceImpl.checkCompanyBySecret(companyInfoJson.getString("company_secret"));
        if(null == systemCompany){
            return SimpleMsgVO.get408Fail("company secret is error");
        }
        //判断该公司下是否有该商品
        if(!commodityServiceImpl.checkCommodity(companyInfoJson)){
            return SimpleMsgVO.get408Fail("app code or name is error");
        }
        data.put("company_id",systemCompany.getCompanyId());
        return  SimpleMsgVO.getOk(data);
    }

    /**
     * 非空校验
     * @param json
     * @return
     */
    private String validate(JSONObject json) {
        if(StringUtils.isEmpty(json.getString("company_secret"))){
            return "company_secret can not be empty";
        }else if(StringUtils.isEmpty(json.getString("app_code"))){
            return "app_code can not be empty";
        }else if(StringUtils.isEmpty(json.getString("app_name"))){
            return "app_name can not be empty";
        }else{
            return null;
        }
    }

}
