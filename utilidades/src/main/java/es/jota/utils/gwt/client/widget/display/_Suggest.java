package es.jota.utils.gwt.client.widget.display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import es.jota.utils.gwt.client.enums.IconoEnum;
import es.jota.utils.gwt.client.widget.presenter.interfaz.ISelectValue;
import es.jota.utils.gwt.client.widget.presenter.interfaz.ISuggestSelection;
import es.jota.utils.gwt.client.widget.presenter.interfaz.ISuggestSuggestion;

public class _Suggest extends Composite implements ISuggestSuggestion.Delegate, ISuggestSelection.Delegate, ISelectValue.IDisplay {
	
	private static SuggestUiBinder uiBinder = GWT.create(SuggestUiBinder.class);
	interface SuggestUiBinder extends UiBinder<Widget, _Suggest>{}
	
	@UiField HTMLPanel selections;
	@UiField TextBox input;
	@UiField Element icono;
	@UiField HTMLPanel suggestions;
	@UiField Anchor clean;
	
	ISuggestSuggestion selectedSuggestion;
	private ISelectValue presenter;
	private int size = 5; // Default size = 5
	private boolean keepFocus = true;

	public _Suggest() {
		selectedSuggestion = null;
		initWidget(uiBinder.createAndBindUi(this));
		setSize( "" + size );
		clean.setVisible( false );
		onScroll( this, suggestions.getElement() );
		onMouse( this, getElement() );
		selections.getElement().setAttribute( "style", "margin: 0; padding: 0; display: none;" );
	}

	private native void onMouse( _Suggest x, Element e )  /*-{
		$wnd.jQuery(e).mouseleave( function() { x.@es.jota.utils.gwt.client.widgets.display.Suggest::keepFocus = false; } );
		$wnd.jQuery(e).mouseenter( function() { x.@es.jota.utils.gwt.client.widgets.display.Suggest::keepFocus = true; } );
	}-*/;

	@UiHandler("clean")
	public void clickOnClean(ClickEvent event) {
		presenter.clear( true );
	}

	@UiHandler("dropdown")
	public void clickOnDropdown(ClickEvent event) {
		input.setText( "" );
		input.setEnabled( true );
		input.setFocus( true );
		dropdown();
	}

	@UiHandler("input")
	public void blurOnInput( BlurEvent event ) {
		if ( !keepFocus ) {
			blur();
		}
	}

	@UiHandler("input")
	public void keyUpOnInput( KeyUpEvent event ) {
		if ( event.getNativeKeyCode() >= KeyCodes.KEY_A  && event.getNativeKeyCode() <= KeyCodes.KEY_Z  ||
				event.getNativeKeyCode() >= KeyCodes.KEY_ZERO  && event.getNativeKeyCode() <= KeyCodes.KEY_NINE  ||
				event.getNativeKeyCode() >= KeyCodes.KEY_NUM_ZERO  && event.getNativeKeyCode() <= KeyCodes.KEY_NUM_NINE ) {
			dropdown();
		}
		if ( selectedSuggestion != null ) {
			if ( event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE ) {
				blur();
			}
		}
	}

	private void blur() {
		presenter.blur();
		dropup();
	}

	@UiHandler("input")
	public void keyDownOnInput( KeyDownEvent event ) {
		if ( event.getNativeKeyCode() == KeyCodes.KEY_DOWN ) {
			if (  selectedSuggestion == null ) {
				dropdown();
			}
			else {
				activeSuggestion( selectedSuggestion.next() );
			}
		}
		if (  selectedSuggestion != null ) {
			if ( event.getNativeKeyCode() == KeyCodes.KEY_UP ) {
				activeSuggestion( selectedSuggestion.previous() );
			}
			if ( event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_TAB ) {
				clickOnSuggestion( selectedSuggestion );
			}
		} 
	}

	private native void onScroll( _Suggest x, Element e )  /*-{
		$wnd.jQuery(e).scroll( function() { x.@es.jota.utils.gwt.client.widgets.display.Suggest::checkScroll()(); } );
	}-*/;

