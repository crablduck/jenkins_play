package cn.wlh.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppCheckException extends Exception{

	private static final long serialVersionUID = 5647321985052352580L;

	private String code;
	
	private String msg;
	
	public AppCheckException(String code,String msg, Throwable ex) {
		super(msg,ex);
		this.code = code;
		this.msg = msg;
	}
	
}
