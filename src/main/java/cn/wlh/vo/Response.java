package cn.wlh.vo;

import lombok.Data;

@Data
public class Response {

    public String message;

    public Integer status;

    public Object data;

}
