/*
 * @(#) NoticeServiceImpl.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.service.impl;

import com.gad.domin.dto.MessageDTO;
import com.gad.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromUserName;
    @Value("${spring.mail.tousername}")
    private String toUserName;
    @Override
    public void notice(MessageDTO messageDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toUserName);
        message.setFrom(fromUserName);
        message.setSubject(messageDTO.getSubject());
        message.setText(messageDTO.getContent());
        mailSender.send(message);
    }
}
