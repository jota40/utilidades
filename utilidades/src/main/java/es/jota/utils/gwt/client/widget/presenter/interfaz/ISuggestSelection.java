package es.jota.utils.gwt.client.widget.presenter.interfaz;

public interface ISuggestSelection {
	public interface Delegate {
		void clickOnSelection(ISuggestSelection selection);
	}

	int getIndex();
}