package es.jota.utils.gwt.client.table.paginator;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.RangeChangeEvent;

public abstract class MyPagerPaginator<T> extends MyPager<T> {

	static final Logger LOG = Logger.getLogger(MyPagerPaginator.class.getName());

	public MyPagerPaginator(HasData<T> display, MyPagerView view) {
		super(display, view);
	}

	public void reset() {
		if ( getPage() == 0 ) {
			RangeChangeEvent.fire( getDisplay(), getDisplay().getVisibleRange() );
		} else {
			super.setPage( 0 );
		}
	}

	public void refresh() {
		RangeChangeEvent.fire( getDisplay(), getDisplay().getVisibleRange() );
	}
	
	@Override
	public void increaseRowCount( int cantidad ) {
		int oldPage = getPage();
		updateRowCount( getDisplay().getRowCount() + cantidad, getDisplay().isRowCountExact() );
		// si no hay cambio de pagina forzamos el refresco
		if ( oldPage == getPage() ) {
			refresh();
		}
	}

	@Override
	protected void onRangeChangedHandler( final HasData<T> display ) {
		LOG.info( "range( " + display.getVisibleRange().getStart() + ", " + display.getVisibleRange().getLength() + " )");
		final int start = display.getVisibleRange().getStart();
		final int length = display.getVisibleRange().getLength();
		LOG.info( "loadAsyncData( " + start + ", " + length + " )");
		loadAsyncData( start, length, new AsyncCallback<List<T>>() {
			@Override
			public void onFailure( Throwable caught ) {
				LOG.severe( "error" );
			}
			@Override
			public void onSuccess( List<T> list ) {
				if ( list != null ) {
					int totalSize = 0;
					if ( list.size() < length ) {
						// ultima pagina
						totalSize = start + list.size();
					} else {
						// pagina intermedia
						totalSize = Math.max( display.getRowCount(), start + list.size() );
					}
					LOG.info( "display.setRowData( " + start + ", " + list.size() + " )");
					display.setRowData( start, list );
					LOG.info( "updateRowCount( " + totalSize + ", " + ( list.size() < length ) + " )\n");
					updateRowCount( totalSize, list.size() < length );
				}
			}
		});
	}
}