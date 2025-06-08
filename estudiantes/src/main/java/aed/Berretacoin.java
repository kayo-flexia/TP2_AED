package aed;

import java.util.ArrayList;

public class Berretacoin {
    private ArrayList<Bloque> bloques; //Es un array de bloque, y bloque.transaccion es un heap según el monto
    private Usuario maximoTenedor; //usuarios es un heap asi que obtener la raiz es O(1)
    private ArrayList<Usuario> usuarios; //es un HEAP segun el saldo de c/u
    //private ArrayList<Usuario> usuariosArreglo; //ordenados por arreglo SIN ser heap
    private int montosTotalesUltimoBloque;
    
    public Berretacoin(int n_usuarios){
        this.usuarios = new ArrayList<>();
        for (int i = 0; i < n_usuarios; i++) {
            usuarios.add(new Usuario(i)); // según el apunte modulos_basicos: colaDePriodidadDesdeSecuencia cuesta O(n)
            //despues de esto, this.usuarios está ordenado según el saldo de cada usuario
        }

        this.maximoTenedor = usuarios.get(0);
        this.bloques = new ArrayList<>();
    }

    public void agregarBloque(Transaccion[] transacciones){
        Bloque b = new Bloque(transacciones); // colaDePriodidadDesdeSecuencia cuesta O(n), ver el constructor de clase Bloque
        this.bloques.add(b); //creo que es O(1) pq es un array de HEAPS
        for (Transaccion transaccion : transacciones) { // itera nb (cantidad de transacciones)
            actualizarMonto(transaccion); //O(log p)
            //this.maximoTenedor = this.usuarios.consultarMax() this.usuarios es HEAP, cuesta Según el apunte es O(1)
        }

        this.maximoTenedor = usuarios.get(0);
        this.montosTotalesUltimoBloque = b.montosTotales(); //es O(n)
    }

    public void actualizarMonto(Transaccion t) {
        Usuario comprador = usuarios.get(t.id_comprador()); // O(log p)
        Usuario vendedor = usuarios.get(t.id_vendedor()); // O(log p)

        comprador.actualizarSaldo(-1*t.monto()); //O(1)
        vendedor.actualizarSaldo(t.monto());
    }

    public Transaccion txMayorValorUltimoBloque(){
        //Devuelve la transacci´on de mayor valor del ´ultimo bloque (sin extraerla). En caso de empate, devuelve aquella de mayor id
        //ya que this.bloques[bloques.size()] es el ultimo bloque, hago consultarMax() del ultimo bloque y es O(1)
        throw new UnsupportedOperationException("Implementar!");
    }

    public Transaccion[] txUltimoBloque(){
        //hay que hacer el return del this.bloques[bloques.size()].txPorID, O(1)
        throw new UnsupportedOperationException("Implementar!");
    }


    public int maximoTenedor(){
        return this.maximoTenedor.id(); //O(1)
    }

    public int montoMedioUltimoBloque(){
        //return this.montosTotalesUltimoB / this.bloques[bloques.size() - 1].length; //O(1)
        return 0;
    }

    public void hackearTx(){
        //desencolar en un heap es O(log n). La raiz la guardardamos en una variable
        //hay que restar la transaccion que borramos a this.montosTotalesUltimoB
        //llamamos a actualizar monto de la transaccion, que es O(log p)
        //llamamos a bloques.TxPorId y eliminamos la transaccion de ahí tambien, que cuesta O(log nb)
        throw new UnsupportedOperationException("Implementar!");
    }
}
