package com.app.utils;


import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.app.utils.renew.RenewCountDownSys;
  
public class CountDownUtils {  
	
    
    private static long day = 0;  
    private static long hour = 0;  
    private static long minute = 0;  
    private static long second = 0;  
    private static long totalSeconds = 60 * 60 * 24 * 1;  
    //private static long totalSeconds = 60*2;  
    
    private static long totalMinutes = 0;  //系统总运行时间（分钟）
    private static long currentRunTime = 0; //当前系统运行时间(秒)
    private static long count = 0; //每次系统运行保存时间的次数（次）
  
    private static boolean dayNotAlready = false;  
    private static boolean hourNotAlready = false;  
    private static boolean minuteNotAlready = false;  
    private static boolean secondNotAlready = false;  
    
    /**
     * 用于服务器计时减少对服务器时间的依赖。
     * 倒计时与服务器时间一致（相加等于24小时），每次倒数结束重新倒计时24小时。
     * 每半小时记录一次信息的数据库。
     * 每次倒计时结束（也就是每天0时）做一次续费检查。
     */
    public static void countDown(){
    	//从服务器获取是否是首次启动服务，如果不是，查找上次计时时间。
    	//如果是首次启动，获取当前时间作为计时开始。
    	Date date = DateUtils.addDays(new Date(), 1);
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MINUTE,0);
		date = cal.getTime();
    	long pastSecond = DateUtils.pastSecond(date);  //获取距离0点的秒数
    	//pastSecond = 100;
    	if(pastSecond < 0){
    		pastSecond = -pastSecond;
    	}
    	if(pastSecond < 10){//距离0点小于10秒，看作是已经到达0点，防止程序运行时间过长，没有启动线程
    		initData(totalSeconds);
    	}else{
    		//否则初始化为程序启动时间到0点时间为初始时间。使得每次都是以服务器的0点执行相关操作。
    		initData(pastSecond);
    	}
    	
    	//logger.info("开始计时！当前时间：" + DateUtils.getDateTime());
    	//logger.info("开始计时！当前计时：" + day + ":" + hour + ":" + minute + ":" + second);
    	//System.out.println("开始计时！当前计时：" + day + ":" + hour + ":" + minute + ":" + second);
    	
