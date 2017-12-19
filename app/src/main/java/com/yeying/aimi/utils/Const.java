package com.yeying.aimi.utils;

/**
 * 常量
 *
 * @author weixiang.qin
 */
public interface Const {

    public static final String SystemTranscode = "send10321,send10318,sendSystem,prize12502,welcomebar,book,order,send30002,send30010,send30011";
    public static final String superPhone = "15811522433,18611066888,18513906663,13520653116,18518253553,17780805209,18011210012,18812310505,18376345033,13668007761";

    /******************** 分隔符 ********************/
    /**
     * 逗号
     */
    public static final String SEPARATOR_COMMA = ",";
    /**
     * 警号
     */
    public static final String SEPARATOR_WARN = "#";
    /**
     * 冒号
     */
    public static final String SEPARATOR_COLON = ":";
    /**
     * 空格
     */
    public static final String SEPARATOR_SPACE = " ";
    /**
     * 空字符
     */
    public static final String SEPARATOR_EMPTY = "";
    /**
     * 分号
     */
    public static final String SEPARATOR_SEMICOLON = ";";
    /**
     * 横线
     */
    public static final String SEPARATOR_SIDE_LINE = "-";
    /**
     * 加号
     */
    public static final String SEPARATOR_ADD = "+";
    /**
     * 星号
     */
    public static final String SEPARATOR_STAR = "*";
    /**
     * 转义星号
     */
    public static final String SEPARATOR_SPLIT_STAR = "\\*";
    /**
     * 换行
     */
    public static final String SEPARATOR_ENTER = "\n";
    /**
     * 竖线
     */
    public static final String SEPARATOR_LINE = "|";
    /**
     * 转义竖线
     */
    public static final String SEPARATOR_SPLIT_LINE = "\\|";

    /******************** 彩种 ********************/
    /**
     * 彩种
     */
    public static final String LOTTERY_TYPE = "lotteryId";
    /**
     * 玩法
     */
    public static final String PLAY_TYPE = "playType";
    /**
     * 期号
     */
    public static final String LOTTERY_ISSUENO = "issueNo";
    /**
     * 开奖时间
     */
    public static final String LOTTERY_PRIZE_TIME = "prizeTime";
    /**
     * 开奖号码
     */
    public static final String LOTTERY_PRIZENO = "prizeNo";
    /**
     * 投注号码
     */
    public static final String LOTTERY_BALLS = "lottery_balls";

    /**
     * 每期机选返回key
     */
    public static final String LOTTERY_AUTO = "lottery_auto";
    /**
     * 订单号  用于支付
     */
    public static final String ORDER_ID = "parentOrderId";
    /**
     * 订单号
     */
    public static final String ORDER_SUBID = "orderId";
    /**
     * 订单类型
     */
    public static final String ORDER_TYPE = "orderType";

    /**
     * 开奖状态
     */
    public static final String PRIZE_TYPE = "prizeType";
    /**
     * 所有订单
     */
    public static final String ORDER_ALL = "1,3,6,7,15,14,13,16,5";

	/*投注记录里的订单类型*/
    /**
     * 所有追号订单
     */
    public static final String ORDER_FLOW_ALL = "3,15,14,13,16";


    /**
     * 继续支付订单
     */
    public static final String ORDER_NO_PAY = "100";
    /**
     * 直投订单
     */
    public static final String ORDER_STAKE = "1";
    /**
     * 正常追号订单
     */
    public static final String ORDER_FLOW = "3";

    /**
     * 套餐订单
     */
    public static final String ORDER_SUITE = "6";

    /**
     * 领彩订单
     */
    public static final String ORDER_LOTTERY = "7";
    /**
     * 中奖订单
     */
    public static final String ORDER_PRIZE = "2,3";

    /**
     * 高级追号
     */
    public static final String ORDER_FLOW_HIGH = "13";
    /**
     * 超级追号
     */
    public static final String ORDER_FLOW_SUPER = "14";

