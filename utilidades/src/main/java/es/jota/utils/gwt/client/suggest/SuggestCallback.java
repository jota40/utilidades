package es.jota.utils.gwt.client.suggest;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

class SuggestCallback implements AsyncCallback<SuggestOracle.Response> {

	private SuggestOracle.Request request;
	private SuggestOracle.Callback callback;

	public SuggestCallback( SuggestOracle.Request request, SuggestOracle.Callback callback ) {
		this.request = request;
		this.callback = callback;
	}

	public void onFailure( Throwable error ) {
		callback.onSuggestionsReady( request, new SuggestOracle.Response() );
	}

	public void onSuccess( SuggestOracle.Response response ) {
		callback.onSuggestionsReady( request, response );
	}
}