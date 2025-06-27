package aed;

import aed.estructuras.heap.Heap;

import java.util.ArrayList;

public class Bloque {
    private Heap<Transaccion> transaccionesPorMonto; // Heap de transacciones por monto
    private Transaccion[] transaccionesEnOrden; // ListaEnlazada en lugar de array para mantener el orden de inserción/otros órdenes

    public Bloque(Transaccion[] t) { //O(nb)
        this.transaccionesPorMonto = new Heap<>(t);  //O(n)
        this.transaccionesEnOrden = new Transaccion[t.length]; // O(1)


        for (int i = 0; i < t.length; i++) {
            // TODO que sea una copia de la tx
            transaccionesEnOrden[i] = t[i]; // O (Nb)
        }
        
    }


    public Heap<Transaccion> heap() {
        return transaccionesPorMonto;
    }

    public Transaccion[] getTransaccionesArray() { // O(nb)
        ArrayList<Transaccion> resArrayList = new ArrayList<>(transaccionesEnOrden.length); // O(nb)?
        // Copiamos el array salteando las transacciones que son Null
        // Por que si las hackeamos están eliminadas
        for (int i = 0; i < transaccionesEnOrden.length; i++) { // O(nb)
            if (transaccionesEnOrden[i] != null) {
                resArrayList.add(transaccionesEnOrden[i]);
            }
        }

        Transaccion[] transaccionesRes = new Transaccion[resArrayList.size()]; // O(1)

        for (int i = 0; i < resArrayList.size(); i++) { // O (Nb)
            transaccionesRes[i] = resArrayList.get(i); // get es O (1)
        }

        return transaccionesRes;
    }

    public void eliminarTransaccionPorId(int id) { // O(1)
        transaccionesEnOrden[id] = null;
    }

    public Transaccion obtenerTransaccionPorId(int id) { // O(1)
        return transaccionesEnOrden[id];
    }
}