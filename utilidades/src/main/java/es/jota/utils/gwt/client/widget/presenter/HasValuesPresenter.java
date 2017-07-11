package es.jota.utils.gwt.client.widget.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import es.jota.utils.gwt.client.widget.presenter.events.ValueAddEvent;
import es.jota.utils.gwt.client.widget.presenter.events.ValueAddHandler;
import es.jota.utils.gwt.client.widget.presenter.events.ValueRemoveEvent;
import es.jota.utils.gwt.client.widget.presenter.events.ValueRemoveHandler;
import es.jota.utils.gwt.client.widget.presenter.interfaz.IHasValues;
import es.jota.utils.gwt.client.widget.presenter.interfaz.ISelectValue;

public abstract class HasValuesPresenter<T> extends HasProviderPresenter<T> implements IHasValues<T> {
	private List<T> selectedValues = new ArrayList<T>();
	private ISelectValue.IDisplay display;
	private boolean multiSelection;

	public HasValuesPresenter( ISelectValue.IDisplay display ) {
		this.display = display;
		this.multiSelection = false;
	}

	public HasValuesPresenter( ISelectValue.IDisplay display, boolean multiSelection ) {
		this.display = display;
		this.multiSelection = multiSelection;
	}

	@Override
	public HandlerRegistration addValueChangeHandler( ValueChangeHandler<List<T>> handler ) {
	    return display.asWidget().addHandler( handler, ValueChangeEvent.getType()) ;
	}

	@Override
	public HandlerRegistration addValueAddHandler( ValueAddHandler<T> handler) {
	    return display.asWidget().addHandler( handler, ValueAddEvent.getType()) ;
	}
	
	@Override
	public HandlerRegistration addValueRemoveHandler( ValueRemoveHandler<T> handler) {
	    return display.asWidget().addHandler( handler, ValueRemoveEvent.getType()) ;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		display.asWidget().fireEvent(event);
	}

	public boolean isMultiSelection() {
		return multiSelection;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
/// multiSelection = true /////////////////////////////////////////////////////////////////////////////	 
///////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setValues( List<T> values, boolean fireEvents ) {
		if ( !multiSelection ) throw new RuntimeException("Este metodo solo puede usarse con la configuracion multiSelection = true");
		if ( values == null || values.isEmpty() ) {
			clear( fireEvents );
		} else {
			selectedValues.clear();
			selectedValues.addAll( values );
			for (T value : values) {
				display.addSelection( getItemText( value ) );
			}
			if ( fireEvents ) {
				ValueChangeEvent.fire( this, selectedValues );
			}
		}
	}

	@Override
	public void setValues( List<T> values ) {
		setValues( values, false );
	}

	@Override
	public List<T> getValues() {
		return selectedValues;
	}

	@Override
	public void removeValue( T value ) {
		removeValue( value, false );
	}

	@Override
	public void addValue( T value ) {
		addValue( value, false );
	}

	@Override
	public void addValue( T value, boolean fireEvents  ) {
		if ( !multiSelection ) throw new RuntimeException("Este metodo solo puede usarse con la configuracion multivalue = true");
		if ( value != null && !selectedValues.contains( value ) ) {
			display.addSelection( getItemText( value ) );
			selectedValues.add( value );
			if ( fireEvents ) {
				ValueAddEvent.fire( this, value );
			}
		}
	}

	@Override
	public void removeValue( T value, boolean fireEvents ) {
		if ( !multiSelection ) throw new RuntimeException("Este metodo solo puede usarse con la configuracion multivalue = true");
		int index = selectedValues.indexOf( value );
		if ( index != -1 ) {
			display.removeItem( index );
			selectedValues.remove( value );
			if ( fireEvents ) {
				ValueRemoveEvent.fire( this, value );
			}
		}
	}

	@Override
	public void removeValue( int index ) {
		T value = selectedValues.get( index );
		removeValue( value, true );
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////
/// multiSelection = false ////////////////////////////////////////////////////////////////////////////	 
///////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setValue( T value, boolean fireEvents ) {
		if ( multiSelection ) throw new RuntimeException("Este metodo solo puede usarse con la configuracion multiSelection = false");
		if ( value == null ) {
			clear( fireEvents );
		} else {
			selectedValues.clear();
			selectedValues.add( value );
			display.setText( getItemText( value ) );
			if ( fireEvents ) {
				ValueChangeEvent.fire( this, selectedValues );
			}
		}
	}

	@Override
	public void setValue( T value ) throws RuntimeException {
		setValue( value, false );
	}

	@Override
	public T getValue() throws RuntimeException {
		if ( multiSelection ) throw new RuntimeException("Este metodo solo puede usarse con la configuracion multivalue = false");
		if ( selectedValues.size() > 1 ) throw new RuntimeException("No puede haber más de 1 valor seleccionado");
		return selectedValues.isEmpty() ? null : selectedValues.iterator().next();
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
	public void clear() {
		clear( false );
	}

	@Override
	public void clear( boolean fireEvents ) {
		selectedValues.clear();
		display.clear();
		if ( fireEvents ) {
			ValueChangeEvent.fire( this, selectedValues );
		}
	}
}