package aed;

import aed.estructuras.heap.Heap;
import aed.estructuras.listaEnlazada.ListaEnlazada;

import java.util.ArrayList;

public class Bloque {
    private Heap<Transaccion> transaccionesPorMonto; // Heap de transacciones por monto
    private ListaEnlazada<Transaccion> transaccionesEnOrden; // ListaEnlazada en lugar de array para mantener el orden de inserción/otros órdenes
    // CAMBIO: Usamos un HashMap para mapear IDs a sus Handles de la ListaEnlazada
    private ArrayList<ListaEnlazada.HandleLE<Transaccion>> transaccionHandles; // Nombre más simple

    public Bloque(Transaccion[] t) { //O(nb)
        this.transaccionesPorMonto = new Heap<>(t);  //O(n)
        this.transaccionesEnOrden = new ListaEnlazada<>(); // O(1)
        
        // Esto es un fix para el caso de test que el ID de transacción no empieza en 0.
        // Por alguna razón solo tomando el ID máximo de las transacciones no funciona,
        // aunque se supone que los IDs son seguidos, siempre el último debería ser el mayor.

        // 1. Calcular máximo ID de transacción
        int maxIdTx = 0;
        for (Transaccion tx : t) {
            if (tx.id() > maxIdTx) {
                maxIdTx = tx.id();
            }
        }
        
        // 2. Crear la lista con tamaño maxIdTx + 1
        this.transaccionHandles = new ArrayList<>(maxIdTx + 1);
        
        // 3. Rellenar con nulls
        for (int i = 0; i <= maxIdTx; i++) {
            this.transaccionHandles.add(null);
        }
        
        // 4. Insertar handles en el índice del ID de la transacción
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

    public void eliminarTransaccionPorId(int id) { // O(1)
        ListaEnlazada.HandleLE<Transaccion> handle = transaccionHandles.get(id); //O(1)


        if (handle != null && handle.estaActivo()) { // Verificar si el handle existe y es válido
            transaccionesEnOrden.eliminar(handle); // Eliminar de la ListaEnlazada usando el handle (O(1))
            handle.invalidar(); // Marcar el handle como inactivo
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