package es.jota.utils.gwt.client.uploader.enums;
public enum ServerResponseEnum {
		TAG_WAIT,
		TAG_CANCELED,
		TAG_FINISHED,
		TAG_CURRENT_BYTES,
		TAG_UNKNOWN,
		TAG_TIME_OUT,
		TAG_ERROR;

		private String error;
		private Integer percent;

		ServerResponseEnum() {
			this.error = null;
			this.percent = null;
		}

		public String getError() {
			return error;
		}

		public ServerResponseEnum setError(String error) {
			this.error = error;
			return this;

		}

		public Integer getPercent() {
			return percent;
		}

		public ServerResponseEnum setPercent(Integer percent) {
			this.percent = percent;
			return this;
		}
		
	}