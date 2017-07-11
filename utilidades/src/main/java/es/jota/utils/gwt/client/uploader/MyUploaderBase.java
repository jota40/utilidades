/*package es.jota.utils.gwt.client.uploader;

import static gwtupload.shared.UConsts.PARAM_BLOBKEY;
import static gwtupload.shared.UConsts.PARAM_BLOBSTORE;
import static gwtupload.shared.UConsts.PARAM_CANCEL;
import static gwtupload.shared.UConsts.PARAM_REMOVE;
import static gwtupload.shared.UConsts.PARAM_SESSION;
import static gwtupload.shared.UConsts.PARAM_SHOW;
import static gwtupload.shared.UConsts.TAG_BLOBSTORE;
import static gwtupload.shared.UConsts.TAG_BLOBSTORE_PATH;
import static gwtupload.shared.UConsts.TAG_CANCELED;
import static gwtupload.shared.UConsts.TAG_CTYPE;
import static gwtupload.shared.UConsts.TAG_CURRENT_BYTES;
import static gwtupload.shared.UConsts.TAG_FIELD;
import static gwtupload.shared.UConsts.TAG_FINISHED;
import static gwtupload.shared.UConsts.TAG_KEY;
import static gwtupload.shared.UConsts.TAG_MESSAGE;
import static gwtupload.shared.UConsts.TAG_MSG_END;
import static gwtupload.shared.UConsts.TAG_MSG_GT;
import static gwtupload.shared.UConsts.TAG_MSG_LT;
import static gwtupload.shared.UConsts.TAG_MSG_START;
import static gwtupload.shared.UConsts.TAG_NAME;
import static gwtupload.shared.UConsts.TAG_PERCENT;
import static gwtupload.shared.UConsts.TAG_SIZE;
import static gwtupload.shared.UConsts.TAG_TOTAL_BYTES;
import static gwtupload.shared.UConsts.TAG_WAIT;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;

import es.jota.utils.gwt.client.uploader.enums.ServerResponseEnum;
import es.jota.utils.gwt.client.uploader.interfaz.IUploaderForm;
import es.jota.utils.gwt.client.uploader.interfaz.IUploaderProgress;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.IUploader.UploaderConstants;
import gwtupload.client.IUploader.Utils;
import gwtupload.client.IsUpdateable;
import gwtupload.client.UpdateTimer;

public class MyUploaderBase extends HTMLPanel implements IsUpdateable {
	protected Status status;

	public interface OnFinishUploadCallback {
		void onFinish( UploadedInfo info, String fileUrl );
	}

	private ServerResponseEnum getServerResponse(String responseTxt) {
		if (responseTxt == null) {
			return null;
		}

		String error = null;
		Document doc = null;
		try {
			doc = XMLParser.parse(responseTxt);
			error = Utils.getXmlNodeValue(doc, "error");
		} catch (Exception e) {
			if (responseTxt.toLowerCase().matches("error")) {
				error = i18nStrs.uploaderServerError() + "\nAction: "
						+ uploaderForm.getFormPanel().getAction() + "\nException: " + e.getMessage()
						+ responseTxt;
			}
		}

		if (error != null) {
			return ServerResponseEnum.TAG_ERROR.setError( error );
		}
		String filename = currentUploaderProgress.getFileName();
		if (Utils.getXmlNodeValue(doc, TAG_WAIT) != null) {
			return ServerResponseEnum.TAG_WAIT;
		}
		if (Utils.getXmlNodeValue(doc, TAG_CANCELED) != null) {
			log("server response is: canceled " + filename, null);
			return ServerResponseEnum.TAG_CANCELED;
		}
		if (Utils.getXmlNodeValue(doc, TAG_FINISHED) != null) {
			log("server response is: finished " + filename, null);
			return ServerResponseEnum.TAG_FINISHED;
		}
		if (Utils.getXmlNodeValue(doc, TAG_PERCENT) != null) {
			lastData = now();
			long done = Long.valueOf(Utils.getXmlNodeValue(doc, TAG_CURRENT_BYTES)) / 1024;
			long total = Long.valueOf(Utils.getXmlNodeValue(doc, TAG_TOTAL_BYTES)) / 1024;
			log("server response transferred  " + done + "/" + total + " " + filename, null);
			return ServerResponseEnum.TAG_CURRENT_BYTES.setPercent( Utils.getPercent(done, total) );
		}
		log("incorrect response: " +  filename + " " + responseTxt, null);

		if (uploadTimeout > 0 && now() - lastData > uploadTimeout) {
			return ServerResponseEnum.TAG_TIME_OUT;
		}
		return ServerResponseEnum.TAG_UNKNOWN;

	}

////////////////////////////////////////////////////////////////////////////////////////////
//	CANCEL
////////////////////////////////////////////////////////////////////////////////////////////
	protected void cancel( IUploaderProgress uploaderProgress ) {
		if ( currentUploaderProgress != uploaderProgress ) {
			try {
				sendAjaxRequestToDeleteUploadedFile( uploaderProgress );
			} catch (Exception e) {
				log("Exception deleting request " + e.getMessage(), e);
			}
		}
		else {
			switch ( status ) {
				case DONE:
					//FIXME no se para que sirve este estado 
				break;
				case DELETED:
					// Estamos en un estado final, pero el fichero NO se ha SUBIDO 
				break;
				case ERROR: case CANCELED:  case INVALID:
					// Estamos en un estado final, pero el fichero NO se ha SUBIDO 
					setStatus( Status.DELETED );
				break;
				case CANCELING:
					// Estamos en proceso de cancelacion 
				break;
				case SUCCESS: case REPEATED:
					// Estamos en un estado final, con el fichero SUBIDO 
					try {
						sendAjaxRequestToDeleteUploadedFile( uploaderProgress );
					} catch (Exception e) {
						log("Exception deleting request " + e.getMessage(), e);
					}
				break;
				case CHANGED: case QUEUED: case SUBMITING:
					// En estos estado aun no se ha iniciado la comunicacion con el servidor
					setStatus( Status.CANCELED );
				break;
				case INPROGRESS:
								setStatus( Status.CANCELING );
								try {
									sendAjaxRequestToCancelCurrentUpload();
								} catch (Exception e) {
									log("Exception cancelling request " + e.getMessage(), e);
								}
				break;
			}
		}
	}

	private void sendAjaxRequestToCancelCurrentUpload() throws RequestException {
		RequestBuilder reqBuilder = new RequestBuilder(RequestBuilder.GET, composeURL(PARAM_CANCEL + "=true"));
		reqBuilder.sendRequest("cancel_upload", onCancelReceivedCallback);
	}
	
	private final RequestCallback onCancelReceivedCallback = new RequestCallback() {
		public void onError(Request request, Throwable exception) {
			log("onCancelReceivedCallback onError: ", exception);
			setStatus( Status.ERROR,  "onCancelReceivedCallback.error" );
		}

		public void onResponseReceived(Request request, Response response) {
			setStatus( Status.CANCELED );
		}
	};
////////////////////////////////////////////////////////////////////////////////////////////
//DELETE
////////////////////////////////////////////////////////////////////////////////////////////
	private void sendAjaxRequestToDeleteUploadedFile( final IUploaderProgress uploaderProgress ) throws RequestException {
		RequestBuilder reqBuilder = new RequestBuilder(RequestBuilder.GET, composeURL(PARAM_REMOVE + "=" + uploaderProgress.getName() ));
		reqBuilder.sendRequest("remove_file", new RequestCallback() {
			public void onError(Request request, Throwable exception) {
				log("onCancelReceivedCallback onError: ", exception);
				setStatus( uploaderProgress, Status.DELETED );
			}
	
			public void onResponseReceived(Request request, Response response) {
				setStatus( uploaderProgress, Status.DELETED );
			}
		});
	}
////////////////////////////////////////////////////////////////////////////////////////////
//VALIDATE SESION
////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sends a request to the server in order to get the session cookie, when
	 * the response with the session comes, it submits the form.
	 * 
	 * This is needed because this client application usually is part of static
	 * files, and the server doesn't set the session until dynamic pages are
	 * requested.
	 * 
	 * If we submit the form without a session, the server creates a new one and
	 * send a cookie in the response, but the response with the cookie comes to
	 * the client at the end of the request, and in the meanwhile the client
	 * needs to know the session in order to ask the server for the upload
	 * status.
	 */
