/*package es.jota.utils.gwt.client.uploader;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;

import es.jota.utils.gwt.client.uploader.interfaz.IUploaderForm;
import es.jota.utils.gwt.client.uploader.interfaz.IUploaderProgress;

public abstract class MyUploaderBaseForm extends Composite implements IUploaderForm {
	
	private MyUploaderBase uploader;
	private FileUpload fileUpload;
	private boolean autoSubmit = true;

	@Override
	public void setUploader(MyUploaderBase uploader) {
		this.uploader = uploader;
	}

	protected void setAutoSubmit(boolean autoSubmit) {
		this.autoSubmit = autoSubmit;
	}

	protected void openFileSelection() {
		if ( uploader.canAddNewFile() )	{
			set( new FileUpload() );
			fileUpload.addChangeHandler( new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					IUploaderProgress uploadProgress = instanceUploaderProgress();
					uploadProgress.setFileUpload( fileUpload );
					uploader.addToQueue( uploadProgress );
					getFormPanel().clear();
					if ( autoSubmit ) {
						start();
					}
				}
			});
			InputElement.as( fileUpload.getElement() ).click();
		}
		else {
			//TODO mostrar error
		}
	}

	protected abstract IUploaderProgress instanceUploaderProgress();

	protected void start() {
		uploader.tryStart();
	}
	
	public void set( FileUpload fileUpload ) {
		this.fileUpload = fileUpload;
		getFormPanel().clear();
		getFormPanel().add( fileUpload );
	}
}*/