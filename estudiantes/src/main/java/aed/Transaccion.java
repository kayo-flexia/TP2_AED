package aed;

public class Transaccion implements Comparable<Transaccion> {
    private int id;
    private int id_comprador;
    private int id_vendedor;
    private int monto;

    public Transaccion(int id, int id_comprador, int id_vendedor, int monto) {
        this.id = id;
        this.id_comprador = id_comprador;
        this.id_vendedor = id_vendedor;
        this.monto = monto;
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

    /*

    public HandleHeap<Transaccion> getHandleEnHeap() {
        return handleEnHeap;
    }

    public void setHandleEnHeap(HandleHeap<Transaccion> handle) {
        this.handleEnHeap = handle;
    }

    */

    @Override
    public int compareTo(Transaccion otro) {
        if (otro == null) { // O(1)
            String mensajeDeError = "No puede compararse con null"; // O(1)
            throw new IllegalArgumentException(mensajeDeError); // O(1)
        }

        int res = 0; // O(1)

        if (this.monto - otro.monto > 0) { // O(1)
            res = 1; // O(1)
        }

        if (this.monto - otro.monto < 0) { // O(1)
            res = -1; // O(1)
        }

        if (this.monto - otro.monto == 0) { // O(1)
            if (this.id > otro.id) { // O(1)
                res = 1; // O(1)
            }
            if (this.id < otro.id) { // O(1)
                res = -1; // O(1)
            }
            if (this.id == otro.id) { // O(1)
                res = 0; // O(1)
            }
        }

        return res; // O(1)
    }


    @Override
    public boolean equals(Object otro){
        if (this == otro) return true; // Si es la misma referencia, es el mismo
        if (otro == null || getClass() != otro.getClass()) return false;
        
        Transaccion that = (Transaccion) otro;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}