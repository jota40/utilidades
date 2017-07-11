/*package es.jota.utils.gwt.client.uploader;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;

import es.jota.utils.gwt.client.uploader.interfaz.IUploaderProgress;
import gwtupload.client.IUploader.Utils;

public abstract class MyUploaderBaseProgress extends Composite implements IUploaderProgress {

	private static String prefix = "GWTU";
	
	private MyUploaderBase uploader;
	private FileUpload fileUpload;

	@Override
	public void setFileUpload( FileUpload fileUpload ) {
		this.fileUpload = fileUpload;
		fileUpload.setName( ( prefix + "-" + Math.random() ).replaceAll("\\.", "") );
	}

	@Override
	public void setUploader(MyUploaderBase uploader) {
		this.uploader = uploader;
	}

	protected void cancel() {
		uploader.cancel( this );
	}

	@Override
	public String getBaseName() {
		return Utils.basename( fileUpload.getFilename() );
	}

	@Override
	public FileUpload getFileUpload() {
		return fileUpload;
	}

	public String getFileName() {
		return fileUpload.getFilename();
	}

	@Override
	public String getName() {
		return fileUpload.getName();
	}
}*/