package com.mq.mqlistener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author 风亦未止
 * @date 2022/7/5 23:01
 */
@Component
public class DlxListener implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //1.接收消息
            System.out.println(new String(message.getBody()));
            //2.业务处理逻辑
            //TODO
            //假如出现异常
            int i=4/0;
            //3.手动签收
            channel.basicAck(deliveryTag,true);
        } catch (Exception e) {
            //4.拒绝签收 requeue=false
            channel.basicNack(deliveryTag,true,false);
            //channel.basicReject(deliveryTag,true)
        }
    }

    @Override
    public void onMessage(Message message) {
        ChannelAwareMessageListener.super.onMessage(message);
    }
}
