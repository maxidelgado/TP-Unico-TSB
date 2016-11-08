/*
 *Clase sencilla que representa un objeto palabra.
 *Necesaria para poder mostrar la información completa en
 *la tabla de la ventana principal.
 */
package soporte;

/**
 *
 * @author Rodrigo Gomez
 */
public class Word {

    private String palabra;
    private int id;
    private int cantidad;
    private int origenes;

    public Word(int id, String palabra, int cantidad, int origenes) {
        this.id = id;
        this.palabra = palabra;
        this.cantidad = cantidad;
        this.origenes = origenes;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getOrigenes() {
        return origenes;
    }

    public void setOrigenes(int origenes) {
        this.origenes = origenes;
    }

    @Override
    public String toString() {
        return "Word{" + "palabra=" + palabra + ", id=" + id + ", cantidad=" + cantidad + ", origenes=" + origenes + '}';
    }

}