/*
	private void sendAjaxRequestToValidateSession() throws RequestException {
		// Using a reusable builder makes IE fail
		RequestBuilder reqBuilder = new RequestBuilder(RequestBuilder.GET, composeURL(PARAM_SESSION + "=true"));
		reqBuilder.setTimeoutMillis(DEFAULT_AJAX_TIMEOUT);
		reqBuilder.sendRequest("create_session", onSessionReceivedCallback);
	}

	private final RequestCallback onSessionReceivedCallback = new RequestCallback() {
		public void onError(Request request, Throwable exception) {
			String message = removeHtmlTags(exception.getMessage());
			setStatus( Status.ERROR,  i18nStrs.uploaderServerUnavailable() + " (2) " + uploaderForm.getFormPanel().getAction() + "\n\n" + message );
		}
		public void onResponseReceived(Request request, Response response) {
			hasSession = true;
			try {
				String s = Utils.getXmlNodeValue( XMLParser.parse(response.getText()), TAG_BLOBSTORE);
				blobstore = "true".equalsIgnoreCase(s);
				// with blobstore status does not make sense
				if (blobstore) {
					updateStatusTimer.setInterval(5000);
				}
				uploaderForm.getFormPanel().submit();
			} catch (Exception e) {
				String message = e.getMessage().contains("error:") ? i18nStrs
						.uploaderServerUnavailable()
						+ " (3) "
						+ uploaderForm.getFormPanel().getAction()
						+ "\n\n"
						+ i18nStrs.uploaderServerError()
						+ "\nAction: "
						+ uploaderForm.getFormPanel().getAction()
						+ "\nException: "
						+ e.getMessage()
						+ response.getText() : i18nStrs.submitError();
				setStatus( Status.ERROR, message);
			}
		}
	};
////////////////////////////////////////////////////////////////////////////////////////////
//UPDATE STATUS
////////////////////////////////////////////////////////////////////////////////////////////
	public void update() {
		try {
			if (waitingForResponse) {
				return;
			}
			waitingForResponse = true;
			// Using a reusable builder makes IE fail because it caches the
			// response
			// So it's better to change the request path sending an additional
			// random parameter
			RequestBuilder reqBuilder = new RequestBuilder(RequestBuilder.GET, composeURL("filename=" + currentUploaderProgress.getName(), "c=" + requestsCounter++));
			reqBuilder.setTimeoutMillis(DEFAULT_AJAX_TIMEOUT);
			reqBuilder.sendRequest("get_status", onStatusReceivedCallback);
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handler called when the status request response comes back.
	 * 
	 * In case of success it parses the xml document received and updates the
	 * progress widget In case of a non timeout error, it stops the status
	 * repeater and notifies the user with the exception.
	 */
