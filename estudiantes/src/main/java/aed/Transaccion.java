package aed;

// Importar el Handle de la Lista Enlazada
import aed.estructuras.listaEnlazada.ListaEnlazada;
import aed.estructuras.heap.HandleHeap; // Mantener si la Transaccion también necesita un handle para el Heap

public class Transaccion implements Comparable<Transaccion> {
    private int id;
    private int id_comprador;
    private int id_vendedor;
    private int monto;
    
    // CAMBIO: El handle debe ser del tipo correcto para la Lista Enlazada
    private ListaEnlazada.HandleLE<Transaccion> handleEnLista; // Capaz no se usa
    private HandleHeap<Transaccion> handleEnHeap; // Si también se necesita un handle para el Heap

    public Transaccion(int id, int id_comprador, int id_vendedor, int monto) {
        this.id = id;
        this.id_comprador = id_comprador;
        this.id_vendedor = id_vendedor;
        this.monto = monto;
        this.handleEnLista = null; // Inicializar a null
        this.handleEnHeap = null; // Inicializar a null
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

    // Renombrado para mayor claridad
    public ListaEnlazada.HandleLE<Transaccion> getHandleEnLista() {
        return handleEnLista;
    }

    // Setter para el handle de la lista
    public void setHandleEnLista(ListaEnlazada.HandleLE<Transaccion> handle) {
        this.handleEnLista = handle;
    }

    // Si también se necesita un handle para el Heap
    public HandleHeap<Transaccion> getHandleEnHeap() {
        return handleEnHeap;
    }

    public void setHandleEnHeap(HandleHeap<Transaccion> handle) {
        this.handleEnHeap = handle;
    }

    @Override
    public int compareTo(Transaccion otro) {
        if (this.monto != otro.monto) {
            return Integer.compare(this.monto, otro.monto);
        } else {
            // Se usa el ID como desempate para que transacciones con el mismo monto
            // tengan un orden consistente.
            return Integer.compare(this.id, otro.id);
        }
    }

    @Override
    public boolean equals(Object otro){
        if (this == otro) return true; // Optimización: misma referencia
        if (otro == null || getClass() != otro.getClass()) return false; // Verifica tipo y null
        
        Transaccion that = (Transaccion) otro;
        return this.id == that.id; // Comparación por ID es la correcta para igualdad
    }

    @Override
    public int hashCode() {
        // Importante sobrescribir hashCode si sobrescribes equals
        return Integer.hashCode(id);
    }
}