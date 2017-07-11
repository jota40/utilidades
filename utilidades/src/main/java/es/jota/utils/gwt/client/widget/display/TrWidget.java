package es.jota.utils.gwt.client.widget.display;

import java.util.List;

import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class TrWidget extends Composite {
	public abstract List<TableRowElement> getTr();
	
	protected Integer getIndex() {
		Integer dev = null;
		if ( getParent() == null) {
			// If the widget had no parent, check to see if it was in the detach list
			// and remove it if necessary.
			if (RootPanel.isInDetachList(this)) {
				RootPanel.detachNow(this);
			}
		} else if ( getParent() instanceof TBodyPanel ) {
			dev = ((TBodyPanel) getParent() ).getWidgetIndex( this );
		} else if (getParent() != null) {
			throw new IllegalStateException( "El padre de este widget debe ser de tipo TBodyPanel");
		}
		return dev;
	}

	@Override
	public void removeFromParent() {
		if ( getParent() == null) {
			// If the widget had no parent, check to see if it was in the detach list
			// and remove it if necessary.
			if (RootPanel.isInDetachList(this)) {
				RootPanel.detachNow(this);
			}
		} else if ( getParent() instanceof TBodyPanel ) {
			((TBodyPanel) getParent() ).remove(this);
		} else if (getParent() != null) {
			throw new IllegalStateException( "El padre de este widget debe ser de tipo TBodyPanel");
		}
	}

	public void moveDown() {
		if ( getParent() == null) {
			// If the widget had no parent, check to see if it was in the detach list
			// and remove it if necessary.
			if (RootPanel.isInDetachList(this)) {
				RootPanel.detachNow(this);
			}
		} else if ( getParent() instanceof TBodyPanel ) {
			((TBodyPanel) getParent() ).moveDown( this );
		} else if (getParent() != null) {
			throw new IllegalStateException( "El padre de este widget debe ser de tipo TBodyPanel");
		}
	}

	public void moveUp() {
		if ( getParent() == null) {
			// If the widget had no parent, check to see if it was in the detach list
			// and remove it if necessary.
			if (RootPanel.isInDetachList(this)) {
				RootPanel.detachNow(this);
			}
		} else if ( getParent() instanceof TBodyPanel ) {
			((TBodyPanel) getParent() ).moveUp( this );
		} else if (getParent() != null) {
			throw new IllegalStateException( "El padre de este widget debe ser de tipo TBodyPanel");
		}
	}
	
	protected String getClassNameForStrip() {
		String dev = "";
		Integer index = getIndex();
		if ( index != null ) {
			dev = index % 2 == 0 ? "par" : "impar";
		}
		return dev;
	}
}