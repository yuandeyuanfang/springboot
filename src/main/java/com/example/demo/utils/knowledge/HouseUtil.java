package com.example.demo.utils.knowledge;

import java.util.HashMap;
import java.util.Map;

/**
 * 房子相关
 */
public class HouseUtil {
    public static double rateThree = 3.3;//3年利率
    public static double rateTwo = 2.7;//2年利率
    public static double rateOne = 1.8;//1年利率
    public static double rateHalf = 3.3;//半年利率
    public static double amountAll = 100000;//贷款金额
    public static double amountMonth = 546.03;//每月还款金额(5.15利率)
    public static double amountAllMonth = 196569.44;//还款总额(5.15利率)
    public static int amountOne = 1000;//每份金额
    public static Map<Integer,Double> amountRateMap = new HashMap<Integer,Double>();//用于存放本息到期信息

    public static void main(String[] args) {
//        System.out.println((int)(22.33/4));
//        //3年定期存30年后的金额
//        amountFree();
//        //考虑每月还款的情况下怎么存钱更划算
        amountNotFree();
    }

    /**
     * 3年定期存30年后的金额（不考虑每月还款）
     * @return
     */
    public static double amountFree(){
        double amountAllTemp = amountAll;
        for (int i=1;i<=10;i++){
            amountAllTemp = amountAllTemp + amountAllTemp*rateThree*3/100;
            System.out.println(amountAll+"本金第"+i+"次后的本息合计金额"+amountAllTemp);
        }
        return amountAllTemp;
    }

    /**
     *考虑每月还款的情况下怎么存钱更划算
     */
    public static void amountNotFree(){
        double amountFree = amountAll;
        for (int i=1;i<=360;i++){
            //检查是否有到期存款
            if(amountRateMap.get(i) != null){
                amountFree = amountFree + amountRateMap.get(i);
                System.out.println("第"+i+"个月-可用金额"+amountFree+";包含存款到期金额"+amountRateMap.get(i));
            }else{
                System.out.println("第"+i+"个月-可用金额"+amountFree);
            }


            //计算满足还款的情况下3年期的存款数量
            amountFree = amountNotFreeThree(amountFree,i);
            //计算满足还款的情况下2年期的存款数量
            amountFree = amountNotFreeTwo(amountFree,i);
            //计算满足还款的情况下1年期的存款数量
            amountFree = amountNotFreeOne(amountFree,i);
            //计算满足还款的情况下半年期的存款数量
            amountFree = amountNotFreeHalf(amountFree,i);

            //扣除当月还款
            if(amountFree < amountMonth){
                System.out.println("第"+i+"个月-可用金额不足，无法还款");
                break;
            }
            amountFree = amountFree - amountMonth;
            System.out.println("第"+i+"个月-还款后可用金额"+amountFree);

        }
    }

    /**
     * 计算满足还款的情况下3年期的存款数量
     * @param amountFree 可用金额
     * @return 剩余可用金额
     */
    public static double amountNotFreeThree(double amountFree,int month){
        double amountUse = amountMonth * 12 * 3;
        if(amountFree > amountUse){
            double amountSave = (int)(amountFree - amountUse)/amountOne * amountOne;
            double amountRate = amountSave + amountSave*rateThree*3/100;
            if(amountRateMap.get(month+36) != null){
                amountRate = amountRate + amountRateMap.get(month+36);
            }
            amountRateMap.put(month+36,amountRate);
            System.out.println("第"+month+"个月存款3年定期，存款金额"+amountSave);
            return amountFree - amountSave;

        }else{
            return amountFree;
        }
    }

    /**
     * 计算满足还款的情况下2年期的存款数量
     * @param amountFree 可用金额
     * @return 剩余可用金额
     */
    public static double amountNotFreeTwo(double amountFree,int month){
        double amountUse = amountMonth * 12 * 2;
        if(amountFree > amountUse){
            double amountSave = (int)(amountFree - amountUse)/amountOne * amountOne;
            double amountRate = amountSave + amountSave*rateTwo*2/100;
            if(amountRateMap.get(month+24) != null){
                amountRate = amountRate + amountRateMap.get(month+24);
            }
            amountRateMap.put(month+24,amountRate);
            System.out.println("第"+month+"个月存款2年定期，存款金额"+amountSave);
            return amountFree - amountSave;

        }else{
            return amountFree;
        }
    }

    /**
     * 计算满足还款的情况下1年期的存款数量
     * @param amountFree 可用金额
     * @return 剩余可用金额
     */
    public static double amountNotFreeOne(double amountFree,int month){
        double amountUse = amountMonth * 12;
        if(amountFree > amountUse){
            double amountSave = (int)(amountFree - amountUse)/amountOne * amountOne;
            double amountRate = amountSave + amountSave*rateOne/100;
            if(amountRateMap.get(month+12) != null){
                amountRate = amountRate + amountRateMap.get(month+12);
            }
            amountRateMap.put(month+12,amountRate);
            System.out.println("第"+month+"个月存款1年定期，存款金额"+amountSave);
            return amountFree - amountSave;

        }else{
            return amountFree;
        }
    }

    /**
     * 计算满足还款的情况下半年期的存款数量
     * @param amountFree 可用金额
     * @return 剩余可用金额
     */
    public static double amountNotFreeHalf(double amountFree,int month){
        double amountUse = amountMonth * 6;
        if(amountFree > amountUse){
            double amountSave = (int)(amountFree - amountUse)/amountOne * amountOne;
            if(amountSave > 0){
                double amountRate = amountSave + amountSave*rateHalf/100/2;
                if(amountRateMap.get(month+6) != null){
                    amountRate = amountRate + amountRateMap.get(month+6);
                }
                amountRateMap.put(month+6,amountRate);
                System.out.println("第"+month+"个月存款半年定期，存款金额"+amountSave);
            }
            return amountFree - amountSave;

        }else{
            return amountFree;
        }
    }

}
