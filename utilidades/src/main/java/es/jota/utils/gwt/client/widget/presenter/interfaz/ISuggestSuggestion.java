package es.jota.utils.gwt.client.widget.presenter.interfaz;

public interface ISuggestSuggestion {
	public interface Delegate {
		void clickOnSuggestion( ISuggestSuggestion suggestion );
		void activeSuggestion( ISuggestSuggestion suggestion );
	}
	ISuggestSuggestion next();
	ISuggestSuggestion previous();
	int getIndex();
	void scrollIntoView();
	void removeStyleName( String className );
	void addStyleName( String className );
}