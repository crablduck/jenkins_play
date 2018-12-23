package cn.wlh.service.impl;

import cn.wlh.mapper.SystemDictMapper;
import cn.wlh.model.SystemDict;
import cn.wlh.service.SystemDictService;
import cn.wlh.vo.TableMsgVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class SystemDictServiceImpl extends ServiceImpl<SystemDictMapper,SystemDict> implements SystemDictService {

    @Resource
    SystemDictMapper systemDictMapper;

    @Override
    public Object getDictList(Page<SystemDict> page, SystemDict systemDict) {
//        String code = systemDict.getCode().trim();
//        String name = systemDict.getName().trim();
        Integer state = systemDict.getState();
        Wrapper<SystemDict> wrapper = new EntityWrapper<>();
        if(null != state){
            wrapper.eq("state",state);
        }
//        if(null != code && !"".equalsIgnoreCase(code)){
//            wrapper.andNew();
//            wrapper.like("code",code);
//        }
//        if(null != name && !"".equalsIgnoreCase(name)){
//            wrapper.or();
//            wrapper.like("name",name);
//        }
        page = this.selectPage(page,wrapper);
        return TableMsgVO.getOk(page.getTotal(),page.getRecords());
    }

    @Override
    public Object updateState(Integer id, String state) {
        return systemDictMapper.updateState(id,state);
    }
}
