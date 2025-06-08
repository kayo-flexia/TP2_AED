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


    // Agregue el equals y el compareTo para el Heap
    @Override
    public boolean equals(Object otro){
        if (this.id == ((Usuario) otro).id) {
            return true; 
        }
        else{
            return false;
        }
    }

    @Override
    public int compareTo(Usuario otro) {
        // Primero comparamos por saldo. Si son iguales, desempata por id
        if (this.saldo != otro.saldo) {
            return Integer.compare(this.saldo, otro.saldo); // orden natural por saldo
        } else {
            return Integer.compare(this.id, otro.id); // desempata por id
        }
    }
    
}
