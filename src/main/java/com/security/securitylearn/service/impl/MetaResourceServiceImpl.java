package com.security.securitylearn.service.impl;

import com.security.securitylearn.security.MetaResource;
import com.security.securitylearn.service.MetaResourceService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yd
 * @date 2019/12/15 10:51
 */
@Service
public class MetaResourceServiceImpl implements MetaResourceService {

    /**
     * 这里应该是从数据库中查询出所有的uri的元数据信息
     * @return uri的元数据信息
     */
    @Override
    public Set<MetaResource> getPatternAndResources() {
        Set<MetaResource> resources = new HashSet<>();
        MetaResource a = new MetaResource();

        a.setPattern("/jwt/test");
        a.setMethod("GET");
        resources.add(a);

        MetaResource b = new MetaResource();
        b.setPattern("/jwt/accessTest");
        b.setMethod("POST");
        resources.add(b);

        return resources;
    }
}
