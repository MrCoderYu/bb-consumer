package io.bbex.common;

import io.bbex.base.metrics.PrometheusMetricsCollector;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;

public class MetricsUtil {

    private static final Counter CREATE_ORDER_QUANTITY_WRONG_COUNTER = Counter.build()
            .namespace("create_order")
            .subsystem("quantity")
            .name("wrong")
            .help("wrong_quantity count of create order")
            .register();

    public static void wrongQuantityCounter() {
        CREATE_ORDER_QUANTITY_WRONG_COUNTER.inc();
    }

    private static Counter priceInvalidCounter = Counter
            .build("quote_price_invalid", "quote price is invalid")
            .labelNames("type", "symbolId")
            .register();

    public static void priceInvalidCounter(String type, String symbolId) {
        priceInvalidCounter.labels(type, symbolId).inc();
    }

    private static Histogram orderUpdateTimeLatency = Histogram.build("order_updatetime_latency", "latency in milliseconds")
            .labelNames("topic")
            .buckets(PrometheusMetricsCollector.RPC_TIME_BUCKETS)
            .register();

    public static void orderUpdateTimeLatencyObserve(String topic, long updateTime) {
        orderUpdateTimeLatency.labels(topic)
                .observe(System.currentTimeMillis() - updateTime);
    }

    private static double[] HANDLE_TIME_BUCKETS = new double[]{
            0.5, 1, 2, 3, 4, 5, 6, 7, 8,
            10,15,20,25,30,35,40,45,50,55,60,70,80,90,
            100, 500,
            1000, 5000,
            10000
    };

    /**
     * 业务处理耗时统计
     */
    public static final Histogram BUSINESS_HANDLE_HISTOGRAM = Histogram.build()
            .namespace("order")
            .subsystem("trigger")
            .name("biz_handle_histogram")
            .labelNames("bizname")
            .buckets(HANDLE_TIME_BUCKETS)
            .help("biz handle latency in milliseconds")
            .register();

}
