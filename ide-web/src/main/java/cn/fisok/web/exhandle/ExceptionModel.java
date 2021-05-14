package cn.fisok.web.exhandle;

/**
 * 异常数据模型封装，主要用于给前端返回异常JSON数据
 */
public class ExceptionModel {
    /**
     * 标识业务异常
     */
    public static final String EXCEPTION_TYPE_BIZ = "BizException";
    /**
     * 标识系统异常
     */
    public static final String EXCEPTION_TYPE_SYS = "SysException";

    private String type = EXCEPTION_TYPE_SYS;
    private String code = "0";
    private String message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
