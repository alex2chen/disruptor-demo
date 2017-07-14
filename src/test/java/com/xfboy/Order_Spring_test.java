package com.xfboy;

import com.disruptor.order.base.event.OrderEvent;
import com.disruptor.order.spring.EventClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.UUID;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class Order_Spring_test {
    @Autowired
    private EventClient eventClient;

    @Test
    public void OrderEventPublishTest() throws IOException {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(UUID.randomUUID().toString());
        orderEvent.setOrderType("jd");
        orderEvent.setOrderFee(199);
        eventClient.publish(orderEvent);
        System.in.read();
    }
}
