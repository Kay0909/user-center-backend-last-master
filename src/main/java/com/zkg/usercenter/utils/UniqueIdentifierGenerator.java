package com.zkg.usercenter.utils;

/**
 * 生成星球编号
 */
public class UniqueIdentifierGenerator {

    private static int counter = 0;
    private static final Object lock = new Object();

    public static String generateUniqueId() {
        synchronized (lock) {
            // 获取当前时间戳（精确到秒）
            long timestamp = System.currentTimeMillis() / 1000;
            
            // 结合时间戳和计数器生成唯一编号
            String uniqueIdSuffix = String.format("%s%03d", timestamp, counter);
            
            // 确保编号不超过10个字符
            if (uniqueIdSuffix.length() > 7) {
                uniqueIdSuffix = uniqueIdSuffix.substring(0, 7);
            }
            
            // 返回最终的唯一编号，格式为 "zkg" + 唯一编号后缀
            String uniqueId = "zkg" + uniqueIdSuffix;
            
            // 增加计数器
            counter++;
            
            return uniqueId;
        }
    }
}