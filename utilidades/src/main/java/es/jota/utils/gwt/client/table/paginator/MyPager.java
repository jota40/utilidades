package es.jota.utils.gwt.client.table.paginator;

import java.util.List;

import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public abstract class MyPager<T> extends AbstractPager {
	public interface MyPagerView extends IsWidget {
		void setPresenter( MyPager<?> Presenter );
		void onRangeOrRowCountChanged();
	}

	private MyPagerView view;
	private AsyncDataProvider<T> provider;
	protected int initialPageSize;

	public MyPager( HasData<T> display, MyPagerView view ) {
		this.initialPageSize = display.getVisibleRange().getLength();
		this.view = view;
		view.setPresenter( this );

		setDisplay( display );

		provider = new AsyncDataProvider<T>(){
			@Override
			protected void onRangeChanged(HasData<T> display) {
				onRangeChangedHandler( display );
			}
		};
		provider.addDataDisplay( display );
	}

	public abstract void reset();
	public abstract void refresh();
	public abstract void increaseRowCount( int cantidad ) throws Exception;
	

	@Override
	public void firstPage() {
		if ( hasPreviousPage() ) {
			super.firstPage();
		}
	}

	@Override
	public void previousPage() {
		if ( hasPreviousPage() ) {
			super.previousPage();
		}
	}
	
	@Override
	public  void nextPage() {
		if ( hasNextPage() ) {
			super.nextPage();
		}
	}

	@Override
	public void lastPage() {
		if ( hasNextPage() ) {
			super.lastPage();
		}
	}
	
	@Override
	public void setPage(int index) {
		super.setPage(index);
	}
	@Override
	public boolean hasNextPage() {
		return super.hasNextPage();
	}

	@Override
	public boolean hasPreviousPage() {
		return super.hasPreviousPage();
	}

	@Override
	protected void onRangeOrRowCountChanged() {
		view.onRangeOrRowCountChanged();
	}

	@Override
	public int getPage() {
		return super.getPage();
	}
	
	@Override
	public int getPageCount() {
		return super.getPageCount();
	}

	protected void updateRowCount( int size, boolean exact ) {
		provider.updateRowCount( size, exact );
	}

	protected abstract void loadAsyncData( int start, int size, AsyncCallback<List<T>> asyncCallback );
	protected abstract void onRangeChangedHandler( HasData<T> display );
}