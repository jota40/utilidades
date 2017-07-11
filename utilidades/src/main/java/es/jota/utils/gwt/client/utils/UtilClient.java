package es.jota.utils.gwt.client.utils;

import java.math.BigDecimal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;


public class UtilClient {

	public interface MyTemplates extends SafeHtmlTemplates {
		@Template("<a href='{0}' class='btn btn-default btn-xs'><span class='{1}'/></a> <a href='{2}'>{3}</a>")
		SafeHtml mainLinkCell( String urlEdit, String icono, String urlView, String text );

		@Template("<a href='{0}' class='btn btn-default btn-xs'><span class='{1}'/></a> <a href='{2}' class='btn btn-default btn-xs'><span class='{3}'/></a> {4}")
		SafeHtml linkCell( String urlEdit, String iconoEdit, String urlView, String iconoView, String text );

		@Template("<a accionId='{0}' accionValue='{1}' class='pull-right btn btn-default btn-xs {2}'><span class='{3}'/></a>")
		SafeHtml accionDelegate( String id, String value, String className, String icono );

		@Template("<a href='{0}' class='pull-right btn btn-default btn-xs {1}'><span class='{2}'/></a>")
		SafeHtml accionLink( String url, String className, String icono );

		@Template("<li class='active'>{0}</li>")
		SafeHtml breadcrumbs1( String uno );

		@Template("<li><a href='{0}'>{1}</a></li><li class='active'>{2}</li>")
		SafeHtml breadcrumbs2( String unoHref, String uno, String dos );

		@Template("<li><a href='{0}'>{1}</a></li><li><a href='{2}'>{3}</a></li><li class='active'>{4}</li>")
		SafeHtml breadcrumbs3( String unoHref, String uno, String dosHref, String dos, String tres );
	}

	public static final MyTemplates TEMPLATES = GWT.create(MyTemplates.class);

	public static class JS {
		public static native void panelSlide(Element element) /*-{
			$wnd.jQuery(element).hover(
	            function () {
	               $wnd.jQuery(element).stop().css('overflow','auto').animate({width:'25%',right:'0'},200);
	            },
	            function () {
	                $wnd.jQuery(element).stop().css('overflow','hidden').animate({width:'0',right:'-1.1%'},200); 
	            }
	        );
		}-*/;
		public static native void show( Element element, boolean show ) /*-{
			if ( show ) {
				$wnd.$(element).show()
			} else {
				$wnd.$(element).hide()
			}
		}-*/;

		public static native void show( Element element, boolean show, int delay ) /*-{
			if ( show ) {
				$wnd.$(element).show( delay )
			} else {
				$wnd.$(element).hide( delay )
			}
		}-*/;
		public static native void show( String className, boolean show ) /*-{
			if ( show ) {
				$wnd.$("."+className).show()
			} else {
				$wnd.$("."+className).hide()
			}
		}-*/;
	}

	public static class Numeros {
		public static String number2String( Number numero ) {
			return number2String( numero, true );
		}

		public static String number2String( Number numero, String sufijo ) {
			return number2String( numero, true, sufijo );
		}

		public static String number2String( Number numero, boolean separadorDeMillares ) {
			return number2String( numero, true, null );
		}
	
		public static String number2String( Number numero, boolean separadorDeMillares, String sufijo ) {
			int numeroDeDecimales = 0;
			if ( numero instanceof Double || numero instanceof Float || numero instanceof BigDecimal ) {
				numeroDeDecimales = 2;
			}
			return number2String( numero, separadorDeMillares, numeroDeDecimales, sufijo );
		}

		public static String number2String( Number numero, boolean separadorDeMillares, int numeroDeDecimales, String sufijo ) {
			String dev = "";
			try {
				if ( numero != null ) {
					sufijo = sufijo == null ? "" : (" '" + sufijo );
					dev = NumberFormat.getFormat( getParteEntera( separadorDeMillares ) + getParteDecimal( numeroDeDecimales ) + sufijo ).format(numero);
				}
			} catch (Exception e) {}
			return dev;
		}

		private static String getParteEntera( boolean separadorDeMillares ) {
			return separadorDeMillares ? "#,##0" : "0";
		}

		private static String getParteDecimal( int numeroDeDecimales ) {
			String dev = numeroDeDecimales <= 0 ? "" : ".";
			for (int i = 0; i < numeroDeDecimales; i++) {
				dev += "0";
			}
			return dev;
		}
		
		public static BigDecimal string2BigDecimal( String numero ) {
			return string2BigDecimal( numero, true );
		}

		public static BigDecimal string2BigDecimal( String numero, boolean separadorDeMillares ) {
			return string2BigDecimal( numero, separadorDeMillares, 2 );
		}

		public static BigDecimal string2BigDecimal( String numero, boolean separadorDeMillares, int numeroDeDecimales ) {
			BigDecimal dev = null;
			try {
				dev = new BigDecimal( NumberFormat.getFormat( getParteEntera( separadorDeMillares ) + getParteDecimal( numeroDeDecimales ) ).parse( numero ) );
			}catch (Exception e) {}
			return dev;
		}
	}

	public static class Cadena {
		public static SafeHtml string2SafeHtml( String string ) {
			return new SafeHtmlBuilder().appendEscapedLines( string ).toSafeHtml();
		}
	}
}
/*
$wnd.jQuery(element).show();
$wnd.jQuery(element).find("li.active").removeClass("active");
$wnd.jQuery(element).find("li:first").addClass("active");
 */
