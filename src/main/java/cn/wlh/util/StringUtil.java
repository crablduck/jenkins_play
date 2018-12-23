package cn.wlh.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	
	public static boolean isDatetime(String datetimeStr) {
		if(null == datetimeStr) return false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			format.parse(datetimeStr);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	public static boolean isUrl(String url) {
		if(null == url) return false;
		return (url.startsWith("http://") || url.startsWith("https://"));
		// String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		/*String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(:[\\d]*)+(/[\\w- ./?%&=]*)?";
		return url.matches(regex);*/
	}
	
	public static boolean isEmpty(String str) {
		return StringUtils.isEmpty(str);
	}
	
	public static String replaceUtf8(String str) {
		if(null == str) return null;
		 return str.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "");
	}
	
	public static boolean notNullAndEques(String source,String target) {
		if (null == source || null==target) {
			return false;
		}
		return source.contentEquals(target);
	}

	public static String getTimeString(String[] split, int begin, int end){
		String timeString = null;
		ArrayList<String> splitString = new ArrayList<>();
		for (int i = begin; i < end; i++) {
			splitString.add(split[i]);
		}
		timeString = org.apache.tomcat.util.buf.StringUtils.join(splitString, '-');
		return timeString;
	}

    /**
     * 把范围转化为多个时间点
     * @param dateRange
     * @return List<String></String>
     */
    public static List<String> getRangeDatePoint(String dateRange){

        String[] split = dateRange.split("-");
        Integer i1 = Integer.parseInt(split[2].trim());
        Integer beginInt = Integer.parseInt(split[2].trim());
        Integer endInt = Integer.parseInt(split[5].trim());
        List<String> timeRanges = new ArrayList<String>();
        for (int i = beginInt; i < endInt+2; i++) {
            String StringPoint = split[0] + "-" + split[1] + "-" + Integer.valueOf(i).toString();
            timeRanges.add(StringPoint);
        }

        return timeRanges;
    }
}
