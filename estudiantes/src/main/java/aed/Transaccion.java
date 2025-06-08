package aed;

public class Transaccion implements Comparable<Transaccion> {
    private int id;
    private int id_comprador;
    private int id_vendedor;
    private int monto;

    public Transaccion(int id, int id_comprador, int id_vendedor, int monto) {
        this.id = id;
        this.id_comprador = id_comprador;
        this.id_vendedor = id_vendedor;
        this.monto = monto;
    }

    @Override
    public int compareTo(Transaccion otro) {
        // Primero comparamos por monto. Si son iguales, desempata por id
        if (this.monto != otro.monto) {
            return Integer.compare(this.monto, otro.monto); // orden natural por monto
        } else {
            return Integer.compare(this.id, otro.id); // desempata por id
        }
    }

    @Override
    public boolean equals(Object otro){
        if (this.id == ((Transaccion) otro).id) {
            return true; // Entiendo que basta con tener el mismo id
        }

        return false;
    }

    public int monto() {
        return monto;
    }

    public int id_comprador() {
        return id_comprador;
    }
    
    public int id_vendedor() {
        return id_vendedor;
    }
}