    /**
     * 高级预约追号
     */
    public static final String ORDER_FLOW_HIGH_FETURE = "15";
    /**
     * 超级预约追号
     */
    public static final String ORDER_FLOW_SUPER_FETURE = "16";
    /**
     * 福彩创建合买
     */
    public static final String ORDER_TYPE_UNION_WELFARE = "4";
    /**
     * 福彩合买类型
     */
    public static final String ORDER_TYPE_UNION_JOIN_WELFARE = "5";

//	/**
//	 * 预约追号订单
//	 */
//	public static final String ORDER_APPOINTMENT = "4";


//	

    /**
     * 双色球
     */
    public static final String LOTTERY_SSQ = "SSQ";
    /**
     * 七乐彩
     */
    public static final String LOTTERY_QLC = "QLC";
    /**
     * 3D
     */
    public static final String LOTTERY_D3 = "D3";
    /**
     * 快三
     */
    public static final String LOTTERY_K3 = "HBP3";

    /**
     * 快乐十分
     */
    public static final String LOTTERY_KLSF = "TJKL10";

    /******************** 交易限制 ********************/
    /**
     * 最大交易金额
     */
    public static final int MAX_TRADE_MONEY = 10000;

    /******************** 双色球玩法 ********************/
    /**
     * 单式
     */
    public static final String LOTTERY_SSQ_DS = "0";
    /**
     * 复式
     */
    public static final String LOTTERY_SSQ_FS = "1";
    /**
     * 胆拖
     */
    public static final String LOTTERY_SSQ_DT = "2";
    /**
     * 每期自选
     */
    public static final String LOTTERY_SSQ_ZX = "28";
    /**
     * 每期机选
     */
    public static final String LOTTERY_SSQ_JX = "29";

    /******************** 七乐彩玩法 ********************/
    /**
     * 单式
     */
    public static final String LOTTERY_QLC_DS = "0";
    /**
     * 复式
     */
    public static final String LOTTERY_QLC_FS = "1";

    /******************** 3D玩法 ********************/
    /**
     * 直选单式
     */
    public static final String LOTTERY_D3_D3DS = "0";
    /**
     * 直选复式
     */
    public static final String LOTTERY_D3_D3FS = "6";
    /**
     * 组三单式
     */
    public static final String LOTTERY_D3_Z3DS = "5";
    /**
     * 组三单式或组六单式
     */
    public static final String LOTTERY_D3_Z3Z6DS = "5";
    /**
     * 组三复式
     */
    public static final String LOTTERY_D3_Z3FS = "7";
    /**
     * 组六复式
     */
    public static final String LOTTERY_D3_Z6FS = "8";
    /**
     * /** 组六单式
     */
    public static final String LOTTERY_D3_Z6DS = "9";

    /******************** 快三玩法 ********************/
    /**
     * 快三和值
     */
    public static final String LOTTERY_K3_SUM = "30";
    /**
     * 二同号复选
     */
    public static final String LOTTERY_K3_TWO_FS = "33";
    /**
     * 三同号通选
     */
    public static final String LOTTERY_K3_THREE_TX = "31";
    /**
     * 二同号单选
     */
    public static final String LOTTERY_K3_TWO_DS = "34";
    /**
     * 三同号单选
     */
    public static final String LOTTERY_K3_THREE_DS = "32";
    /**
     * 二不同号
     */
    public static final String LOTTERY_K3_TWO_BT = "36";
    /**
     * 三不同号
     */
    public static final String LOTTERY_K3_THREE_BT = "35";
    /**
     * 三连号通选
     */
    public static final String LOTTERY_K3_THREE_LH = "37";


    /*************************** 快乐十分遗漏查询类型***********************************************/
    public static final String OMIT_KLSF_ONE_NORMAL = "BALL1_OMIT";
    public static final String OMIT_KLSF_ONE_RED = "BALL1_RED_OMIT";
    public static final String OMIT_KLSF_TWO_FREE = "K2_OMIT";
    public static final String OMIT_KLSF_THREE_FREE = "K3_OMIT";

