package net.antonywarner.guava.eventbus.annotations.spring;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

import javax.inject.Inject;

import net.antonywarner.guava.eventbus.annotations.Publish;
import net.antonywarner.guava.eventbus.annotations.Publisher;
import net.antonywarner.guava.eventbus.annotations.Subscriber;
import net.antonywarner.guava.eventbus.annotations.spring.PublishingAdviceITest.PublishingAdviceITestConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PublishingAdviceITestConfig.class })
public class PublishingAdviceITest {
	private static final int NO_OF_LATEST_POSTS = 3;
	private static final String BLOG_POST = "Blog post %s";

	@Inject
	Blog publisher;

	@Inject
	RSSFeed subscriber;

	@Test
	public void testPublish() {
		int totalNumberOfPosts = 8;

		assertThat(subscriber.getLatestBlogEntries().size(), is(0));
		int count = 1;

		// Fill up the RSS feed:
		for (; count <= NO_OF_LATEST_POSTS; count++) {
			publisher.post(String.format(BLOG_POST, count));
			assertThat(subscriber.getLatestBlogEntries().size(), is(count));
		}

		// Add more posts, removing older entries:
		for (int i = 0; i < (totalNumberOfPosts - NO_OF_LATEST_POSTS); i++) {
			publisher.post(String.format(BLOG_POST, count++));
			assertThat(subscriber.getLatestBlogEntries().size(), is(NO_OF_LATEST_POSTS));
		}
		String latestPost = subscriber.getLatestBlogEntries().iterator().next().toString();
		assertThat(latestPost, is(String.format(BLOG_POST, (totalNumberOfPosts))));
	}

	@Configuration
	@EnableAspectJAutoProxy
	static class PublishingAdviceITestConfig {

		@Bean
		EventBus eventBus() {
			return new EventBus();
		}

		@Bean
		Blog publisher() {
			return new Blog();
		}

		@Bean
		RSSFeed subscriber() {
			return new RSSFeed(NO_OF_LATEST_POSTS);
		}

		@Bean
		PublishingAdvice publishingAdvice() {
			return new PublishingAdvice(eventBus());
		}

		@Bean
		SubscriberBeanPostProcessor subscriberBeanPostProcessor() {
			return new SubscriberBeanPostProcessor(eventBus());
		}
	}

	@Publisher
	static class Blog {

		public Blog() {
		}

		@Publish
		public BlogEntry post(String content) {
			return new BlogEntry(content);
		}
	}

	static class BlogEntry {
		private final String content;

		public BlogEntry(String content) {
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}

	@Subscriber
	static class RSSFeed {
		private final Deque<BlogEntry> latestBlogEntries;

		RSSFeed(int numberOfLatestBlogEntries) {
			latestBlogEntries = new LinkedBlockingDeque<>(numberOfLatestBlogEntries);
		}

		Collection<BlogEntry> getLatestBlogEntries() {
			return Collections.unmodifiableCollection(latestBlogEntries);
		}

		@Subscribe
		public void onBlogEntry(BlogEntry blogEntry) {

			synchronized (latestBlogEntries) {

				while (!latestBlogEntries.offerFirst(blogEntry)) {
					latestBlogEntries.removeLast();
				}
			}
		}
	}
}
