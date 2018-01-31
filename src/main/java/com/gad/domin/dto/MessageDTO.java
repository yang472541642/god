/*
 * @(#) MessageDTO.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.domin.dto;

import java.io.Serializable;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
public class MessageDTO implements Serializable {
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 邮件主题
     */
    private String subject;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
