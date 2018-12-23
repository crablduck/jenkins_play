package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

@Data
@TableName("field_manage")
public class Field {

    @TableId(value = "field_id",type = IdType.AUTO)
    public Integer fieldId;

    @TableField("field_name")
    public String fieldName;

    @TableField("display_name")
    public String displayName;

    @TableField("field_type")
    public String fieldType;

    public String description;

}
