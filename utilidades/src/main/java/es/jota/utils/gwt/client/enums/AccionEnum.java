package es.jota.utils.gwt.client.enums;
public enum AccionEnum {
    DESCONOCIDA( -1, IconoEnum.DESCONOCIDO, ColorEnum.DESCONOCIDO ),
    BORRAR( 1, IconoEnum.BORRAR, ColorEnum.ROJO ),
    ;

    private Integer id;
    private IconoEnum icono;
    private ColorEnum color;

    
    private AccionEnum() {
        this.id = null;
        this.icono = null;
        this.color = null;
    }

    private AccionEnum( Integer id, IconoEnum icono, ColorEnum color ) {
        this.id = id;
        this.icono = icono;
        this.color = color;
    }
 
    public Integer getId() {
		return id;
	}

	public IconoEnum getIcono() {
		return icono;
	}

	public ColorEnum getColor() {
		return color;
	}

	public static AccionEnum getEnum( Object id ) {
    	try {
    		return getEnum( Integer.parseInt( id.toString() ) );
    	} catch (Exception e) {
			return DESCONOCIDA;
		}
    }
 
    private static AccionEnum getEnum( int id ) {
    	switch ( id ) {
			case 1: return BORRAR;
    	}
    	return DESCONOCIDA;
    }
}