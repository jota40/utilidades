package es.jota.utils.gwt.client.widget.display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Accion extends Composite implements HasClickHandlers {
	private static AccionUiBinder uiBinder = GWT.create(AccionUiBinder.class);
	interface AccionUiBinder extends UiBinder<Widget, Accion>{}

	@UiField Element icono;
	@UiField SpanElement text;
	
	public Accion() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setText( String text ) {
		this.text.setInnerHTML( text );
	}

	public void setHref( String href ) {
		if ( href != null ) {
			setHref( href );
		}
	}

	public void setClass( String className ) {
		setStyleName( className );
	}

	public void setIcono( String icono ) {
		this.icono.setClassName( icono );
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
	    return addDomHandler(handler, ClickEvent.getType());
	}
}