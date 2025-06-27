package aed;

import java.util.ArrayList;
import aed.estructuras.heap.Heap;
import aed.estructuras.heap.Heap.HandleHeap;

public class Berretacoin {
    // No se si realmente hace falta seguir todos los bloques, con el ultimo era suficiente.
    private ArrayList<Bloque> bloques;
    private Usuario maximoTenedor;
    private Heap<Usuario> usuarios;
    private ArrayList<Heap.HandleHeap<Usuario>> handlesUsuarios;
    private int montosTotalesUltimoBloque;
    private int cantidadTransaccionesUltimoBloque;

    private final int MAX_USUARIOS;

    public Berretacoin(int n_usuarios) { 
        MAX_USUARIOS = n_usuarios;

        this.handlesUsuarios = new ArrayList<>(n_usuarios + 1);
        for (int i = 0; i <= MAX_USUARIOS; i++) { //O(p)
           this.handlesUsuarios.add(null); // llenar con nulls, fix del error que teniamos
        }

        Usuario[] arregloUsuarios = new Usuario[n_usuarios + 1]; //O(1)?
        HandleHeap<Usuario>[] handles = new HandleHeap[MAX_USUARIOS + 1]; //O(1)

        for (int i = 0; i <= MAX_USUARIOS; i++) { //O(p)
            arregloUsuarios[i] = new Usuario(i); //O(1)
            if (i == 0) arregloUsuarios[i].actualizarSaldo(-1); //O(1)
        }

        this.usuarios = new Heap<>(arregloUsuarios, handles, MAX_USUARIOS); //O(p)

        // Guardar los handles en la lista
        for (int i = 0; i <= n_usuarios; i++) {
            handlesUsuarios.set(arregloUsuarios[i].id(), handles[i]); //O(1), llenamos todas las posiciones.
        }

        this.maximoTenedor = usuarios.maximo(); //O(1)
        this.bloques = new ArrayList<>(); //O(1)
    }

    public void agregarBloque(Transaccion[] transacciones){
        Bloque b = new Bloque(transacciones); //O(nb)
        this.bloques.add(b); //O(1) MAL, es O(la cantidad de bloques). Estamos concatenando un elemento a un array (porque no hay posiciones disponibles, ya que nunca definimos el tamaño del array bloques), y eso cuesta O(la cantidad de bloques + 1). estamos agregando una nueva variable de complejidad: la cantidad de bloques. Se solucionaría si usamos vector

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
        if (bloques.isEmpty()) {
            return null; 
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
        if (bloques.isEmpty()) return;

        Bloque ultimoBloque = bloques.get(bloques.size() - 1);
        if (ultimoBloque.heap().estaVacio()) return;

        Transaccion t = ultimoBloque.heap().desencolar(); // O(log nb)
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

        // Si es de creación, no hace falta hackear nada
        if (idComprador != 0) {
            this.montosTotalesUltimoBloque -= montoTransaccion;
            this.cantidadTransaccionesUltimoBloque -= 1;
        } 

        // Actualizar los handles de los usuarios
        usuarios.actualizar(handleComprador);
        usuarios.actualizar(handleVendedor);

        this.maximoTenedor = usuarios.maximo();
    }

}
