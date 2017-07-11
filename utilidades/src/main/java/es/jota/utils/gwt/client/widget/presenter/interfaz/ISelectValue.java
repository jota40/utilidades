package es.jota.utils.gwt.client.widget.presenter.interfaz;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;

public interface ISelectValue {
	public interface IDisplay extends IsWidget, HasText {
		void addSelection( String item );
		void removeItem( int index );
		void clear();
		void setPresenter( ISelectValue presenter );
		void callbackRunning();
		void callbackDone();
		int getVisibleItems();
		void addSuggestion( String text );
	}
	void loadAcceptableValues();
	void addMoreAcceptableValues();
	void blur();
	void addValue( int index );

	void removeValue( int index );
	void clear( boolean fireEvent );
}