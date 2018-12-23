package cn.wlh.controller.user;

import cn.wlh.service.SystemCompanyService;
import cn.wlh.vo.SimpleMsgVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("company")
public class SystemCompanyController {

    @Resource
    SystemCompanyService systemCompanyServiceImpl;

    /**
     * 获取所有公司列表
     * @return
     */
    @RequestMapping("getCompanyList")
    public Object getCompanyList(){
//        try{
            return SimpleMsgVO.getOk(systemCompanyServiceImpl.selectList(null));
//        }catch (Exception e){
//            return SimpleMsgVO.get500Fail();
//        }
    }

}
