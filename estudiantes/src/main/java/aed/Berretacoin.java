package aed;

import java.util.ArrayList;
import aed.estructuras.heap.Heap;
import aed.estructuras.heap.Heap.HandleHeap;
import aed.estructuras.listaEnlazada.ListaEnlazada;

public class Berretacoin {
    private Bloque ultimoBloque;
    private Usuario maximoTenedor;
    private Heap<Usuario> usuarios;
    private ArrayList<Heap.HandleHeap<Usuario>> handlesUsuarios;
    private int montosTotalesUltimoBloque;
    private int cantidadTransaccionesUltimoBloque;

    public Berretacoin(int n_usuarios) { //O(p)
        this.handlesUsuarios = new ArrayList<>(n_usuarios + 1);
        for (int i = 0; i <= n_usuarios; i++) { //O(p)
           this.handlesUsuarios.add(null); // llenar con nulls, fix del error que teniamos
        }

        Usuario[] arregloUsuarios = new Usuario[n_usuarios + 1]; //O(1)?
        HandleHeap<Usuario>[] handles = new HandleHeap[n_usuarios + 1]; //O(1)

        for (int i = 0; i <= n_usuarios; i++) { //O(p)
            arregloUsuarios[i] = new Usuario(i); //O(1)
            if (i == 0) arregloUsuarios[i].actualizarSaldo(-1); //O(1)
        }

        this.usuarios = new Heap<>(arregloUsuarios, handles); //O(p)

        // Guardar los handles en la lista
        for (int i = 0; i <= n_usuarios; i++) {
            handlesUsuarios.set(arregloUsuarios[i].id(), handles[i]); //O(1), llenamos todas las posiciones.
        }

        this.maximoTenedor = usuarios.maximo(); //O(1)
        this.ultimoBloque = null;
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
        } //O(1)

        this.maximoTenedor = usuarios.maximo(); //O(1)
        this.montosTotalesUltimoBloque = sumaMontos; //O(1)
        this.cantidadTransaccionesUltimoBloque = cantidadValidas; // O(1)
        //O(nb + nb*log p) = O(nb*log p)
    }


    public void actualizarMonto(Transaccion t) { //O(log p)
        int idComprador = t.id_comprador(); //O(1)
        int idVendedor = t.id_vendedor(); //O(1)
        int montoTransaccion = t.monto(); //O(1)

        // Acceso O(1) usando el ID como índice
        HandleHeap<Usuario> handleComprador = handlesUsuarios.get(idComprador); //O(1)
        HandleHeap<Usuario> handleVendedor = handlesUsuarios.get(idVendedor); //O(1)

        if (handleComprador == null || handleVendedor == null || !handleComprador.estaActivo() || !handleVendedor.estaActivo()) { //O(1)
            System.err.println("Error: Transacción con ID de usuario inválido o inactivo: Comprador " + idComprador + ", Vendedor " + idVendedor); //O(1)
            return;
        }

        Usuario comprador = usuarios.obtenerValor(handleComprador); // O(1) con el método obtenerValor
        Usuario vendedor = usuarios.obtenerValor(handleVendedor); // O(1) con el método obtenerValor

        comprador.actualizarSaldo(-montoTransaccion); //O(1)
        vendedor.actualizarSaldo(montoTransaccion); //O(1)

        usuarios.actualizar(handleComprador); //O(log p)
        usuarios.actualizar(handleVendedor); //O(log p)
    }


    public Transaccion txMayorValorUltimoBloque(){ //O(1)
        //Devuelve la transacción de mayor valor del último bloque (sin extraerla).
        if (ultimoBloque == null) {
            return null; 
        }
        
        Transaccion mayorTransaccion = ultimoBloque.heap().maximo(); // O(1) porque es un heap
        return mayorTransaccion;
    }

    public Transaccion[] txUltimoBloque() { //O(nb)
        if (ultimoBloque == null) {
            return new Transaccion[0];
        }

        return ultimoBloque.getTransaccionesArray(); //O(nb)
    }



    public int maximoTenedor(){
        return this.maximoTenedor.id(); //O(1) ya que tengo usuarios de 0 a n, pero el usuario 0 no existe
    }

    public int montoMedioUltimoBloque() {
        if (ultimoBloque == null || cantidadTransaccionesUltimoBloque == 0) {
            return 0;
        }
        return montosTotalesUltimoBloque / cantidadTransaccionesUltimoBloque;
    }


    public void hackearTx() {
        if (ultimoBloque == null) return;

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
        usuarios.actualizar(handleComprador); // Actualizamos el handle antes de cambiar el saldo del vendedor

        vendedor.actualizarSaldo(-montoTransaccion);

        // Si es de creación, no hace falta hackear nada
        if (idComprador != 0) {
            this.montosTotalesUltimoBloque -= montoTransaccion;
            this.cantidadTransaccionesUltimoBloque -= 1;
        } 

        // Actualizar los handles de los usuarios
        usuarios.actualizar(handleVendedor);

        this.maximoTenedor = usuarios.maximo();
    }

}
