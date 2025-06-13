package aed;

public class Transaccion implements Comparable<Transaccion> {
    private int id;
    private int id_comprador;
    private int id_vendedor;
    private int monto;
    
    private Handle<Transaccion, Integer> handleLista;

    public Transaccion(int id, int id_comprador, int id_vendedor, int monto) {
        this.id = id;
        this.id_comprador = id_comprador;
        this.id_vendedor = id_vendedor;
        this.monto = monto;
        this.handleLista = null;
    }

    public int id() {
        return id;
    }

    public int monto() {
        return monto;
    }

    public int id_comprador() {
        return id_comprador;
    }

    public int id_vendedor() {
        return id_vendedor;
    }

    public Handle<Transaccion, Integer> handleLista() {
        return handleLista;
    }

    public void setHandleLista(Handle<Transaccion, Integer> handle) {
        this.handleLista = handle;
    }

    @Override
    public int compareTo(Transaccion otro) {
        if (this.monto != otro.monto) {
            return Integer.compare(this.monto, otro.monto);
        } else {
            return Integer.compare(this.id, otro.id);
        }
    }

    @Override
    public boolean equals(Object otro){
        if (otro instanceof Transaccion) {
            return this.id == ((Transaccion) otro).id;
        }
        return false;
    }
}
