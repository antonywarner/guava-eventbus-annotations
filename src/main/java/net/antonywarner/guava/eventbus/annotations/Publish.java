package net.antonywarner.guava.eventbus.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.common.eventbus.EventBus;

/**
 * Declares that the return value of a method invocation should be posted to an {@link EventBus}
 * 
 * @author Antony Warner
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Publish {

}
