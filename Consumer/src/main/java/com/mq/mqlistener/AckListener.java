package com.mq.mqlistener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Consumer ACK机制：
 *  1. 设置手动签收。acknowledge="manual"
 *  2. 让监听器类实现ChannelAwareMessageListener接口
 *  3. 如果消息成功处理，则调用channel的 basicAck()签收
 *  4. 如果消息处理失败，则调用channel的basicNack()拒绝签收，broker重新发送给consumer
 * @author 风亦未止
 * @date 2022/7/4 22:21
 */
@Component
public class AckListener implements ChannelAwareMessageListener {
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
            //4.拒绝签收
            channel.basicNack(deliveryTag,true,false);
            //channel.basicReject(deliveryTag,true)
        }

    }
    @Override
    public void onMessage(Message message) {
        ChannelAwareMessageListener.super.onMessage(message);
    }
}
