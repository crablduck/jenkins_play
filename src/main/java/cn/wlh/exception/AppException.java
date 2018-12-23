package cn.wlh.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppException extends RuntimeException{

	private static final long serialVersionUID = 6721166969213281524L;

	private String code;
	
	private String msg;
	
	public AppException(String code,String msg, Throwable ex) {
		super(msg,ex);
		this.code = code;
		this.msg = msg;
	}
	
}
