package aykutsoysal.shared;

import com.google.gwt.event.shared.GwtEvent;

public class PasteEvent extends GwtEvent<PasteEventHandler> {

	public static Type<PasteEventHandler> TYPE = new Type<PasteEventHandler>();
	
	private final String value;
	
	public PasteEvent(String value) {
		this.value=value;
	}

	@Override
	public Type<PasteEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PasteEventHandler handler) {
		handler.onPaste(this);
	}

	public String getValue() {
		return value;
	}

}
