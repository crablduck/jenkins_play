package cn.wlh.controller.order;

import cn.wlh.model.Field;
import cn.wlh.model.FieldItem;
import cn.wlh.model.Strategy;
import cn.wlh.service.IFieldItemService;
import cn.wlh.service.IFieldService;
import cn.wlh.vo.SimpleMsgVO;
import cn.wlh.vo.TableMsgVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("field")
public class FieldController extends BaseController{

    @Autowired
    private IFieldService fieldService;

    @Autowired
    private IFieldItemService fieldItemService;

    @RequestMapping("/getFieldList")
    public Object getFieldList(Page<Field> page, Field field) {
        try {
            page  = fieldService.getField(page, field);
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return TableMsgVO.getOk(page.getTotal(),page.getRecords());
    }

    @RequestMapping("/getCompareRela")
    public Object getCompareRela(@RequestParam("fieldName") String fieldName){

        String fieldType = fieldService.getFieldType(fieldName);
        List<FieldItem> compareRelaList = fieldItemService.getCompareRela(fieldType);
        return SimpleMsgVO.getOk(compareRelaList);
    }

    @RequestMapping("/getFieldAllList")
    public Object getFieldAllList() {
        List<Field> fieldList = null;
        try {
            fieldList  = fieldService.getField();
        } catch (Exception e) {
            logger.info("服务器调用失败");
            return SimpleMsgVO.get500Fail();
        }
        logger.info("调用成功");
        return SimpleMsgVO.getOk(fieldList);
    }

}
