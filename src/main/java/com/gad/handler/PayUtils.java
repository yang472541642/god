package com.gad.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gad.common.Constant;
import com.gad.common.HttpHelper;
import com.gad.common.MoneyTypeEnum;
import com.gad.domin.AutoPay;
import com.gad.domin.PlayTypeEnum;
import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gad.common.Constant.payUrl;
import static com.gad.common.Constant.ssc_host;

/**
 * @author zhongchao
 * @title PayUtils.java
 * @Date 2017-11-30
 * @since v1.0.0
 */
public class PayUtils {
    private static final Logger logger = LoggerFactory.getLogger(AutoBuyHandler.class);

    private String cqssCokie;

    private AutoPay autoPay;

    public PayUtils(AutoPay autoPay, String cqssCokie) {
        this.autoPay = autoPay;
        this.cqssCokie = cqssCokie;
    }


    private String genData(String number) {
        logger.info("进入构建数据");
        logger.info("autpay的数据是：{}", JSON.toJSONString(autoPay));
        try {
            MoneyTypeEnum moneyType = autoPay.getMoneyTypeEnum();
            PlayTypeEnum payType = autoPay.getPlayTypeEnum();
            // 保留三位小数
            DecimalFormat df2 = new DecimalFormat("#0.000");
    
            String[] split = number.split(Constant.NUM_SPLIT);
            String data = String.format(payType.getValue(), split);
    
            BigDecimal oneMoney = BigDecimal.valueOf(Double.valueOf(moneyType.getMoney()));
            // 注数
            int numberCount = 1;
            for (String aSplit : split) {
                numberCount = numberCount * getNumberCount(aSplit);
            }
            String win = df2.format(BigDecimal.valueOf(Double.valueOf(payType.getMoney()))
                    .multiply(BigDecimal.valueOf(Double.valueOf(moneyType.getMultiple()))));
    
    
            // 加倍
            BigDecimal pow = BigDecimal.valueOf(autoPay.getPlayOdds()).pow(autoPay.getCount() - 1).multiply(BigDecimal.valueOf(10));
            String payMoney = df2.format(oneMoney.multiply(BigDecimal.valueOf(numberCount)).multiply(BigDecimal.valueOf(pow.intValue())));
            autoPay.setPayMoney(new BigDecimal(payMoney));
            autoPay.setWinMoney(BigDecimal.valueOf(Double.valueOf(win)).multiply(pow));
            data += moneyType.getString() + "/" + moneyType.getMoney() + "/" + win + "/;" + payMoney + ";" + numberCount + ";" + pow.intValue();
            logger.info("购买参数：{}", data);
            return data;
        } catch (Exception e) {
            logger.error("构建数据报错了：{}", e);
        }
        return null;
        
    }
    
    private int getNumberCount(String number) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(number);
        return m.replaceAll("").trim().length();

    }

    public static void main(String[] args) {
        DecimalFormat df2 = new DecimalFormat("#0.000");

        String win = df2.format(BigDecimal.valueOf(1.9520)
                .multiply(BigDecimal.valueOf(10)));

        BigDecimal oneMoney = new BigDecimal("0.2");
        String payMoney = df2.format(oneMoney.multiply(BigDecimal.valueOf(64)).multiply(BigDecimal.valueOf(2)));
        String winMoney = df2.format(BigDecimal.valueOf(Double.valueOf(win)).multiply(BigDecimal.valueOf(2)));

        System.out.println(payMoney);
        System.out.println(winMoney);
    }

    /**
     * 投注
     */
    public String autoPay() throws IOException {
        logger.info("进入autopay");
        logger.info("ssccookie={}", cqssCokie);
        String number = autoPay.getNumbers();
        // 获取期数号码
        HttpHelper httpHelper = HttpHelper.create()
                .get(ssc_host)
                .addHeader("Cookie", cqssCokie).execute();
        Document parse = Jsoup.parse(httpHelper.getResponseContent());
        Element nextStrEle = parse.getElementById("nextPeriodStr");
        if (nextStrEle == null) {
            throw new RuntimeException("获取期数失败");
        }
        String nextStr = nextStrEle.text().trim();
        autoPay.setNper(nextStr);
        logger.info("获取的期数：{}", nextStr);

        Map<String, String> autoPayParams = Maps.newLinkedHashMap();

        // 定位胆 复式
        autoPayParams.put("sscType", "cqssc");
        autoPayParams.put("period", nextStr);
        autoPayParams.put("data", genData(number));
        autoPayParams.put("isShare", "1");
        String tryurl = "http://www.cscaipiao.com/lottery/loadSSCNextPeriod.html?lotteryType=cqssc&_=" + System.currentTimeMillis();
        String first = HttpHelper.create()
                .get(tryurl)
                .addHeader("Accept-Encoding", "gzip, deflate,sdch")
                .addHeader("Cookie", cqssCokie)
                .execute().getResponseContent();
        String result = HttpHelper.create()
                .post(payUrl)
                .addHeader("Cookie", cqssCokie)
                .addHeader("Referer", "http://cscaipiao.com/lottery/ssc/cqssc.html")
                .addHeader("Origin", "http://cscaipiao.com")
                .addHeader("Host", "cscaipiao.com")
                .addHeader("DNT", "1")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addParams(autoPayParams)
                .execute().getResponseContent();
        JSONObject jsonObject = JSON.parseObject(result);
        return (String) jsonObject.get("msg");
    }
}
