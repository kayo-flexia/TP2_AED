package aed;

import java.util.ArrayList;

public class Berretacoin {
    private ArrayList<Bloque> bloques;
    private Usuario maximoTenedor; //si cada vez que agregamos un bloque llamamos una funcion chiquita que busque el maximo tenedor de dicho bloque, lo comparamos con esta prop. Entonces el método maximoTenedor sólo hace un return y es O(1).
    //No se si llamar una función 'a' dentro de otra funcion 'b' hace que varíe la complejidad. Creo que sólo ocurre si O(b) es más compleja que O(a). Por ejemplo, b tiene complejidad O(n) y a tiene complejidad O(1) por separado. Llamar a 'b' dentro de 'a' hace que 'a' pase a complejidad O(n)
    private ArrayList<Usuario> usuarios; //para lógica de maximoTenedor
    
    public Berretacoin(int n_usuarios){
        
    }

    public void agregarBloque(Transaccion[] transacciones){
        throw new UnsupportedOperationException("Implementar!");
    }

    public Transaccion txMayorValorUltimoBloque(){
        throw new UnsupportedOperationException("Implementar!");
    }

    public Transaccion[] txUltimoBloque(){
        throw new UnsupportedOperationException("Implementar!");
    }

    public int maximoTenedor(){
        throw new UnsupportedOperationException("Implementar!");
    }

    public int montoMedioUltimoBloque(){
        throw new UnsupportedOperationException("Implementar!");
    }

    public void hackearTx(){
        throw new UnsupportedOperationException("Implementar!");
    }
}
