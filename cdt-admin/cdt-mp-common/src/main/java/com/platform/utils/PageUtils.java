package com.platform.utils;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1L;
    //总记录数
    private int totalCount;
    //每页记录数
    private int pageSize;
    //总页数
    private int totalPage;
    //当前页数
    private int currPage;
    //列表数据
    private List list;

    private BigDecimal totalOrderSum;

    private BigDecimal totalGoodsSum;

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public PageUtils(List list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    public PageUtils(int totalCount, int pageSize, int currPage, List list, BigDecimal totalOrderSum, BigDecimal totalGoodsSum) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
        this.currPage = currPage;
        this.list = list;
        this.totalOrderSum = totalOrderSum;
        this.totalGoodsSum = totalGoodsSum;
    }

    public PageUtils(PageInfo pageInfo) {
        this.list = pageInfo.getList();
        this.totalCount = (int) pageInfo.getTotal();
        this.pageSize = pageInfo.getPageSize();
        this.currPage = pageInfo.getPageNum();
        this.totalPage = pageInfo.getPages();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public BigDecimal getTotalOrderSum() {
        return totalOrderSum;
    }

    public void setTotalOrderSum(BigDecimal totalOrderSum) {
        this.totalOrderSum = totalOrderSum;
    }

    public BigDecimal getTotalGoodsSum() {
        return totalGoodsSum;
    }

    public void setTotalGoodsSum(BigDecimal totalGoodsSum) {
        this.totalGoodsSum = totalGoodsSum;
    }
}