    /******************** 快乐十分界面选择Tag ********************/
    /**
     * 首位数投
     */
    public static final String LOTTERY_KLSF_ONE_NORMAL = "0";
    /**
     * 首位红投
     */
    public static final String LOTTERY_KLSF_ONE_RED = "1";
    /**
     * 任选二
     */
    public static final String LOTTERY_KLSF_TWO_FREE = "2";
    /**
     * 选二连直
     */
    public static final String LOTTERY_KLSF_TWO_CONTINUOUS = "3";
    /**
     * 选二连组
     */
    public static final String LOTTERY_KLSF_TWO_CONTINUOUS_FREE = "4";
    /**
     * 任选三
     */
    public static final String LOTTERY_KLSF_THREE_FREE = "5";
    /**
     * 选三前组
     */
    public static final String LOTTERY_KLSF_FRONT_THREE_FREE = "6";
    /**
     * 选三前直
     */
    public static final String LOTTERY_KLSF_FRONT_THREE_CONTINUOUS = "7";
    /**
     * 任选四
     */
    public static final String LOTTERY_KLSF_FOUR = "8";
    /**
     * 任选五
     */
    public static final String LOTTERY_KLSF_FIVE = "9";
    /**
     * 胆拖：前三组
     */
    public static final String KLSF_TAG_BT_FTG = "10";
    /**
     * 胆拖：快乐三
     */
    public static final String KLSF_TAG_BT_HAPPY3 = "11";
    /**
     * 胆拖：快乐四
     */
    public static final String KLSF_TAG_BT_HAPPY4 = "12";
    /**
     * 胆拖：快乐五
     */
    public static final String KLSF_TAG_BT_HAPPY5 = "13";
	
	/*购买彩票时的订单类型*/
    /******************** 追号类型 ********************/
    /**
     * 正常追号
     */
    public static final String LOTTERY_TYPE_FLOW = "0";
    /**
     * 追号套餐
     */
    public static final String LOTTERY_TYPE_SUITE = "1";

    /**
     * 高级追号
     */
    public static final String LOTTERY_TYPE_FLOW_HIGHT = "3";
    /**
     * 超级追号博大奖
     */
    public static final String LOTTERY_TYPE_FLOW_SUPER = "4";
    /**
     * 预约追号
     */
    public static final String LOTTERY_TYPE_FLOW_FETURE = "5";


    /******************** 购彩方式 ********************/
    /**
     * 购彩方式
     */
    public static final String LOTTERY_BUY_TYPE = "buy_type";
    /**
     * 购彩
     */
    public static final String LOTTERY_BUY = "buy";
    /**
     * 胆拖购彩
     */
    public static final String LOTTERY_DT = "dantuo";
    /**
     * 套餐
     */
    public static final String LOTTERY_SUITE = "suite";
    /**
     * 快捷投注
     */
    public static final String LOTTERY_SHORTCUT = "shortcut";
    /**
     * 推荐号码
     */
    public static final String LOTTERY_RECOMMEND = "recommend";

    /******************** 停追条件 ********************/
    /**
     * 不停追
     */
    public static final String STOP_NO_PRIZE = "0";
    /**
     * 中奖停追
     */
    public static final String STOP_PRIZE = "1";
    /**
     * 大奖停追
     */
    public static final String STOP_BIG_PRIZE = "2";

    /**
     * 高级追号
     */
    public static final String STOP_BETTER_PRIZE = "3";

    /**
     * 超级追号
     */
    public static final String STOP_BEST_PRIZE = "4";

    /******************** 广播类型 ********************/
    /**
     * 类型
     */
    public static final String BROADCAST_TYPE = "type";
    /**
     * 普通信息
     */
    public static final String BROADCAST_COMMON = "common";
    /**
     * 错误信息
     */
    public static final String BROADCAST_ERROR = "error";

