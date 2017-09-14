package com.foomei.common.dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageQuery {
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
    private String sortDir;
    private String searchKey;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public PageRequest buildPageRequest() {
        return buildPageRequest(1, 10);
    }

    public PageRequest buildPageRequest(int pageNo, int pageSize) {
        return buildPageRequest(pageNo, pageSize, null, null);
    }

    public PageRequest buildPageRequest(String sortBy, String sortDir) {
        return buildPageRequest(1, 10, sortBy, sortDir);
    }

    public PageRequest buildPageRequest(int pageNo, int pageSize, String sortBy, String sortDir) {
        int page = this.pageNo != null ? (this.pageNo - 1) : (pageNo - 1);
        int size = this.pageSize != null ? this.pageSize : pageSize;

        if(StringUtils.isEmpty(this.sortBy) && StringUtils.isEmpty(sortBy)) {
            return new PageRequest(page, size);
        } else {
            Sort.Direction direction = StringUtils.isNotEmpty(this.sortDir) ? Sort.Direction.fromString(this.sortDir) : Sort.Direction.fromString(sortDir);
            String[] properties =  StringUtils.isNotEmpty(this.sortBy) ? StringUtils.split(this.sortBy, ",") : StringUtils.split(sortBy, ",");
            return new PageRequest(page, size, direction, properties);
        }
    }
}
