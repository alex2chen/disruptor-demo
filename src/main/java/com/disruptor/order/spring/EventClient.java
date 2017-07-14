package com.disruptor.order.spring;

import com.disruptor.order.base.event.AbstractEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/6/27
 */
@Component
public class EventClient implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void publish(AbstractEvent event) {
        String eventName = StringUtils.uncapitalize(event.getClass().getSimpleName());
        IEventPublisher publisher = (IEventPublisher) applicationContext.getBean(eventName + "Publisher");
        if (publisher != null) {
            publisher.publish(event);
        }
    }
}
