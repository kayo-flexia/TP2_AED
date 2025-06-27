package aed.estructuras.listaEnlazada;

public class ListaEnlazada<T> {
    private Nodo primero;
    private Nodo ultimo;
    private int tamano;

    // Usamos protected para que HandleLE pueda acceder
    protected class Nodo {
        protected Nodo anterior;
        protected Nodo siguiente;
        protected T valor;

        Nodo(T v) {
            this.anterior = null;
            this.siguiente = null;
            this.valor = v;
        }
    }

    // Definimos el HANDLE apra la lista enlazada
    // Clase HandleLE: permite acceso O(1) a un nodo específico
    public static class HandleLE<T> {
        // Necesita ser de tipo ListaEnlazada<T>.Nodo porque Nodo es una clase interna no estática de ListaEnlazada
        private ListaEnlazada<T>.Nodo nodoInterno;
        private boolean activo; // Indica si el handle sigue siendo válido o lo invalidamos

        // Constructor: Solo el método de la lista enlazada debería crear Handles
        protected HandleLE(ListaEnlazada<T>.Nodo nodo) {
            this.nodoInterno = nodo;
            this.activo = true;
        }

        // Método para obtener el nodo interno (usado por la ListaEnlazada)
        public ListaEnlazada<T>.Nodo getNodo() {
            return nodoInterno;
        }

        // Verifica si el handle es válido
        public boolean estaActivo() {
            return activo;
        }

        public T getValor() {
            if (!activo || nodoInterno == null) {
                throw new IllegalStateException("Intento de acceder al valor de un handle inactivo o inválido.");
            }
            return nodoInterno.valor;
        }
        public void setValor(T nuevoValor) {
            if (!activo || nodoInterno == null) {
                throw new IllegalStateException("Intento de modificar el valor de un handle inactivo o inválido.");
            }
            this.nodoInterno.valor = nuevoValor;
        }

        // Invalida el handle cuando el nodo es eliminado
        public void invalidar() {
            this.activo = false;
            this.nodoInterno = null; // Liberar la referencia al nodo
        }
    }

    public ListaEnlazada() {
        this.primero = null;
        this.ultimo = null;
        this.tamano = 0;
    }

    public int longitud() {
        return this.tamano;
    }

    public void agregarAdelante(T elem) {
        Nodo nuevo = new Nodo(elem);
        if (this.primero == null) {
            this.primero = nuevo;
            this.ultimo = nuevo;    
        } else{
            nuevo.siguiente = this.primero;
            nuevo.anterior = null;
            this.primero.anterior = nuevo;
            this.primero = nuevo;
        }      
        this.tamano++;
    }


    public HandleLE<T> agregarAdelanteConHandle(T elem) { //O(1)
        Nodo nuevo = new Nodo(elem);
        if (this.primero == null) {
            this.primero = nuevo;
            this.ultimo = nuevo;
        } else {
            nuevo.siguiente = this.primero;
            this.primero.anterior = nuevo; // Asegurar que el 'viejo' primero apunte al nuevo
            this.primero = nuevo;
        }
        this.tamano++;
        return new HandleLE<>(nuevo); // Devolvemos el handle
    }

    public void agregarAtras(T elem) { 
        Nodo nuevo = new Nodo(elem);
        if (this.primero == null) {
            this.primero = nuevo;
            this.ultimo = nuevo;
        } else{
            this.ultimo.siguiente = nuevo;
            nuevo.anterior = this.ultimo;
            this.ultimo = nuevo;
            
        }
        this.tamano++;

        // ejemplo:
        // [A] ⇄ [B] ⇄ [C]

        // "C".siguiente → "D"

        // "D".anterior → "C"

        // ultimo = "D"

        // primero = "A"

        // [A] ⇄ [B] ⇄ [C] ⇄ [D]

    }

    public HandleLE<T> agregarAtrasConHandle(T elem) { //O(1)
        Nodo nuevo = new Nodo(elem);
        if (this.primero == null) {
            this.primero = nuevo;
            this.ultimo = nuevo;
        } else {
            this.ultimo.siguiente = nuevo;
            nuevo.anterior = this.ultimo;
            this.ultimo = nuevo;
        }
        this.tamano++;
        return new HandleLE<>(nuevo); // Devolvemos el handle
    }

    public T obtener(int i) { //O(n)
        Nodo actual = this.primero;
        int j = 0;
        if (i >= this.tamano || i < 0) {
            return null;
        }
        while( i != j ){
            actual = actual.siguiente;
            j++;
        }
        return actual.valor;
        
    }

    public void eliminar(int i) { //O(n)
        if (i >= this.tamano || this.tamano <= 0) {
            return;
        } else if (this.tamano == 1) {
            this.primero = null;
            this.ultimo = null;
            this.tamano--;    
            return;
        } 
        else if(i == (this.tamano - 1)){
            this.ultimo = this.ultimo.anterior;
            this.ultimo.siguiente = null;
            this.tamano--;    
            return;
        } else if(i == 0){
            this.primero = this.primero.siguiente;
            this.primero.anterior = null;
            this.tamano--;
            return;
        } 

        Nodo actual = this.primero;
        for( int j = 0; j < i; j++ ){
            actual = actual.siguiente;
        }

       Nodo anterior  = actual.anterior;
       Nodo siguiente = actual.siguiente;

       anterior.siguiente = siguiente;
       siguiente.anterior = anterior;

       this.tamano--;

    }

    public void eliminar(HandleLE<T> handle) { //O(1)
        if (handle == null || !handle.estaActivo()) {
            return; // Handle inválido
        }

        Nodo nodoAEliminar = handle.getNodo();

        // Si el nodo es el primero
        if (nodoAEliminar.anterior == null) {
            this.primero = nodoAEliminar.siguiente;
        } else {
            nodoAEliminar.anterior.siguiente = nodoAEliminar.siguiente;
        }

        // Si el nodo es el ultimo
        if (nodoAEliminar.siguiente == null) {
            this.ultimo = nodoAEliminar.anterior;
        } else {
            nodoAEliminar.siguiente.anterior = nodoAEliminar.anterior;
        }

        // Si la lista queda vacía
        if (this.tamano == 1) { // Caso especial: era el único elemento
            this.primero = null;
            this.ultimo = null;
        }

        this.tamano--;
        handle.invalidar(); // INvalidar el handle del que elimino
    }

    public void modificar(int indice, T elem) {
        if (indice >= this.tamano || this.tamano == 0) {
            return;
        }

        Nodo actual = this.primero;
        for( int j = 0; j < indice; j++ ){
            actual = actual.siguiente;
        }
        actual.valor = elem;
    }

    public void modificar(HandleLE<T> handle, T nuevoValor) { //O(1)
        if (handle == null || !handle.estaActivo()) {
            return; // Handle inválido
        }
        handle.getNodo().valor = nuevoValor;
    }

    // Constructor por copia
    public ListaEnlazada(ListaEnlazada<T> lista) {
        for (int i = 0; i < lista.tamano; i++) {
            Nodo nuevo = new Nodo(lista.obtener(i));
            this.agregarAtras(nuevo.valor);
        }   
    }
    
    @Override
    public String toString() {
        if (this.tamano == 0) { // Manejar el caso de lista vacía para evitar error de substring
            return "[]";
        }
        String res = "[";

        for (int i = 0; i < this.tamano; i++) {
            res += obtener(i) + ", ";
        }
        res = res.substring(0, res.length() - 2);
        res += "]";

        return res;
    }
}

