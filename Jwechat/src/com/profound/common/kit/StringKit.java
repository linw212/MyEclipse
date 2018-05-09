package com.profound.common.kit;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringKit.
 */
public class StringKit {
	static SimpleDateFormat df_sdf1=new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat df_sdf2=new SimpleDateFormat("yyyy-MM");
	static DecimalFormat nb_ft=new DecimalFormat("0.00");
	/**
	 * 首字母变小写
	 */
	public static String firstCharToLowerCase(String str) {
		Character firstChar = str.charAt(0);
		String tail = str.substring(1);
		str = Character.toLowerCase(firstChar) + tail;
		return str;
	}
	
	/**
	 * 首字母变大写
	 */
	public static String firstCharToUpperCase(String str) {
		Character firstChar = str.charAt(0);
		String tail = str.substring(1);
		str = Character.toUpperCase(firstChar) + tail;
		return str;
	}
	/**
	 * 判断字符串是否是整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInt(String str) {
		boolean isNumber = false;
		if (notBlank(str)) {
			str.matches("^([1-9]\\d*)|(0)$");
			isNumber = true;
		}
		return isNumber;
	}
	/**
	 * 判断字符串为 null 或者为  "" 时返回 true
	 */
	public static boolean isBlank(Object str) {
		return str == null || "".equals(str.toString().trim())||"null".equalsIgnoreCase(str.toString().trim()) ? true : false;
	}
	
