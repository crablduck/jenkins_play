package cn.wlh.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.*;

@Data
public class Person {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer order_id;

    @TableField("company_name")
    private String company_name;

    @TableField("company_secret")
    private String company_secret;//公司秘钥

    @TableField("app_code")
    private String app_code;//应用编码

    @TableField("app_name")
    private String app_name;//应用名称

    @TableField("user_id")
    private Integer user_id;//用户id

    @TableField("order_no")
    private String order_no;//订单号

    @TableField("id_type")
    private Integer id_type;//证件类型

    @TableField("idcard")
    private String idcard;//证件号码

    @TableField("phone")
    private String phone;//手机号码

    @TableField("mobile_id")
    private String mobile_id;//设备id

    @TableField("age")
    private Integer age;//年龄

    @TableField("married")
    private Integer married;//婚姻

    @TableField("education")
    private Integer education;//学历

    @TableField("city")
    private String city;//城市

    @TableField("user_real_name")
    private String user_real_name;//用户真实名字

    @TableField("user_name")
    private String user_name;//昵称

    @TableField("mail_count")
    private Integer mail_count;//通讯录条数

    @TableField("vocation")
    private Integer vocation;//职业

    @TableField("industry")
    private String industry;//行业

    @TableField("register_interval")
    private String register_interval;//注册间隔

    @TableField("emergency_contact")
    private String emergency_contact;//联系人（多个联系人）

    @TableField("black_box")
    private String black_box;//同盾采集设备特征必传参数

    @TableField("gps_location")
    private String gps_location;

    @TableField("order_status")
    private Integer order_status = 1;

    @TableField("money")
    private Float money;

    @TableField("limit_date")
    private int limit_date;

    @TableField("apply_oid")
    private String apply_oid;

    @TableField("description_array")
    private String description_array;

    @TableField("base_description_array")
    private String base_description_array;

    @TableField("add_time")
    private Date add_time;

    @TableField("over_day")
    private Integer over_day;

    @TableField("total_score")
    private Integer totalScore = 850;

    @TableField(exist = false)
    private String rangeTime;

    @TableField(exist = false)
    private Set<String> conTactList;

    @TableField(exist = false)
    private HashSet<String> descriptions = new HashSet<String>();

    public HashSet<String> getDescriptions() {
        return descriptions;
    }
    public void setDescriptions(String descriptions) {
        this.descriptions.add(descriptions);
        String description = this.descriptions.toString();
        this.description_array = description.substring(1, description.length()-1);


        StringBuilder sb = new StringBuilder();
        Iterator<String> it = this.descriptions.iterator();

        while(it.hasNext()){
            //判断是否有下一个
            sb.append(it.next()).append("~");
        }
        this.description_array = sb.toString().substring(1, sb.toString().length()-1);
    }


    @TableField(exist = false)
    private HashSet<String> baseDescriptions = new HashSet<>();

    public HashSet<String> getBaseDescriptions() {
        return baseDescriptions;
    }
    public void setBaseDescriptions(String descriptions) {
        this.baseDescriptions.add(descriptions);

        StringBuilder sb = new StringBuilder();
        Iterator<String> it = this.baseDescriptions.iterator();

        while(it.hasNext()){
            //判断是否有下一个
            sb.append(it.next()).append("~");
        }
        this.base_description_array = sb.toString().substring(1, sb.toString().length()-1);

    }

    public void setConTactList() {
        Set<String> phoneSet = new HashSet<>();
        String[] split = this.emergency_contact.split(",");
        for (int i = 0; i < split.length; i++) {
            String[] phoneArr = split[i].split(":");
            for (int j = 0; j < phoneArr.length; j++) {
                String phone = phoneArr[2];
                phoneSet.add(phone);
            }
        }
    }

    public Set<String> getConTactList() {
        return conTactList;
    }
}
