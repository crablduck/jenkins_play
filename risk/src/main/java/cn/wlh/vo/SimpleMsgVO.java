package cn.wlh.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleMsgVO implements Serializable {

	/**
	 * 状态码
	 */
	private String code;

	/**
	 * 信息
	 */
	private String message;

	/**
	 * 数据
	 */
	private Object data;

	/**
	 * 返回成功，无数据
	 * @return
	 */
	public static SimpleMsgVO getOk() {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("200");
		msg.setMessage("SUCCESS");
		return msg;
	}

	/**
	 * 返回成功，有数据
	 * @param data
	 * @return
	 */
	public static SimpleMsgVO getOk(Object data) {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("200");
		msg.setMessage("SUCCESS");
		msg.setData(data);
		return msg;
	}

	/**
	 * 账号被锁定
	 * @return
	 */
	public static SimpleMsgVO get800Fail() {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("800");
		msg.setMessage("账号被锁！请联系管理员");
		return msg;
	}


	/**
	 * 返回失败，提示信息
	 * @return
	 */
	public static SimpleMsgVO get401Fail(String message) {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("401");
		msg.setMessage(message);
		return msg;
	}

	/**
	 * 账号错误
	 * @return
	 */
	public static SimpleMsgVO get400Fail() {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("400");
		msg.setMessage("密码错误");
		return msg;
	}

	/**
	 * 账号错误
	 * @return
	 */
	public static SimpleMsgVO get402Fail() {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("402");
		msg.setMessage("账号不存在");
		return msg;
	}

	/**
	 * 删除失败，该角色已被使用
	 * @return
	 */
	public static SimpleMsgVO get403Fail() {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("403");
		msg.setMessage("删除失败，该角色已被使用");
		return msg;
	}

	/**
	 * 请求数据是无效的json格式
	 * @return
	 */
	public static SimpleMsgVO get407Fail() {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("407");
		msg.setMessage("The request data is a invalid json format");
		return msg;
	}

	/**
	 * 非空
	 * @return
	 */
	public static SimpleMsgVO get406Fail(String message) {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("406");
		msg.setMessage(message);
		return msg;
	}

	/**
	 * 非空
	 * @return
	 */
	public static SimpleMsgVO get408Fail(String message) {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("408");
		msg.setMessage(message);
		return msg;
	}

	/**
	 * 系统错误
	 * @return
	 */
	public static SimpleMsgVO get500Fail() {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("500");
		msg.setMessage("system error");
		return msg;
	}



	public static SimpleMsgVO getSignFail() {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("401");
		msg.setMessage("sign fail");
		return msg;
	}



	public static SimpleMsgVO get500Fail(String str) {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("500");
		msg.setMessage(str);
		return msg;
	}

	public static SimpleMsgVO get501Fail(String str) {
		SimpleMsgVO msg = new SimpleMsgVO();
		msg.setCode("501");
		msg.setMessage(str);
		return msg;
	}

}
