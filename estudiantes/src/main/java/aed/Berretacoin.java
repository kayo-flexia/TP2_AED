package aed;

import java.util.ArrayList;

public class Berretacoin {
    private ArrayList<Bloque> bloques; //Es un array de bloque, y bloque.transaccion es un heap según el monto
    private Usuario maximoTenedor; //usuarios es un heap asi que obtener la raiz es O(1)
    private Heap<Usuario> usuarios; //es un HEAP segun el saldo de c/u
    //private ArrayList<Usuario> usuariosArreglo; //ordenados por arreglo SIN ser heap
    private int montosTotalesUltimoBloque;
    private Handle<Usuario, Integer>[] handlesUsuarios;

    
    @SuppressWarnings("unchecked")
    public Berretacoin(int n_usuarios) {
        this.usuarios = new Heap<>();
        this.handlesUsuarios = new Handle[n_usuarios + 1];

        for (int i = 0; i < n_usuarios + 1; i++) {
            Usuario usuario = new Usuario(i);
            Handle<Usuario, Integer> handle = usuarios.encolar(usuario);
            handlesUsuarios[i] = handle;
        }

        this.maximoTenedor = usuarios.maximo(); // O usuarios.maximo()
        this.bloques = new ArrayList<>();
    }

    public void agregarBloque(Transaccion[] transacciones){
        Bloque b = new Bloque(transacciones); // colaDePriodidadDesdeSecuencia cuesta O(n), ver el constructor de clase Bloque
        this.bloques.add(b); //creo que es O(1) pq es un array de HEAPS
        for (Transaccion transaccion : transacciones) { // itera nb (cantidad de transacciones)
            actualizarMonto(transaccion); //O(log p)
            //this.maximoTenedor = this.usuarios.consultarMax() this.usuarios es HEAP, cuesta Según el apunte es O(1)
        }

        this.maximoTenedor = usuarios.maximo();
        this.montosTotalesUltimoBloque = b.montosTotales(); //es O(n)
    }

    public void actualizarMonto(Transaccion t) {
        int idComprador = t.id_comprador();
        int idVendedor = t.id_vendedor();

        Handle<Usuario, Integer> handleComprador = handlesUsuarios[idComprador];
        Handle<Usuario, Integer> handleVendedor = handlesUsuarios[idVendedor];

        Usuario comprador = usuarios.obtenerElemento(handleComprador.getRef());
        Usuario vendedor = usuarios.obtenerElemento(handleVendedor.getRef());

        comprador.actualizarSaldo(-t.monto());
        vendedor.actualizarSaldo(t.monto());

        usuarios.actualizar(handleComprador);
        usuarios.actualizar(handleVendedor);

        this.maximoTenedor = usuarios.maximo();
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
        return this.maximoTenedor.id(); //O(1) ya que tengo usuarios de 0 a n, pero el usuario 0 no existe
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
