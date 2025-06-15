package aed;

import java.util.ArrayList;
import aed.estructuras.heap.Heap;
import aed.estructuras.heap.Heap.HandleHeap;
import aed.estructuras.listaEnlazada.ListaEnlazada;

public class Berretacoin {
    private ArrayList<Bloque> bloques;
    private Usuario maximoTenedor;
    private Heap<Usuario> usuarios;
    private ArrayList<Heap.HandleHeap<Usuario>> handlesUsuarios;
    private int montosTotalesUltimoBloque;
    private int cantidadTransaccionesUltimoBloque;

    public Berretacoin(int n_usuarios) { //O(p)
        this.handlesUsuarios = new ArrayList<>(n_usuarios + 1);
        for (int i = 0; i <= n_usuarios; i++) {
           this.handlesUsuarios.add(null); // Pre-llenar con nulls
        }

        Usuario[] arregloUsuarios = new Usuario[n_usuarios + 1];
        HandleHeap<Usuario>[] handles = new HandleHeap[n_usuarios + 1];

        for (int i = 0; i <= n_usuarios; i++) {
            arregloUsuarios[i] = new Usuario(i);
            if (i == 0) arregloUsuarios[i].actualizarSaldo(-1);
        }

        this.usuarios = new Heap<>(arregloUsuarios, handles);

        // Guardar los handles en la lista
        for (int i = 0; i <= n_usuarios; i++) {
            // Asegurarse de que el ID del usuario coincida con el índice
            handlesUsuarios.set(arregloUsuarios[i].id(), handles[i]);
        }

        this.maximoTenedor = usuarios.maximo();
        this.bloques = new ArrayList<>();


    }

    public void agregarBloque(Transaccion[] transacciones){
        Bloque b = new Bloque(transacciones); //O(nb)
        this.bloques.add(b); //O(1)

        int sumaMontos = 0;
        int cantidadValidas = 0;

        for (Transaccion transaccion : transacciones) { //O(nb)
            actualizarMonto(transaccion); //O(log p)
            // Contar solo si comprador != 0
            if (transaccion.id_comprador() != 0) {
                sumaMontos += transaccion.monto();
                cantidadValidas++;
            } //O(1)
        }

        this.maximoTenedor = usuarios.maximo(); //O(1)
        this.montosTotalesUltimoBloque = sumaMontos; //O(1)
        this.cantidadTransaccionesUltimoBloque = cantidadValidas; // O(1)
        //O(nb + nb*log p) = O(nb*log p)
    }


    public void actualizarMonto(Transaccion t) { //O(log p)
        int idComprador = t.id_comprador();
        int idVendedor = t.id_vendedor();
        int montoTransaccion = t.monto();

        // Acceso O(1) usando el ID como índice
        HandleHeap<Usuario> handleComprador = handlesUsuarios.get(idComprador);
        HandleHeap<Usuario> handleVendedor = handlesUsuarios.get(idVendedor);

        if (handleComprador == null || handleVendedor == null || !handleComprador.estaActivo() || !handleVendedor.estaActivo()) {
            System.err.println("Error: Transacción con ID de usuario inválido o inactivo: Comprador " + idComprador + ", Vendedor " + idVendedor);
            return;
        }

        Usuario comprador = usuarios.obtenerValor(handleComprador); // O(1) con el método obtenerValor
        Usuario vendedor = usuarios.obtenerValor(handleVendedor); // O(1) con el método obtenerValor

        comprador.actualizarSaldo(-montoTransaccion);
        vendedor.actualizarSaldo(montoTransaccion);

        usuarios.actualizar(handleComprador);
        usuarios.actualizar(handleVendedor);
    }


