package aed;

import java.util.ArrayList;

public class Bloque {
    private Heap<Transaccion> transacciones; //es un heap segun monto y NO segun id como dice el enunciado
    private Transaccion[] txPorId; //(como viene, hay que guardar en el constructor) como lista enlazada o doblemente enlazada o ArrayList, cualquiera es válida. y a esta hay que agregarle un handle


    // Tiene que ser complejidad O(Nb * Log P)
    // Nb es el número de transacciones y P es el número de usuarios
    // Acá entiendo que necesitaríamos el Handle para poder cumplir la complejidad

    public Bloque(Transaccion[] t) {
        this.transacciones = new Heap<>(t);       // COMPLEJIDAD O(n)
        this.txPorId = new Transaccion[t.length]; // Arreglo para acceder por id

        // Quedaría guardar las transacciones en el arreglo
    }

    public int montosTotales(){
        //for t in this.transacciones i < transacciones.size
            //count += t.monto
            //return count
        return 0;
    }
}
