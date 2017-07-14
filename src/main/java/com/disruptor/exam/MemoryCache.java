package com.disruptor.exam;


import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 内存缓存
 * 用于在并发线程中记录任务进度
 *
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/28
 */
public class MemoryCache {
    private static MemoryCache memoryCache;
    private static ReadWriteLock lock = new ReentrantReadWriteLock();
    private ConcurrentMap<String, Object> cache = Maps.newConcurrentMap();

    private MemoryCache() {
    }

    public static MemoryCache getInstance() {
        lock.readLock().lock();
        try {
            if (null == memoryCache) {
                lock.readLock().unlock();
                lock.writeLock().lock();
                if (null == memoryCache) { //再次检查，避免多个线程同一时刻判空创建多个memoryCache
                    memoryCache = new MemoryCache();
                }
                lock.readLock().lock(); // 更新锁
                lock.writeLock().unlock();
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        } finally {
            lock.readLock().unlock();
        }

        return memoryCache;
    }

    /**
     * 向内存缓存中添加数据
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    /**
     * 获取内存缓存中存放的指定数据
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return cache.get(key);
    }

}
