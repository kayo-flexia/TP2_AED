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
        this.transaccionHandles = new ArrayList<>(t.length); //O(n)
        for (int i = 0; i < t.length; i++) {
            this.transaccionHandles.add(null); // Rellenar con nulls para usar set()
        } //O(nb)

        // Iterar sobre las transacciones para agregarlas a la ListaEnlazada y guardar sus handles
        for (Transaccion tx : t) {
            ListaEnlazada.HandleLE<Transaccion> listaHandle = transaccionesEnOrden.agregarAtrasConHandle(tx); //O(1)
            tx.setHandleEnLista(listaHandle); //O(1) Creo que no lo usamos

            // Usar el ID como índice en el ArrayList
            this.transaccionHandles.set(tx.id(), listaHandle); // O(1)
        } //O(nb)
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