package es.jota.utils.gwt.client.widget.display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;

import es.jota.utils.gwt.client.widget.presenter.interfaz.ISuggestSuggestion;

public class SuggestSuggestion extends Composite implements ISuggestSuggestion {
	private static SuggestSuggestionUiBinder uiBinder = GWT.create(SuggestSuggestionUiBinder.class);
	interface SuggestSuggestionUiBinder extends UiBinder<Widget, SuggestSuggestion>{}

	@UiField Anchor link;

	private ISuggestSuggestion.Delegate delegate;

	public SuggestSuggestion( ISuggestSuggestion.Delegate delegate, String text ) {
		this.delegate = delegate;
		initWidget(uiBinder.createAndBindUi(this));
		link.getElement().setInnerSafeHtml( new SafeHtmlBuilder().appendHtmlConstant( text ).toSafeHtml() );
	}

	@UiHandler("link")
	public void onClick(ClickEvent event) {
		if ( delegate != null ) {
			delegate.clickOnSuggestion( this );
		}
	}

	@UiHandler("link")
	public void onMouseMove(MouseMoveEvent event) {
		if ( delegate != null ) {
			delegate.activeSuggestion( this );
		}
	}

	private boolean hasNext() {
		return getIndex() + 1  < ((IndexedPanel)getParent()).getWidgetCount();
	}

	private boolean hasPrevious() {
		return getIndex() - 1 >= 0;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
/// Interface ISuggestSuggestion //////////////////////////////////////////////////////////////////////	 
///////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public int getIndex() {
		return ((IndexedPanel)getParent()).getWidgetIndex( this );
	}

	@Override
	public SuggestSuggestion next() {
		SuggestSuggestion next = this;
		if ( hasNext() ) {
			next = (SuggestSuggestion) ((IndexedPanel)getParent()).getWidget( getIndex() + 1 );
		}
		return next;
	}

	@Override
	public SuggestSuggestion previous() {
		SuggestSuggestion previous = this;
		if ( hasPrevious() ) {
			previous = (SuggestSuggestion) ((IndexedPanel)getParent()).getWidget( getIndex() - 1 );
		}
		return previous;
	}

	@Override
	public void scrollIntoView() {
		getElement().scrollIntoView();
/*
		Element element = getElement();
		Element parent = element.getParentElement();

		int viewSize = parent.getScrollHeight();
		int viewPort = parent.getClientHeight();
		int maxScroll = viewSize - viewPort;
//		System.out.println( "viewSize = " + viewSize );
//		System.out.println( "viewPort = " + viewPort );

		int position = element.getOffsetTop();
		int size = element.getClientHeight();
		int newScroll = position - ( viewPort / 2 ) + (size / 2  );
		newScroll = newScroll < 0 ? 0 : newScroll;
		newScroll = newScroll > maxScroll ? maxScroll : newScroll;
		parent.setScrollTop( newScroll );

//		System.out.println( "position = " + position );
//		System.out.println( "size = " + size );
//		System.out.println( newScroll + " == " + maxScroll );
//		System.out.println( "" );
*/
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
}