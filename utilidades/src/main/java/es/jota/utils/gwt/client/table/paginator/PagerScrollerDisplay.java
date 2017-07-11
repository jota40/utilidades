package es.jota.utils.gwt.client.table.paginator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import es.jota.utils.gwt.client.table.paginator.MyPager.MyPagerView;

public class PagerScrollerDisplay extends Composite implements MyPagerView {
	
	private static PagerScrollerDisplayUiBinder uiBinder = GWT.create(PagerScrollerDisplayUiBinder.class);
	interface PagerScrollerDisplayUiBinder extends UiBinder<Widget, PagerScrollerDisplay>{}

	@UiField Anchor mas;
	private MyPager<?> Presenter;

	@UiHandler("mas")
	public void primeroYListarOnClick(ClickEvent event) {
		Presenter.nextPage();
	}

	@Override
	public void setPresenter( MyPager<?> Presenter ) {
		this.Presenter = Presenter;
	}

	public PagerScrollerDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private void setEnabled( Anchor link, boolean enabled ) {
		if ( enabled ) {
			link.getElement().getParentElement().removeClassName( "disabled" );
		} else {
			link.getElement().getParentElement().addClassName( "disabled" );
		}
	}

	@Override
	public void onRangeOrRowCountChanged() {
		setEnabled( mas, Presenter.hasNextPage() );
	}
}