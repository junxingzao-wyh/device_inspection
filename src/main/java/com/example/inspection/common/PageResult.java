package com.example.inspection.common;

import java.util.List;

/**
 * 统一分页返回结果，page 从 1 开始
 * 显式 getter/setter，避免依赖 Lombok
 */
public class PageResult<T> {

    private List<T> list;
    private long total;
    private int page;
    private int size;

    public List<T> getList() { return list; }
    public void setList(List<T> list) { this.list = list; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public static <T> PageResult<T> of(List<T> list, long total, int page, int size) {
        PageResult<T> result = new PageResult<>();
        result.setList(list);
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        return result;
    }
}