    public Transaccion txMayorValorUltimoBloque(){ //O(1)
        //Devuelve la transacción de mayor valor del último bloque (sin extraerla). En caso de empate, devuelve aquella de mayor id
        //ya que this.bloques[bloques.size()] es el ultimo bloque, hago consultarMax() del ultimo bloque y es O(1)
        if (bloques.isEmpty()) {
            return null; // o lanzar una excepción si no hay bloques
        }
        Bloque ultimoBloque = bloques.get(bloques.size() - 1);
        Transaccion mayorTransaccion = ultimoBloque.heap().maximo(); // O(1) porque es un heap
        return mayorTransaccion;
    }

    public Transaccion[] txUltimoBloque() { //O(nb)
        if (bloques.isEmpty()) {
            return new Transaccion[0];
        }
        Bloque ultimoBloque = bloques.get(bloques.size() - 1);
        // Suponemos que Bloque tiene un método que devuelve un arreglo con las transacciones ordenadas por ID
        return ultimoBloque.getTransaccionesArray(); //O(nb)
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
        if (ultimoBloque.heap().estaVacio()) return; // bloque vacío, nada que hackear

        Transaccion t = ultimoBloque.heap().desencolar();
        int idComprador = t.id_comprador();
        int idVendedor = t.id_vendedor();
        int idTx = t.id();
        int montoTransaccion = t.monto();

        HandleHeap<Usuario> handleComprador = handlesUsuarios.get(idComprador);
        HandleHeap<Usuario> handleVendedor = handlesUsuarios.get(idVendedor);
        Usuario comprador = usuarios.obtenerValor(handleComprador); // O(1) con el método obtenerValor
        Usuario vendedor = usuarios.obtenerValor(handleVendedor); // O(1) con el método obtenerValor

        ultimoBloque.eliminarTransaccionPorId(idTx);

        comprador.actualizarSaldo(montoTransaccion);
        vendedor.actualizarSaldo(-montoTransaccion);

        if (idComprador != 0) {
            this.montosTotalesUltimoBloque -= montoTransaccion;
            this.cantidadTransaccionesUltimoBloque -= 1;
        }

        usuarios.actualizar(handleComprador);
        usuarios.actualizar(handleVendedor);

        this.maximoTenedor = usuarios.maximo();


        // if (bloques.isEmpty()) return; // nada que hacer si no hay bloques

        // Bloque ultimoBloque = bloques.get(bloques.size() - 1); //O(1)
        // Heap<Transaccion> heapTx = ultimoBloque.heap(); //O(1)

        // if (heapTx.tamaño() == 0) return; // bloque vacío, nada que hackear

        // // 1. Desencolar la transacción de mayor valor (raíz)
        // Transaccion txHackeada = heapTx.desencolar(); //O(log nb)

        // // 2. Restar el monto de la transacción hackeada del total del último bloque
        // if (txHackeada.id_comprador() != 0) {
        //     montosTotalesUltimoBloque -= txHackeada.monto();
        // } //O(1)


        // // revertir saldos: sumamos monto al comprador (le devolvemos el dinero)
        // // y restamos monto al vendedor (le quitamos lo que recibió)
        // int idComprador = txHackeada.id_comprador(); //O(1)
        // int idVendedor = txHackeada.id_vendedor(); //O(1)

        // Handle<Usuario> handleComprador = handlesUsuarios[idComprador]; //O(1)
        // Handle<Usuario> handleVendedor = handlesUsuarios[idVendedor]; //O(1)

        // Usuario comprador = usuarios.obtenerElemento(handleComprador.getRef()); //O(1)
        // Usuario vendedor = usuarios.obtenerElemento(handleVendedor.getRef()); //O(1)

        // comprador.actualizarSaldo(txHackeada.monto()); //O(1)
        // vendedor.actualizarSaldo(-txHackeada.monto()); //O(1)

        // // actualizar heap de usuarios (log p)
        // usuarios.actualizar(handleComprador);
        // usuarios.actualizar(handleVendedor);

        // // actualizar maximoTenedor
        // maximoTenedor = usuarios.maximo(); //O(1)

        // // Eliminar la transacción del arreglo ordenado por ID dentro del bloque
        // ultimoBloque.eliminarTransaccionPorId(txHackeada);

    }

}