    /******************** 选号类型 ********************/
    /**
     * 类型
     */
    public static final String STAKE_TYPE = "stake_type";
    /**
     * 重号
     */
    public static final int STAKE_REPEAT = 1;
    /**
     * 单号
     */
    public static final int STAKE_SINGLE = 2;

    /******************** 每期机选 ********************/
    /**
     * 不机选
     */
    public static final String RANDOM_BALL_NO = "0";
    /**
     * 机选
     */
    public static final String RANDOM_BALL_YES = "1";

    /******************** 修改结果 ********************/
    /**
     * 修改结果
     */
    public static final String RESULT = "result";
    /**
     * 修改成功
     */
    public static final String RESULT_SUCCESS = "success";
    /**
     * 修改失败
     */
    public static final String RESULT_FAIL = "fail";

    /******************** 帮助信息 ********************/
    /**
     * 帮助类型
     */
    public static final String HELP_TYPE = "helpType";
    /**
     * 合买规则
     */
    public static final String HELP_UNION = "helpUnion";
    /**
     * 常见问题
     */
    public static final String HELP_COMMON_PROBLEM = "helpProblem";
    /**
     * 彩豆说明
     */
    public static final String HELP_CAIDOU = "helpCaidou";
    /**
     * 如何注册
     */
    public static final String HELP_REGISTER = "helpRegister";
    /**
     * 如何充值
     */
    public static final String HELP_RECHARGE = "helpRecharge";
    /**
     * 追号及套餐
     */
    public static final String HELP_FLOW = "helpFlow";
    /**
     * 玩法规则
     */
    public static final String HELP_PLAY = "helpPlay";

    /**
     * 双色球玩法规则
     */
    public static final String HELP_PLAY_SSQ = "ssqPlay";
    public static final String HELP_PLAY_OFFICIAL_SSQ = "ssqPlayOfficial";
    /**
     * 七乐彩玩法规则
     */
    public static final String HELP_PLAY_QLC = "qlcPlay";
    public static final String HELP_PLAY_OFFICIAL_QLC = "qlcPlayOfficial";
    /**
     * 3D玩法规则
     */
    public static final String HELP_PLAY_D3 = "d3Play";
    public static final String HELP_PLAY_OFFICIAL_D3 = "d3PlayOfficial";
    /**
     * 快乐十分玩法规则
     */
    public static final String HELP_PLAY_KLSF = "klsfPlay";
    public static final String HELP_PLAY_OFFICIAL_KLSF = "klsfPlayOfficial";
    /**
     * 如何提现
     */
    public static final String HELP_WITHDRAW = "helpWithDraw";
    /**
     * 注册协议
     */
    public static final String HELP_REGISTER_AGREE = "helpRegisterAgree";
    /**
     * 购彩协议
     */
    public static final String HELP_STAKE_AGREE = "helpStakeAgree";
    /**
     * 关于
     */
    public static final String ABOUT = "about";

    /******************** 通知类型 ********************/
    /**
     * 开奖公告
     */
    public static final String NOTIFY_PRIZENO = "1";
    /**
     * 活动线上
     */
    public static final String NOTIFY_ACTIVITY_UP = "2";
    /**
     * 活动线下
     */
    public static final String NOTIFY_ACTIVITY_DOWN = "3";
    /**
     * 礼券
     */
    public static final String NOTIFY_TICKET = "4";
    /**
     * 积分
     */
    public static final String NOTIFY_INTERVAL = "5";
    /**
     * 中奖
     */
    public static final String NOTIFY_PRIZE = "6";

    /******************** 活动 ********************/
    /**
     * 活动Id
     */
    public static final String ACTIVITY_ID = "activityId";

    /******************** 站内信 ********************/
    /**
     * 站内信Id
     */
    public static final String LETTER_ID = "letterId";

    /******************** 分页 ********************/
    /**
     * 每页查询数量
     */
    public static final int PAGE_SIZE = 5;
    /**
     * 下拉刷新标识
     */
    public final static int PULL_DOWN = 1;
    /**
     * 加载更多标识
     */
    public final static int LOAD_MORE = 2;

