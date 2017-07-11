package es.jota.utils.gwt.client.widget.presenter.events;

import com.google.gwt.event.shared.EventHandler;

public interface ValueRemoveHandler<T> extends EventHandler {
  void onValueRemove(ValueRemoveEvent<T> event);
}
