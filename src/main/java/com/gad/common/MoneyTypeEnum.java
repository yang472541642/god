package com.gad.common;

/**
 * @Filename MoneyTypeEnum.java
 * @Description
 * @Version 1.0
 * @Author zhongc
 * @Email zhong_ch@foxmail.com
 * @History <li>Author: zhongc</li>
 * <li>Date: 2017/11/27</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
public enum MoneyTypeEnum {

    YUAN("2", "100", "YUAN"),
    JIAO("0.2", "10", "JIAO"),
    FEN("0.02", "1", "FEN");

    private String money;

    private String multiple;

    private String string;

    /**
     * 根据倍数获取
     *
     * @param multiple
     */
    public static MoneyTypeEnum getByMultiple(String multiple) {
        MoneyTypeEnum[] values = MoneyTypeEnum.values();
        for (MoneyTypeEnum moneyTypeEnum : values) {
            if (moneyTypeEnum.getMultiple().equals(multiple)) {
                return moneyTypeEnum;
            }
        }
        return null;
    }

    private MoneyTypeEnum(String money, String multiple, String string) {
        this.money = money;
        this.multiple = multiple;
        this.string = string;
    }

    public static MoneyTypeEnum valueOfByMultiple(String multiple) {
        for (MoneyTypeEnum moneyTypeEnum : MoneyTypeEnum.values()) {
            if (moneyTypeEnum.getMultiple().equals(multiple)) {
                return moneyTypeEnum;
            }
        }
        return null;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
