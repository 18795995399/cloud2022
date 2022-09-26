package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.cloudapicommons.entities.CommonResult;
import com.atguigu.springcloud.cloudapicommons.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import com.netflix.discovery.DiscoveryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("payment")
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    //    服务发现
//    @Resource
//    private DiscoveryClient discoveryClient;

    // 日志对象
    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("get/{id}")
    public CommonResult<Payment> selectOne(@PathVariable("id") Long id) {
        Payment payment = this.paymentService.queryById(id);
        logger.info("查询操作：根据ID获取数据。");
        return new CommonResult<Payment>(200, "select success， serverPort：" + serverPort, payment);
    }

    @PostMapping("create")
    public CommonResult<Payment> create(@RequestBody Payment payment) {
        logger.info(payment.toString());
        Payment insert = this.paymentService.insert(payment);
        System.out.println(insert);
        System.out.println("1234567890");
        return new CommonResult<>(200, "insert success， serverPort：" + serverPort, insert);
    }

    @GetMapping("queryAllByLimit")
    public CommonResult<List<Payment>> queryAllByLimit(@RequestParam(defaultValue = "0") int offset,
                                                       @RequestParam(defaultValue = "10") int limit) {
        List<Payment> payment = this.paymentService.queryAllByLimit(offset, limit);

        return new CommonResult<>(200, "select success， serverPort：" + serverPort, payment);
    }

    /*@GetMapping("discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        services.forEach(System.out::println);

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        instances.forEach(instance -> {
            System.out.println(instance.getServiceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
        });

        return this.discoveryClient;
    }*/

    @GetMapping("lb")
    public String getPaymentLB() {
        return serverPort;
    }

    @GetMapping("feign/timeout")
    public String getFeignTimeOut() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }

    /**
     * 链路监控
     *
     * @return
     */
    @GetMapping("zipkin")
    public String paymentZipkin() {
        return "hi,i`am paymentzipkin server fall back.welcome to atguigu.hahahahahhahahah";
    }
}
