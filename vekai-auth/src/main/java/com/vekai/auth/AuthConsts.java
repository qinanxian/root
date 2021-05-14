package com.vekai.auth;

import java.util.regex.Pattern;

public interface AuthConsts {
    String SESSION_USER = "sessionUser";
    String REDIRECT_LOCATION_PARAM = "redirectLocation";
    Pattern HTTP_PATTERN = Pattern.compile("^(http|https)",Pattern.CASE_INSENSITIVE);
    String LOGON_STATUS_SUCCESS = "SUCCESS";
    String LOGON_STATUS_FAIL = "FAIL";
    String LOGON_STATUS_PASSWORD_EXPIRE = "PASSWORD_EXPIRED";
    String JWT_ATTRIBUTE = "userJwt";

    String ACOUNT_PWD_INCORRECT_MSG = "账号或密码有误";
    String LOGIN_FAIL_MSG_ATTRIBUTE_NAME = "loginFailMsgAttributeName";
    String AUTH_FAIL_HEADER = "WWW-Authenticate";
    int AUTH_FAIL_STATUS_CODE = 401;
    String SESSION_INVALID = "session invalid";
    String UNAUTHORIZATION = "unauthz";
    String KICK_OUT = "kick out";
    String PWD_NEED_RESET = "pwd need reset";

    String READ_TOKEN = "r";
    String WRITE_TOKEN = "w";
    String RW_DIVIDER_TOKEN = "@";
    String MENU_SCHEME_TOKEN = "menu";
    String DATAFORM_SCHEME_TOKEN = "df";
    String REST_SCHEME_TOKEN = "rest";
    String WILDCARD_TOKEN = "*";
    String SCHEME_DIVIDER_TOKEN = ":";
    String ROOT_PART_TOKEN = "/";
    String PART_DIVIDER_TOKEN = "/";
    String SUBPART_DIVIDER_TOKEN = ",";
    String NOP_URI = "/blankHandler";
    String NOP_HANDLER = "/nop";

}
