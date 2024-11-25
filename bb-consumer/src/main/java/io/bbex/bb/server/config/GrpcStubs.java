package io.bbex.bb.server.config;

import com.toobit.tbsc.client.RpcClient;
import com.toobit.tbsc.client.RpcClientFactory;
import com.toobit.tbsc.common.config.ConsumerConfig;
import com.toobit.tbsc.common.config.grpc.GrpcClientChannelOptions;
import com.toobit.tbsc.registry.ServiceDiscovery;
import io.bbex.base.account.FuturesServerGrpc;
import io.bbex.base.account.OrderServiceGrpc;
import io.bbex.base.account.TestServiceGrpc;
import io.bbex.base.common.MessageServiceGrpc;
import io.bbex.base.grpc.client.channel.IGrpcClientPool;
import io.bbex.base.grpc.tbsc.ChannelManager;
import io.bbex.base.quote.QuoteServiceGrpc;
import io.bbex.base.token.SymbolServiceGrpc;
import io.bbex.base.token.TokenServiceGrpc;
import io.bbex.bb.service.BBLocalCacheService;
import io.bbex.bb.service.LocalCacheService;
import io.bbex.broker.grpc.order.FuturesOrderServiceGrpc;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GrpcStubs implements BBLocalCacheService.CacheServiceInitListener, LocalCacheService.CacheServiceInitListener {

    @Autowired
    IGrpcClientPool pool;

    @Autowired
    GrpcClientProperties grpcClientProperties;

    @Autowired
    private GrpcClientChannelOptions grpcClientChannelOptions;

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    private static final String BB_PROVIDER = "bb-provider";

    @PostConstruct
    public void init() {
        grpcClientProperties.getList().forEach(client -> {
            pool.setShortcut(client.getName(), client.getHost(), client.getPort());
            // init for nacos
            ConsumerConfig consumerConfig = new ConsumerConfig();
            consumerConfig.setProvider(client.getName());
            RpcClient rpcClient = RpcClientFactory.getOrCreateRpcClient(consumerConfig,
                    grpcClientChannelOptions, serviceDiscovery, null);
            rpcClient.start();
        });
    }

    public TestServiceGrpc.TestServiceBlockingStub testServiceBlockingStub() {
        return TestServiceGrpc.newBlockingStub(RpcClientFactory.getRpcClient(BB_PROVIDER)).withDeadlineAfter(10, TimeUnit.SECONDS);
    }


    @Override
    public String quoteChannelShortcut() {
        return "";
    }

//    @Override
//    public String marginServiceChannelShortcut() {
//        return "";
//    }
}
