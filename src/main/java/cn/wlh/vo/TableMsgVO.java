package cn.wlh.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * layui table返回封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMsgVO implements Serializable {

    /**
     * 状态码
     */
    private String code;

    /**
     * 总条数
     */
    private Integer count;

    /**
     * 数据
     */
    private Object data;

    public static TableMsgVO getOk(Integer count, Object data){
        return new TableMsgVO("0",count,data);
    }

}
