package com.test.product.trading.common.injector;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import java.util.Date;

/**
 * <p>
 * TableMetaObjectHandler
 * </p>
 *
 * @author yh
 * @since 2025-06-14
 */
@Configuration
@ConditionalOnClass(MybatisConfiguration.class)
public class TableMetaObjectHandler implements MetaObjectHandler {


    private static final String FIELD_CREATE_TIME = "createTime";
    private static final String FIELD_UPDATE_TIME = "updateTime";


    @Override
    public void insertFill(MetaObject metaObject) {

        Object fieldCreateTime = getFieldValByName(FIELD_CREATE_TIME, metaObject);
        Date date = new Date();
        try {
            if (fieldCreateTime == null) {
                setFieldValByName(FIELD_CREATE_TIME, date, metaObject);
            }
        } catch (Exception e) {

        }

        try {
            Object fieldUpdateTime = getFieldValByName(FIELD_UPDATE_TIME, metaObject);
            if (fieldUpdateTime == null) {
                setFieldValByName(FIELD_UPDATE_TIME, date, metaObject);
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        Date date = new Date();
        try {
            Object fieldValByName = getFieldValByName(FIELD_UPDATE_TIME, metaObject);
            if (fieldValByName == null) {
                setFieldValByName(FIELD_UPDATE_TIME, date, metaObject);
            }
        } catch (Exception e) {

        }
    }


}
