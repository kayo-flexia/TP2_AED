package aed;

import java.util.ArrayList;
//maxHeap
public class Heap<T extends Comparable<T>> {
    private ArrayList<Nodo> heap; // implementamos un árbol binario completo representado mediante un ArrayList.
    // Cada nodo tiene a lo sumo dos hijos. Se llena de izquierda a derecha.

    private class Nodo {
        T valor; //por ej, el usuario, una transaccion
        Handle<T> handle; //una referencia indirecta al índice del nodo, para ubicarlo rápidamente

        Nodo(T valor, Handle<T> handle) {
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
        this.heap = new ArrayList<>(); // heap vacío
    }

    // Construir desde Array, complejidad O(n) porque usamos heapify (algoritmo de Floyd)
    public Heap(T[] secuencia) {
        this.heap = new ArrayList<>();
        for (int i = 0; i < secuencia.length; i++) {
            Handle<T> handle = new Handle<>(i);
            heap.add(new Nodo(secuencia[i], handle));
        } // se copian los elementos al array elementos. Se crea un arbol binario desordenado, asociando cada elemento con un handle

        for (int i = (heap.size() / 2) - 1; i >= 0; i--) { // comienza desde (n/2)-1 Porque los nodos a partir de n/2 son hojas, así que no hace falta aplicar bajar sobre ellos.
            bajar(i); // se convierte el array en heap
        }
    }

    private void bajar(int i) {
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
            bajar(mayor);
        }
    }

    private void intercambiar(int i, int j) {
        //intercambia dos nodos del array. Actualiza sus handles
        Nodo ni = heap.get(i); //O(1)
        Nodo nj = heap.get(j);

        heap.set(i, nj); //O(1) porque hay que obtener y sobreescribir?
        heap.set(j, ni);

        ni.handle.setRef(j);
        nj.handle.setRef(i);
    }

    private void subir(int i) {
        while (i > 0 && heap.get(i).valor.compareTo(heap.get(indicePadre(i)).valor) > 0) {
            intercambiar(i, indicePadre(i));
            i = indicePadre(i);
        }
        // Compara el nuevo nodo con su padre. Si es mayor, intercambia.
    }

    public Handle<T> encolar(T elemento) {
        //Crea un nuevo nodo con el elemento y un handle apuntando al final del array. Lo agrega al final del heap y lo hace subir segun corresponda. Devuelve el handle para que otras estructuras lo puedan usar
        Handle<T> handle = new Handle<>(heap.size());
        Nodo nodo = new Nodo(elemento, handle);
        heap.add(nodo);
        subir(heap.size() - 1);
        return handle;
    }

    public T desencolar() {
        if (estaVacio()) {
            throw new IllegalStateException("El heap está vacío.");
        }
        //guarda el valor de la raiz. Lo intercambia por el ultimo elemento, y lo baja segun corresponda. Devuelve el valor que estaba en la raiz
        T max = heap.get(0).valor;
        int ultimo = heap.size() - 1;
        intercambiar(0, ultimo);
        heap.remove(ultimo);

        if (!estaVacio()) {
            bajar(0);
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

    protected T obtenerElemento(int i) {
        return heap.get(i).valor;
    }

    public void actualizar(Handle<T> handle) {
        //Cuando un elemento cambia, hay que actualizar el heap. No sabemos si el nuevo valor es mayor o menor que antes, por eso lo bajamos o lo subimos.
        int i = handle.getRef();
        bajar(i);
        subir(i);
    }
}