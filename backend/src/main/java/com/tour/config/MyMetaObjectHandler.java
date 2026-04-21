package com.tour.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 字段自动填充处理器
 *
 * <p>在 INSERT 时自动填充 {@code createTime} 和 {@code updateTime}，
 * 在 UPDATE 时自动更新 {@code updateTime}。
 * 实体字段需标注 {@link FieldFill} 注解才会生效。</p>
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /** 插入时填充创建时间和更新时间 */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    /** 更新时填充更新时间 */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
