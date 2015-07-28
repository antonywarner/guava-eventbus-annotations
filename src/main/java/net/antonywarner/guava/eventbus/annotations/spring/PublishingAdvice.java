package net.antonywarner.guava.eventbus.annotations.spring;

import javax.inject.Inject;

import net.antonywarner.guava.eventbus.annotations.Publish;
import net.antonywarner.guava.eventbus.annotations.Publisher;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;

/**
 * An {@link Aspect} containing after advice that publishes the result of a successful method invocation to an
 * {@link EventBus}. The advice is applied to each method annotated with {@link Publish} on each bean annotated with
 * {@link Publisher}
 *
 * @author Antony Warner
 */
@Aspect
public class PublishingAdvice {
	private final EventBus eventBus;

	@Inject
	public PublishingAdvice(EventBus eventBus) {
		this.eventBus = Preconditions.checkNotNull(eventBus);
	}

	@AfterReturning(pointcut = "@target(net.antonywarner.guava.eventbus.annotations.Publisher) && @annotation(net.antonywarner.guava.eventbus.annotations.Publish)", returning = "returnValue")
	public void publish(Object returnValue) {

		if (returnValue != null) {
			this.eventBus.post(returnValue);
		}
	}
}
