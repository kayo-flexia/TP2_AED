package aed;

import java.util.ArrayList;

public class Berretacoin {
    private ArrayList<Bloque> bloques; //Es un array de Heaps
    private Usuario maximoTenedor; //si cada ve z que agregamos un bloque llamamos una funcion chiquita que busque el maximo tenedor de dicho bloque, lo comparamos con esta prop. Entonces el método maximoTenedor sólo hace un return y es O(1).
    //No se si llamar una función 'a' dentro de otra funcion 'b' hace que varíe la complejidad. Creo que sólo ocurre si O(b) es más compleja que O(a). Por ejemplo, b tiene complejidad O(n) y a tiene complejidad O(1) por separado. Llamar a 'b' dentro de 'a' hace que 'a' pase a complejidad O(n)
    private ArrayList<Usuario> usuarios; //para lógica de maximoTenedor, HEAP segun el saldo de c/u
    
    public Berretacoin(int n_usuarios){
        this.usuarios = new ArrayList<>();
        for (int i = 0; i < n_usuarios; i++) {
            usuarios.add(new Usuario(i)); // según el apunte modulos_basicos: colaDePriodidadDesdeSecuencia cuesta O(n)
            //despues de esto, this.usuarios está ordenado según el saldo de cada usuario
        }
    }

    public void agregarBloque(Transaccion[] transacciones){
        Bloque b = new Bloque(transacciones); // colaDePriodidadDesdeSecuencia cuesta O(n)
        this.bloques.add(b); //creo que es O(1) pq es un array de HEAPS
        for (Transaccion transaccion : transacciones) { // itera nb (cantidad de transacciones)
            actualizarMonto(transaccion); //O(1)
            //this.maximoTenedor = this.usuarios.consultarMax() this.usuarios es HEAP, cuesta Según el apunte es O(1)
        }
    }

    public void actualizarMonto(Transaccion t) {
        Usuario comprador = usuarios.get(t.id_comprador());
        Usuario vendedor = usuarios.get(t.id_vendedor());
        //obtener es O(1)

        comprador.actualizarSaldo(-1*t.monto()); //O(1)
        vendedor.actualizarSaldo(t.monto());
    }

    public Transaccion txMayorValorUltimoBloque(){
        throw new UnsupportedOperationException("Implementar!");
    }

    public Transaccion[] txUltimoBloque(){
        throw new UnsupportedOperationException("Implementar!");
    }

    public int maximoTenedor(){
        return this.maximoTenedor.id();
    }

    public int montoMedioUltimoBloque(){
        throw new UnsupportedOperationException("Implementar!");
    }

    public void hackearTx(){
        throw new UnsupportedOperationException("Implementar!");
    }
}
