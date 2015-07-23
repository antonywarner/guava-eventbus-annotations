package net.antonywarner.guava.eventbus.annotations.spring;

import javax.inject.Inject;

import net.antonywarner.guava.eventbus.annotations.Subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;

/**
 * A {@link BeanPostProcessor} that registers Spring beans annotated with {@link Subscriber} with an {@link EventBus}
 *
 * @author Antony Warner
 */
public class SubscriberBeanPostProcessor implements BeanPostProcessor {
	private Logger logger = LoggerFactory.getLogger(SubscriberBeanPostProcessor.class);

	private final EventBus eventBus;

	@Inject
	public SubscriberBeanPostProcessor(final EventBus eventBus) {
		this.eventBus = Preconditions.checkNotNull(eventBus);
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Preconditions.checkNotNull(bean);
		Preconditions.checkNotNull(beanName);

		if (bean.getClass().isAnnotationPresent(Subscriber.class)) {
			eventBus.register(bean);
			logger.debug("Registered subscriber '{}'", beanName);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}