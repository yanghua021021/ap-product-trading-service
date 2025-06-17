package com.test.product.trading.common.enums;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yh
 * @since 2025-06-14
 * 通用枚举类
 */
@Getter
@AllArgsConstructor
public class CodeEnum {

    @Getter
    @AllArgsConstructor
    public enum CurrencyEnum {
        CNY("CNY", "人民币");
        public String code;
        public String desc;
    }

    @Getter
    @AllArgsConstructor
    public enum DateEnum {
        LATEST("99991231", "当前最新");
        public String code;
        public String desc;
    }

    @Getter
    @AllArgsConstructor
    public enum TradeTypeEnum {
        RECHARGE(1, "充值"),
        BUY(2, "购买");
        public int code;
        public String desc;
    }

    @Getter
    @AllArgsConstructor
    public enum StatusEnum {
        SUCCESS(1, "成功");
        public int code;
        public String desc;
    }

}
