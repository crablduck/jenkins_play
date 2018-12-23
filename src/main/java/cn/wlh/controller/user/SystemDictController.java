package cn.wlh.controller.user;

import cn.wlh.model.SystemDict;
import cn.wlh.service.SystemDictService;
import cn.wlh.vo.SimpleMsgVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("dict")
public class SystemDictController {

    @Resource
    SystemDictService systemDictServiceImpl;

    /**
     *  获取所有字典数据
     */
    @RequestMapping("getDictList")
    public Object getDictList(Page<SystemDict> page,SystemDict systemDict){
        return systemDictServiceImpl.getDictList(page,systemDict);
    }

    /**
     * 添加或编辑字典数据
     * @param systemDict
     * @return
     */
    @RequestMapping("addOrUpdateDict")
    public Object addOrUpdateDict(SystemDict systemDict){
        if(null == systemDict.getId()){//添加字典
            systemDictServiceImpl.insert(systemDict);
        }else{//编辑字典
            systemDictServiceImpl.updateById(systemDict);
        }
        return SimpleMsgVO.getOk();
    }

    /**
     * 编辑字典状态
     * @param id
     * @param state
     * @return
     */
    @RequestMapping("updateState/{id}")
    public Object updateState(@PathVariable("id") Integer id,String state){
        try{
            return SimpleMsgVO.getOk(systemDictServiceImpl.updateState(id,state));
        }catch (Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

    /**
     * 根据id删除字典数据
     * @param id
     * @return
     */
    @RequestMapping("delDict/{id}")
    public Object delDict(@PathVariable("id") Integer id){
        try{
            return SimpleMsgVO.getOk(systemDictServiceImpl.deleteById(id));
        }catch(Exception e){
            return SimpleMsgVO.get500Fail();
        }
    }

}
