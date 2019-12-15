package com.security.securitylearn.service;

import com.security.securitylearn.security.MetaResource;

import java.util.Set;

/**
 * @author qian
 * @date 2019/12/15 10:50
 */
public interface MetaResourceService {

    /**
     * @return uri的元数据
     */
    Set<MetaResource> getPatternAndResources();

}
