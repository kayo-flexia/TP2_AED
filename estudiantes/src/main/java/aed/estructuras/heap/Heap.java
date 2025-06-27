package aed.estructuras.heap;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>> {
    private ArrayList<Nodo> heap;

    private class Nodo {
        T valor;
        HandleHeap<T> handle;

        Nodo(T valor, HandleHeap<T> handle) {
            this.valor = valor;
            this.handle = handle;
        }
    }

    // Definimos el handle para el heap
    public static class HandleHeap<T> {
        private Integer refInterna;
        // private boolean activo;

        public HandleHeap(int refInterna) {
            this.refInterna = refInterna;
          //  this.activo = true;
        }

        public int getRef() {
            return refInterna;
        }

        protected void setRef(int nuevaRef) {
            this.refInterna = nuevaRef;
        }

        /*
        public boolean estaActivo() {
            return activo;
        }

 
        protected void invalidar() {
            this.activo = false;
            this.refInterna = null;
        }
        */
    }

    private int indicePadre(int i) {
        return (i - 1) / 2;
    }

    private int indiceHijoIzq(int i) {
        return 2 * i + 1;
    }

    private int indiceHijoDer(int i) {
        return 2 * i + 2;
    }

    public Heap() {
        this.heap = new ArrayList<>();
    }

    public Heap(T[] secuencia) {
        this.heap = new ArrayList<>();
        for (int i = 0; i < secuencia.length; i++) {
            HandleHeap<T> handle = new HandleHeap<>(i);
            heap.add(new Nodo(secuencia[i], handle));
        }

        for (int i = (heap.size() / 2) - 1; i >= 0; i--) {
            bajar(i);
        }
    }

    public Heap(T[] secuencia, HandleHeap<T>[] handlesExternos) {
        this.heap = new ArrayList<>();
        for (int i = 0; i < secuencia.length; i++) {
            HandleHeap<T> handle = new HandleHeap<>(i);
            heap.add(new Nodo(secuencia[i], handle));
            handlesExternos[i] = handle;
        }

        for (int i = (heap.size() / 2) - 1; i >= 0; i--) {
            bajar(i);
        }
    }

    private void bajar(int i) {
        int hijoIzq = indiceHijoIzq(i);
        int hijoDer = indiceHijoDer(i);
        int mayor = i;

        if (hijoIzq < heap.size() &&
                heap.get(hijoIzq).valor.compareTo(heap.get(mayor).valor) > 0) {
            mayor = hijoIzq;
        }

        if (hijoDer < heap.size() &&
                heap.get(hijoDer).valor.compareTo(heap.get(mayor).valor) > 0) {
            mayor = hijoDer;
        }

        if (mayor != i) {
            intercambiar(i, mayor);
            bajar(mayor);
        }
    }

    private void intercambiar(int i, int j) {
        Nodo ni = heap.get(i);
        Nodo nj = heap.get(j);

        heap.set(i, nj);
        heap.set(j, ni);

        ni.handle.setRef(j);
        nj.handle.setRef(i);

    }

    private void subir(int i) {
        while (i > 0 && heap.get(i).valor.compareTo(heap.get(indicePadre(i)).valor) > 0) {
            intercambiar(i, indicePadre(i));
            i = indicePadre(i);
        }
    }

    public HandleHeap<T> encolar(T elemento) {
        HandleHeap<T> handle = new HandleHeap<>(heap.size());
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
        if (handle == null) {
            throw new IllegalArgumentException("El handle proporcionado es nulo o inactivo.");
        }
        return heap.get(handle.getRef()).valor;
    }

    public void actualizar(HandleHeap<T> handle) {
        int i = handle.getRef();
        bajar(i);
        subir(i);
    }
}
