package es.jota.utils.gwt.client.widget.display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;

import es.jota.utils.gwt.client.widget.presenter.HasValuesPresenter;
/*
public class MyCheckBox extends HasValuesPresenter<Boolean> {

	private static MyCheckBoxUiBinder uiBinder = GWT.create(MyCheckBoxUiBinder.class);
	interface MyCheckBoxUiBinder extends UiBinder<Widget, MyCheckBox> {}

	@UiField Anchor link;
	@UiField InputElement input;
	@UiField SpanElement text;
	private String color;
	private Object id;

	public MyCheckBox() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("link")
	public void onClick(ClickEvent event) {
		Element e = event.getNativeEvent().getEventTarget().cast();
		String name = e.getTagName();
		if ( "INPUT".equals(name) ) {
			if (input.isChecked() ) {
				setValue( Boolean.TRUE, true );
			} else {
				setValue( Boolean.FALSE, true );
			}
		}
	}
	
	public void setText( String text ) {
		this.text.setInnerHTML( text );
	}

	@Override
	protected void renderSetValue( Boolean value ) {
		if ( value != null && value ) {
			input.setChecked( true );
			text.removeClassName("label-orden-filter");
			if ( color != null ) {
				text.setAttribute( "style", "background-color:" + color );
			}
		} else {
			input.setChecked( false );
			text.addClassName("label-orden-filter");
			text.removeAttribute( "style" );
		}
	}
	
	public void setStyle( String style ) {
		link.getElement().setAttribute( "style", style);
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getText() {
		return this.text.getInnerHTML();
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}
}*/