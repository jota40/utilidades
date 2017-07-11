package es.jota.utils.gwt.client.my;

import com.google.gwt.place.shared.Place;
import com.google.gwt.safehtml.shared.SafeHtml;

public abstract class MyPlace extends Place {
	public abstract SafeHtml getBreadcrumbs();

	public abstract boolean isInstanceOf( String clazz );

}