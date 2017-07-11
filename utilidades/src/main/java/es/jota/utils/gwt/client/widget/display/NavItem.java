package es.jota.utils.gwt.client.widget.display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import es.jota.utils.gwt.client.my.MyPlace;

public class NavItem extends Composite implements HasClickHandlers {
	private static NavItemIUiBinder uiBinder = GWT.create(NavItemIUiBinder.class);
	interface NavItemIUiBinder extends UiBinder<Widget, NavItem>{}

	@UiField Anchor link;
	@UiField Element icono;
	@UiField SpanElement text;
	private String clazz = "";
	
	public NavItem() {
		initWidget(uiBinder.createAndBindUi(this));
		getElement().setAttribute("role", "presentation");
	}
	
	public void setText( String text ) {
		this.text.setInnerHTML( text );
	}

	public void setHref( String href ) {
		if ( href != null ) {
			this.link.setHref( href );
		}
	}

	public void setClass( String className ) {
		setStyleName( className );
	}

	public void setIcono( String icono ) {
		this.icono.setClassName( icono );
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return link.addClickHandler(handler);
	}
	
	public void setPlace( String place ) {
		clazz = place;
	}

	public void checkActive( MyPlace place ){
		if ( place.isInstanceOf( clazz ) ) {
			addStyleName("active");
		} else {
			removeStyleName("active");
		}
	}
}