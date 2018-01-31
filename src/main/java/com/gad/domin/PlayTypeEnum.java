package com.gad.domin;

/**
 * 购买方案
 *
 * @author zhongchao
 * @title PlayTypeEnum.java
 * @Date 2017-12-01
 * @since v1.0.0
 */
public enum PlayTypeEnum {
    /**
     * 定位胆 个位
     */
    POSITION_ONE(1, "定位胆/定位胆/复式/_,_,_,_,%s/", "0.1958"),
    /**
     * 定位胆 十位
     */
    POSITION_TEN(2, "定位胆/定位胆/复式/_,_,_,%s,_/", "0.1958"),

    /**
     * 定位胆 百位
     */
    POSITION_HUNDRED(3, "定位胆/定位胆/复式/_,_,%s,_,_/", "0.1958"),

    /**
     * 定位胆 千位
     */
    POSITION_THOUSAND(4, "定位胆/定位胆/复式/_,%s,_,_,_/", "0.1958"),

    /**
     * 定位胆 万位
     */
    POSITION_WAN(5, "定位胆/定位胆/复式/%s,_,_,_,_/", "0.1958"),

    /**
     * 前二/直选/复式
     */
    POSITION_DIRECT_FIRST_TOW(6, "前二/直选/复式/%s,%s,_,_,_/", "1.9580"),

    /**
     * 后二/直选/复式
     */
    POSITION_DIRECT_LAST_TOW(7, "后二/直选/复式/_,_,_,%s,%s/", "1.9580"),

    /**
     * 后三/直选/复式
     */
    POSITION_DIRECT_LAST_THREE(8, "后三/直选/复式/_,_,%s,%s,%s/", "19.5800"),

    /**
     * 前三/直选/复式
     */
    POSITION_DIRECT_FIRST_THREE(9, "前三/直选/复式/%s,%s,%s,_,_/", "19.5800"),

    /**
     * 前四/直选/复式
     */
    POSITION_DIRECT_FIRST_FOUR(10, "前四/直选/复式/%s,%s,%s,%s,_/", "195.8000"),

    /**
     * 前四/直选/复式
     */
    POSITION_DIRECT_LAST_FOUR(11, "前四/直选/复式/_,%s,%s,%s,%s", "195.800"),

    /**
     * 五星独选/直选/复式
     */
    POSITION_DIRECT_FIVE(12, "前四/直选/复式/%s,%s,%s,%s,%s", "1950.800");

    /**
     * ID
     */
    private int key;

    /**
     * 文字
     */
    private String value;

    /**
     * 所挣金额 以分为单位！
     */
    private String money;

    private PlayTypeEnum(int key, String value, String money) {
        this.key = key;
        this.value = value;
        this.money = money;
    }


    public static PlayTypeEnum getByKey(int key) {
        PlayTypeEnum playTypeEnum = null;
        for (PlayTypeEnum type : PlayTypeEnum.values()) {
            if (type.getKey() == key) {
                return type;
            }
        }
        return playTypeEnum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
