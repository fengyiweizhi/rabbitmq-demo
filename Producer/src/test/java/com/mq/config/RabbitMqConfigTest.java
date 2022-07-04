package com.mq.config;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 风亦未止
 * @date 2022/7/4 16:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqConfigTest extends TestCase {

    @Autowired
    public RabbitTemplate rabbitTemplate;

    /**
     * 测试生产者
     */
    @Test
    public void test(){
        rabbitTemplate.convertAndSend("springboot_item_topic_exchange","item.insert","插入");
        rabbitTemplate.convertAndSend("springboot_item_topic_exchange","item.update","更新");
        rabbitTemplate.convertAndSend("springboot_item_topic_exchange","item.delete","删除");
    }

    /**
     * 1. 确认模式开启：ConnectionFactory中开启publisher-confirms="true"
     * 2.确认模式测试-设置回调函数
     */
    @Test
    public void testSure(){
        //定义回调函数
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 相关的配置信息
             * @param b exchange交换机 是否成功收到了消息。true 成功，false代表失败
             * @param s 接收的信息
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                if(b){
                    System.out.println("接收成功"+s);
                }else{
                    System.out.println("接收失败"+s);
                    //do some 做一些处理，让消息重新发送
                }
            }
        });
        //3.发送消息
        rabbitTemplate.convertAndSend("springboot_item_topic_exchange","item.insert","插入");
    }

    /**
     * 回退模式
     */
    @Test
    public void testReturn(){
        //设置交换机处理失败消息模式
        rabbitTemplate.setMandatory(true);
        //2.设置ReturnCallBack
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             *
             * @param message 消息对象
             * @param i 错误码
             * @param s 错误信息
             * @param s1 交换机
             * @param s2 路由键
             */
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("消息："+message);
                System.out.println("错误码："+i);
                System.out.println("错误信息："+s);
                System.out.println("交换机："+s1);
                System.out.println("路由键："+ s2);
                //do some
            }
        });
        //3.发送消息
        rabbitTemplate.convertAndSend("springboot_item_topic_exchange","item.insert","插入");
    }


    /**
     * 消息单独过期
     */
    @Test
    public void testTimeOut(){
       //消息处理对象，设置消息参数信息
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置过期时间
                message.getMessageProperties().setExpiration("100000");
                return message;
            }
        };
        //发送消息
        rabbitTemplate.convertAndSend("springboot_item_topic_exchange","item.insert","插入",messagePostProcessor);
    }
}