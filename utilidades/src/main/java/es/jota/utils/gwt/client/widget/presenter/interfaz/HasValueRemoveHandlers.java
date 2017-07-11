package es.jota.utils.gwt.client.widget.presenter.interfaz;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

import es.jota.utils.gwt.client.widget.presenter.events.ValueRemoveHandler;

public interface HasValueRemoveHandlers<T> extends HasHandlers {
	HandlerRegistration addValueRemoveHandler( ValueRemoveHandler<T> handler );
}