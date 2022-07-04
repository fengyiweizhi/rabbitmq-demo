package com.mq.config;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
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
}