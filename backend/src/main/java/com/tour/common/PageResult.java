package com.tour.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用分页结果包装类
 *
 * <p>用于将 MyBatis-Plus 分页结果转换为前端友好的结构，
 * 包含当前页数据、总记录数、当前页码、每页大小及总页数。</p>
 *
 * @param <T> 分页记录类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    /** 当前页数据列表 */
    private List<T> records;

    /** 总记录数 */
    private long total;

    /** 当前页码（从 1 开始） */
    private long current;

    /** 每页大小 */
    private long size;

    /** 总页数 */
    private long pages;
}