/*	private final RequestCallback onStatusReceivedCallback = new RequestCallback() {
		public void onError(Request request, Throwable exception) {
			waitingForResponse = false;
			if (exception instanceof RequestTimeoutException) {
				log("GWTUpload: onStatusReceivedCallback timeout error, asking the server again.", null);
			} else {
				log("GWTUpload: onStatusReceivedCallback error: " + exception.getMessage(), exception);
				String message =	removeHtmlTags(exception.getMessage()) +
									"\n" + exception.getClass().getName() +
									"\n" + exception.toString();
				setStatus( Status.ERROR,  i18nStrs.uploaderServerUnavailable() + " (4) " + uploaderForm.getFormPanel().getAction() + "\n\n" + message );
			}
		}

		public void onResponseReceived(Request request, Response response) {
			waitingForResponse = false;
			switch ( status ) {
				case DONE: case ERROR: case CANCELED: case DELETED: case INVALID: case SUCCESS: case REPEATED:
					// Estamos en un estado final, por tanto no hacemos nada 
				break;
				case CHANGED: case QUEUED: case SUBMITING:
					// Estamos en un estado de espera no deberian llegar ninguna respuesta del servidor
					// para un fichero en este estado 
				break;
				case INPROGRESS:
				case CANCELING:
					// Esta esperando una respuesta del servidor
						switch ( getServerResponse( response.getText() ) ) {
						case TAG_CANCELED: setStatus( Status.CANCELED );
						break;
						case TAG_CURRENT_BYTES: currentUploaderProgress.updatePercent( ServerResponseEnum.TAG_CURRENT_BYTES.getPercent() );
						break;
						case TAG_ERROR: setStatus( Status.ERROR,  ServerResponseEnum.TAG_ERROR.getError() );
						break;
						case TAG_TIME_OUT: cancel( currentUploaderProgress );
						break;
						case TAG_FINISHED:
						case TAG_UNKNOWN:
						case TAG_WAIT:
						break;
					}
				break;
			}
		}
	};
////////////////////////////////////////////////////////////////////////////////////////////
//BLOBSTORE RECIVE
////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sends a request to the server in order to get the blobstore path. When
	 * the response with the session comes, it submits the form.
	 */
