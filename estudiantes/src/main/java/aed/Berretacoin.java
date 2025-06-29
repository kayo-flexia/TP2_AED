package aed;

import java.util.ArrayList;
import aed.estructuras.heap.Heap;
import aed.estructuras.heap.Heap.HandleHeap;

public class Berretacoin {
    private Bloque ultimoBloque;
    private Heap<Usuario> usuarios;
    private ArrayList<Heap.HandleHeap<Usuario>> handlesUsuarios;
    private int montosTotalesUltimoBloque; // mover adentro del bloque
    private int cantidadTransaccionesUltimoBloque;

    public Berretacoin(int n_usuarios) { //O(p)
        this.handlesUsuarios = new ArrayList<>(n_usuarios + 1);
        for (int i = 0; i <= n_usuarios; i++) {
           this.handlesUsuarios.add(null); // llenar con nulls, fix del error que teniamos
        }

        Usuario[] arregloUsuarios = new Usuario[n_usuarios + 1];
        HandleHeap<Usuario>[] handles = new HandleHeap[n_usuarios + 1];

        for (int i = 0; i <= n_usuarios; i++) { // O (P)
            arregloUsuarios[i] = new Usuario(i);
            if (i == 0) arregloUsuarios[i].actualizarSaldo(-1);
        }

        this.usuarios = new Heap<>(arregloUsuarios, handles); // O (P)

        // Guardar los handles en la lista
        for (int i = 0; i <= n_usuarios; i++) {
            handlesUsuarios.set(arregloUsuarios[i].id(), handles[i]);
        }

    }

    public void agregarBloque(Transaccion[] transacciones){
        Bloque nuevoBloque = new Bloque(transacciones); //O(nb)
        ultimoBloque = nuevoBloque;

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

        if (handleComprador == null || handleVendedor == null) {
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
        //Devuelve la transacción de mayor valor del último bloque (sin extraerla).
        /* 
        if (Bloque.heap().s == null) {
            return null; 
        }
        */
        
        Transaccion mayorTransaccion = ultimoBloque.heap().maximo(); // O(1) porque es un heap
        return mayorTransaccion;
    }

    public Transaccion[] txUltimoBloque() { //O(nb)
        return ultimoBloque.getTransaccionesArray(); //O(nb)
    }



    public int maximoTenedor(){
        return usuarios.maximo().id(); //O(1) ya que tengo usuarios de 0 a n, pero el usuario 0 no existe
    }

    public int montoMedioUltimoBloque() {
        if (cantidadTransaccionesUltimoBloque == 0) {
            return 0;
        }
        return montosTotalesUltimoBloque / cantidadTransaccionesUltimoBloque;
    }


    public void hackearTx() {
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

        comprador.actualizarSaldo(montoTransaccion); // O(1)
        usuarios.actualizar(handleComprador);
        vendedor.actualizarSaldo(-montoTransaccion); // O(1)

        // Si es de creación, no hace falta hackear nada
        if (idComprador != 0) {
            this.montosTotalesUltimoBloque -= montoTransaccion;
            this.cantidadTransaccionesUltimoBloque -= 1;
        } 

        // Actualizar los handles de los usuarios
        usuarios.actualizar(handleVendedor);
    }

}
