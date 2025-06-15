package aed.estructuras.heap;

import java.util.ArrayList;
//maxHeap
public class Heap<T extends Comparable<T>> {
    private ArrayList<Nodo> heap; // implementamos un árbol binario completo representado mediante un ArrayList.
    // Cada nodo tiene a lo sumo dos hijos. Se llena de izquierda a derecha.

    private class Nodo {
        T valor; //por ej, el usuario, una transaccion
        HandleHeap<T> handle; //una referencia indirecta al índice del nodo, para ubicarlo rápidamente

        Nodo(T valor, HandleHeap<T> handle) {
            this.valor = valor;
            this.handle = handle;
        }
    }

    // se navega el arbol de la siguiente manera:
    private int indicePadre(int i) {
        return (i - 1) / 2;
    }

    private int indiceHijoIzq(int i) {
        return 2 * i + 1;
    }

    private int indiceHijoDer(int i) {
        return 2 * i + 2;
    }

    // Constructores
    public Heap() {
        this.heap = new ArrayList<>(); // heap vacío NO SE USA
    }

    // Construir desde Array, complejidad O(n) porque usamos heapify (algoritmo de Floyd) NO SE USA
    public Heap(T[] secuencia) {
        this.heap = new ArrayList<>();
        for (int i = 0; i < secuencia.length; i++) {
            HandleHeap<T> handle = new HandleHeap<>(i);
            heap.add(new Nodo(secuencia[i], handle));
        } // se copian los elementos al array elementos. Se crea un arbol binario desordenado, asociando cada elemento con un handle

        for (int i = (heap.size() / 2) - 1; i >= 0; i--) { // comienza desde (n/2)-1 Porque los nodos a partir de n/2 son hojas, así que no hace falta aplicar bajar sobre ellos.
            bajar(i); // se convierte el array en heap
        }
    }
    
    //construir desde array y devolver los handles
    public Heap(T[] secuencia, HandleHeap<T>[] handlesExternos) { //O(n)
        this.heap = new ArrayList<>(); //O(n)
        for (int i = 0; i < secuencia.length; i++) {
            HandleHeap<T> handle = new HandleHeap<>(i); //O(1)
            heap.add(new Nodo(secuencia[i], handle)); //O(1)
            handlesExternos[i] = handle; //O(1)
        } //creamos un arbol binario. No esta ordenado como heap

        for (int i = (heap.size() / 2) - 1; i >= 0; i--) { //O(n)
            bajar(i);
        } 
    }

    private void bajar(int i) { // O(log x)
        int hijoIzq = indiceHijoIzq(i);
        int hijoDer = indiceHijoDer(i);
        int mayor = i;

        // Compara el nodo con sus hijos
        if (hijoIzq < heap.size() &&
                heap.get(hijoIzq).valor.compareTo(heap.get(mayor).valor) > 0) {
            mayor = hijoIzq;
        }

        if (hijoDer < heap.size() &&
                heap.get(hijoDer).valor.compareTo(heap.get(mayor).valor) > 0) {
            mayor = hijoDer;
        }

        // Si alguno es mayor, intercambia con el más grande y continúa bajando.

        if (mayor != i) { 
            intercambiar(i, mayor); //cambia el handle tambien
            bajar(mayor); //por esto es O(log x)
        }
    }

    private void intercambiar(int i, int j) { //O(1)??
        //intercambia dos nodos del array. Actualiza sus handles
        Nodo ni = heap.get(i);
        Nodo nj = heap.get(j);

        heap.set(i, nj); 
        heap.set(j, ni);

        ni.handle.setRef(j);
        nj.handle.setRef(i);
    }

    private void subir(int i) { //O(log x)
        while (i > 0 && heap.get(i).valor.compareTo(heap.get(indicePadre(i)).valor) > 0) {
            intercambiar(i, indicePadre(i));
            i = indicePadre(i);
        }
        // Compara el nuevo nodo con su padre. Si es mayor, intercambia.
    }

    public HandleHeap<T> encolar(T elemento) { //O(log x)
        //Se agrega el elemento al final, creando un nuevo nodo con el elemento y un handle apuntando al final del array. Lo hace subir y devuelve el handle 
        HandleHeap<T> handle = new HandleHeap<>(heap.size());
        Nodo nodo = new Nodo(elemento, handle);
        heap.add(nodo);
        subir(heap.size() - 1); // O(log x)
        return handle;
    }

    public T desencolar() { //O(log x)
        if (estaVacio()) {
            throw new IllegalStateException("El heap está vacío.");
        }
        //guarda el valor de la raiz. Lo intercambia por el ultimo elemento, y lo baja segun corresponda. Devuelve el valor que estaba en la raiz
        T max = heap.get(0).valor; //O(1)
        int ultimo = heap.size() - 1;
        intercambiar(0, ultimo); //O(1)
        heap.remove(ultimo);

        if (!estaVacio()) {
            bajar(0); //O(log x)
        }
        return max;

    }

    public T maximo() {
        return this.heap.get(0).valor;
    }

    public boolean estaVacio() {
        return heap.isEmpty();
    }

    public int tamaño() {
        return heap.size();
    }

    public T obtenerValor(int i) {
        return heap.get(i).valor;
    }

    public T obtenerValor(HandleHeap<T> handle) {
        if (handle == null || !handle.estaActivo()) {
            throw new IllegalArgumentException("El handle proporcionado es nulo o inactivo.");
        }
        return heap.get(handle.getRef()).valor;
    }

    public void actualizar(HandleHeap<T> handle) { //O(log x)
        //Cuando un elemento cambia, hay que actualizar el heap. No sabemos si el nuevo valor es mayor o menor que antes, por eso lo bajamos o lo subimos.
        int i = handle.getRef();
        bajar(i);
        subir(i);
    }
}