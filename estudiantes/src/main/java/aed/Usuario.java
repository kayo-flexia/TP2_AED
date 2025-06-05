package aed;

public class Usuario {
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
}
