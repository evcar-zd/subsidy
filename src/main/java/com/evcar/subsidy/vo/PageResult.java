package com.evcar.subsidy.vo;

/**
 * Created by Kong on 2017/6/1.
 */
public class PageResult {
    private Object content ;
    private Integer pageNo ;    //当前页
    private Long totalElement ; //总页数

    public PageResult(){
        this.pageNo = 0 ;
        this.totalElement = 0L ;
    }

    public PageResult(Object content,Integer pageNo,Long totalElement){
        this.content = content ;
        this.pageNo = pageNo ;
        this.totalElement = totalElement ;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(Long totalElement) {
        this.totalElement = totalElement;
    }
}