    /******************** 注册投注站信息 ********************/
    /**
     * 投注站Id
     */
    public static final String STATION_ID = "stationId";
    /**
     * 投注站名称
     */
    public static final String STATION_NAME = "stationName";
    /**
     * 投注站地址
     */
    public static final String STATION_ADDRESS = "stationAddress";

    /******************** 用户名 ********************/
    /**
     * 用户名
     */
    public static final String USERNAME = "userName";

    /******************** 主页 ********************/
    /**
     * 跳转标签
     */
    public static final String INTENT_TAB = "intentTab";
    /**
     * 首页
     */
    public static final String INDEX_TAB = "index_tab";
    /**
     * 本期页
     */
    public static final String ISSUE_TAB = "issue_tab";
    /**
     * 购物袋
     */
    public static final String BAG_TAB = "buy_bag_tab";
    /**
     * 我的阿甘
     */
    public static final String MY_TAB = "my_tab";


    /**
     * 嵌入底部导航
     */
    public static final String FREE_TAB = "free_tab";
    /******************** 登录 ********************/
    /**
     * 是否需要提示
     */
    public static final String LOGIN_NEED_TIPS = "loginNeedTips";
    /**
     * 是否可以返回
     */
    public static final String LOGIN_CAN_BACK = "loginCanBack";

    /**
     * 是否来自投注页
     */
    public static final String LOGIN_FORM_ORDER = "isFromOrder";
    /**
     * 是否来自我的彩票页
     */
    public static final String LOGIN_FORM_TICKET = "isFromTicket";
    /**
     * 是否登录到首页
     */
    public static final String LOGIN_INDEX = "loginIndex";
    /**
     * 是否已经登录
     */
    public static final String IS_LOGIN = "isLogin";
    /**
     * 用户id
     */
    public static final String USERID = "userId";

    /*********************咨讯************************/
    /**
     * 咨讯详情newsId
     */
    public static final String NEWS_DETAIL_NEWSID = "newsId";
    /**
     * 咨讯详情title
     */
    public static final String NEWS_DETAIL_TITLE = "newsTitle";
    /**
     * 咨讯详情日期
     */
    public static final String NEWS_DETAIL_DATE = "newsDate";
    /**
     * 咨讯详情来源
     */
    public static final String NEWS_DETAIL_SOURCE = "newsSource";
    /**
     * 咨讯详情内容
     */
    public static final String NEWS_DETAIL_CONTENT = "newsContent";

    /*********************资金明细************************/
    /**
     * 全部
     */
    public static final String FUND_ALL = "fundAll";
    /**
     * 购彩
     */
    public static final String FUND_BUY = "fundBuy";
    /**
     * 充值
     */
    public static final String FUND_RECHARGE = "fundRecharge";
    /**
     * 提现
     */
    public static final String FUND_WITHDRAW = "fundWithdraw";
    /**
     * 派奖
     */
    public static final String FUND_PRIZE = "fundPrize";
    /**
     * 红包
     */
    public static final String FUND_GIFT = "fundGift";


    /*********************跳转类型************************/
    /**
     * 跳转类型
     */
    public static final String LOTTERY_GOTO_TYPE = "gotoType";
    /**
     * 首页跳转
     */
    public static final String LOTTERY_GOTO_MAIN = "main";

    /**
     * 二级跳转页面跳转
     */
    public static final String LOTTERY_GOTO_EJ = "ej";

    /**************快乐十分拼接单类型**************/

    /**
     * 首位数投类型/首位红投类型
     */
    public static final int[] PICK1SHU = {1};

    /**
     * 二连直
     */
    public static final int[] PICK2ODER = {1, 1};

    /**
     * 二连组/快乐二
     */
    public static final int[] PICK2 = {2};

    /**
     * 三连直
     */
    public static final int[] PICK3ORDER = {1, 1, 1};

    /**
     * 三连组/快乐三
     */
    public static final int[] PICK3 = {3};