/*	private void sendAjaxRequestToGetBlobstorePath() throws RequestException {
		RequestBuilder reqBuilder = new RequestBuilder(RequestBuilder.GET,
				composeURL(PARAM_BLOBSTORE + "=true"));
		reqBuilder.setTimeoutMillis(DEFAULT_AJAX_TIMEOUT);
		reqBuilder.sendRequest("blobstore", onBlobstoreReceivedCallback);
	}

	private final RequestCallback onBlobstoreReceivedCallback = new RequestCallback() {
		public void onError(Request request, Throwable exception) {
			String message = removeHtmlTags(exception.getMessage());
			setStatus( Status.ERROR,  i18nStrs.uploaderServerUnavailable() + " (1) " + uploaderForm.getFormPanel().getAction() + "\n\n" + message );
		}
		public void onResponseReceived(Request request, Response response) {
			String text = response.getText();
			String url = null;
			try {
				url = Utils.getXmlNodeValue(XMLParser.parse(text), TAG_BLOBSTORE_PATH);
			} catch (DOMParseException e) {
				String bpath = "<" + TAG_BLOBSTORE_PATH + ">";
				String sbpath = "</" + TAG_BLOBSTORE_PATH + ">";
				if (text.contains(bpath)) {
					url = text.replaceAll("[\r\n]+", "")
							.replaceAll("^.*" + bpath + "\\s*", "")
							.replaceAll("\\s*" + sbpath + ".*$", "");
				}
			} catch (Exception e) {
				setStatus( Status.ERROR,  i18nStrs.uploaderBlobstoreError() + "\n>>>\n" + e.getMessage() + "\n>>>>\n" + e );
				return;
			}
			if (url != null && url.length() > 0 && !"null".equalsIgnoreCase(url)) {
				uploaderForm.getFormPanel().setAction( url );
			}
			receivedBlobPath = true;
			uploaderForm.getFormPanel().submit();
		}
	};
////////////////////////////////////////////////////////////////////////////////////////////
//SUBMIT HANDLER
////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Handler called when the file form is submitted
	 * 
	 * If any validation fails, the upload process is canceled.
	 * 
	 * If the client hasn't got the session, it asks for a new one and the
	 * submit process is delayed until the client has got it
	 */
