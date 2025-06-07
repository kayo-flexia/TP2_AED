package aed;

import java.util.ArrayList;

import estructuras.Heap;
import estructuras.ListaEnlazada;

public class Bloque {
    // private ArrayList<Transaccion> transacciones;
    private Heap<Transaccion> transacciones; //es un heap segun monto y NO segun id como dice el enunciado
    private ListaEnlazada<Transaccion> txPorId; //(como viene, hay que guardar en el constructor) como lista enlazada o doblemente enlazada o ArrayList, cualquiera es válida. y a esta hay que agregarle un handle

    public Bloque(Transaccion[] t) {
        //this.transacciones = New Heap t; o algo asi
        //this.txPorId = new EstructuraElegida t
    }

    public int montosTotales(){
        //for t in this.transacciones i < transacciones.size
            //count += t.monto
            //return count
        return 0;
    }
}
