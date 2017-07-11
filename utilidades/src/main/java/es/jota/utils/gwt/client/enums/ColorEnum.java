package es.jota.utils.gwt.client.enums;
public enum ColorEnum {
    DESCONOCIDO( -1, "" ),
    ROJO( 1, "danger" ),
    VERDE( 2, "success"),
    AZUL( 3, "primary"),
    ;

    private Integer id;
    private String color;

    private ColorEnum() {
        this.id = null;
        this.color = null;
    }

    private ColorEnum( Integer id, String color ) {
        this.id = id;
        this.color = color;
    }
 
    public Integer getId() {
		return id;
	}

	public String get() {
		return color;
	}

	public String getBtn() {
		return "btn-" + color;
	}

	public static ColorEnum getEnum( Object id ) {
    	try {
    		return getEnum( Integer.parseInt( id.toString() ) );
    	} catch (Exception e) {
			return DESCONOCIDO;
		}
    }
 
    private static ColorEnum getEnum( int id ) {
    	switch ( id ) {
			case 1: return ROJO;
			case 2: return VERDE;
			case 3: return AZUL;
    	}
    	return DESCONOCIDO;
    }
}