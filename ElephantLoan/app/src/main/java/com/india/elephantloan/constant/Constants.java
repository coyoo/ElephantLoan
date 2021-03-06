package com.india.elephantloan.constant;


import com.india.elephantloan.utils.AppUtil;
import com.india.elephantloan.utils.LogUtils;
import com.india.elephantloan.utils.UIUtils;


/**
 * Created by zhangzc on 2017/4/27.
 */
public class Constants {

    /*-------------------------  系统配置 begin ---------------------------*/


    //SharedPreferences
    public static String NAME = UIUtils.getContext().getPackageName();
    //版本号
    public static String version = UIUtils.getVersion();
    //log开关
    public static int DEBUGLEVEL = LogUtils.LEVEL_ALL;


    public static final String APPVERSION = String.valueOf(AppUtil.getVersion(UIUtils.getContext()));// 版本id
    public static final String VERSIONNAME = (AppUtil.getVersionName(UIUtils.getContext()));


    /*-------------------------  系统配置 end ---------------------------*/


    /*-------------------------  url begin ---------------------------*/


    public static final String URL = "URL";
    //public static final String DEF_IP = "http://192.168.0.104:10101/";//http://client.d7v28.cn/
    //public static final String BASE_URL = "http://192.168.0.104:10101/elep/"; //HttpLoader.getBase_url();
    public static final String BASE_URL = "https://gw.jq62.com/elep/";
    public static final String LOGIN = BASE_URL + "api/dashboard/app/v1/login";
    public static final String GET_OTP = BASE_URL + "api/dashboard/app/v1/code/sms";
    public static final String SUBMIT_USER_MEG = BASE_URL + "api/dashboard/app/user/v1/save";
    public static final String GET_USER_MSG = BASE_URL + "api/dashboard/app/user/v1/getcurrusser";
    public static final String SEND_ORDER = BASE_URL + "api/dashboard/order/v1/create";
    public static final String GO_PAY = BASE_URL + "api/dashboard/pay/gopay";
    public static final String SAVE_FEEDBACK = BASE_URL + "api/dashboard/feedback/v1/save";
    public static final String GET_CURRUSER_PAYSTATE = BASE_URL + "api/dashboard/app/user/v1/paystatus";
    public static final String IS_REGISTER = BASE_URL + "api/dashboard/app/user/v1/checkphoneisreg";
    public static final String GET_ALL_LOAN = BASE_URL + "api/dashboard/app/product/v1/list";
    public static final String GET_PAY_SUCCESS = BASE_URL + "api/dashboard/pay/back/succ";
    public static final String GET_PAY_FAILURE = BASE_URL + "api/dashboard/pay/back/fail";
    public static final String POST_PRODUCT = BASE_URL + "api/dashboard/app/click/v1/pvanduv/cal";


    /*-------------------------  url end ---------------------------*/

    /*-------------------------  请求码 begin ---------------------------*/


    /*-------------------------  请求码 end ---------------------------*/

    /*-------------------------  参数 begin ---------------------------*/
    public static final String APP_VERSION = "appVersion";
    public static final String DOMAIN = "PandaLoan";
    public static final String CLIENT_USER_SESSION = "token";
    public static final String PHONE_NUM = "phone_num";
    public static final String BIRTHDAY = "brithday";
    public static final String GENDER = "gender";
    public static final String CAMPAIGN = "campaign";
    public static final String VERSION = "version";
    public static final String CLIENTID = "clientId";
    public static final String APP_NAME = "Elephant Loan";
    public static final String RAZORPAY_NAME = "Elephant Loan Pay";
    public static final String IMAGE = "https://uranus-prod.oss-cn-beijing.aliyuncs.com/elep.png";
    public static final String ISFIRSTENTER = "isFirstEnter";


    /*-------------------------  userinfo begin ---------------------------*/

    /*-------------------------  userinfo end ---------------------------*/

}
