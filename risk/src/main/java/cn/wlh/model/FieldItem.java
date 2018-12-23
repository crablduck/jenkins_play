package cn.wlh.model;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

@Data
@TableName("field_item")
public class FieldItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("compare_rela")
    private String compareRela;

    @TableField("field_type")
    private String fieldType;
}
