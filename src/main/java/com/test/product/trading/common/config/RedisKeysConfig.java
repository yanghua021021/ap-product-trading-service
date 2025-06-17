package com.test.product.trading.common.config;

/**
 * <p>
 * RedisKeysConfig
 * </p>
 *
 * @author yh
 * @since 2025-06-15
 */
public class RedisKeysConfig {

    // 用户充值锁
    public static final String LOCK_USER_ACCOUNT_FLOW_RECHARGE_KEY = "lock:user:account:flow:recharge:key";

    // 商品库存缓存
    public static final String CACHE_MERCHANT_PRODUCT_STOCK_KEY = "cache:merchant:product:stock:key";

    // 用户购买商品锁
    public static final String LOCK_ORDER_BUY_PRODUCT_SKU_KEY = "lock:order:buy:product:sku:key";

}