    /**
     * 快乐四
     */
    public static final int[] PICKHAPPY4 = {4};

    /**
     * 快乐五
     */
    public static final int[] PICKHAPPY5 = {5};


    /**
     * 传递数据
     */
    public static final String PASSDATA = "passData";

    /**
     * 传递值
     */
    public static final String PASSVALUE = "passValue";

    /**
     * 补全email
     */
    public static final String FILLEMAIL = "fillEmail";

    /**
     * 修改email
     */
    public static final String MODIFYEMAIL = "modifyEmail";

    public static final String TZSHARE = "tz_share";

    public static final String START_NOTIFY_TIMER = "com.ssports.o2o.service.notify.timer.start";

    public static final String STOP_NOTIFY_TIMER = "com.ssports.o2o.service.notify.timer.stop";


    public static final String RECHARGE_TYPE_ALIPAY = "2";
    public static final String RECHARGE_TYPE_UMPAY = "4";
    /**
     * 银联支付类型
     */
    public static final String RECHARGE_TYPE_UPPAY = "9";
    // 正式环境mode为00  测试环境为01
    public static final String UPPAY_MODE = "00";


    /**
     * 支付方式  0 单一支付
     */
    public static final String PAY_SINGLE = "0";
    /**
     * 支付方式  1 混合支付
     */
    public static final String PAY_COMBINE = "1";
    /**
     * 虚拟资金支付
     */
    public static final String PAY_TYPE_CASH = "0";
    /**
     * 使用礼金劵支付
     */
    public static final String PAY_TYPE_GIFT = "1";
    /**
     * 支付宝支付
     */
    public static final String PAY_TYPE_ALIPAY = "2";
    /**
     * 联动优势
     */
    public static final String PAY_TYPE_UMPAY = "3";
    /**
     * 银联支付
     */
    public static final String PAY_TYPE_UPPAY = "9";

    /**
     * 合买玩法：混合
     */
    public static final String LOTTERY_UNION_HH = "-1";

    /**
     * 胆码随机个数--前三组和快乐三
     */
    public static final int RANDOM_COUNT_BT_FTG_AND_HAPPY3 = 2;
    /**
     * 胆码随机个数--快乐四
     */
    public static final int RANDOM_COUNT_BT_HAPPY4 = 3;
    /**
     * 胆码随机个数--快乐五
     */
    public static final int RANDOM_COUNT_BT_HAPPY5 = 4;
    /**
     * 拖码随机个数--前三组/快乐三、四、五
     */
    public static final int RANDOM_COUNT_TOW = 2;
	
	/*
	 * 快乐十分玩法
	 */
    /**
     * 胆拖：前三组
     */
    public static final String KLSF_TYPE_BT_FTG = "35";
    /**
     * 胆拖：前三组
     */
    public static final String KLSF_TYPE_BT_HAPPY3 = "32";
    /**
     * 胆拖：前三组
     */
    public static final String KLSF_TYPE_BT_HAPPY4 = "42";
    /**
     * 胆拖：前三组
     */
    public static final String KLSF_TYPE_BT_HAPPY5 = "52";
	
	/*---快乐十分走势 界面选择标记---*/
    /**
     * 开奖号码
     */
    public static final int WINNING_NUMBERS_TAG = 1001;
    /**
     * 基本走势
     */
    public static final int BASIC_TREND_TAG = 1002;
    /**
     * 连号走势
     */
    public static final int CONSECUTIVE_TREND_TAG = 1003;
    /**
     * 左斜连号走势
     */
    public static final int LEFT_INCLINE_TREND_TAG = 1004;
    /**
     * 右斜连号走势
     */
    public static final int RIGHT_INCLINE_TREND_TAG = 1005;
    /**
     * 小区走势
     */
    public static final int SMALL_AREA_TREND_TAG = 1006;
    /**
     * 大区走势
     */
    public static final int LARGE_AREA_TREND_TAG = 1007;

    public String KEY = "hexinjingu_owl01";
}
