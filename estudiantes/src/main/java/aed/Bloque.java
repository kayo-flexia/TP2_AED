package aed;

import java.util.ArrayList;

public class Bloque {
    // private ArrayList<Transaccion> transacciones;
    private Transaccion[] transacciones; //es un heap segun monto y NO segun id como dice el enunciado
    private Usuario maximoTenedor; //la idea es que la lógica esté acá así Berretacoin.txMayorValorUltimoBloque() es sólo un return
    private int montoMedio; //ídemo mayorTransaccion

    public Bloque(Transaccion[] t) {
        this.transacciones = t;
        //this.mayorTransaccion = getMayorTx(this.transacciones);
        //this.montoMedio = getMontoMedio(this.transacciones);

    }

    //private getMayorTx(){
    //  const mTx = 0;
    //  ...
    //  this.mayorTransaccion = mTx;
    //}

    //private getMayorTx(){
    //  const m = 0;
    //  ...
    //  this.montoMedio = m;
    //}
}
