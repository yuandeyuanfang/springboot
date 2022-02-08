package com.example.demo.utils.knowledge;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 房子相关
 */
public class HouseUtil {
    public static double rateThirty = 4.65;//贷款利率
    public static double rateThree = 3.3;//3年利率
    public static double rateTwo = 2.7;//2年利率
    public static double rateOne = 2.0;//1年利率
    public static double rateHalf = 2.0;//半年利率
    public static double rateMonth = 2.0;//月利率（余额宝）
    public static double amountAll = 100000;//贷款金额
    public static double amountMonth = 0;//每月还款金额
    public static double amountAllMonth = 0;//还款总额
    public static int amountOne = 1000;//每份理财金额
    public static Map<Integer,Double> amountRateMap = new HashMap<Integer,Double>();//用于存放本息到期信息

    public static void main(String[] args) {
        //计算每月还款金额
        double invest = amountAll; // 本金
        int periodNum = 360;//期数
        double periodRate = rateThirty/100/12; // 每期利率
        System.out.println("等额本息---本金：" + invest);
        double perMonthPrincipalInterest = getPerPeriodPrincipalInterest(invest, periodRate, periodNum);
        System.out.println("等额本息---每期还款本息：" + perMonthPrincipalInterest);
//        Map<Integer, BigDecimal> mapInterest = getPerPeriodInterest(invest, periodRate, periodNum);
//        System.out.println("等额本息---每期还款利息：" + mapInterest);
//        Map<Integer, BigDecimal> mapPrincipal = getPerPeriodPrincipal(invest, periodRate, periodNum);
//        System.out.println("等额本息---每期还款本金：" + mapPrincipal);
//        double count = getInterestCount(invest, periodRate, periodNum);
//        System.out.println("等额本息---总利息：" + count);
        double principalInterestCount = getPrincipalInterestCount(invest, periodRate, periodNum);
        System.out.println("等额本息---应还本息总和：" + principalInterestCount);

        amountMonth = perMonthPrincipalInterest;
        amountAllMonth = principalInterestCount;

        //System.out.println((int)(22.33/4));
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
        for (int i=1;i<=460;i++){
            //检查是否有到期存款
            if(amountRateMap.get(i) != null){
                amountFree = amountFree + amountRateMap.get(i);
                System.out.println("第"+i+"个月-可用金额"+amountFree+";包含存款到期金额"+amountRateMap.get(i));
            }else{
                if(i <= 360){
                    System.out.println("第"+i+"个月-可用金额"+amountFree);
                }
            }

            if(i <= 360){
                //计算满足还款的情况下3年期的存款数量
                amountFree = amountNotFreeThree(amountFree,i);
                //计算满足还款的情况下2年期的存款数量
                amountFree = amountNotFreeTwo(amountFree,i);
                //计算满足还款的情况下1年期的存款数量
                amountFree = amountNotFreeOne(amountFree,i);
                //计算满足还款的情况下半年期的存款数量
                amountFree = amountNotFreeHalf(amountFree,i);
                //计算满足还款的情况下一月期的存款数量
                amountFree = amountNotFreeMonth(amountFree,i);

                //扣除当月还款
                if(amountFree < amountMonth){
                    System.out.println("第"+i+"个月-可用金额不足，无法还款");
                    break;
                }
                amountFree = amountFree - amountMonth;
                System.out.println("第"+i+"个月-还款后可用金额"+amountFree);
            }

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

    /**
     * 计算满足还款的情况下一月期的存款数量
     * @param amountFree 可用金额
     * @return 剩余可用金额
     */
    public static double amountNotFreeMonth(double amountFree,int month){
        double amountUse = amountMonth * 1;
        if(amountFree > amountUse){
            double amountSave = (int)(amountFree - amountUse)/amountOne * amountOne;
            if(amountSave > 0){
                double amountRate = amountSave + amountSave*rateMonth/100/12;
                if(amountRateMap.get(month+1) != null){
                    amountRate = amountRate + amountRateMap.get(month+1);
                }
                amountRateMap.put(month+1,amountRate);
                System.out.println("第"+month+"个月存款一月定期，存款金额"+amountSave);
            }
            return amountFree - amountSave;

        }else{
            return amountFree;
        }
    }

    /**
     * 每期本金+利息
     * 公式：每月偿还本息=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1)
     * @param invest
     *            本金
     * @param periodRate
     *            每期利率
     * @param periodMonthNum
     *            期数
     * @return
     */
    public static double getPerPeriodPrincipalInterest(double invest, double periodRate, int periodMonthNum) {
        BigDecimal monthIncome = new BigDecimal(invest)
                .multiply(new BigDecimal(periodRate * Math.pow(1 + periodRate, periodMonthNum)))
                .divide(new BigDecimal(Math.pow(1 + periodRate, periodMonthNum) - 1), 4, BigDecimal.ROUND_HALF_UP);
        return monthIncome.doubleValue();
    }

    /**
     * 每期利息
     * 公式：每月偿还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
     * @param invest
     *            本金
     * @param periodRate
     *            每期利率
     * @param periodMonthNum
     *            期数
     * @return
     */
    public static Map<Integer, BigDecimal> getPerPeriodInterest(double invest, double periodRate, int periodMonthNum) {
        Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
        BigDecimal periodInterest;
        for (int i = 1; i < periodMonthNum + 1; i++) {
            BigDecimal multiply = new BigDecimal(invest).multiply(new BigDecimal(periodRate));
            BigDecimal sub = new BigDecimal(Math.pow(1 + periodRate, periodMonthNum))
                    .subtract(new BigDecimal(Math.pow(1 + periodRate, i - 1)));
            periodInterest = multiply.multiply(sub).divide(new BigDecimal(Math.pow(1 + periodRate, periodMonthNum) - 1),
                    6, BigDecimal.ROUND_HALF_UP);
            periodInterest = periodInterest.setScale(4, BigDecimal.ROUND_HALF_UP);
            map.put(i, periodInterest);
        }
        return map;
    }

    /**
     * 每期还款本金
     *
     * @param invest
     *            本金
     * @param periodRate
     *            每期利率
     * @param periodNum
     *            期数
     * @return
     */
    public static Map<Integer, BigDecimal> getPerPeriodPrincipal(double invest, double periodRate, int periodNum) {
        BigDecimal monthIncome = new BigDecimal(invest)
                .multiply(new BigDecimal(periodRate * Math.pow(1 + periodRate, periodNum)))
                .divide(new BigDecimal(Math.pow(1 + periodRate, periodNum) - 1), 4, BigDecimal.ROUND_HALF_UP);
        Map<Integer, BigDecimal> mapInterest = getPerPeriodInterest(invest, periodRate, periodNum);
        Map<Integer, BigDecimal> mapPrincipal = new HashMap<Integer, BigDecimal>();

        for (Map.Entry<Integer, BigDecimal> entry : mapInterest.entrySet()) {
            mapPrincipal.put(entry.getKey(), monthIncome.subtract(entry.getValue()));
        }
        return mapPrincipal;
    }

    /**
     * 总利息
     *
     * @param invest
     *            本金
     * @param periodRate
     *            每期利率
     * @param periodNum
     *            期数
     * @return
     */
    public static double getInterestCount(double invest, double periodRate, int periodNum) {
        BigDecimal count = new BigDecimal(0);
        Map<Integer, BigDecimal> mapInterest = getPerPeriodInterest(invest, periodRate, periodNum);

        for (Map.Entry<Integer, BigDecimal> entry : mapInterest.entrySet()) {
            count = count.add(entry.getValue());
        }
        return count.doubleValue();
    }

    /**
     * 本息总和
     *
     * @param invest
     *            本金
     * @param periodRate
     *            每期利率
     * @param periodNum
     *            期数
     * @return
     */
    public static double getPrincipalInterestCount(double invest, double periodRate, int periodNum) {
        BigDecimal perMonthInterest = new BigDecimal(invest);
        Map<Integer, BigDecimal> mapInterest = getPerPeriodInterest(invest, periodRate, periodNum);
        for (Map.Entry<Integer, BigDecimal> entry : mapInterest.entrySet()) {
            perMonthInterest = perMonthInterest.add(entry.getValue());
        }
        return perMonthInterest.doubleValue();
    }

    /**
     * 每期还款日期
     * @param start_date
     * 				起租日
     * @param perPeriodMonthNum
     * 				每期月数
     * @param periodNum
     * 				期数
     * @return
     */
    public static Map<Integer, String> getRepaymentDate(String start_date, int perPeriodMonthNum, int periodNum) {
        Map<Integer, String> periodRepaymentDate = new HashMap<Integer, String>();
        String nextRepaymentDate = start_date;
        periodRepaymentDate.put(1, nextRepaymentDate);
        for (int i = 2; i < periodNum + 1; i++) {
            nextRepaymentDate = getMonthAdd(perPeriodMonthNum, nextRepaymentDate, "yyyyMMdd");
            periodRepaymentDate.put(i, nextRepaymentDate);
        }
        return periodRepaymentDate;
    }

    /**
     * 功能描述：返回指定日期加上多少月之后的时间<BR>
     * @param from   yyyyMMdd
     * @param day
     * @param formatStr
     * @return
     */
    public static String getMonthAdd(int day,String from,String formatStr){
        SimpleDateFormat sdf=new SimpleDateFormat(formatStr);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(from));
        } catch (Exception e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MONTH, day);
        String date = sdf.format(calendar.getTime());
        return date;
    }

}
