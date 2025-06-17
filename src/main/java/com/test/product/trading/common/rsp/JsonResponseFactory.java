package com.test.product.trading.common.rsp;

/**
 * <p>
 * JsonResponseFactory
 * </p>
 *
 * @author yh
 * @since 2025-06-14
 */
public final class JsonResponseFactory {
    public JsonResponseFactory() {
    }

    public static String getMessage(int status) {
        switch (status) {
            case 200:
                return "成功";
            case 404:
                return "请求的路径不存在：%s";
            case 405:
                return "Content type '%s' 不支持";
            case 10000:
                return "参数错误";
            case 20000:
                return "系统繁忙";
            case 30000:
                return "保存失败";
            case 40000:
                return "未查询到数据";
            case 50000:
                return "服务调用超时，请稍后再试";
            case 60000:
                return "删除失败";
            case 70000:
                return "下单失败";
            case 90001:
                return "分布式锁抢占失败";
            case 99999:
                return "服务调用异常，请稍后再试";
            default:
                return "未知错误";
        }
    }

    public static JsonResponse build(int status, String message, Object data) {
        return new JsonResponse(status, message, data);
    }

    public static JsonResponse success() {
        return success("");
    }

    public static JsonResponse success(Object data) {
        return build(200, getMessage(200), data);
    }

    public static JsonResponse error(int status) {
        return error(status, "");
    }

    public static JsonResponse error(int status, Object data) {
        return build(status, getMessage(status), data);
    }

    public static JsonResponse errorParams() {
        return error(10000);
    }

    public static JsonResponse errorSysBusy() {
        return error(20000);
    }

    public static JsonResponse errorUnknown() {
        return error(99999);
    }

    public static JsonResponse errorTimeOut() {
        return error(50000);
    }

    public static JsonResponse errorSave() {
        return error(30000);
    }

    public static JsonResponse errorEmpty() {
        return error(40000);
    }

    public static JsonResponse errorLocked() {
        return error(90001);
    }
}