	/**
	 * 判断字符串不为 null 而且不为  "" 时返回 true
	 */
	public static boolean notBlank(Object str) {
		return str == null || "".equals(str.toString().trim())|| "null".equalsIgnoreCase(str.toString().trim()) ? false : true;
	}
	/**判断字符串数组是否存在空字符串*/
	public static boolean notBlank(String... strings) {
		if (strings == null)
			return false;
		for (String str : strings)
			if (str == null || "".equals(str.trim())|| "null".equalsIgnoreCase(str.trim()))
				return false;
		return true;
	}
	/**判断对象数组是否存在空对象*/
	public static boolean notNull(Object... paras) {
		if (paras == null)
			return false;
		for (Object obj : paras)
			if (obj == null)
				return false;
		return true;
	}
	/**
	 * 把数组转换成字符串，并且以分隔符（decollator） 分割开,
	 * @param strs 对象数组
	 * @param decollator 分割符
	 * @return 分割后的字符串
	 */
	public static String arrayToString(Object[] strs,String decollator) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			str.append(strs[i] + decollator);
		}
		if (str.length() > 0) {
			str.deleteCharAt(str.length() - decollator.length());
		}
		return str.toString();
	}
	/**
	 * 把object数组转换成String，如 id[]：1,2,3 转换为'1','2','3',
	 * 数组中的空对象或空字符串将被剔除
	 * @param objs
	 * @return 分割后的字符串
	 */
	public static String arrayIDToString(Object[] objs) {
		int num=0;
		StringBuffer string=new StringBuffer();
		for(Object obj:objs)
		{
			if(isBlank(obj))
				continue;
			String temp=obj.toString();
			temp=temp.trim().replace("'", "").replace("\"", "");
			if(num==0)
			{
				string.append("'"+temp+"'");
				num++;
			}
			else
				string.append(",'"+temp+"'");
		}
		return string.toString();

	}
	/**
	 * 将集合转换为字符串表示,集合中的对象默认采用toString()方法转换为字符串
	 * 采用,号进行分割
	 * @param collection 集合对象
	 * @return
	 */
	public static String arrayIDToString(@SuppressWarnings("rawtypes") Collection collection) 
	{
		if(collection==null||collection.size()==0)
			return "";
		else
			return arrayIDToString(collection.toArray());

	}
	/**
	 * 将字符串处理成适合in查询的条件(如:aa,bb,cc 处理成'aa','bb','cc')
	 * @param stringByComma
	 * @return
	 */
	public static String change_in(String stringByComma)
	{
		return change_in(stringByComma,",");
	}
	/**
	 * 将字符串处理成适合in查询的条件(如:aa;bb;cc 处理成'aa','bb','cc')
	 * @param stringByComma 处理的字符串
	 * @param decollator 分割字符串
	 * @return
	 */
	public static String change_in(String stringByComma,String decollator)
	{
		if(stringByComma==null||stringByComma.equals(""))
			return "''";
		String[] strings=stringByComma.trim().split(decollator);
		return arrayIDToString(strings);
	}
	/**
	 * 将数值转换为中文单位表示的字符串,一般用于报表数据转换
	 * 当数值大于1亿则转换到亿单位级,当数值小于1亿大于1万将转换到万单位级
	 * 如:8934343232 转换后为89.3亿;4343232转换后未434.3万
	 * @param num
	 * @return
	 */
	public static String transitionNum(double num)
	{
		DecimalFormat dFormat = new DecimalFormat("0.0");
		if(num<10000&&num>-10000)
			return dFormat.format(num);
		num=num/10000;
//转亿表示
		if (num / 10000d > 1||num / 10000d < -1)
			return dFormat.format(num / 10000d) + "亿";
//转万表示
		return dFormat.format(num) + "万";
	}
	/**
	 * 去除字符串中的空格、回车、换行符、制表符 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str)
	{
		String dest = "";
		if (str != null)
		{
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	/**
	 * 将字符串(true或false 转为boolean类型的true或false)
	 * @param boolean_str
	 * @return
	 */
	public static boolean changeToBoolean(String boolean_str)
	{
		if(StringKit.notBlank(boolean_str))
		{
			if(boolean_str.trim().equals("true"))
				return true;
			else
				return false;
		}
		else
			return false;
	}
	/**
	 * 转成大写
	 * @param str
	 * @return
	 */
	public static String toUpperStr(String str)
	{
		if(isBlank(str))
			return "";
		else
			return str.toUpperCase();
	}
	/**
	 * 转成小写
	 * @param str
	 * @return
	 */
	public static String toLowerStr(String str)
	{
		if(isBlank(str))
			return "";
		else
			return str.toLowerCase();
	}
	/**
	 * 将字符串转换为int类型
	 * @param str
	 * @return
	 * @throws NumberFormatException
	 */
	public static int parseToInt(String str) throws NumberFormatException
	{
		if(isBlank(str))
			return 0;
		else
			return Integer.parseInt(str);
	}
	/**
	 * 将字符串转换为double类型
	 * @param str
	 * @return
	 * @throws NumberFormatException
	 */
	public static double parseToDouble(String str) throws NumberFormatException
	{
		if(isBlank(str))
			return 0;
		else
			return Double.parseDouble(str);
	}
	/**
	 * 将对象转换为double,如果对象为空则转换为0
	 * @param num
	 * @return
	 */
	public static Double parseToDouble(Object num)
	{
		if(isBlank(num))
			return 0d;
		else if(num instanceof Double)
			return (Double)num;
		else if(num instanceof BigDecimal)
			return ((BigDecimal)num).doubleValue();
		else if(num instanceof Number)
			return ((Number)num).doubleValue();
		else
			return new Double(parseToDouble(num.toString()));
	}
	/**
	 * 将对象转换为字符串表示,若对象为null则返回空字符串
	 * @param obj 转换的对象
	 * @return
	 */
	public static String ValueOf(Object obj) {
		if(obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}
	/**
	 * 格式化小数,精确到小数点后2位
	 * @param value
	 * @return 格式化后的小数字符串表示
	 */
	public static String formatDouble_str(double value)
	{
		return nb_ft.format(value);
	}
	/**
	 * 格式化小数,精确到小数点后2位
	 * @param value
	 * @return 格式化后的小数
	 */
	public static double formatDouble_num(double value)
	{
		return Double.parseDouble(nb_ft.format(value));
	}
	/**
	 * 字符串中 去掉重复的数据
	 * 例如：1,2,3,4,5,4,3,2,1   得到 1,2,3,4,5
	 * @param str
	 * @return
	 */
	public static String delRepeatByStrNodyh(String str){
		String result = "";
		if(notBlank(str)){
			String[] list = str.split(",");
			for(int i=0; i<list.length; i++){
				if(result.indexOf(list[i]) == -1 ){
					result = result + list[i] + ",";
				}
			}
		}
		if(notBlank(str)){
			result=result.substring(0, result.length()-1);
		}
		return result;
	}
	
	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.replaceAll("-", "");
	}
	/**
	 * 获取一个补全的位码
	 * @param num 需要补全的数值
	 * @param offset 补全位数
	 * @return
	 */
	public static String getSeq(int num,int offset)
	{
		int num_length=String.valueOf(num).length();
		if(num_length>=offset)
			return String.valueOf(num);
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<offset-num_length;i++)
		{
			sb.append("0");
		}
		sb.append(String.valueOf(num));
		return sb.toString();
	}
	
	  
	/** */
	/**
	 * 
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * 
	 * @param v2
	 *            加数
	 * 
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).doubleValue();
	}

	/** */
	/**
	 * 
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * 
	 * @param v2
	 *            减数
	 * 
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).doubleValue();
	}
	
	/**生成纯数字的六位随机码*/
	public static String getRandomCode(){
		int[] array = {0,1,2,3,4,5,6,7,8,9};
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
		    int index = rand.nextInt(i);
		    int tmp = array[index];
		    array[index] = array[i - 1];
		    array[i - 1] = tmp;
		}
		String result = "";
		for(int i = 0; i < 6; i++)
		    result += array[i];
		System.out.println(result);
		return result;
	}
    /**
     * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh"
     * "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/foo.jar"
     */
    public static String getRootPath(URL url) {
        String fileUrl = url.getFile();
        int pos = fileUrl.indexOf('!');

        if (-1 == pos) {
            return fileUrl;
        }

        return fileUrl.substring(5, pos);
    }

    /**
     * "cn.fh.lightning" -> "cn/fh/lightning"
     * @param name
     * @return
     */
    public static String dotToSplash(String name) {
        return name.replaceAll("\\.", "/");
    }

    /**
     * "Apple.class" -> "Apple"
     */
    public static String trimExtension(String name) {
        int pos = name.indexOf('.');
        if (-1 != pos) {
            return name.substring(0, pos);
        }

        return name;
    }

    /**
     * /application/home -> /home
     * @param uri
     * @return
     */
    public static String trimURI(String uri) {
        String trimmed = uri.substring(1);
        int splashIndex = trimmed.indexOf('/');

        return trimmed.substring(splashIndex);
    }
}




