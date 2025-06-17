package com.test.product.trading.common.rsp;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * JsonResponse
 * </p>
 *
 * @author yh
 * @since 2025-06-14
 */
public class JsonResponse implements Serializable {
    public static final int OK_STATUS = 200;
    public static final String OK_MESSAGE = "成功";
    public static final int ERROR_PARAMS_STATUS = 10000;
    public static final String ERROR_PARAMS_MESSAGE = "参数错误";
    public static final int ERROR_SYS_BUSY_STATUS = 20000;
    public static final String ERROR_SYS_BUSY_MESSAGE = "系统繁忙";
    public static final int ERROR_SAVE_STATUS = 30000;
    public static final String ERROR_SAVE_MESSAGE = "保存失败";
    public static final int ERROR_EMPTY_DATA_STATUS = 40000;
    public static final String ERROR_EMPTY_DATA_MESSAGE = "未查询到数据";
    public static final int ERROR_TIME_OUT_STATUS = 50000;
    public static final String ERROR_TIME_OUT_MESSAGE = "服务调用超时，请稍后再试";
    public static final int ERROR_DELETE_STATUS = 60000;
    public static final String ERROR_DELETE_MESSAGE = "删除失败";
    public static final int ERROR_TRY_LOCK_FAIL_STATUS = 90001;
    public static final String ERROR_TRY_LOCK_FAIL_MESSAGE = "分布式锁抢占失败";
    public static final int ERROR_UNKNOWN_STATUS = 99999;
    public static final String ERROR_UNKNOWN_MESSAGE = "服务调用异常，请稍后再试";
    private static final long serialVersionUID = -5669121967614976662L;
    public static final int ERROR_PATH_NOT_FOUND_STATUS = 404;
    public static final String ERROR_PATH_NOT_FOUND_MESSAGE = "请求的路径不存在：%s";
    public static final int ERROR_CONTENT_TYPE_NOT_SUPPORT_STATUS = 405;
    public static final String ERROR_CONTENT_TYPE_NOT_SUPPORT_MESSAGE = "Content type '%s' 不支持";
    private int status = 0;
    private String message = "";
    private Object data = "";

    public JsonResponse() {
    }

    public JsonResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return 200 == this.status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof JsonResponse)) {
            return false;
        } else {
            JsonResponse that = (JsonResponse)o;
            return this.status == that.status && Objects.equals(this.message, that.message) && Objects.equals(this.data, that.data);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.status, this.message, this.data});
    }

    public String toString() {
        return "JsonResponse{status=" + this.status + ", message='" + this.message + '\'' + ", data=" + this.data + '}';
    }
}
