package es.jota.utils.gwt.client.widget.presenter.interfaz;

import java.util.List;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;

public interface IHasValues<T> extends HasValueChangeHandlers<List<T>>, HasValueAddHandlers<T>, HasValueRemoveHandlers<T> {
	void setValue( T value ) throws RuntimeException;
	void setValue( T value, boolean fireEvents ) throws RuntimeException;

	void setValues( List<T> values ) throws RuntimeException;
	void setValues( List<T> values, boolean fireEvents ) throws RuntimeException;
	
	void clear();
	void clear( boolean fireEvent );

	void removeValue( int index ) throws RuntimeException;
	void removeValue( T value ) throws RuntimeException;
	void removeValue( T value, boolean fireEvents ) throws RuntimeException;

	void addValue( T value ) throws RuntimeException;
	void addValue( T value, boolean fireEvents ) throws RuntimeException;

	T getValue() throws RuntimeException;
	List<T> getValues() throws RuntimeException;
}