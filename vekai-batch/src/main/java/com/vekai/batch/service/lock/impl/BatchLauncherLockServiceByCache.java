package com.vekai.batch.service.lock.impl;

import com.vekai.batch.service.lock.BatchLauncherLockService;
import cn.fisok.raw.kit.StringKit;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 批量运行锁
 */
public class BatchLauncherLockServiceByCache implements BatchLauncherLockService {

    protected BatchLauncherLockStoreByCache lockStore;

    public BatchLauncherLockStoreByCache getLockStore() {
        return lockStore;
    }

    @Autowired
    public void setLockStore(BatchLauncherLockStoreByCache lockStore) {
        this.lockStore = lockStore;
    }

    /**
     * 获取资源锁，这里不考虑如果任务挂了，锁超时释放的问题，
     * 如果调度程序自己挂了，导致锁无法释放，这种问题，暂时不作考虑
     * @param key
     * @return
     */
    public boolean lock(final String key){
        String value1 = StringKit.uuid();
        String value2 = lockStore.getLockValue(key,value1);
        //如果传入的UUID和返回的UUID一样
        //表示没有被缓存，没有被缓存，表示锁可用
        return value1.equals(value2);
    }

    public void unlock(final String key){
        lockStore.clearLockValue(key);
    }
}
