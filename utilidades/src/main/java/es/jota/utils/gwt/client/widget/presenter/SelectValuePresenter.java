package es.jota.utils.gwt.client.widget.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.jota.utils.gwt.client.widget.presenter.interfaz.IAcceptableValuesProvider;
import es.jota.utils.gwt.client.widget.presenter.interfaz.ISelectValue;

public abstract class SelectValuePresenter<T> extends HasValuesPresenter<T> implements ISelectValue {
	private List<T> acceptableValues;
	private int start = 0;
	private int size = 8; // Default size = 8
	private boolean done;
	private long timeStamp;
	private ISelectValue.IDisplay display;
	private IAcceptableValuesProvider acceptableValuesProvider;

	public SelectValuePresenter( ISelectValue.IDisplay display ) {
		this( display, false );
	}

	public SelectValuePresenter( ISelectValue.IDisplay display, boolean multiSelection ) {
		super( display, multiSelection );
		this.display = display;
		display.setPresenter( this );
		size = display.getVisibleItems() + 2;
		acceptableValues = new ArrayList<T>();
	}
	
	protected void setAcceptableValuesProvider( IAcceptableValuesProvider acceptableValuesProvider ) {
		this.acceptableValuesProvider = acceptableValuesProvider;
	}

	protected void setAcceptableValues( List<T> acceptableValues, long timestamp ) {
		display.callbackDone();
		if ( this.timeStamp == timestamp ) {
			start += acceptableValues.size();
			done = acceptableValues.size() < size;
			acceptableValues.removeAll( getValues() );
			this.acceptableValues.addAll( acceptableValues );
			for (T acceptableValue : acceptableValues) {
				display.addSuggestion( marker( getItemText( acceptableValue ), display.getText(), "<strong><u>", "</u></strong>") );
			}
		}
	}

	protected String marker( String text, String input, String openTag, String closeTag ) {
		String dev = "";
		input = input.toLowerCase();
		if ( !input.isEmpty() ) {
			int index = text.toLowerCase().indexOf( input );
			while ( index >= 0 && !text.isEmpty() ) {
				dev += text.substring( 0, index ) + openTag + text.substring( index, index + input.length() ) + closeTag;
				text = text.substring( index + input.length() );
				index = text.toLowerCase().indexOf( input );
			}
		}
		return dev + text;
	}

	protected List<T> match( List<T> acceptableValues, String query ) {
		List<T> dev = new ArrayList<T>();
		for ( T acceptableValue : acceptableValues ) {
			if ( getItemText( acceptableValue ).toLowerCase().contains( query.toLowerCase() ) ) {
				dev.add( acceptableValue );
			}
		}
		return dev;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
/// Interface SelectValue /////////////////////////////////////////////////////////////////////////////	 
///////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void loadAcceptableValues() {
		start = 0;
		done = false;
		acceptableValues.clear();
		addMoreAcceptableValues();
	}

	@Override
	public void addMoreAcceptableValues() {
		if ( !done ) {
			display.callbackRunning();
			timeStamp = new Date().getTime();
			acceptableValuesProvider.load( start, size + getValues().size() , display.getText(), timeStamp );
		}
	}

	@Override
	public void blur() {
		if ( !isMultiSelection() ) {
			setValue( getValue() );
		}
	}

	@Override
	public void addValue( int index ) {
		if ( isMultiSelection() ) {
			addValue( acceptableValues.get( index ), true );
		} else {
			setValue( acceptableValues.get( index ), true );
		}
	}
}