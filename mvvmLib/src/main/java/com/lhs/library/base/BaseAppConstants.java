package com.lhs.library.base;

public class BaseAppConstants {

    public static class SPKey {
        public static final String KEY_ACCESS_TOKEN_CSLA = "key_access_token_CSLA";
        public static final String KEY_ACCESS_TOKEN_CHAT = "key_access_token_chat";
        public static final String KEY_USER_ID = "key_user_id";
        public static final String KEY_USER_PWD = "key_user_pwd";
        public static final String KEY_USER_NAME = "key_user_name";

        public static final String KEY_SETTING_OTHER_PEOPLE = "setting_other_people_%s";
        public static final String KEY_SETTING_FRIEND = "key_setting_friend_%s";
        public static final String KEY_SETTING_COMPANY = "setting_company_%s";

        public static final String KEY_SIGN = "key_sign_%s_%s";

        public static final String KEY_AUTH_CODE = "auth_code";

        public static final String KEY_PHONE_NOTICE = "phone_notice";

        public static final String KEY_QINIU_DOMAIN = "qiniu_domain";
    }

    public static class BundleConstant {
        public final static String ARG_PARAMS_0 = "param0";
        public final static String ARG_PARAMS_1 = "param1";
        public final static String ARG_PARAMS_2 = "param2";
        public final static String ARG_PARAMS_3 = "param3";
        public final static String ARG_PARAMS_4 = "param4";
    }

    public static class AppMessageWhat {
        public final static int MSG_WHAT_MODIFY_TEL_FIRST = 3000;
        public final static int MSG_WHAT_MODIFY_TEL_SECOND = 3001;
        public final static int MSG_WHAT_UPDATE_LOGIN_PASSWORD = 3002;
        public final static int MSG_WHAT_GET_TRIP_PRICE_DETAIL = 3004;
        public final static int MSG_WHAT_MODIFY_USER_HEAD = 3005;
        public final static int MSG_WHAT_ADD_ACCOUNT = 3006;

        public final static int MSG_WHAT_REFRESH_FAIL = 0x400;
        public final static int MSG_WHAT_LOADMORE_FAIL = 0x401;
        public final static int MSG_WHAT_LOGIN_FAIL = 0x402;
    }

    public static class SerializableFileNames {
        public final static String FILE_LOGIN_DATA = "login_data";
        public final static String FILE_USER_INFO_CSLA = "user_info_csla";
        public final static String FILE_USER_INFO_IM = "user_info_im";

        public final static String FILE_LOGIN_RESULT = "key_login_result";

        public final static String FILE_USER_WALLET = "key_user_wallet";
        public final static String FILE_PRODUCT_CHARACTER_VALUES = "character_values";
        public final static String FILE_PRODUCT_GOOD_DETAIL = "good_detail_list";
    }


}
