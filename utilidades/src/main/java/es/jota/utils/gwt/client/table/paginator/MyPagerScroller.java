package es.jota.utils.gwt.client.table.paginator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

public abstract class MyPagerScroller<T> extends MyPager<T> {
	static final Logger LOG = Logger.getLogger(MyPagerScroller.class.getName());

	public MyPagerScroller(HasData<T> display, MyPagerView view) {
		super(display, view);
	}

	@Override
	public void nextPage() {
		if ( hasNextPage() ) {
			getDisplay().setVisibleRange( 0, getDisplay().getVisibleRange().getLength() + initialPageSize );
		}
	}

	@Override
	public void reset() {
	    Range range = new Range( 0, initialPageSize );
	    if ( range.equals( getDisplay().getVisibleRange() ) ) {
			RangeChangeEvent.fire( getDisplay(), range );
	    } else {
	    	getDisplay().setVisibleRange( range );
	    }
	}

	@Override
	public void refresh() {
	    Range range = new Range( 0, getDisplay().getRowCount() );
	    if ( range.equals( getDisplay().getVisibleRange() ) ) {
			RangeChangeEvent.fire( getDisplay(), range );
	    } else {
	    	getDisplay().setVisibleRange( range );
	    }
	}

	@Override
	public void increaseRowCount( int cantidad ) throws Exception {
		throw new Exception("No es necesaio usar este metodo en el paginados con scroll");
	}

	@Override
	protected void onRangeChangedHandler( final HasData<T> display ) {
		LOG.info( "range( " + display.getVisibleRange().getStart() + ", " + display.getVisibleRange().getLength() + " )");
		int lenghtVisible = display.getVisibleRange().getLength();
		final int start;
		final int length;
		if ( lenghtVisible == initialPageSize ) {
			// Estamos pidiendo la primera pagina
			start = 0;
			length = initialPageSize;
		} else if ( lenghtVisible == display.getRowCount() ) {
			// Estamos pidiendo los mismos datos que hay visibles y por lo tanto es un refresh
			start = 0;
			length = display.getRowCount();
		} else {
			start = display.getRowCount();
			length = initialPageSize;
		}
	
		LOG.info( "loadAsyncData( " + start + ", " + length + " )");
		loadAsyncData( start, length, new AsyncCallback<List<T>>() {
			@Override
			public void onFailure( Throwable caught ) {
				LOG.log( Level.SEVERE, "", caught );
			}
			@Override
			public void onSuccess( List<T> list ) {
				if ( list != null ) {
					LOG.info( "display.setRowData( " + start + ", " + list.size() + " )");
					display.setRowData( start, list );
					LOG.info( "updateRowCount( " + ( start + list.size() ) + ", " + ( list.size() < length ) + " )\n");
					updateRowCount( start + list.size(), list.size() < length );
				}
			}
		});
	}
}