package com.tc.mall.portal.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 取消订单消息的发送者
 *
 * @author honggang.liu
 */
@Component
public class CancelOrderSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderSender.class);
//    @Resource
//    private AmqpTemplate amqpTemplate;

    public void sendMessage(Long orderId, final long delayTimes) {
//        //给延迟队列发送消息
//        amqpTemplate.convertAndSend(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(), QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(), orderId, new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                //给消息设置延迟毫秒值
//                message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
//                return message;
//            }
//        });
        LOGGER.info("send orderId:{}", orderId);
    }
}
