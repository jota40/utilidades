package es.jota.utils.gwt.client.enums;
public enum IconoEnum {
    DESCONOCIDO( -1, "" ),
    BORRAR( 1, "trash" ),
    EDITAR( 2, "edit" ),
    VER( 3, "eye-open" ),
    PROCESANDO( 4, "time" ),
    CARET( 5, "caret" ),
    ;

    private Integer id;
    private String icono;

    private IconoEnum() {
        this.id = null;
        this.icono = null;
    }

    private IconoEnum( Integer id, String icono ) {
        this.id = id;
        this.icono = icono;
    }
 
    public Integer getId() {
		return id;
	}

	public String get() {
		return "glyphicon glyphicon-" + icono;
	}

	public String getRaw() {
		return "glyphicon " + icono;
	}

	public static IconoEnum getEnum( Object id ) {
    	try {
    		return getEnum( Integer.parseInt( id.toString() ) );
    	} catch (Exception e) {
			return DESCONOCIDO;
		}
    }
 
    private static IconoEnum getEnum( int id ) {
    	switch ( id ) {
			case 1: return BORRAR;
			case 2: return EDITAR;
			case 3: return VER;
			case 4: return PROCESANDO;
			case 5: return CARET;
    	}
    	return DESCONOCIDO;
    }

	public String getSpin() {
		// TODO Auto-generated method stub
		return null;
	}
}