/*	protected SubmitHandler onSubmitFormHandler = new SubmitHandler() {
		public void onSubmit(SubmitEvent event) {
			if (!hasSession) {
				event.cancel();
				try {
					sendAjaxRequestToValidateSession();
				} catch (Exception e) {
					log("Exception in validateSession", null);
				}
				return;
			}
			if ( status == Status.INPROGRESS || status == Status.CANCELING || !validate() ) {
				event.cancel();
				setStatus( Status.INVALID );
				return;
			}
			//FIXME evitar que se vuelva a subir el que esta subido
			
			if (blobstore && !receivedBlobPath) {
				event.cancel();
				try {
					sendAjaxRequestToGetBlobstorePath();
				} catch (Exception e) {
					log("Exception in getblobstorePath", null);
				}
				return;
			}
			receivedBlobPath = false;

			serverResponse = null;
			serverInfo = new UploadedInfo();

			setStatus( Status.INPROGRESS );

			lastData = now();
		}
	};
////////////////////////////////////////////////////////////////////////////////////////////
//COMPLETE HANDLER
////////////////////////////////////////////////////////////////////////////////////////////
	protected SubmitCompleteHandler onSubmitCompleteHandler = new SubmitCompleteHandler() {
		public void onSubmitComplete(SubmitCompleteEvent event) {
			serverResponse = event.getResults();
			if (serverResponse != null) {
				serverResponse = serverResponse.replaceFirst(".*" + TAG_MSG_START + "([\\s\\S]*?)" + TAG_MSG_END + ".*", "$1");
				serverResponse = serverResponse.replace(TAG_MSG_LT, "<")
						.replace(TAG_MSG_GT, ">").replace("&lt;", "<")
						.replaceAll("&gt;", ">");
			}
			log("onSubmitComplete: " + serverResponse, null);
			try {
				// Parse the xml and extract serverInfo
				Document doc = XMLParser.parse(serverResponse);
				serverInfo.name = Utils.getXmlNodeValue(doc, TAG_NAME);
				serverInfo.ctype = Utils.getXmlNodeValue(doc, TAG_CTYPE);
				String size = Utils.getXmlNodeValue(doc, TAG_SIZE);
				if (size != null) {
					serverInfo.size = Integer.parseInt(size);
				}
				serverInfo.field = Utils.getXmlNodeValue(doc, TAG_FIELD);
				serverInfo.message = Utils.getXmlNodeValue(doc, TAG_MESSAGE);
				serverInfo.key = Utils.getXmlNodeValue(doc, TAG_KEY);

				// If the server response is a valid xml
				switch ( getServerResponse( serverResponse ) ) {
					case TAG_CANCELED: setStatus( Status.CANCELED );
					break;
					case TAG_CURRENT_BYTES:	String msg = i18nStrs.uploaderBadServerResponse() + "\n" + serverResponse;
											if (blobstore) {
												msg += "\n" + i18nStrs.uploaderBlobstoreBilling();
											}
											log(msg, null);
											setStatus( Status.SUCCESS );
					break;
					case TAG_ERROR: setStatus( Status.ERROR, ServerResponseEnum.TAG_ERROR.getError() );
					break;
					case TAG_TIME_OUT:	setStatus( Status.ERROR, i18nStrs.uploaderTimeout() );
					break;
					case TAG_FINISHED:	log("POST response from server has been received", null);
										setStatus( Status.SUCCESS );
					break;
					case TAG_UNKNOWN:
					break;
					case TAG_WAIT:		log("server response received, cancelling the upload " +  currentUploaderProgress.getFileName() + " " + serverResponse, null); 
										setStatus( Status.SUCCESS );
					break;
				}
			} catch (Exception e) {
				log("onSubmitComplete exception parsing response: ", e);
				// Otherwise force an ajax request so as we have not to wait to
				// the timer schedule
				updateStatusTimer.run();
			}
			startNext();
		}
	};
////////////////////////////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////////////////////////

	public static final UploaderConstants I18N_CONSTANTS = GWT.create(UploaderConstants.class);

	static HTML mlog;
	static Logger logger;
	
	private static final int DEFAULT_TIME_MAX_WITHOUT_RESPONSE = 60000;
	private static final int DEFAULT_AJAX_TIMEOUT = 10000;
	private static final int DEFAULT_UPDATE_INTERVAL = 500;

	private static int uploadTimeout = DEFAULT_TIME_MAX_WITHOUT_RESPONSE;
	private static int statusInterval = DEFAULT_UPDATE_INTERVAL;

	public static void log(String msg, Throwable e) {
		if (mlog == null) {
			if (Window.Location.getParameter("log") != null) {
				mlog = new HTML();
				RootPanel.get().add(mlog);
				log(msg, e);
			} else {
				if (logger == null) {
					logger = Logger.getLogger("Gwt client Uploader");
				}
				logger.info(msg);
				GWT.log(msg, e);
			}
		} else {
			String html = (msg + "\n" + (e != null ? e.getMessage() : ""))
					.replaceAll("\n", "<br/>");
			mlog.setHTML(mlog.getHTML() + html);
		}
	}

	/**
	 * Configure the frequency to send status requests to the server.
	 */
