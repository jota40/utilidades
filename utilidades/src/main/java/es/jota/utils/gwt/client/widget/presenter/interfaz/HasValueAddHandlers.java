package es.jota.utils.gwt.client.widget.presenter.interfaz;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

import es.jota.utils.gwt.client.widget.presenter.events.ValueAddHandler;

public interface HasValueAddHandlers<T> extends HasHandlers {
	HandlerRegistration addValueAddHandler( ValueAddHandler<T> handler );
}