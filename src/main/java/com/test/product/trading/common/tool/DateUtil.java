package com.test.product.trading.common.tool;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSONObject;

public class DateUtil {

    public static final String PATTERM = "yyyy-MM-dd HH:mm:ss";

    public static final String TIME_PATTERM = "yyyyMMddHHmmss";

    public static final String YEAR_PATTERN = "yyyy-MM-dd";

    public static final String YEAR_MONTH = "yyyyMMdd";

    public static ThreadLocal<DateFormat> getThreadDateFormat(String pattern) {
        return ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
    }

    public static String tranDate2String(Date date) {
        return getThreadDateFormat(YEAR_PATTERN).get().format(date);
    }

    public static String tranDateMonth2String(Date date) {
        return getThreadDateFormat(YEAR_MONTH).get().format(date);
    }

    public static String tranDateTime2String(Date date) {
        return getThreadDateFormat(TIME_PATTERM).get().format(date);
    }

    /**
     * 日期格式转化字符串格式
     *
     * @param date 日期
     * @return 返回日期格式字符串
     */
    public static String getFormatDate(Date date) {
        return getThreadDateFormat(PATTERM).get().format(date);
    }

    public static Long timeBetween(Date startDate, Date endDate) {

        long timeDiffMs = endDate.getTime() - startDate.getTime();
        long diff = (long) Math.ceil(timeDiffMs / 1000);

/*
    final Instant startDateInstant = startDate.toInstant();
    final Instant endDateInstant = endDate.toInstant();
    long seconds = Duration.between(startDateInstant, endDateInstant).getSeconds();*/
        return diff < 0 ? 0 : diff;
    }

    /**
     * @return java.util.Date
     * @Description String字符串转化成Date
     * @Param
     * @param: str
     **/
    public static Date tranString2Date(String str) {
        try {
            return getThreadDateFormat(PATTERM).get().parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 日期加减
     * @Param date
     * @Param days
     **/
    public static String getDateAddYMD(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(calendar.getTime());
    }

    /**
     * @return 日期格式化
     * @Param
     **/
    public static Date formatDate() {
        String format = getThreadDateFormat(PATTERM).get().format(new Date());
        return tranString2Date(format);
    }

    /**
     * @return java.util.Date
     * @Description 日期区间的校验
     * @Param 2者同时存在时，开始时间不能大于结束时间
     * @param: taskStartTime
     * @param: taskEndTime
     **/
    public static boolean checkDateVaild(String taskStartTime, String taskEndTime) {
        if (StringUtils.isBlank(taskStartTime) || StringUtils.isBlank(taskEndTime)) {
            return true;
        }
        Timestamp startTime = StringUtils.isBlank(taskStartTime) ? null : Timestamp.valueOf(taskStartTime);
        Timestamp endTime = StringUtils.isBlank(taskEndTime) ? null : Timestamp.valueOf(taskEndTime);
        return !endTime.before(startTime);
    }

    /**
     * 时间戳转换
     *
     * @param resultJson json对象
     */
    public static void setTimeStampTransitionInfo(JSONObject resultJson) {
        final Long createTime = resultJson.getLong("createTime");
        final Long updateTIme = resultJson.getLong("updateTime");
        // 判读是否为空
        if (ObjectUtils.isNotEmpty(createTime) && ObjectUtils.isNotEmpty(updateTIme)) {
            Instant createInstant = Instant.ofEpochMilli(createTime);
            final Instant updateInstant = Instant.ofEpochMilli(updateTIme);
            ZonedDateTime createDateTime = ZonedDateTime.ofInstant(createInstant, ZoneId.systemDefault());
            ZonedDateTime updateDateTime = ZonedDateTime.ofInstant(updateInstant, ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            final String oneFormat = formatter.format(createDateTime);
            final String towFormat = formatter.format(updateDateTime);
            if (resultJson.containsKey("auditTime")) {
                final Long auditTime = resultJson.getLong("auditTime");
                final Instant auditTimeInfo = Instant.ofEpochMilli(auditTime);
                final ZonedDateTime auditTimeInfos = ZonedDateTime.ofInstant(auditTimeInfo, ZoneId.systemDefault());
                resultJson.put("auditTime", formatter.format(auditTimeInfos));
            }
            resultJson.put("createTime", oneFormat);
            resultJson.put("updateTime", towFormat);
        }
    }
}
