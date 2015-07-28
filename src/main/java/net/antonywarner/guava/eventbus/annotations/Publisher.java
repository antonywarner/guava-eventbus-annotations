package net.antonywarner.guava.eventbus.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.common.eventbus.EventBus;

/**
 * Declares a type to be an {@link EventBus} publisher
 * 
 * @author Antony Warner
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Publisher {

}
