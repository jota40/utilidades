package es.jota.utils.gwt.client.table.paginator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import es.jota.utils.gwt.client.table.paginator.MyPager.MyPagerView;

public class PagerPaginationDisplay extends Composite implements MyPagerView {
	
	private static PagerPaginationDisplayUiBinder uiBinder = GWT.create(PagerPaginationDisplayUiBinder.class);
	interface PagerPaginationDisplayUiBinder extends UiBinder<Widget, PagerPaginationDisplay>{}

	@UiField HTMLPanel pagination;
	@UiField Anchor primero;
	@UiField Anchor anterior;
	@UiField Anchor ultimo;
	@UiField Anchor siguiente;

	private MyPager<?> Presenter;

	@UiHandler("primero")
	public void primeroYListarOnClick(ClickEvent event) {
		Presenter.firstPage();
	}

	@UiHandler("anterior")
	public void anteriorYNuevoOnClick(ClickEvent event) {
		Presenter.previousPage();
	}

	@UiHandler("siguiente")
	public void siguienteOnClick(ClickEvent event) {
		Presenter.nextPage();
	}

	@UiHandler("ultimo")
	public void ultimoOnClick(ClickEvent event) {
		Presenter.lastPage();
	}

	@Override
	public void setPresenter( MyPager<?> Presenter ) {
		this.Presenter = Presenter;
	}

	public PagerPaginationDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
		pagination.getElement().setAttribute( "style", "margin: 0;");
	}

	private void setEnabled( Anchor link, boolean enabled ) {
		if ( enabled ) {
			link.getElement().getParentElement().removeClassName( "disabled" );
		} else {
			link.getElement().getParentElement().addClassName( "disabled" );
		}
	}

	private void drawPagination( int actualPage, int totalPages ) {
		pagination.clear();
		int offset = 2;
		int start = actualPage - offset;
		int end = actualPage + offset + 1;
		start = start < 0 ? 0 : start;
		end = end > totalPages ? totalPages : end;
		for ( int i = start ; i < end ; i++ ) {
			addLinkToPagination( i, i == actualPage );
		}		
	}
	
	private void addLinkToPagination( final int page, boolean active ) {
		Anchor link = new Anchor( "" + ( page + 1 ) );
		link.addClickHandler( new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Presenter.setPage( page );
			}
		});
		HTMLPanel li = new HTMLPanel("li", "");
		li.setStyleName( active ? "active" : "" );
		li.add( link );
		pagination.add( li );
	}

	@Override
	public void onRangeOrRowCountChanged() {
		setEnabled( primero, Presenter.hasPreviousPage() );
		setEnabled( anterior, Presenter.hasPreviousPage() );
		setEnabled( siguiente, Presenter.hasNextPage() );
		setEnabled( ultimo, Presenter.hasNextPage() );
		drawPagination( Presenter.getPage(), Presenter.getPageCount() );
	}
}