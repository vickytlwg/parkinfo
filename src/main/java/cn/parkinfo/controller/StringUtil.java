/**
 * 
 */
package cn.parkinfo.controller;

import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * 
 */
public class StringUtil {

  public static String mapToLogInfo(Map<String, String> map) {
    if (map == null || map.size() <= 0)
      return null;

    String[] splits = new String[] {
        "=", ";" };
    StringBuffer sb = new StringBuffer();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      String key = removeSplit(entry.getKey(), splits);
      String value = removeSplit(entry.getValue(), splits);
      sb.append(key);
      sb.append(splits[0]);
      sb.append(value);
      sb.append(splits[1]);
    }
    return sb.toString();

  }

  public static String mapToString(Map<String, String> map, String[] splits) {
    if (map == null || map.size() <= 0)
      return null;
    if (splits == null || splits.length < 2)
      return null;

    StringBuffer sb = new StringBuffer();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      String key = removeSplit(entry.getKey(), splits);
      String value = removeSplit(entry.getValue(), splits);
      sb.append(key);
      sb.append(splits[0]);
      sb.append(value);
      sb.append(splits[1]);
    }
    return sb.toString();

  }

  private static String removeSplit(String source, String[] splits) {
    if (source == null || source.trim().length() <= 0)
      return null;
    if (splits == null || splits.length <= 0)
      return source;

    for (String split : splits) {
      source = source.replaceAll(split, "");
    }
    return source;
  }
  
  public static String getCn(){
    UUID uuid = UUID.randomUUID();
    String str = uuid.toString();   
    return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);   
  }
  
  public static String getString(Object obj){
    return obj==null?"":obj.toString();
  }
  
  public static boolean isEmpty(String obj){
	  return obj==null||obj.trim().length()==0;
  }
  
}
