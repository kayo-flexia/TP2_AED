package aed;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>> {
    private ArrayList<Nodo> heap; // Array2Heap
    // Cada nodo tiene a lo sumo dos hijos. Se llena de izquierda a derecha.

    private class Nodo {
        T valor;
        Handle<T, Integer> handle;

        Nodo(T valor, Handle<T, Integer> handle) {
            this.valor = valor;
            this.handle = handle;
        }
    }

    // Constructores
    public Heap() {
        this.heap = new ArrayList<>(); // heap vacío
    }

    // Construir desde Array, complejidad O(n) porque usamos heapify (algoritmo de
    // Floyd)
    public Heap(T[] secuencia) {
        this.heap = new ArrayList<>();
        for (int i = 0; i < secuencia.length; i++) {
            Handle<T, Integer> handle = new Handle<>(i);
            heap.add(new Nodo(secuencia[i], handle));

        } // se copian los elementos al array elementos

        for (int i = (heap.size() / 2) - 1; i >= 0; i--) { // comienza desde (n/2)-1 Porque los nodos a partir de n/2
            // son hojas, así que no hace falta aplicar bajar sobre ellos.
            bajar(i); // se convierte el array en heap
        }
    }

    // Obtener índice de nodos familia dado un i (sirve para otros métodos)
    private int indicePadre(int i) {
        return (i - 1) / 2;
    }

    private int indiceHijoIzq(int i) {
        return 2 * i + 1;
    }

    private int indiceHijoDer(int i) {
        return 2 * i + 2;
    }

    public Handle<T, Integer> encolar(T elemento) {
        Handle<T, Integer> handle = new Handle<>(heap.size());
        Nodo nodo = new Nodo(elemento, handle);
        heap.add(nodo);
        subir(heap.size() - 1);
        return handle;
    }

    public T desencolar() {
        if (estaVacio()) {
            throw new IllegalStateException("El heap está vacío.");
        }

        T max = heap.get(0).valor;
        int ultimo = heap.size() - 1;
        intercambiar(0, ultimo);
        heap.remove(ultimo);

        if (!estaVacio()) {
            bajar(0);
        }
        return max;

    }

    private void intercambiar(int i, int j) {
        Nodo ni = heap.get(i);
        Nodo nj = heap.get(j);

        heap.set(i, nj);
        heap.set(j, ni);

        ni.handle.setRef(j);
        nj.handle.setRef(i);
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
            intercambiar(i, mayor);
            bajar(mayor);
        }
    }

    private void subir(int i) {
        while (i > 0 && heap.get(i).valor.compareTo(heap.get(indicePadre(i)).valor) > 0) {
            intercambiar(i, indicePadre(i));
            i = indicePadre(i);
        }
        // Compara el nuevo nodo con su padre. Si es mayor, intercambia.
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

    public void actualizar(Handle<T, Integer> handle) {
        int i = handle.getRef();
        bajar(i);
        subir(i);
    }

}