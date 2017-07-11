package es.jota.utils.gwt.client.widget.presenter;


public abstract class HasProviderPresenter<T> {
	protected interface IItemProvider<T> {
		String get( T item );
	}

	private IItemProvider<T> itemTextProvider;
	protected void setItemTextProvider( IItemProvider<T> itemTextProvider ) {
		this.itemTextProvider = itemTextProvider;
	}
	protected String getItemText( T item ) {
		return itemTextProvider.get( item );
	}
/*
	protected IItemProvider<T> itemIdProvider;
	protected void setItemIdProvider( IItemProvider<T> itemIdProvider ) {
		this.itemIdProvider = itemIdProvider;
	}

	protected String getId( T item ) {
		return itemIdProvider.get( item );
	}
/*
	protected interface IItemDescriptionProvider<T> {
		String get( T item );
	}
	protected IItemDescriptionProvider<T> itemDescriptionProvider;
	protected void setItemDescriptionProvider( IItemDescriptionProvider<T> itemDescriptionProvider ) {
		this.itemDescriptionProvider = itemDescriptionProvider;
	}*/
}