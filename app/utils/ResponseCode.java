/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 * 状态码常量接口
 * @author kortide
 */
public interface ResponseCode {

    /**
     * 测试用商店uuid
     */
    public static final String shop_uuid = "c207e7f2-a2dd-453a-b12f-34565fcd0952";

    public static final String S_ROOT="/home/zhaolin";
    /**
     * 0:成功
     */
    public static final int RESPONSE_OK = 0;
    /**
     * 2001:用户为空
     */
    public static final int UID_EMPTY = 2001;
    /**
     * 2002:Widget不存在
     */
    public static final int INVALID_WIDGETS = 2002;
    /**
     * 2003:背包不存在
     */
    public static final int EMPTY_BACKPACK = 2003;
    /**
     * 2004:库存为零
     */
    public static final int NUM_ZERO = 2004;
    /**
     * 2005:没有足够的库存数量
     */
    public static final int NOT_ENOUGH_WIDGETS = 2005;
    /**
     * 2006:Widget状态为绑定
     */
    public static final int ALREADY_BINDED = 2006;
    /**
     * 2007:在拍卖行
     */
    public static final int PLACE_IN = 2007;
    /**
     * 2008:不再拍卖行
     */
    public static final int NOT_IN_PLACE = 2008;
    /**
     * 2009:用户金币不足
     */
    public static final int NOT_ENOUGH_GOLD = 2009;
    /**
     * 2010:不是用户自己的widget
     */
    public static final int NOT_USERS_WIDGETS = 2010;
    /**
     * 2011:创建widget失败
     */
    public static final int CREATE_WIDGET_FAILED = 2011;
    /**
     * 2012:删除widget失败
     */
    public static final int DELETE_WIDGET_FAILED = 2012;
    /**
     * 2013:添加widget失败
     */
    public static final int ADD_WIDGET_FAILED = 2013;
    /**
     * 2014:背包已经占满
     */
    public static final int BACKPACK_FULL = 2014;
    /**
     * 2015:还没添加好友
     */
    public static final int FRIEND_NUM_ZERO = 2015;
    /**
     * 2016:文件错误
     */
    public static final int FILE_ERROR = 2016;
    /**
     * 2017:缩略图不存在
     */
    public static final int NOT_THUMP_IMAGE = 2017;
    /**
     * 2018:邮箱不存在
     */
    public static final int BOXMAIL_EMPTY = 2018;
    /**
     * 2019:不是用户的邮件
     */
    public static final int NOT_ONESELF_MAIL = 2019;
    /**
     * 2020:一个邮件最多发送7个widgets
     */
    public static final int NOT_OVER_MAIL_MAX = 2020;
    /**
     * 2021:发送邮件失败
     */
    public static final int SEND_MAIL_FALSE = 2021;
    /**
     * 2022:用户不存在
     */
    public static final int USER_NOT_EXIST = 2022;
    /**
     * 2023:所有背包已满
     */
    public static final int ALL_BACKPACK_FULL = 2023;
//    /**
//     * 2024:不是用户的背包 重复的定义同2032
//     */
//    public static final int NOT_USER_BAG = 2024;
    /**
     * 2025:接收者的背包不存在
     */
    public static final int TARGET_BAG_NOT_EXIST = 2025;
    /**
     * 2026:背包不存在
     */
    public static final int BAGS_NOT_EXIST = 2026;
    /**
     * 2027:不能发送给自己
     */
    public static final int NOT_SEND_YOUESELF = 2027;
    /**
     * 2028:一个用户最多7个背包
     */
    public static final int OVER_USER_BAG_NUM = 2028;
    /**
     * 2029:关闭的背包必须为空（不能装widgets）
     */
    public static final int CLOSE_BAG_NOT_EMPTY = 2029;
    /**
     * 2030:其它背包已满
     */
    public static final int OTHER_BACKPACKS_FULL = 2030;
    /**
     * 2031:需要绑定确认
     */
    public static final int RESPONSE_BIND = 2031;
    /**
     * 2032:不是用户的背包
     */
    public static final int NOT_USERS_BAG = 2032;
    /**
     * 2033:IMEI号码已经注册过了
     */
    public static final int USER_ALREADY_EXIST = 2033;
    /**
     * 2034:背包栏不存在
     */
    public static final int INVALID_BAGSLOT = 2034;
    /**
     * 2035:背包栏不存在
     */
    public static final int EMPTY_SHOP = 2035;
    /**
     * 2998:参数验证错误
     */
    public static final int PARAMS_VALIDATION_ERROR = 2998;
    /**
     * 2999:未知错误
     */
    public static final int OTHER_FAILURE = 2999;
    /**
     * 3000:登录错误
     */
    public static final int LOGIN_ERROR = 3000;
    /**
     * 3001:用户消失
     */
    public static final int USER_DISAPPEAR = 3001;
}
