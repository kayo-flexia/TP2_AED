package aed;

// Implementa el Comparable para poder usarlo en el Heap
public class Usuario implements Comparable<Usuario> {
    private int id;
    private int saldo;

    public Usuario(int id){
        this.id = id;
        this.saldo = 0;
    }

    public Usuario(int id, int saldo){
        this.id = id;
        this.saldo = saldo >= 0 ? saldo : 0; 
    }

    public void actualizarSaldo(int s){
        this.saldo += s;
    }

    public int id(){
        return this.id;
    }

    public int saldo(){
        return this.saldo;
    }


    // Agregue el equals y el compareTo para el Heap
     @Override
    public boolean equals(Object otro){
        if (this == otro) return true; // Misma referencia, son iguales
        if (otro == null || getClass() != otro.getClass()) return false; // Null o de diferente clase

        Usuario that = (Usuario) otro; // Casteo seguro
        return this.id == that.id; // Comparación por ID
    }

    @Override
    public int hashCode() {
        // Es crucial sobrescribir hashCode si sobrescribes equals
        return Integer.hashCode(id);
    }

    @Override
    public int compareTo(Usuario otro) {
        if (this.saldo != otro.saldo) {
            return Integer.compare(this.saldo, otro.saldo); // mayor saldo primero
        } else {
            return Integer.compare(otro.id, this.id);
        }
    }

}