	/**
	 * Comprueba si el scroll esta cerca del tope inferior para pedir mas datos
	 * @param parent
	 * @param element
	 */
	private void checkScroll() {
		Element parent = suggestions.getElement();
		int viewSize = parent.getScrollHeight();
		int viewPort = parent.getClientHeight();
		int maxScroll = viewSize - viewPort;
//		System.out.println( "viewSize = " + viewSize );
//		System.out.println( "viewPort = " + viewPort );

		int newScroll = parent.getScrollTop();
//		System.out.println( newScroll + " == " + maxScroll );
//		System.out.println( "" );

		if ( maxScroll != 0 && newScroll == maxScroll ) {
			presenter.addMoreAcceptableValues();
		}
	}

	private void dropup() {
		suggestions.removeStyleName("show");
		suggestions.clear();
		selectedSuggestion = null;
	}

	private void dropdown() {
		dropup();
		suggestions.addStyleName("show");
		suggestions.clear();
		presenter.loadAcceptableValues();
	}

	public void setSize( String stringSize ) {
		try {
			size = Integer.parseInt( stringSize );
		}catch (Exception e) {
		}
		suggestions.getElement().setAttribute("style", "max-height:" + ( 12 + size * 26 ) + "px; width:100%;"); 		
	}

	public void setPlaceholder( String placeholder ) {
		input.getElement().setAttribute( "placeholder", placeholder );
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
/// Interface ISelectValue ////////////////////////////////////////////////////////////////////////////	 
///////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setPresenter( ISelectValue presenter ) {
		this.presenter = presenter;
	}

	@Override
	public int getVisibleItems() {
		return size;
	}

	@Override
	public void setText( String value ) {
		input.setText( value );
		input.setEnabled( false );
		clean.setVisible( true );
		dropup();
	}
	
	@Override
	public String getText() {
		return input.getText();
	}
	
	@Override
	public void clear() {
		input.setText( "" );
		input.setEnabled( true );
		clean.setVisible( false );
		dropup();
		selections.clear();
		selections.setVisible( false );
	}
	
	@Override
	public void callbackRunning() {
		icono.setClassName( IconoEnum.PROCESANDO.get() );
		suggestions.add( new SuggestSuggestion( null, "Loading..." ) );
	}
	
	@Override
	public void callbackDone() {
		suggestions.remove( suggestions.getWidgetCount() - 1 );
		icono.setClassName( IconoEnum.CARET.getRaw() );
	}
	
	@Override
	public void addSuggestion( String text ) {
		suggestions.add( new SuggestSuggestion( this, text ) );
		// Si no hay ningun elemento seleccionado y hay elementos para selecionar, selecciona el primero
		if ( selectedSuggestion == null && suggestions.getWidgetCount() > 0) {
			activeSuggestion( (SuggestSuggestion) suggestions.getWidget( 0 ) );
		}
	}

	@Override
	public void addSelection( String texto ) {
		if ( selections.getWidgetCount() == 0 ) {
			selections.setVisible( true );
		}
		selections.add( new SuggestSelection( this, texto ) );
		input.setText( "" );
		dropup();
	}

	@Override
	public void removeItem( int index ) {
		selections.remove( index );
		if ( selections.getWidgetCount() == 0 ) {
			selections.setVisible( false );
		}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
/// Interface ISuggestSuggestion.Delegate /////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void clickOnSuggestion( ISuggestSuggestion suggestion ) {
		presenter.addValue( suggestion.getIndex() );
	}

	@Override
	public void activeSuggestion( ISuggestSuggestion suggestion ) {
		if ( suggestion != null && suggestion != selectedSuggestion ) {
			if ( selectedSuggestion != null ) {
				selectedSuggestion.removeStyleName("active");
			}
			suggestion.addStyleName("active");
			suggestion.scrollIntoView();
			selectedSuggestion = suggestion;
		}
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
/// Interface ISuggestSelection.Delegate /////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void clickOnSelection( ISuggestSelection selection ) {
		presenter.removeValue( selection.getIndex() );
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
}