package net.antonywarner.guava.eventbus.annotations.spring;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;

@RunWith(MockitoJUnitRunner.class)
public class PublishingAdviceTest {

	@Mock
	private EventBus eventBus;

	private PublishingAdvice publishingAdvice;

	@Before
	public void setUp() {
		publishingAdvice = new PublishingAdvice(eventBus);
	}

	@Test
	public void testPosts() {
		publishingAdvice.publish(1);
		verify(eventBus).post(1);
	}

	@Test
	public void testDoesPostNull() {
		publishingAdvice.publish(null);
		verify(eventBus, never()).post(Mockito.any());
	}
}
