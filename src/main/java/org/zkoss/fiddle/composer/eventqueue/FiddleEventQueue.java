package org.zkoss.fiddle.composer.eventqueue;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;

public class FiddleEventQueue {
	private EventQueue sourceQueue;

	public static FiddleEventQueue lookup(String queueName) {
		return new FiddleEventQueue(queueName);
	}

	public FiddleEventQueue(String queueName) {
		sourceQueue = EventQueues.lookup(queueName, true);
	}

	public void subscribe(FiddleEventListener<? extends Event> evtListener) {
		evtListener.setQueue(sourceQueue);
		sourceQueue.subscribe(evtListener);
	}

	protected void publish(Event evt) {
		sourceQueue.publish(evt);
	}

}
