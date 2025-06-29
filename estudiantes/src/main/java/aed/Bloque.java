package aed;

import aed.estructuras.heap.Heap;
import aed.estructuras.listaEnlazada.ListaEnlazada;

import java.util.ArrayList;

public class Bloque {
    private Heap<Transaccion> transaccionesPorMonto; // Heap de transacciones por monto
    private ListaEnlazada<Transaccion> transaccionesEnOrden; // ListaEnlazada en lugar de array para mantener el orden de inserción/otros órdenes
    private ArrayList<ListaEnlazada.HandleLE<Transaccion>> transaccionHandles;

    public Bloque(Transaccion[] t) { //O(nb)
        this.transaccionesPorMonto = new Heap<>(t);  //O(n)
        this.transaccionesEnOrden = new ListaEnlazada<>(); // O(1)
        
        // Esto es un fix para el caso de test que el ID de transacción no empieza en 0.
        // Por alguna razón solo tomando el ID máximo de las transacciones no funciona,
        // aunque se supone que los IDs son seguidos, siempre el último debería ser el mayor.

        //Calcular máximo ID de transacción
        int maxIdTx = 0;
        for (Transaccion tx : t) {
            if (tx.id() > maxIdTx) {
                maxIdTx = tx.id();
            }
        }
        
        //Crear la lista
        this.transaccionHandles = new ArrayList<>(maxIdTx + 1);
        
        // Rellenar con nulls
        for (int i = 0; i <= maxIdTx; i++) {
            this.transaccionHandles.add(null);
        }
        
        //Insertar handles en el índice del ID de la transacción
        for (Transaccion tx : t) {
            ListaEnlazada.HandleLE<Transaccion> listaHandle = transaccionesEnOrden.agregarAtrasConHandle(tx);
            tx.setHandleEnLista(listaHandle);
            this.transaccionHandles.set(tx.id(), listaHandle);
        }
    }


    public Heap<Transaccion> heap() {
        return transaccionesPorMonto;
    }

    public Transaccion[] getTransaccionesArray() { // O(nb)
        Transaccion[] array = (Transaccion[]) new Transaccion[transaccionesEnOrden.longitud()];
        for (int i = 0; i < transaccionesEnOrden.longitud(); i++) {
            array[i] = transaccionesEnOrden.obtener(i);
        }
        return array;
    }

    public void eliminarTransaccionPorId(int id) {
        aed.estructuras.listaEnlazada.Iterador<Transaccion> it = transaccionesEnOrden.iterador();
        int indice = 0;
        
        //Eso es O(N) y no O(N*N)
        while (it.haySiguiente()) {
            Transaccion tx = it.siguiente();
            if (tx.id() == id) {
                transaccionesEnOrden.eliminar(indice); // Eliminar por índice
                return;
            }
            indice++;
        }
    }


    public Transaccion obtenerTransaccionPorId(int id) {
        ListaEnlazada.HandleLE<Transaccion> handle = transaccionHandles.get(id);
        if (handle != null && handle.estaActivo()) {
            return handle.getValor();
        }
        return null;
    }
}