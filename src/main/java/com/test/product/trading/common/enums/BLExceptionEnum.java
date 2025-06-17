package com.test.product.trading.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BLExceptionEnum {

    INTERFACE_PARAMETER_ERROR(100000, "参数错误"),
    THIRD_PARTY_SERVICE_EXCEPTION(200000, "三方服务异常"),
    BUSSINESS_EXCEPTION(300000, "业务异常"),
    DB_EXCEPTION(400000, "数据库异常"),
    SYSTEM_EXCEPTION(500000, "系统异常"),

    // 1xxx 异常
    FAILED_TO_DECRYPT_THE_CIPHERTEXT(100001, "密文解密失败"),
    INVALID_MOBILE_NUMBER(100002, "手机号错误，请检查手机号"),
    FAILED_TO_ENCRYPT_THE_EXT(100003, "明文加密失败"),
    REQ_MODE_ERROR(100004, "请求模式错误"),
    OPEN_ACCOUNT_TYPE_ERROR(100005, "开户类型错误"),
    FIRST_TAKE_PICTURE_ERROR(100006, "身份证上传标识错误"),
    ACCT_FLAG_ERROR(100007, "开通账户标识错误"),

    CUST_ID_IS_NOT_EXISTS(100013, "CUST_ID不存在"),

    // 2xxx 三方服务异常
    ALIYUN_SMS_INIT_EXCEPTION(200101, "阿里云短信初始化失败"),
    ALIYUN_SMS_SEND_EXCEPTION(200102, "阿里云短信发送失败"),

    EXORC_SERVICE_ERROR(200103, "OCR服务调用失败"),
    EXORC_ID_FRONT_SIDE_EXCEPTION(200104, "身份证正面照未能识别"),
    EXORC_ID_BACK_SIDE_EXCEPTION(200105, "身份证反面照未能识别"),

    EXORC_FACE_DECETION_EXCEPTION(200106, "人脸检测失败"),
    EXORC_ACTION_LIVE_EXCEPTION(200107, "动作活体检测失败"),
    EXORC_LIVE_NESS_EXCEPTION(200108, "静默活体检测失败"),
    ZSTD_VIDEO_CAN_NOT_EMPTY(200109, "视频文件为空"),

    // 3xxx 业务异常
    VERIFICATION_CODE_IS_INVALID(300001, "验证码失效"),
    VERIFICATION_CODE_ERROR_PLEASE_TRY_AGAIN(300003, "验证码错误，请重试"),
    VERIFICATION_CODE_DAY_LIMIT(300003, "当日验证码获取次数已达到上限，请明天重试"),
    VERIFICATION_TOO_BUSY_PLEASE_TRY_AGAIN(300004, "获取验证码操作频繁，请稍候重试"),

    THE_VIP_INVITATION_CODE_IS_INVALID(300005, "贵宾邀请码无效"),
    TOKEN_IS_INVALID(300006, "token验证失败"),
    CUST_INFO_IS_NOT_EXISTS(300007, "客户基本信息不存在"),

    REFFER_INFO_IS_NOT_EXISTS(300008, "推荐人信息不存在"),
    UPLOAD_FILE_CAN_NOT_EMPTY(300009, "上传文件为不能为空文件"),
    CAN_NOT_SUPPORT_FILE_FORMAT(300010, "不支持的文件格式"),
    OPN_FILE_IS_NOT_EXISTS(300011, "文档资源不存在"),
    DOWNLOAD_OPN_FILE_FAILED(300012, "证件图片下载失败"),
    CUST_ID_INFO_IS_NOT_EXISTS(300013, "客户证件信息不存在"),
    EDU_PARAMETER_ERROR(300015, "学历参数错误"),
    OCP_PARAMETER_ERROR(300016, "职业参数错误"),
    POS_PARAMETER_ERROR(300017, "职务参数错误"),
    JOB_UNIT_NM_PARAMETER_ERROR(300017, "工作单位参数错误"),
    YR_INCM_PARAMETER_ERROR(300018, "年收入参数错误"),
    ACTL_BENFC_PARAMETER_ERROR(300019, "实际受益人参数错误"),
    ACTL_CTRLR_PARAMETER_ERROR(300020, "实际控制人参数错误"),
    TAX_RSDT_PARAMETER_ERROR(300021, "纳税身份参数错误"),
    ITGR_RCRD_PARAMETER_ERROR(300022, "诚信记录参数错误"),
    PLEASE_HANDLE_IT_AT_THE_COUNTER(300023, "您的情况不符合线上开户条件，请临柜办理"),
    SCH_NM_CAN_NOT_EMPTY(300024, "您的职业为学生,请输入学校名称"),
    STD_POS_ILLEGE(300025, "您的职业为学生,职务非法"),
    RACE_CD_ILLEGE(300026, "民族参数错误"),
    COMPUTE_AGE_ERROR(300027, "年龄计算错误"),
    TOO_YOUNG_PLEASE_HANDLE_IT_AT_THE_COUNTER(300028, "您未满16岁，不满足开立证券账户的条件"),
    AGE_ILLEGE_PLEASE_HANDLE_IT_AT_THE_COUNTER(300029, "您的年龄不符合线上开户条件，请临柜办理"),
    ID_CARD_HAS_EXPIRED(300030, "您的身份证有效期已过，无法继续办理"),
    ID_CARD_DATE_ILLEGE_EXPIRED(300031, "您的身份证有效期非法，无法继续办理"),
    ID_CARD_NO_ILLEGE_EXPIRED(300032, "您的身份证号码非法，无法继续办理"),
    ID_TP_CD_ILLEGE(300033, "证件类型参数错误"),
    ID_DATA_ALERDY_EXISTS(300034, "证件已被使用"),
    CUST_ID_FILE_IS_NOT_EXISTS(300035, "请先上传证件信息"),

    LOGIN_TOO_BUSY_PLEASE_TRY_AGAIN(300035, "登录操作频繁，请稍候重试"),
    CUST_NAME_LENGTH_ILLEGE(300036, "真实姓名长度必须在1到100长度之间"),
    SIGN_ORG_LENGTH_ILLEGE(300037, "签发机关长度必须在1到100长度之间"),
    CARD_ADDR_LENGTH_ILLEGE(300038, "证件地址长度必须在1到100长度之间"),
    TRADE_PWD_NOT_EQUAL(300039, "两次输入密码不同，请重新收入！"),

    VIDE_AUTH_TOO_BUSY_PLEASE_TRY_AGAIN(300035, "视频认证操作频繁，请稍候重试"),
    FACE_ACTION_CONFIG_TOO_BUSY_PLEASE_TRY_AGAIN(300036, "获取活体动作操作频繁，请稍候重试"),
    FACE_ACTION_CONFIG_ERROR(300037, "获取活体动作失败"),
    FACE_ACTION_LIVE_TOO_BUSY_PLEASE_TRY_AGAIN(300038, "活体动作检测操作频繁，请稍候重试"),
    FACE_COMPARE_ING_PLEASE_TRY_AGAIN(300039, "人脸对比中，请稍候重试"),
    FACE_COMPARE_VS_ID_ERROR(300040, "您的面部特征与身份证不符，请检查"),
    LIVE_NESS_ERROR(300041, "静默活体检测失败"),
    GET_ID_FACE_ERROR(300042, "身份证人脸图片获取失败"),
    GENERATE_ACTION_LIVE_ERROR(300043, "生成活体动作列表失败"),
    FACE_ACTION_LIVE_NOT_EXISTS(300044, "活体动作不存在"),
    HIT_INFO_NOT_EXISTS(300045, "提示信息不存在"),
    CPTL_ACCT_OR_PWD_ERROR(300046, "账号与密码不匹配"),

    // 4XXXX 数据库异常
    DB(400001, "数据库异常"),

    // 5xxx 业务异常
    SYMMETRY_ENCRPTY_ERROR(500001, "对称加密失败"),
    NONE_SYMMETRY_ENCRPTY_ERROR(500002, "对称解密失败"),
    DATE_FORMAT_ERROR(500003, "日期格式化失败"),

    ;

    // 错误代码
    private final Integer code;
    // 错误描述
    private final String message;
}