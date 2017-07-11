package es.jota.utils.gwt.client.widget.display;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class TBodyPanel extends HTMLPanel {

	public TBodyPanel(String html) {
		super( "tbody", html );
	}

	@Override
	public void add( Widget widget ) {
		if ( ! (widget instanceof TrWidget) ) {
			throw new IllegalStateException( "Este panel solo acepta TrWidget");
		}
		add( (TrWidget) widget);
	}

	public void add( TrWidget tr ) {
		// Detaches if necessary
		tr.removeFromParent();
		// Logical attach
		getChildren().add( tr );
		// Physical attach (all TRs)
		for ( Element element : tr.getTr() ) {
		    DOM.appendChild( getElement(), element );
//			getElement().appendChild( element );
		}
		// Adopt
		adopt( tr );
	}

	@Override
	public boolean remove( Widget widget ) {
		if ( ! (widget instanceof TrWidget) ) {
			throw new IllegalStateException( "Este panel solo acepta TrWidget");
		}
		return remove( (TrWidget) widget);
	}

	public boolean remove( TrWidget tr ) {
		// Validate.
		if (tr.getParent() != this) {
			return false;
		}
		// Orphan.
		try {
			orphan(tr);
		} finally {
			// Physical detach.
			for ( Element element : tr.getTr() ) {
				getElement().removeChild( element );
			}

			// Logical detach.
			getChildren().remove(tr);
		}
		return true;
	}
	
	public void moveUp(  TrWidget tr  ) {
		int index = getWidgetIndex( tr );
		// El widget a mover hacia arriba debe estar an la posicion numeroDeTrs y n
		if ( index > 0 && index < getWidgetCount() ) {
			List<TableRowElement> up = ( (TrWidget) getWidget( index - 1 ) ).getTr();
			// Logical swap
			getChildren().remove( index );
			getChildren().insert( tr, index - 1 );

			// Physical Swap
			for (TableRowElement element : tr.getTr() ) {
				getElement().insertBefore( element, up.iterator().next()  );
			}
		}
	}

	public void moveDown(  TrWidget tr  ) {
		int index = getWidgetIndex( tr );
		// El widget a mover hacia abajo debe estar an la posicion 0 y n-1
		if ( index > -1 && index < getWidgetCount() - 1  ) {
			List<TableRowElement> down = ( (TrWidget) getWidget( index + 1 ) ).getTr();
			// Logical swap
			getChildren().remove( index );
			getChildren().insert( tr, index + 1 );

			// Physical Swap
			for ( int i = tr.getTr().size() - 1 ; i >= 0 ; i-- ) {
				getElement().insertAfter( tr.getTr().get(i), down.get( down.size() - 1 )  );
			}
		}
	}
}