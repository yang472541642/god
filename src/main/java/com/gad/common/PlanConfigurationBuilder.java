/*
 * @(#) PlanConfigerBuilder.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.common;

import com.gad.domin.dto.PlanConfig;
import org.springframework.stereotype.Component;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
@Component
public class PlanConfigurationBuilder {
    private PlanConfig config;

    public PlanConfig getConfig() {
        return config;
    }
}
