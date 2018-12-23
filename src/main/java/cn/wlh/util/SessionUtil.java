package cn.wlh.util;

import cn.wlh.common.RedisObjHelper;
import cn.wlh.model.SystemUser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 	缓存工具类
 */
@Component
public class SessionUtil {

	@Resource
	private RedisObjHelper redisObjHelper;

	private final static String session_key = "session-risk";

	public HttpSession getSession() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		HttpSession session = request.getSession();
		return session;
	}

	/**
	 * 退出
	 */
	public void logout() {
		HttpSession session = getSession();
		session.invalidate();
	}

	/**
	 * 向redis存入数据，默认3600秒
	 * @param key
	 * @param obj
	 */
	public void put(String key, Object obj) {
		redisObjHelper.put(key, obj, 1 * 60 * 60);
	}

	/**
	 * 向redis存入数据，并指定时间
	 * @param key
	 * @param obj
	 * @param timeout
	 */
	public void put(String key, Object obj,int timeout) {
		redisObjHelper.put(key, obj, timeout);
	}

	/**
	 * 通过key获取数据
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return redisObjHelper.get(key);
	}

	/**
	 * 向redis中存入用户信息,默认3600秒
	 * @param systemUser
	 */
	public void putUserInfo(SystemUser systemUser){
		String id = getId();
		redisObjHelper.put(session_key + id, systemUser, 1 * 60 * 60);
	}

	/**
	 * 向redis中取出用户信息
	 */
	public SystemUser getUserInfo(){
		String id = getId();
		Object obj = get(session_key + id);
		return (SystemUser) obj;
	}

	public String getId() {
		HttpSession session = getSession();
		return session.getId();
	}

}
