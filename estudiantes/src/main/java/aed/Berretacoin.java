package aed;

import java.util.ArrayList;

public class Berretacoin {
    private ArrayList<Bloque> bloques; //Es un array de bloque, y bloque.transaccion es un heap según el monto
    private Usuario maximoTenedor; //usuarios es un heap asi que obtener la raiz es O(1)
    private Heap<Usuario> usuarios; //es un HEAP segun el saldo de c/u
    //private ArrayList<Usuario> usuariosArreglo; //ordenados por arreglo SIN ser heap
    private int montosTotalesUltimoBloque;
    private Handle<Usuario>[] handlesUsuarios;

    private int cantidadTransaccionesUltimoBloque;
    
    public Berretacoin(int n_usuarios) {
        this.usuarios = new Heap<>();
        this.handlesUsuarios = new Handle[n_usuarios + 1];

        for (int i = 0; i <= n_usuarios; i++) {
            Usuario usuario = new Usuario(i);
            if (i == 0) usuario.actualizarSaldo(-1); // excluye al usuario 0 del máximo
            //Creo que eso no hace falta pq el maximo es el de mayo id
            Handle<Usuario> handle = usuarios.encolar(usuario);
            handlesUsuarios[i] = handle;
        }

        this.maximoTenedor = usuarios.maximo(); // O usuarios.maximo()
        this.bloques = new ArrayList<>();
    }

    public void agregarBloque(Transaccion[] transacciones){
        Bloque b = new Bloque(transacciones);
        this.bloques.add(b);

        int sumaMontos = 0;
        int cantidadValidas = 0;

        for (Transaccion transaccion : transacciones) {
            actualizarMonto(transaccion);
            this.maximoTenedor = this.usuarios.maximo();

            // Contar solo si comprador != 0
            if (transaccion.id_comprador() != 0) {
                sumaMontos += transaccion.monto();
                cantidadValidas++;
            }
        }

        this.maximoTenedor = usuarios.maximo();
        this.montosTotalesUltimoBloque = sumaMontos;
        this.cantidadTransaccionesUltimoBloque = cantidadValidas;
    }


    public void actualizarMonto(Transaccion t) {
        int idComprador = t.id_comprador();
        int idVendedor = t.id_vendedor();

        Handle<Usuario> handleComprador = handlesUsuarios[idComprador];
        Handle<Usuario> handleVendedor = handlesUsuarios[idVendedor];

        Usuario comprador = usuarios.obtenerElemento(handleComprador.getRef());
        Usuario vendedor = usuarios.obtenerElemento(handleVendedor.getRef());

        comprador.actualizarSaldo(-t.monto());
        vendedor.actualizarSaldo(t.monto());

        usuarios.actualizar(handleComprador);
        usuarios.actualizar(handleVendedor);

        this.maximoTenedor = usuarios.maximo();
    }



    public Transaccion txMayorValorUltimoBloque(){
        //Devuelve la transacción de mayor valor del último bloque (sin extraerla). En caso de empate, devuelve aquella de mayor id
        //ya que this.bloques[bloques.size()] es el ultimo bloque, hago consultarMax() del ultimo bloque y es O(1)
        if (bloques.isEmpty()) {
            return null; // o lanzar una excepción si no hay bloques
        }
        Bloque ultimoBloque = bloques.get(bloques.size() - 1);
        Transaccion mayorTransaccion = ultimoBloque.heap().maximo(); // O(1) porque es un heap
        return mayorTransaccion;
    }

    public Transaccion[] txUltimoBloque() {
        if (bloques.isEmpty()) {
            return new Transaccion[0];
        }
        Bloque ultimoBloque = bloques.get(bloques.size() - 1);
        // Suponemos que Bloque tiene un método que devuelve un arreglo con las transacciones ordenadas por ID
        return ultimoBloque.transaccionesPorId(); // debería devolver una copia, no la referencia interna
    }



    public int maximoTenedor(){
        return this.maximoTenedor.id(); //O(1) ya que tengo usuarios de 0 a n, pero el usuario 0 no existe
    }

    public int montoMedioUltimoBloque() {
        if (bloques.isEmpty() || cantidadTransaccionesUltimoBloque == 0) {
            return 0;
        }
        return montosTotalesUltimoBloque / cantidadTransaccionesUltimoBloque;
    }


    public void hackearTx() {
        if (bloques.isEmpty()) return; // nada que hacer si no hay bloques

        Bloque ultimoBloque = bloques.get(bloques.size() - 1);
        Heap<Transaccion> heapTx = ultimoBloque.heap();

        if (heapTx.tamaño() == 0) return; // bloque vacío, nada que hackear

        // 1. Desencolar la transacción de mayor valor (raíz)
        Transaccion txHackeada = heapTx.desencolar();

        // 2. Restar el monto de la transacción hackeada del total del último bloque
        montosTotalesUltimoBloque -= txHackeada.monto();

        // if (txHackeada.id_comprador() != 0) {
        //     montosTotalesUltimoBloque -= txHackeada.monto();
        // } creo que va esto


        // 3. Revertir el efecto de la transacción en los usuarios
        // Para revertir, llamamos a actualizarMonto con los valores inversos:
        // pero actualizarMonto solo suma/resta y actualiza heap usuarios,
        // así que podemos crear un método específico para revertir o hacer manualmente:

        int idComprador = txHackeada.id_comprador();
        int idVendedor = txHackeada.id_vendedor();

        Handle<Usuario> handleComprador = handlesUsuarios[idComprador];
        Handle<Usuario> handleVendedor = handlesUsuarios[idVendedor];

        Usuario comprador = usuarios.obtenerElemento(handleComprador.getRef());
        Usuario vendedor = usuarios.obtenerElemento(handleVendedor.getRef());

        // revertir saldos: sumamos monto al comprador (le devolvemos el dinero)
        // y restamos monto al vendedor (le quitamos lo que recibió)
        comprador.actualizarSaldo(txHackeada.monto());
        vendedor.actualizarSaldo(-txHackeada.monto());

        // actualizar heap de usuarios (log p)
        usuarios.actualizar(handleComprador);
        usuarios.actualizar(handleVendedor);

        // actualizar maximoTenedor
        maximoTenedor = usuarios.maximo();

        // 4. Eliminar la transacción del arreglo ordenado por ID dentro del bloque
        ultimoBloque.eliminarTransaccionPorId(txHackeada);
    }

}
