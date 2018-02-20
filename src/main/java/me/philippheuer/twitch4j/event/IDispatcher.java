/*
 * MIT License
 *
 * Copyright (c) 2018 twitch4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.philippheuer.twitch4j.event;

/**
 * Manages event listeners and event logic.
 * @author Austin [https://github.com/austinv11]
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
public interface IDispatcher {
	/**
	 * Registers a listener using {@link EventSubscriber} method annotations.
	 * @param listener The listener.
	 */
	void registerListener(Object listener);
	/**
	 * Registers a listener using {@link EventSubscriber} method annotations.
	 * @param listener The listener.
	 */
	void registerListener(Class<?> listener);
	/**
	 * Registers a single event listener.
	 * @param listener The listener.
	 * @param <EVENT> Type of the event.
	 */
	<EVENT extends Event> void registerListener(IListener<EVENT> listener);

	/**
	 * This registers a temporary event listener using {@link EventSubscriber} method annotations.
	 * Meaning that when it listens to an event, it immediately unregisters itself.
	 * @param listener The listener.
	 */
	void registerTempListener(Object listener);
	/**
	 * This registers a temporary event listener using {@link EventSubscriber} method annotations.
	 * Meaning that when it listens to an event, it immediately unregisters itself.
	 * @param listener The listener.
	 */
	void registerTempListener(Class<?> listener);
	/**
	 * This registers a temporary single event listener.
	 * Meaning that when it listens to an event, it immediately unregisters itself.
	 * @param <EVENT> Type of the temporary event.
	 * @param listener The listener.
	 */
	<EVENT extends Event> void registerTempListener(IListener<EVENT> listener);

	/**
	 * Unregisters a listener using {@link EventSubscriber} method annotations.
	 * @param listener The listener.
	 */
	void unregisterListener(Object listener);
	/**
	 * Unregisters a listener using {@link EventSubscriber} method annotations.	 *
	 * @param listener The listener class with static methods.
	 */
	void unregisterListener(Class<?> listener);
	/**
	 * Unregisters a single event listener.
	 * @param listener The listener.
	 * @param <EVENT> Type of the event.
	 */
	<EVENT extends Event> void unregisterListener(IListener<EVENT> listener);

	/**
	 * Dispatches an event.
	 * @param event The event
	 * @param <EVENT> Type of the event.
	 */
	<EVENT extends Event> void dispatch(EVENT event);
}
