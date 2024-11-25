package io.bbex.bb.server;

import io.bbex.base.account.TestReply;
import io.bbex.base.account.TestRequest;
import io.bbex.bb.server.grpc.client.GrpcTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private GrpcTestService grpcTestService;

    @RequestMapping("grpc")
    public String testGrpc() {
        TestReply testReply = grpcTestService.test(TestRequest.newBuilder().build());
        return "success";
    }

}
