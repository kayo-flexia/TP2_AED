package aed;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>> {
    private ArrayList<T> elementos;

    // Constructor
    public Heap() {
        this.elementos = new ArrayList<>();
    }

    //Construir desde Array 
    //COMPLEJIDAD O(n)
    public Heap(T[] secuencia) {
        this.elementos = new ArrayList<>();
        for (T elemento : secuencia) {
            this.elementos.add(elemento);
        }

        for (int i = (elementos.size() / 2) - 1; i >= 0; i--) {
            bajar(i);
        }
    }

    public boolean estaVacio() {
        return elementos.isEmpty();
    }
    
    public int tamaño() {
        return elementos.size();
    }

    public void insertar(T elemento) {
        elementos.add(elemento);
        subir(elementos.size() - 1);
    }

    // Obtener índice de nodos familia dado un i
    private int indicePadre(int i) {
        return (i - 1) / 2;
    }
    
    private int indiceHijoIzq(int i) {
        return 2 * i + 1;
    }
    
    private int indiceHijoDer(int i) {
        return 2 * i + 2;
    }

    private void intercambiar(int i, int j) {
        T temp = elementos.get(i);
        elementos.set(i, elementos.get(j));
        elementos.set(j, temp);
    }

    private void bajar(int i) {
        int hijoIzq = indiceHijoIzq(i);
        int hijoDer = indiceHijoDer(i);
        int mayor = i;
        
        // Encontrar el mayor entre padre e hijos
        if (hijoIzq < elementos.size() && 
            elementos.get(hijoIzq).compareTo(elementos.get(mayor)) > 0) {
            mayor = hijoIzq;
        }
        
        if (hijoDer < elementos.size() && 
            elementos.get(hijoDer).compareTo(elementos.get(mayor)) > 0) {
            mayor = hijoDer;
        }
        
        if (mayor != i) {
            intercambiar(i, mayor);
            bajar(mayor);
        }
    }

    private void subir(int i) {
        while (i > 0 && elementos.get(i).compareTo(elementos.get(indicePadre(i))) > 0) {
            intercambiar(i, indicePadre(i));
            i = indicePadre(i);
        }
    }
}