        new Timer().schedule(new TimerTask() {
            public void run() {  
                if (secondNotAlready) {  
                    startCount();  
                     //System.out.println("当前计时：" + day + ":" + hour + ":" + minute + ":" + second + ":" + currentRunTime);
                    //每隔半小时记录一次计时时间以及服务器时间，防止因为服务器停机造成计时出现比较大误差
                    if((minute == 30 || minute == 0) && second == 0){
                    	//开启新线程，减少程序执行造成时间误差
                    	new RecordCountDownThread().start();
                    }
                } else {  
                    //cancel();  
                	initData(totalSeconds);
                	//每日0点，执行一次检查。需要检查是否有停机，如果停机需要加上停机的时间。
                	//开启新线程，减少程序执行造成时间误差
                    new CheckOfficeRenewThread().start();
                }  
  
            }  
        }, 0, 1000);  
    }
    
    /**
     * 记录计时线程
     * @author carl
     *
     */
	public static class RecordCountDownThread extends Thread{
		@Override
		public void run() {
			//System.out.println("当前计时：" + day + ":" + hour + ":" + minute + ":" + second);
        	System.out.println("---------------半小时---------------");
        	RenewCountDownSys renewCountDownSys = new RenewCountDownSys();
        	//判断是否是项目刚启动,如果是，则需要从数据库查询历史信息。
        	if(totalMinutes == 0 || currentRunTime == 0 || count == 0){
        		renewCountDownSys = null;
        		if(renewCountDownSys != null){
        			//系统不是第一次部署（启动）
        			//获取停机的时间，防止计时出现较大误差。
        			Date oldSysDate = renewCountDownSys.getSysDate();
        			Long pastMinutes =  DateUtils.pastMinutes(oldSysDate); 
        			if(pastMinutes > 0){
        				totalMinutes = pastMinutes + renewCountDownSys.getTotalMinutes();
        			}
        		}else{
        			//第一次部署
        			totalMinutes = currentRunTime/60;
        		}
        	}else{
        		totalMinutes = totalMinutes + 30;
        	}
        	//System.out.println("==================RecordCountDownThread totalMinutes:" + totalMinutes);
        	if(renewCountDownSys == null){
        		renewCountDownSys = new RenewCountDownSys();
        	}
        	//renewCountDownSys.setId(null);
        	count = count + 1;
        	renewCountDownSys.setCount(count);
        	renewCountDownSys.setTotalMinutes(totalMinutes);
    		renewCountDownSys.setCountDownTime(hour + ":" + minute + ":" + second);
			renewCountDownSys.setCurrentRunTime(currentRunTime);
			renewCountDownSys.setHour(hour);
			renewCountDownSys.setMinutes(minute);
			renewCountDownSys.setSecond(second);
			renewCountDownSys.setSysDate(new Date());
			
			//renewCountDownSysService.save(renewCountDownSys);
		}
	}
	
    
    /**
     * 检查公司续费线程
     * @author carl
     *
     */
	public static class CheckOfficeRenewThread extends Thread{
		@Override
		public void run() {
			Boolean isOk = true;
			int oneDateL = 24*60 + 30; //允许时差在30分钟内
			int oneDateS = 24*60 - 30; //
			Date currentDate = new Date();
		}
	}
	
    /** 
     * 初始化赋值 
     *  
     * @param totalSeconds 
     */  
    private static void initData(long totalSeconds) {  
        resetData();  
  
        if (totalSeconds > 0) {  
            secondNotAlready = true;  
            second = totalSeconds;  
            if (second >= 60) {  
                minuteNotAlready = true;  
                minute = second / 60;  
                second = second % 60;  
                if (minute >= 60) {  
                    hourNotAlready = true;  
                    hour = minute / 60;  
                    minute = minute % 60;  
                    if (hour > 24) {  
                        dayNotAlready = true;  
                        day = hour / 24;  
                        hour = hour % 24;  
                    }  
                }  
            }  
        }  
  
        //System.out.println("初始格式化后——>" + day + "天" + hour + "小时" + minute + "分钟" + second + "秒");  
    }  
  
    private static void resetData() {  
        day = 0;  
        hour = 0;  
        minute = 0;  
        second = 0;  
        dayNotAlready = false;  
        hourNotAlready = false;  
        minuteNotAlready = false;  
        secondNotAlready = false;  
    }  
  
    /** 
     * 计算各个值的变动 
     *  
     */  
    private static void startCount() {  
        if (secondNotAlready) {  
            if (second > 0) {  
                second--;  
                currentRunTime ++;
                if (second == 0 && !minuteNotAlready) {  
                    secondNotAlready = false;  
                }  
            } else {  
                if (minuteNotAlready) {  
                    if (minute > 0) {  
                        minute--;  
                        second = 59;  
                        if (minute == 0 && !hourNotAlready) {  
                            minuteNotAlready = false;  
                        }  
  
                    } else {  
                        if (hourNotAlready) {  
                            if (hour > 0) {  
                                hour--;  
                                minute = 59;  
                                second = 59;  
                                if (hour == 0 && !dayNotAlready) {  
                                    hourNotAlready = false;  
                                }  
  
                            } else {  
                                if (dayNotAlready) {  
                                    day--;  
                                    hour = 23;  
                                    minute = 59;  
                                    second = 59;  
                                    if (day == 0) {  
                                        dayNotAlready = false;  
                                    }  
  
                                }  
                            }  
                        }  
                    }  
  
                }  
            }  
  
        }  
  
        //System.out.println("距离截止日期还有——>" + day + "天" + hour + "小时" + minute + "分钟" + second + "秒");  
    }  
  
    
    
/*    public static void main(String[] args) {  
        //从服务器获取是否是首次启动服务，如果不是，查找上次计时时间。
    	
    	//如果是首次启动，获取当前时间作为计时开始。
    	Date date = DateUtils.addDays(new Date(), 1);
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MINUTE,0);
		date = cal.getTime();
    	long pastSecond = DateUtils.pastSecond(date);  //获取距离0点的秒数
    	if(pastSecond < 0){
    		pastSecond = -pastSecond;
    	}
    	if(pastSecond < 10){//距离0点小于10秒，看作是已经到达0点，防止程序运行时间过长，没有启动线程
    		initData(totalSeconds);
    	}else{
    		//否则初始化为程序启动时间到0点时间为初始时间。使得每次都是以服务器的0点执行相关操作。
    		initData(pastSecond);
    	}
    	
    	initData(totalSeconds);
  
        new Timer().schedule(new TimerTask() {  
            public void run() {  
                if (secondNotAlready) {  
                    startCount();  
                    //System.out.println(day + ":" + hour + ":" + minute + ":" + second);
                    System.out.println(day + ":" + hour + ":" + minute + ":" + second + ":" + currentRunTime);
                    //每隔半小时记录一次计时时间以及服务器时间，防止因为服务器停机造成计时出现比较大误差
                    //需要避开0点
                    if((minute == 0 || minute == 1)&& second == 0){
                    	System.out.println(day + ":" + hour + ":" + minute + ":" + second);
                    	System.out.println("---------------半小时---------------");
                    }
                } else {  
                    //cancel();  
                	System.out.println("0点----");
                	System.out.println(day + ":" + hour + ":" + minute + ":" + second + ":" + currentRunTime);
                	initData(totalSeconds);
                	//每日0点，执行一次检查。
                }  
  
            }  
        }, 0, 1000);  
    } */
}  

