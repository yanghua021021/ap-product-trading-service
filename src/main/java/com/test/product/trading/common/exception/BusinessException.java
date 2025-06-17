package com.test.product.trading.common.exception;

import com.test.product.trading.common.enums.BLExceptionEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(BusinessException businessException) {
        super(businessException.getMessage());
        this.code = businessException.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(BLExceptionEnum bussinessExceptionEnum) {
        super(bussinessExceptionEnum.getMessage());
        this.code = bussinessExceptionEnum.getCode();
    }

}
