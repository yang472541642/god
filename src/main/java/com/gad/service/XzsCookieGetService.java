/*
 * @(#) XzsCookieGetService.java ,2017年12月01日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.service;

import org.apache.http.Header;

/**
 * @author caojianlong
 * @title XzsCookieGetService
 * @date 2017/12/1
 * @since v1.0.0
 */
public interface XzsCookieGetService {

    /**
     * 检查浏览器
     * @throws Exception
     */
    public void chk() throws Exception;

    /**
     * 添加cookie
     * @param str
     */
    public void appendCookie(String str);

    public void appendCookie(Header... headers);


    public String getCookie();

}
