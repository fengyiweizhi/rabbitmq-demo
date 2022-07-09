package com.mq.mqlistener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 风亦未止
 * @date 2022/7/8 23:10
 */
@Component
public class MyListener {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "order_queue"),
            exchange = @Exchange(name = "order_exchange",type = ExchangeTypes.TOPIC),
            key = {"order.#"}
    ))
    public void myListener(String s){
        System.out.println(s);
    }
}
