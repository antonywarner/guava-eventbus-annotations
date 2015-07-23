package net.antonywarner.guava.eventbus.annotations.spring;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import net.antonywarner.guava.eventbus.annotations.Subscriber;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;

@RunWith(MockitoJUnitRunner.class)
public class SubscriberBeanPostProcessorTest {

	@Mock
	private EventBus eventBus;

	private SubscriberBeanPostProcessor subscriberBeanPostProcessor;

	@Before
	public void setUp() {
		subscriberBeanPostProcessor = new SubscriberBeanPostProcessor(eventBus);
	}

	@Test
	public void testSubscriber() {
		Object bean = new TestSubscriber();
		subscriberBeanPostProcessor.postProcessBeforeInitialization(bean, "testSubscriber");
		verify(eventBus).register(bean);
	}

	@Test
	public void testNonSubscriber() {
		Object bean = 1;
		subscriberBeanPostProcessor.postProcessBeforeInitialization(bean, "notASubscriber");
		verify(eventBus, never()).register(bean);
	}

	@Subscriber
	private static final class TestSubscriber {
	}
}
