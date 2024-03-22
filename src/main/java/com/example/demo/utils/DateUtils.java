package com.example.demo.utils;


import dm.jdbc.util.StringUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
	
	private static final DateFormat mmddFormat = new SimpleDateFormat("MM-dd");
	private static final DateFormat hhmmFormat = new SimpleDateFormat("HH:mm");
	
	public static int compareHM(String hm1, String hm2){
		try{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HHmmss");
			Date date1 = format.parse("2001-01-01 "+hm1+"00");
			Date date2 = format.parse("2001-01-01 "+hm2+"00");
			if(date1.getTime()>date2.getTime())
				return 1;
			else if(date1.getTime()< date2.getTime())
				return -1;
			else
				return 0;
		}
		catch (ParseException e){
		}
		return 0;
	}
	public static Date StrToDate1(String str, String format){
		try{
			if(StringUtil.isEmpty(format))
				format = "yyyy/MM/dd";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(str);
		}catch(ParseException pe){
			return null;
		}
	}
	public static Date StrToDate1(String str){
		return StrToDate1(str,"yyyy/MM/dd");
	}
	
	public static int compareHM2(String hm1, String hm2){
		try{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date1 = format.parse("2001-01-01 "+hm1+":00");
			Date date2 = format.parse("2001-01-01 "+hm2+":00");
			if(date1.getTime()>date2.getTime())
				return 1;
			else if(date1.getTime()< date2.getTime())
				return -1;
			else
				return 0;
		}
		catch (ParseException e){
		}
		return 0;
	}
	
	/**
	 * 这样写才能避免对象的任意创建，达到简便又能节省内存空间
	 * @author XuGuo
	 * @since 2009-07-23
	 * @param date
	 * @return
	 */
	public static String formatMD(Date date){
		return date==null?"":mmddFormat.format(date);
	}
	
	public static String formatHM(Date date){
		return date==null?"":hhmmFormat.format(date);
	}
	
	public static String formatDateTime(Date date, String format){
		if (date==null) return "";
		if (format==null) return date.toString();
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	public static String fromatY(Date date){
		return date==null?"":formatDateTime(date,"yyyy");
	}
	public static String formatY0M0D(Date date){
		return date==null?"":formatDateTime(date,"yyyyMMdd");
	}
	public static String formatMMHHSS(Date date){
		return date==null?"":formatDateTime(date,"HHmmss");
	}
	public static String formatYMD(Date date){
		return date==null?"":formatDateTime(date,"yyyy-MM-dd");
	}
	public static String formatYMDHM(Date date){
		return date==null?"":formatDateTime(date,"yyyy-MM-dd HH:mm");
	}
	public static String formatDateTimeByDate(Date date){
		return date==null?"":formatDateTime(date,"yyyy-MM-dd HH:mm:ss");
	}
	public static String formatYMDHMByDate(Date date){
		return date==null?"":formatDateTime(date,"yyyyMMddHHmm");
	}
    public static boolean showNew(Date time){
        if (time==null) return false;
        return DateUtils.addDays(time,3).compareTo(new Date())>=0;
    }	
    public static Date addDays(Date srcDate, int addDays)
    {
    	return getNextDayCurrDay(srcDate,addDays);
    }    
    
    public static String addDays(String strDate, int addDays) {
    	Date date = StrToDate(strDate);
    	return formatYMD(addDays(date,addDays));
    }
    
    
    public static Date addMinutes(Date srcDate, int minutes)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(srcDate);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }       
    
    public static Date getNextDayCurrDay(Date currDate, int i){
    	if(currDate==null) return null;
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(currDate);
    	gc.add(GregorianCalendar.DATE, i);
    	return gc.getTime();
    }
    
	public static int getCurrDay(){
    	Calendar now = Calendar.getInstance();
    	return now.get(Calendar.DAY_OF_WEEK);
    }
	
	/**
	 * 字符串转化为日期
	 * @param str 被转化的字符串
	 * @param format 转化格式
	 * @return 返回日期
	 * @throws ParseException
	 * @author sys53
	 * @serialData 2007-11-03
	 */
	public static Date StrToDate(String str, String format){
		try{
			if(StringUtil.isEmpty(format))
				format = "yyyy-MM-dd";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(str);
		}catch(ParseException pe){
			return null;
		}
	}
	 
	/**  
	  * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的分钟  
	  */  
	 public static String getNextDay(Date nowdate, int delay) {
		 try{   
			  String mdate = "";
			  long myTime = (nowdate.getTime() / 1000) + delay * 60 ;  
			  nowdate.setTime(myTime * 1000);   
			  mdate = formatYMDHMByDate(nowdate);  
			  return mdate;   
		  }catch(Exception e){
			  e.printStackTrace();
			  return formatYMDHMByDate(nowdate);   
		  }   
	 }    
	 
	/**
	 * 字符串转化为日期,默认格式为:yyyy-MM-dd
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date StrToDate(String str){
		return StrToDate(str,"yyyy-MM-dd");
	}
	
	/**
	 * 判断某天是否在某个星期时间内 比如"2009-05-10" 是否在"1,2,4,5"星期内
	 * @param strDate
	 * @param week
	 * @return
	 */
	public static boolean isExistInWeek(String strDate, String week){
		Date date = StrToDate(strDate);
		int days = dayOfWeek(date);
		if(week.indexOf(String.valueOf(days))>=0){
			return true;
		}
		return false;
	}
	
	//判断日期为星期几,1为星期一,6为星期六,7为星期天，依此类推   
	  public static int  dayOfWeek(Date date)   {
		  //首先定义一个calendar，必须使用getInstance()进行实例化   
	      Calendar aCalendar= Calendar.getInstance();
	      //里面野可以直接插入date类型   
	      aCalendar.setTime(date);   
	      //计算此日期是一周中的哪一天   
	      int   x=aCalendar.get(Calendar.DAY_OF_WEEK);
	      if(x==1)
	    	  x=7;
	      else
	    	  x = x - 1;
	      return  x;   
	  }
	
	/**
	 * 转换字符串日期类型为 "yyyy-MM-dd" 类型
	 * @param date  06MAY09
	 * @return 
	 * 
	 */
	public static String getParsedDate(String strDate){
		String[] monIntArray = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		String[] monStrArray = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
		String year = "20"+strDate.substring(5,7);
		String month = strDate.substring(2,5);
		String day = strDate.substring(0,2);
		for(int i=0;i<monStrArray.length;i++){
			if(monStrArray[i].equalsIgnoreCase(month)){
				month = monIntArray[i];
				break;
			}
		}
		return year+"-"+month+"-"+day;
	}
	
	public static long getNumOfDays(String date1, String date2){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1;
		try {
			d1 = df.parse(date1);
			Date d2 = df.parse(date2);
			long diff = Math.abs(d2.getTime()-d1.getTime());
			return (long)(diff/(1000*60*60*24));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return 0;
	}
	
	public static long getDiffDays(String date1, String date2){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d1;
        long diff = 0;
		try {
			d1 = df.parse(date1);
			Date d2 = df.parse(date2);
			long diff_1 = d2.getTime()-d1.getTime();
			if(diff_1>=0){
				diff = Math.abs(diff_1);
				return (long)(diff/(1000*60*60*24));
			}
			else{
				return (long)(diff_1/(1000*60*60*24));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return 0;
	}
	
	public static int getDiffDays(Date start, Date end){
		long diff = Math.abs(end.getTime()-start.getTime());
		return (int)(diff/(1000*60*60*24));		
	}
	
	public static long getNumOfDays(Date date1, Date date2){
        long diff = Math.abs(date2.getTime()-date1.getTime());
        return (long)(diff/(1000*60*60*24));
	}	
	
	/**
	 * 断判两个日期之间时差是否在5分钟以上
	 * @param d1　日期1
	 * @param d2 日期2
	 * @return 返回true两个日期之间相差5分钟以上，false相差十分钟以内.
	 */
	public static boolean compare(Date d1, Date d2)
	{
		if((d2.getTime()-d1.getTime())>600000l){
			return true;
		}
		return false;
	}	
	
	/**
	 * 获取某天是星期几
	 * @param d
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getTheDay(Date d){
		return "日一二三四五六".charAt(d.getDay())+"";
	}
	
	/**
	 * 获取得某年的第几周的起始日期和结束日期
	 * @param year 年份
	 * @param week 第几周
	 * @return String 数组， [0] 起始日期    [1] 结束日期
	 */
	public static Date[]  weekDateEx(int year , int week){
		if(week<1||week>52)return null;
		Date d[] = new Date[2];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.WEEK_OF_YEAR, week);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		d[0] = c.getTime();
		c.add(Calendar.DATE, 6);
		d[1] = c.getTime();
		return d;
	}
	public static String[]  weekDate(int year , int week){
		if(week<1||week>52)return null;
		String s [] = new String[2];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.WEEK_OF_YEAR, week);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		s[0] = formatYMD(c.getTime());
		c.add(Calendar.DATE, 6);
		s[1] = formatYMD(c.getTime());
		return s;
		
	}
	/**
	 * 获取得某年的第几月的起始日期和结束日期
	 * @param year 年份
	 * @param month 第几月
	 * @return String 数组， [0] 起始日期    [1] 结束日期
	 */
	public static Date[]  monthDateEx(int year , int month){
		if(month<1||month>12) return null;
		Date s [] = new Date[2];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		s[0] = c.getTime();
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		s[1] = c.getTime();
		return s;
		
	}
	public static String[]  monthDate(int year , int month){
		if(month<1||month>12) return null;
		String s [] = new String[2];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		s[0] = formatYMD(c.getTime());
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		s[1] = formatYMD(c.getTime());
		return s;
		
	}
	/**
	 * 获取得某年的第几季度的起始日期和结束日期
	 * @param year 年份
	 * @param season 第几季
	 * @return String 数组， [0] 起始日期    [1] 结束日期
	 */
	public static Date[]  seasonDateEx(int year , int season){
		if(season<1||season>4)return null;
		String y= String.valueOf(year);
		Date[]s = new Date[2];
		switch(season){
		case 1 : s[0]= StrToDate(y+"-01-01");s[1]= StrToDate(y+"-03-31");break;
		case 2 : s[0]= StrToDate(y+"-04-01");s[1]= StrToDate(y+"-06-30");break;
		case 3 : s[0]= StrToDate(y+"-07-01");s[1]= StrToDate(y+"-09-30");break;
		case 4 : s[0]= StrToDate(y+"-10-01");s[1]= StrToDate(y+"-12-31");break;
		}
		return s;
		
	}
	public static String[]  seasonDate(int year , int season){
		if(season<1||season>4)return null;
		String y= String.valueOf(year);
		String[]s = new String[2];
		switch(season){
		case 1 : s[0]= y+"-01-01";s[1]= y+"-03-31";break;
		case 2 : s[0]= y+"-04-01";s[1]= y+"-06-30";break;
		case 3 : s[0]= y+"-07-01";s[1]= y+"-09-30";break;
		case 4 : s[0]= y+"-10-01";s[1]= y+"-12-31";break;
		}
		return s;
		
	}
	
	/**
	 * 获取某年某月有多少天 如:20090225 返回28
	 * @param strDate 某天
	 * @return
	 */
	@SuppressWarnings({ "static-access", "deprecation" })
	public static int getDaysOfMonth(String strDate){
		int day = 0; 
		Calendar cal = Calendar.getInstance();
		//格式化日期 
		SimpleDateFormat dformat = new SimpleDateFormat("yyyymmdd");
		try { 
			Date date = dformat.parse(strDate);
			cal.setTime(date); 
			//在当前月份上加一,由于JAVA种JAN为0,所以这里加2 
			cal.add(cal.MONTH, 2); 
			//设置日期为1号 
			cal.set(cal.DATE, 1); 
			//向前退一天 
			cal.add(cal.DAY_OF_MONTH, -1); 
			date = cal.getTime(); 
			//得到当前日,即是本月的天数 
			day = date.getDate(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return day; 
	}
		
	public static int getDateOfMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
		
	/**
	 * 获取当前日期的下个月的若干天后的日期
	 * @param days
	 * @return
	 */
	public static String getDateInNextMonthOfNextDays(int days){
		return formatYMD(getNextDayCurrDay(StrToDate(getNextMonthFirst()),days));
	}
	
	/**  
     * 得到二个日期间的间隔天数  
     */  
	 public static String getTwoDay(String sj1, String sj2) {
	     SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
	     long day = 0;   
	     try {   
	      Date date = myFormatter.parse(sj1);
	      Date mydate = myFormatter.parse(sj2);
	      day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);   
	     } catch (Exception e) {
	      return "";   
	     }   
	     return day + "";   
	 }   
	
		
	 /**  
	     * 两个时间之间的天数  
	     *   
	     * @param date1  
	     * @param date2  
	     * @return  
	     */  
	 public static long getDays(String date1, String date2) {
	     if (date1 == null || date1.equals(""))   
	      return 0;   
	     if (date2 == null || date2.equals(""))   
	      return 0;   
	     // 转换为标准时间   
	     SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
	     Date date = null;
	     Date mydate = null;
	     try {   
	      date = myFormatter.parse(date1);   
	      mydate = myFormatter.parse(date2);   
	     } catch (Exception e) {
	     }   
	     long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);   
	     return day;   
	 }
	    
	 // 计算当月最后一天,返回字符串   
	 public static String getDefaultDay(){
	    String str = "";
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	    Calendar lastDate = Calendar.getInstance();
	    lastDate.set(Calendar.DATE,1);//设为当前月的1号
	    lastDate.add(Calendar.MONTH,1);//加一个月，变为下月的1号
	    lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
	       
	    str=sdf.format(lastDate.getTime());   
	    return str;     
	 }   
	 
	 public static Date getLastMonthOfMonth(Date d){
		 Calendar lastDate = Calendar.getInstance();
		 lastDate.setTime(d);
		 lastDate.set(Calendar.DATE,1);//设为当前月的1号
		 lastDate.add(Calendar.MONTH,1);//加一个月，变为下月的1号
		 lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
		 return lastDate.getTime();
	 }
	    
	 // 上月第一天   
	 public static String getPreviousMonthFirst(){
	    String str = "";
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	    Calendar lastDate = Calendar.getInstance();
	    lastDate.set(Calendar.DATE,1);//设为当前月的1号
	    lastDate.add(Calendar.MONTH,-1);//减一个月，变为下月的1号
	    //lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天   
	       
	    str=sdf.format(lastDate.getTime());   
	    return str;     
	 }   
	 
	 
	 //获取当月第一天   
	 public static String getFirstDayOfMonth(){
	    String str = "";
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	    Calendar lastDate = Calendar.getInstance();
	    lastDate.set(Calendar.DATE,1);//设为当前月的1号
	    str=sdf.format(lastDate.getTime());   
	    return str;     
	 }   
	 
	 public static Date getFirstDayOfMonth(Date d){
		 Calendar lastDate = Calendar.getInstance();
		 lastDate.setTime(d);
		 lastDate.set(Calendar.DATE,1);//设为当前月的1号
		 return lastDate.getTime();
	 }
	 
	    
	 //获取当天时间    
	 public static String getNowTime(String dateformat){
	     Date now   =   new Date();
	     SimpleDateFormat dateFormat   =   new SimpleDateFormat(dateformat);//可以方便地修改日期格式
	     String hehe  = dateFormat.format(now);
	     return hehe;   
	 }   
	    
	 // 获得当前日期与本周日相差的天数   
	 private static int getMondayPlus() {   
	     Calendar cd = Calendar.getInstance();
	     // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......   
	     int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK)-1;         //因为按中国礼拜一作为第一天所以这里减1
	     if (dayOfWeek == 1) {   
	         return 0;   
	     } else {   
	         return 1 - dayOfWeek;   
	     }   
	 }   
	    
	
	    
	// 获得下周星期日的日期   
	 public static String getNextSunday() {
	        
	     int mondayPlus = getMondayPlus();   
	     GregorianCalendar currentDate = new GregorianCalendar();
	     currentDate.add(GregorianCalendar.DATE, mondayPlus + 7+6);
	     Date monday = currentDate.getTime();
	     DateFormat df = DateFormat.getDateInstance();
	     String preMonday = df.format(monday);
	     return preMonday;   
	 }   
	    
	    
	//获得上月最后一天的日期   
	 public static String getPreviousMonthEnd(){
	     String str = "";
	     SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	     Calendar cal = Calendar.getInstance();
	     cal.set(Calendar.DAY_OF_MONTH, 1);
	     cal.add(Calendar.DAY_OF_YEAR, -1);
	     str=sdf.format(cal.getTime());   
	    return str;     
	 }   
	    
	//获得下个月第一天的日期   
	 public static String getNextMonthFirst(){
	     String str = "";
	     SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	    Calendar lastDate = Calendar.getInstance();
	    lastDate.add(Calendar.MONTH,1);//减一个月
	    lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
	    str=sdf.format(lastDate.getTime());   
	    return str;     
	 }   
	 public static Date getNextMonthFirst(Date d){
	    Calendar lastDate = Calendar.getInstance();
	    lastDate.setTime(d);
	    lastDate.add(Calendar.MONTH,1);//减一个月
	    lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
	    return lastDate.getTime();     
	 }   
	 
	    
	//获得下个月最后一天的日期   
	 public static String getNextMonthEnd(){
	     String str = "";
	     SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	    Calendar lastDate = Calendar.getInstance();
	    lastDate.add(Calendar.MONTH,1);//加一个月
	    lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
	    lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
	    str=sdf.format(lastDate.getTime());   
	    return str;     
	 }   
	    
	 //获得明年最后一天的日期   
	 public static String getNextYearEnd(){
	     String str = "";
	     SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	     Calendar lastDate = Calendar.getInstance();
	     lastDate.add(Calendar.YEAR,1);//加一个年
	     lastDate.set(Calendar.DAY_OF_YEAR, 1);
	   	 lastDate.roll(Calendar.DAY_OF_YEAR, -1);
	   	 str=sdf.format(lastDate.getTime());   
	   	 return str;     
	 }   
	    
	//获得明年第一天的日期   
	 public static String getNextYearFirst(){
	     String str = "";
	     SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
	     Calendar lastDate = Calendar.getInstance();
	     lastDate.add(Calendar.YEAR,1);//加一个年
	     lastDate.set(Calendar.DAY_OF_YEAR, 1);
	     str=sdf.format(lastDate.getTime());   
	     return str;     
	        
	 }   
	    
	//获得本年有多少天   
	 @SuppressWarnings("unused")
	private static int getMaxYear(){   
	     Calendar cd = Calendar.getInstance();
	     cd.set(Calendar.DAY_OF_YEAR,1);//把日期设为当年第一天
	     cd.roll(Calendar.DAY_OF_YEAR,-1);//把日期回滚一天。
	     int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
	     return MaxYear;   
	 }   
	 
	 private static int getYearPlus(){   
	     Calendar cd = Calendar.getInstance();
	     int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);//获得当天是一年中的第几天
	     cd.set(Calendar.DAY_OF_YEAR,1);//把日期设为当年第一天
	     cd.roll(Calendar.DAY_OF_YEAR,-1);//把日期回滚一天。
	     int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
	     if(yearOfNumber == 1){   
	         return -MaxYear;   
	     }else{   
	         return 1-yearOfNumber;   
	     }   
	 }   
	//获得本年第一天的日期   
	 public static String getCurrentYearFirst(){
	     int yearPlus = getYearPlus();   
	     GregorianCalendar currentDate = new GregorianCalendar();
	     currentDate.add(GregorianCalendar.DATE,yearPlus);
	     Date yearDay = currentDate.getTime();
	     DateFormat df = DateFormat.getDateInstance();
	     String preYearDay = df.format(yearDay);
	     return preYearDay;   
	 }   
	    
	    
	//获得本年最后一天的日期 *   
	 public static String getCurrentYearEnd(){
	     Date date = new Date();
	     SimpleDateFormat dateFormat   =   new SimpleDateFormat("yyyy");//可以方便地修改日期格式
	     String years  = dateFormat.format(date);
	     return years+"-12-31";   
	 }   
	    
	    
	//获得上年第一天的日期 *   
	 public static String getPreviousYearFirst(){
	     Date date = new Date();
	     SimpleDateFormat dateFormat   =   new SimpleDateFormat("yyyy");//可以方便地修改日期格式
	     String years  = dateFormat.format(date); int years_value = Integer.parseInt(years);
	     years_value--;   
	     return years_value+"-1-1";   
	 }   
	 
	    
	//获得本季度   
	 public static String getThisSeasonTime(int month){
	     int array[][] = {{1,2,3},{4,5,6},{7,8,9},{10,11,12}};   
	     int season = 1;   
	     if(month>=1&&month<=3){   
	         season = 1;   
	     }   
	     if(month>=4&&month<=6){   
	         season = 2;   
	     }   
	     if(month>=7&&month<=9){   
	         season = 3;   
	     }   
	     if(month>=10&&month<=12){   
	         season = 4;   
	     }   
	     int start_month = array[season-1][0];   
	     int end_month = array[season-1][2];   
	        
	     Date date = new Date();
	     SimpleDateFormat dateFormat   =   new SimpleDateFormat("yyyy");//可以方便地修改日期格式
	     String years  = dateFormat.format(date);
	     int years_value = Integer.parseInt(years);
	        
	     int start_days =1;//years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);   
	     int end_days = getLastDayOfMonth(years_value,end_month);   
	     String seasonDate = years_value+"-"+start_month+"-"+start_days+";"+years_value+"-"+end_month+"-"+end_days;
	     return seasonDate;   
	        
	 }   
	    
	 /**  
	  * 获取某年某月的最后一天  
	  * @param year 年  
	  * @param month 月  
	  * @return 最后一天  
	  */  
	private static int getLastDayOfMonth(int year, int month) {   
	      if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8  
	                || month == 10 || month == 12) {   
	            return 31;   
	      }   
	      if (month == 4 || month == 6 || month == 9 || month == 11) {   
	            return 30;   
	      }   
	      if (month == 2) {   
	            if (isLeapYear(year)) {   
	                return 29;   
	            } else {   
	                return 28;   
	            }   
	      }   
	      return 0;   
	}   
	/**  
	 * 是否闰年  
	 * @param year 年  
	 * @return   
	 */  
	public static boolean isLeapYear(int year) {   
	     return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);   
	}   
	
	/**
	 * 是否是同一天的时间判断，主要是正对有些时间带时分秒，有些时间不带时分秒
	 * @author XuGuo
	 * @since 2009-04-13
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isTheSameDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);  
		c2.setTime(d2);  
		return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
			&& (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
			&& (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
	}
	/**
	 * 是否是同一天的时间判断，主要是正对有些时间带时分秒，有些时间不带时分秒
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isTheSameMonth(Date d1, Date d2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);  
		c2.setTime(d2);  
		return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR))
		&& (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
	}
	/**
	 * 判断某一日期是否在在其他两个日期范围内
	 * @param fromDate
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isDateBetweenTwoDates(String fromDate, Date date1,
                                                Date date2) {
		Date date = DateUtils.StrToDate(fromDate,"yyyy-MM-dd") ;
		if(date.compareTo(date1) >= 0 && date.compareTo(date2) <= 0) {
			return true ;
		}
		return false;
	}
	
	/**
	 * 获取2个日期之间所有日期的列表
	 * @param sdate
	 * @param edate
	 * @return
	 */
	public static List<String> getDayList(String sdate, String edate){
		List<String> days = null;
		if(!StringUtil.isEmpty(sdate)&&!StringUtil.isEmpty(edate)){
			days = new ArrayList<String>();
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(DateUtils.StrToDate(sdate,"yyyy-MM-dd"));
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(DateUtils.StrToDate(edate,"yyyy-MM-dd"));
			while(cal1.compareTo(cal2)<=0){
				int year = cal1.get(Calendar.YEAR);
				int month = cal1.get(Calendar.MONTH) + 1;
				int day = cal1.get(Calendar.DAY_OF_MONTH);
				String d = year+ "-" + month + "-" + day;
				days.add(d);
				cal1.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		return days;
	}
	
	/**
	 * 获取2个日期之间的占用天数<br/>
	 * 例如:date1-2012-5-1 15:00,date2:2012-5-3 10:00,则占用天数为3天<br/>
	 */
	public static int getOccupyDays(Date date1, Date date2){
		if(date1!=null&&date2!=null){
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			int y1 = cal1.get(Calendar.YEAR);
			int y2 = cal2.get(Calendar.YEAR);
			int d1 = cal1.get(Calendar.DAY_OF_YEAR);
			int d2 = cal2.get(Calendar.DAY_OF_YEAR);
			if(y1<y2){
				cal1.set(y1, 11, 31);
				int d = cal1.get(Calendar.DAY_OF_YEAR)-d1+1;
				for(y1++;y1<y2;y1++){
					cal1.set(y1, 11, 31);
					d += cal1.get(Calendar.DAY_OF_YEAR);
				}
				d += d2;
				return d;
			}
			else if(y2<y1){
				cal2.set(y2, 11, 31);
				int d = cal2.get(Calendar.DAY_OF_YEAR)-d2+1;
				for(y2++;y2<y1;y2++){
					cal2.set(y2, 11, 31);
					d += cal2.get(Calendar.DAY_OF_YEAR);
				}
				d += d1;
				return d;
			}
			else{
				return Math.abs(d1-d2)+1;
			}
		}
		if((date1==null&&date2!=null)||(date1!=null&&date2==null)){
			return 1;
		}
		return 0;
	}
	
	/**
	 * 获取当天的起止时间 如：2012-5-1 00:00到2012-5-1 23:59
	 * @return
	 */
	public static Date[] getCurDayBeginAndEnd(){
		Date[] d = new Date[2];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		d[0] = cal.getTime();
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		d[1] = cal.getTime();
		return d;
	}	
	
	public static Date getMonLastDay(String mon) {
		Date lastday = null;
		if(mon!=null){
			Calendar cal = Calendar.getInstance();
			Date md = DateUtils.StrToDate(mon, "yyyy-MM");
			cal.setTime(md);
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			lastday = cal.getTime();
		}
		return lastday;
	}
	
	/**
	 * 获取两个日期之间的月份
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDiffMonths(Date start, Date end){
	   int iMonth = 0;
	   int flag = 0;
	   try {
		   Calendar objCalendarDate1 = Calendar.getInstance();
		   objCalendarDate1.setTime(start);	
		   Calendar objCalendarDate2 = Calendar.getInstance();
		   objCalendarDate2.setTime(end);
	
		   if (objCalendarDate2.equals(objCalendarDate1))
			   return 0;
		   if (objCalendarDate1.after(objCalendarDate2)) {
			   Calendar temp = objCalendarDate1;
			   objCalendarDate1 = objCalendarDate2;
			   objCalendarDate2 = temp;
		   }
		   if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1.get(Calendar.DAY_OF_MONTH)) flag = 1;
	
		   if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR))
			   iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR)) * 12 + objCalendarDate2.get(Calendar.MONTH) - flag) - objCalendarDate1.get(Calendar.MONTH);
		   else
			   iMonth = objCalendarDate2.get(Calendar.MONTH) - objCalendarDate1.get(Calendar.MONTH) - flag;
	   } catch (Exception e) {
		   e.printStackTrace();
	   }
	   return iMonth;
	}
	
	public static Date addMonths(Date srcDate, int addMonths){
		if(srcDate==null) return null;
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(srcDate);
    	gc.add(GregorianCalendar.MONTH, addMonths);
		return gc.getTime();
    }
	
	/**
	 * 获取每月固定日期
	 */
	public static String[]  monthDateOil(int year , int month){
		if(month<1||month>12) return null;
		String s [] = new String[2];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-2);
		c.set(Calendar.DAY_OF_MONTH, 29);
		s[0] = formatYMD(c.getTime());
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 26);
		s[1] = formatYMD(c.getTime());
		return s;
		
	}
	
	/**
	 * 2个日期所占月份数，注意日期先后不能混乱
	 * @param sdate 开始日期
	 * @param edate 结束日期
	 * @return
	 */
	public static int getMonCount(Date sdate, Date edate){
		if(sdate==null||edate==null){
			return 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdate);
		int y1 = cal.get(Calendar.YEAR);
		int m1 = cal.get(Calendar.MONTH);
		cal.setTime(edate);
		int y2 = cal.get(Calendar.YEAR);
		int m2 = cal.get(Calendar.MONTH);
		if(y2!=y1){
			m2 += 12*(y2-y1);
		}
		return m2-m1+1;
	}
	
	public static String[] getMonsStr(Date sdate, Date edate){
		if(sdate==null||edate==null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdate);
		int y1 = cal.get(Calendar.YEAR);
		int m1 = cal.get(Calendar.MONTH);
		cal.setTime(edate);
		int y2 = cal.get(Calendar.YEAR);
		int m2 = cal.get(Calendar.MONTH);
		if(y2!=y1){
			m2 += 12*(y2-y1);
		}
		int monCount = m2-m1+1;
		String[] monsArr = new String[monCount];
		int j = m1 + 1;
		for(int i=0;i<monCount;i++,j++){
			if(j<13){
				if(j<10){
					monsArr[i] = y1 + "-0" + j; 
				}
				else{
					monsArr[i] = y1 + "-" + j; 
				}
			}
			else{
				j = 1;
				y1++;
				monsArr[i] = y1 + "-0" + j; 
			}
		}
		return monsArr;
	}
	
	/**
	 * 
	 * @param mth 格式:yyyy-mm
	 * @return
	 */
	public static int getMonthDayCount(String mth){
		Date d = DateUtils.StrToDate(mth + "-01");
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
}