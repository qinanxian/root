package cn.fisok.raw.holder;

/**
 * 控制是否在当前线程上关闭审计
 */
public abstract class GlobalSupportHolder {
    private static final ThreadLocal<Boolean> auditEnableHolder = new ThreadLocal<Boolean>();


    public static void clear(){
        auditEnableHolder.remove();
    }

    public static boolean auditEnable(){
        Boolean enable = auditEnableHolder.get();
        if(enable == null)return true;
        return enable;
    }

    public static void auditEnable(Boolean enable){
        auditEnableHolder.set(enable);
    }

    public static void clearAudit(){
        auditEnableHolder.remove();
    }
}