/*	public static void setStatusInterval(int statusInterval) {
		MyUploaderBase.statusInterval = statusInterval;
	}

	/**
	 * Configure the maximal time without a valid response from the server. When
	 * this period is reached, the upload process is canceled.
	 */
/*	public static void setUploadTimeout(int uploadTimeout) {
		MyUploaderBase.uploadTimeout = uploadTimeout;
	}

	private static long now() {
		return (new Date()).getTime();
	}

	private boolean blobstore = false;
	private boolean hasSession = false;
	private boolean receivedBlobPath = false;
	private boolean waitingForResponse = false;
	private long lastData = now();

	private int requestsCounter = 0;

	private String serverResponse = null;
	private UploadedInfo serverInfo = null;
	private OnFinishUploadCallback onFinishUploadCallback;
	private final UpdateTimer updateStatusTimer = new UpdateTimer(this, statusInterval);

	protected UploaderConstants i18nStrs = I18N_CONSTANTS;
	private IUploaderForm uploaderForm;
	private LinkedList<IUploaderProgress> queueUploaderProgress;
	private LinkedList<IUploaderProgress> doneUploaderProgress;
	private IUploaderProgress currentUploaderProgress;
	private int maxSubidasSimultaneas;

	public MyUploaderBase( IUploaderForm uploaderForm ) {
		this( uploaderForm, 1 );
	}

	public MyUploaderBase( IUploaderForm uploaderForm, int maxSubidasSimultaneas ) {
		super("");
		add( uploaderForm );
		this.maxSubidasSimultaneas = maxSubidasSimultaneas;
		queueUploaderProgress = new LinkedList<IUploaderProgress>();
		doneUploaderProgress = new LinkedList<IUploaderProgress>();

		this.uploaderForm = uploaderForm;
		uploaderForm.setUploader(this);
	
		FormPanel formPanel = uploaderForm.getFormPanel(); 
		formPanel.setEncoding( FormPanel.ENCODING_MULTIPART );
		formPanel.setMethod( FormPanel.METHOD_POST );
		formPanel.setAction("servlet.gupld");
		formPanel.addSubmitHandler( onSubmitFormHandler );
		formPanel.addSubmitCompleteHandler( onSubmitCompleteHandler );

		status = Status.UNINITIALIZED;
	}

	public void setOnFinishUploadCallback( OnFinishUploadCallback callback ) {
		this.onFinishUploadCallback = callback;
	}
	/**
	 * Returns the link for getting the uploaded file from the server It's
	 * useful to display uploaded images or generate links to uploaded files.
	 */
