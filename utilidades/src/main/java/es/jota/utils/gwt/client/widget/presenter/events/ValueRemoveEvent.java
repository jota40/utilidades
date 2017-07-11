package es.jota.utils.gwt.client.widget.presenter.events;

import com.google.gwt.event.shared.GwtEvent;

import es.jota.utils.gwt.client.widget.presenter.interfaz.HasValueRemoveHandlers;
public class ValueRemoveEvent<T> extends GwtEvent<ValueRemoveHandler<T>> {

	/**
	 * Handler type.
	 */
	private static Type<ValueRemoveHandler<?>> TYPE;

	/**
	 * Fires a value remove event on all registered handlers in the handler
	 * manager. If no such handlers exist, this method will do nothing.
	 * 
	 * @param <T> the old value type
	 * @param source the source of the handlers
	 * @param value the value
	 */
	public static <T> void fire(HasValueRemoveHandlers<T> source, T value) {
		if ( TYPE != null ) {
			ValueRemoveEvent<T> event = new ValueRemoveEvent<T>(value);
			source.fireEvent(event);
		}
	}

	/**
	 * Fires value change event if the old value is not equal to the new value.
	 * Use this call rather than making the decision to short circuit yourself for
	 * safe handling of null.
	 * 
	 * @param <T> the old value type
	 * @param source the source of the handlers
	 * @param oldValue the oldValue, may be null
	 * @param newValue the newValue, may be null
	 */
	public static <T> void fireIfNotEqual( HasValueRemoveHandlers<T> source, T oldValue, T newValue ) {
		if ( shouldFire(source, oldValue, newValue ) ) {
			fire( source, newValue );
		}
	}

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<ValueRemoveHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<ValueRemoveHandler<?>>();
		}
		return TYPE;
	}

	/**
	 * Convenience method to allow subtypes to know when they should fire a value
	 * change event in a null-safe manner.
	 * 
	 * @param <T> value type
	 * @param source the source
	 * @param oldValue the old value
	 * @param newValue the new value
	 * @return whether the event should be fired
	 */
	protected static <T> boolean shouldFire(HasValueRemoveHandlers<T> source, T oldValue, T newValue) {
		return oldValue != newValue && ( oldValue == null || !oldValue.equals( newValue ) );
	}

	private final T value;

	/**
	 * Creates a value change event.
	 * 
	 * @param value the value
	 */
	protected ValueRemoveEvent(T value) {
		this.value = value;
	}

	// The instance knows its BeforeSelectionHandler is of type I, but the TYPE
	// field itself does not, so we have to do an unsafe cast here.
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public final Type<ValueRemoveHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	@Override
	public String toDebugString() {
		return super.toDebugString() + getValue();
	}

	@Override
	protected void dispatch(ValueRemoveHandler<T> handler) {
		handler.onValueRemove(this);
	}
}
