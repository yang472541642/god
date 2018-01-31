/*
 * @(#) NoticeService.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.service;

import com.gad.domin.dto.MessageDTO;

/**
 * @author caojianlong
 * @title NoticeService
 * @date 2017/11/10
 * @since v1.0.0
 */
public interface NoticeService {
    /**
     * 邮件通知
     * @param dto
     */
    void notice(MessageDTO dto);
}
