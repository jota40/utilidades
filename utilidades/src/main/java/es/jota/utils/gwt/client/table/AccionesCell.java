package es.jota.utils.gwt.client.table;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public abstract class AccionesCell extends AbstractCell<SafeHtml> {

	public AccionesCell() {
		super( BrowserEvents.CLICK );
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, SafeHtml value, NativeEvent event, ValueUpdater<SafeHtml> valueUpdater) {
		
		// Handle the click event.
		if ( BrowserEvents.CLICK.equals( event.getType() ) ) {
			// Ignore clicks that occur outside of the outermost element.
			EventTarget eventTarget = event.getEventTarget();
			Element element = Element.as(eventTarget);
			String accionId = "";
			String accionValue = "";
			while ( element != parent && "".equals( accionId ) ) {
				accionId = element.getAttribute("accionId");
				accionValue = element.getAttribute("accionValue");
				element = element.getParentElement();
			}
			if ( !"".equals( accionId ) ) {
				doAction( accionId, accionValue );
			}
		}
	}

	@Override
	public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(value);
		}
	}

	protected abstract void doAction( String accionId, String accionValue );
}