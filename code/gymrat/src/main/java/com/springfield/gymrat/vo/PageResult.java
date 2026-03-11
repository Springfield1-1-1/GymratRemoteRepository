package com.springfield.gymrat.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer pageSize;

    public PageResult() {}

    public PageResult(List<T> records, Long total) {
        this.records = records;
        this.total = total;
    }

    public PageResult(List<T> records, Long total, Integer page, Integer pageSize) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }
}
