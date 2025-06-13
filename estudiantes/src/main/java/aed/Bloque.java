package aed;

public class Bloque {
    private Heap<Transaccion> transacciones; // Heap de transacciones por monto
    private Transaccion[] txPorId;           // Acceso directo por id de transacción

    public Bloque(Transaccion[] t) {
        this.transacciones = new Heap<>(t);           // O(n)
        
        int maxId = 0;
        for (Transaccion tx : t) {
            if (tx.id() > maxId) {
                maxId = tx.id(); // buscamos el id más alto para dimensionar el array
            }
        }

        this.txPorId = new Transaccion[maxId + 1]; // nos aseguramos de que entren todos los ids

        for (Transaccion tx : t) {
            int id = tx.id();
            txPorId[id] = tx;

            // Creamos y guardamos un Handle a la posición en txPorId
            Handle<Transaccion, Integer> handle = new Handle<>(id);
            tx.setHandleLista(handle); // suponiendo que implementás esto en la clase Transaccion
        }
    }

    public int montosTotales() {
        int suma = 0;
        for (int i = 0; i < txPorId.length; i++) {
            if (txPorId[i] != null) {
                suma += txPorId[i].monto();
            }
        }
        return suma;
    }

    public Heap<Transaccion> heap() {
        return transacciones;
    }

    public Transaccion[] transaccionesPorId() {
        return txPorId;
    }


    // método para eliminar la ultima transaccion
    public void eliminarTransaccionPorId(Transaccion tx) {
    int id = tx.id();
    if (id >= 0 && id < txPorId.length && txPorId[id] == tx) {
        txPorId[id] = null;  // Eliminamos la referencia para que ya no se considere
    }
}

}
