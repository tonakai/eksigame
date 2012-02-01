package aykutsoysal.client;

import aykutsoysal.shared.PasteEvent;
import aykutsoysal.shared.PasteEventHandler;
import aykutsoysal.shared.SozlukEntry;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Eksigame implements EntryPoint {

	private static final String BUTTON_BUDUR_HOCAM = "Budur hocam";
	private static final String STYLE_BUDUR_HOCAM = "budur_hocam";
	private static final String BUTTON_GEC_BUNLARI = "Geç Bunları";
	private static final String STYLE_GEC_BUNLARI = "gec_bunlari";
	private static final String BUTTON_OLUYO_GIBI = "Oluyo gibi";
	private static final String STYLE_OLUYO_GIBI = "oluyo_gibi";
	private static final String BUTTON_ALGERIA = "Algeria! algeria!";
	private static final String STYLE_ALGERIA = "algeria_algeria";

	private static final String BASLIK_TEXT = "Başlık: ";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final CoreServiceAsync coreService = GWT.create(CoreService.class);

	private SozlukEntry currentEntry;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final RootPanel historyContainer;
		final Button sendButton = new Button(BUTTON_GEC_BUNLARI);

		final PasteAwareTextBox baslikTextBox = new PasteAwareTextBox();
		final Label baslikPreviewLabel = new Label();
		final HTML entryField = new HTML();

		final Label loadingLabel = new Label();
		loadingLabel.setText("hemen yüklüyorum bi başlık...");
		loadingLabel.getElement().setId("loading");

		final Button errorButton = new Button("Erör oluştu. tekrar dene?");
		errorButton.getElement().setId("errorButton");
		errorButton.setVisible(false);
		final AsyncCallback<SozlukEntry> callback = new AsyncCallback<SozlukEntry>() {

			@Override
			public void onSuccess(SozlukEntry result) {
				loadingLabel.setVisible(false);
				errorButton.setVisible(false);
				sendButton.setEnabled(true);
				baslikTextBox.setEnabled(true);
				baslikPreviewLabel.setText(BASLIK_TEXT + result.getHeader());
				entryField.setHTML(result.getEntry());
				currentEntry = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				errorButton.setVisible(true);
				loadingLabel.setVisible(false);
			}

		};

		coreService.fetchSozlukEntry(callback);
		// We can add style names to widgets
		sendButton.setStyleName(STYLE_GEC_BUNLARI);

		baslikTextBox.getElement().setId("baslikTextBox");
		baslikPreviewLabel.getElement().setId("baslikPreviewLabel");
		// baslikPreviewLabel.addStyleName("baslikPreviewLabel");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("entryFieldContainer").add(entryField);
		RootPanel.get("entryFieldContainer").add(errorButton);
		RootPanel.get("entryFieldContainer").add(loadingLabel);
		RootPanel.get("entryFieldContainer").add(baslikPreviewLabel);
		RootPanel.get("entryFieldContainer").add(baslikTextBox);
		RootPanel.get("entryFieldContainer").add(sendButton);

		historyContainer = RootPanel.get("historyContainer");
		// Focus the cursor on the name field when the app loads
		baslikTextBox.setFocus(true);
		baslikTextBox.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Super hile savar");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("tek tek yazarsan olur belki");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new Image("/img/copypasteyapmaoc.jpg"));
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.setGlassEnabled(true);
		dialogBox.setModal(true);
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		baslikTextBox.addPasteEventHandler(new PasteEventHandler() {
			
			@Override
			public void onPaste(PasteEvent event) {
				if (baslikPreviewLabel.getText().length()-BASLIK_TEXT.length() == event.getValue().length()) {
					dialogBox.center();
					sendButton.setEnabled(false);
				}
			}
		});
		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendAnswerToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendAnswerToServer();
				}
				String updated = BASLIK_TEXT + baslikTextBox.getText();
				if (currentEntry.getHeader().length() > baslikTextBox.getText().length()) {
					updated += currentEntry.getHeader().substring(baslikTextBox.getText().length());
				}
				baslikPreviewLabel.setText(updated);

				if (baslikTextBox.getText().trim().isEmpty()) {
					sendButton.setText(BUTTON_GEC_BUNLARI);
					sendButton.setStyleName(STYLE_GEC_BUNLARI);
				} else if (currentEntry.getHeader().length() < baslikTextBox.getText().length()) {
					sendButton.setText(BUTTON_ALGERIA);
					sendButton.setStyleName(STYLE_ALGERIA);
				} else if (baslikPreviewLabel.getText().indexOf("_") == -1) {
					sendButton.setText(BUTTON_BUDUR_HOCAM);
					sendButton.setStyleName(STYLE_BUDUR_HOCAM);
				} else {
					sendButton.setText(BUTTON_OLUYO_GIBI);
					sendButton.setStyleName(STYLE_OLUYO_GIBI);
				}
			}

			private void sendAnswerToServer() {

				sendButton.setEnabled(false);
				loadingLabel.setVisible(true);
				baslikTextBox.setEnabled(false);
				entryField.setHTML("");

				coreService.answerSozlukEntry(currentEntry.getKey(), baslikTextBox.getText(),
						new AsyncCallback<SozlukEntry>() {

							@Override
							public void onSuccess(SozlukEntry result) {
								baslikTextBox.setText("");
								sendButton.setText(BUTTON_GEC_BUNLARI);
								sendButton.setStyleName(STYLE_GEC_BUNLARI);
								baslikTextBox.setFocus(true);
								Anchor anchor = new Anchor(result.getHeader(), result.getUrl(), "_blank");
								anchor.setStylePrimaryName(result.getStatus());
								// historyContainer.add(anchor);
								historyContainer.insert(anchor, 0);
								coreService.fetchSozlukEntry(callback);
							}

							@Override
							public void onFailure(Throwable caught) {
								errorButton.setVisible(true);
								loadingLabel.setVisible(false);
								currentEntry = null;
							}
						});

			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		baslikTextBox.addKeyUpHandler(handler);

		ClickHandler errorHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				coreService.fetchSozlukEntry(callback);
			}
		};
		errorButton.addClickHandler(errorHandler);
	}
}
