package es.jota.utils.gwt.client.widget.display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

import es.jota.utils.gwt.client.widget.presenter.interfaz.ISuggestSelection;

public class SuggestSelection extends Composite implements ISuggestSelection {
	private static SuggestSelectionUiBinder uiBinder = GWT.create(SuggestSelectionUiBinder.class);
	interface SuggestSelectionUiBinder extends UiBinder<Widget, SuggestSelection>{}

	@UiField Anchor link;
	@UiField SpanElement text;

	private ISuggestSelection.Delegate delegate;

	public SuggestSelection( ISuggestSelection.Delegate delegate, String text ) {
		this.delegate = delegate;
		initWidget(uiBinder.createAndBindUi(this));
		this.text.setInnerHTML( text );
	}

	@UiHandler("link")
	public void onClick(ClickEvent event) {
		delegate.clickOnSelection( this );
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
/// Interface ISuggestSelection ///////////////////////////////////////////////////////////////////////	 
///////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public int getIndex() {
		return ((IndexedPanel)getParent()).getWidgetIndex( this );
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
}