/*	public String fileUrl() {
		String ret = composeURL(PARAM_SHOW + "=" + currentUploaderProgress.getName());
		if (serverInfo.key != null) {
			ret += "&" + PARAM_BLOBKEY + "=" + serverInfo.key;
		}
		return ret;
	}

	private String composeURL(String... params) {
		String ret = uploaderForm.getFormPanel().getAction();
		ret = ret.replaceAll("[\\?&]+$", "");
		String sep = ret.contains("?") ? "&" : "?";
		for (String par : params) {
			ret += sep + par;
			sep = "&";
		}
		for (Entry<String, List<String>> e : Window.Location.getParameterMap()
				.entrySet()) {
			ret += sep + e.getKey() + "=" + e.getValue().get(0);
		}
		ret += sep + "random=" + Math.random();
		return ret;
	}

	private String removeHtmlTags(String message) {
		return message.replaceAll("<[^>]+>", "");
	}

	private void setStatus( Status status ) {
		setStatus( status, null );
	}

	private void setStatus( Status status, String msg ) {
		setStatus( currentUploaderProgress, status, null );
	}
	private void setStatus( IUploaderProgress uploaderProgress, Status status ) {
		setStatus( uploaderProgress, status, null );
	}

	private void setStatus( IUploaderProgress uploaderProgress, Status status, String msg ) {
		if ( currentUploaderProgress == uploaderProgress ) {
			this.status = status;
		}
		String name = uploaderProgress.getFileName() + ": ";
		switch ( status ) {
			case CHANGED: case QUEUED:
				uploaderProgress.updateText( name + i18nStrs.uploadStatusQueued() );
			break;
			case SUBMITING:
				uploaderProgress.updateText( name + i18nStrs.uploadStatusSubmitting() );
			break;
			case INPROGRESS:
			    updateStatusTimer.squeduleStart();
				uploaderProgress.updateText( name + i18nStrs.uploadStatusInProgress() );
			break;
			case CANCELING:
				updateStatusTimer.cancel();
				uploaderProgress.updateText( name + i18nStrs.uploadStatusCanceling() );
			break;
			case SUCCESS: case REPEATED: 
				updateStatusTimer.cancel();
				uploaderProgress.updateText( name + i18nStrs.uploadStatusSuccess() );
				if ( onFinishUploadCallback != null ) onFinishUploadCallback.onFinish( serverInfo, fileUrl() );
			break;
			case INVALID:
				uploaderProgress.updateText( name + i18nStrs.uploaderInvalidExtension() );
				removeProgress( uploaderProgress );
			break;
			case CANCELED:
				updateStatusTimer.cancel();
				uploaderProgress.updateText( name + i18nStrs.uploadStatusCanceled() );
			break;
			case ERROR:
				updateStatusTimer.cancel();
				uploaderProgress.updateText( name + msg );
			break;
			case DELETED:
				uploaderProgress.updateText( name + i18nStrs.uploadStatusDeleted());
				removeProgress( uploaderProgress );
			break;
		}
		uploaderProgress.updateStatus( status );
	}

	private void removeProgress( IUploaderProgress uploaderProgress) {
		((MyUploaderBaseProgress)uploaderProgress).removeFromParent();
		queueUploaderProgress.remove( uploaderProgress );
		doneUploaderProgress.remove( uploaderProgress );
	}

	private void startNext() {
		if ( queueUploaderProgress.remove( currentUploaderProgress ) ) {
			doneUploaderProgress.add( currentUploaderProgress );
		}
		currentUploaderProgress = null;
		if ( !queueUploaderProgress.isEmpty() ) {
			submit( queueUploaderProgress.getFirst() );
		} else {
			uploaderForm.getFormPanel().clear();
			status = Status.DONE;
		}
	}

	public void addToQueue( IUploaderProgress myUploaderProgress  ) {
		if ( queueUploaderProgress.size() + doneUploaderProgress.size()  < maxSubidasSimultaneas ) {
			add( myUploaderProgress );
			myUploaderProgress.setUploader( this );
			queueUploaderProgress.add( myUploaderProgress );
			setStatus( myUploaderProgress, Status.CHANGED );
		}
	}

	public void tryStart() {
		switch ( status ) {
			case CHANGED: case QUEUED:
			case SUBMITING:
			case INPROGRESS:
			case CANCELING:
			case SUCCESS: case REPEATED: 
			case INVALID:
			case CANCELED:
			case ERROR:
			case DELETED:
			break;
			case UNINITIALIZED:
			case DONE:
				startNext();
			break;
		}
	}

	private void submit( IUploaderProgress uploaderProgress ) {
		currentUploaderProgress = uploaderProgress;
		setStatus( Status.SUBMITING );
		uploaderForm.set( uploaderProgress.getFileUpload() );
		uploaderForm.getFormPanel().submit();
	}


	protected boolean validate() {
		return currentUploaderProgress.validate();
	}

	public boolean canAddNewFile() {
		return queueUploaderProgress.size() + doneUploaderProgress.size() < maxSubidasSimultaneas;
	}
}*/