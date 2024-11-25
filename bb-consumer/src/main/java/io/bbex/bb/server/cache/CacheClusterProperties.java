/*
 ************************************
 * @项目名称: guild-grpc
 * @文件名称: CacheClusterConfig
 * @Date 2018/11/04
 * @Author will.zhao@bbex.io
 * @Copyright（C）: 2018 BitBili Inc.   All rights reserved.
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的。
 **************************************
 */
package io.bbex.bb.server.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "bb.redis-cluster")
public class CacheClusterProperties {
    private List<String> shards = new ArrayList<>();

    public List<String> getShards() {
        return this.shards;
    }
}

