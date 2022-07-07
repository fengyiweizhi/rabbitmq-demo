package com.mq.mqlistener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 监听消息
 * @author 风亦未止
 * @date 2022/1/9 19:18
 */
@Component
public class MqListener {

    /**
     * 监听某个队列的消息
     * @param message
     */
    @RabbitListener(queues = "order_queue")
    public void mqListener(String message){
        System.out.println(message);
    }

}
