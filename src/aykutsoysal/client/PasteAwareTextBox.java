package aykutsoysal.client;

import aykutsoysal.shared.PasteEvent;
import aykutsoysal.shared.PasteEventHandler;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class PasteAwareTextBox extends TextBox {

	private EventBus eventBus;
	public PasteAwareTextBox() {
		eventBus = new SimpleEventBus();
		sinkEvents(Event.ONPASTE);
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (event.getTypeInt()) {
			case Event.ONPASTE: {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
	
					@Override
					public void execute() {
						PasteEvent pasteEvent = new PasteEvent(getText());
						fireEvent(pasteEvent);
					}
				});
				break;
			}
		}
	}

	public HandlerRegistration addPasteEventHandler(PasteEventHandler handler) {
		return addHandler(handler, PasteEvent.TYPE);
	}
}
