package io.bbex.bb.server.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.StandardEnvironment;

public class PlanOrderConfig {

    /**
     * 服务实例名称
     *
     * -Dbbex.instanceName=bb-order
     */
    private static final String SERVICE_INSTANCE_NAME =  (new StandardEnvironment()).getProperty("bbex.instanceName");

    private static final String DEFAULT_SERVICE_INSTANCE_NAME = "bb-order";

    public static String getServiceInstanceName() {
        if (StringUtils.isEmpty(SERVICE_INSTANCE_NAME)) {
            return DEFAULT_SERVICE_INSTANCE_NAME;
        } else {
            return SERVICE_INSTANCE_NAME;
        }
    }
}
