package org.ieee.odm.bpa;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ieee.odm.model.base.ModelStringUtil;
import org.junit.Test;

public class TestChineseName {

	//@Test
	public void TestChinese() throws Exception {
		final String s="B  yn MW50    525.MW-173 -76.7";//""B     梅林2M  110.xg";
        final String str="T     PANNANG122.0 PANNAN50525.  720.1      0.0202            22.0 536.";//"L     DCZQ=   500. YANDU=  500.2 5000 .00001.00015      .00525 1.0                        4*720/1.0";
		char[] c=s.toCharArray();
        int cnCharNum=getNum(s);
        System.out.println(getStringReturnEmptyString(s,7,14-cnCharNum));
        String s2=ModelStringUtil.replaceChineseChar(s);
        System.out.println(ModelStringUtil.replaceChineseChar(s));
		System.out.println(getStringReturnEmptyString(s2,15,18));
		String str2=ModelStringUtil.replaceChineseChar(str);
		System.out.println(str2);
		String sec= ModelStringUtil.getStringReturnEmptyString(str2,33, 33).trim();
		System.out.println(str.startsWith("L "));
	}
	
	public static String getStringReturnEmptyString(String str, int beginCol, int endCol) {
		if (str.length() <=endCol){
			if(beginCol>str.length()){
				return "";
			}else if(beginCol<=str.length()){
			    return str.substring(beginCol-1, str.length()).trim();
		     }
		}
		return str.substring(beginCol-1, endCol).trim();
	}
//	public static void main(String[] args) 
//	{ 
////	String input = "中文语言fd何as最近几天dafds工作dfds效率不高fdsf番dfsfdsa啊"; 
////	String temp = null; 
////	Pattern p = Pattern.compile("[u4E00-u9FA5]+"); 
////	Matcher m = p.matcher(input); 
////	while (m.find()) 
////	{ 
////	temp = m.group(0); 
////	System.out.println(temp + ":" + temp.length()); 
////	} 
//	
//	String regEx = "[\u4e00-\u9fa5]"; 
//	//System.out.println(regEx); 
//	String str = "中文fda她jjs"; 
////	String tem= str.replaceAll(regEx,"aa"); 
////	System.out.println(""+(tem.length()-str.length())); 
//	System.out.println(getNum(str));
//	} 
	
	public static int getNum(String str){ 
		int count=0; 
		String regEx = "[\u4e00-\u9fa5]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(str); 
		while(m.find())count++; 
		return count; 
		} 
	





}
