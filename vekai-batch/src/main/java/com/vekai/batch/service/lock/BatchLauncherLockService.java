package com.vekai.batch.service.lock;

/**
 * 批量运行锁
 */
public interface BatchLauncherLockService {
    boolean lock(final String key);
    void unlock(final String key);
}
