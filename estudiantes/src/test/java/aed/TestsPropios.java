package aed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestsPropios {

    private Berretacoin berretacoin;

    @BeforeEach
    public void setUp() {
        berretacoin = new Berretacoin(20);
    }

    @Test
    public void maximoTenedorConMuchosEmpates() {
        for (int i = 1; i <= 5; i++) {
            Transaccion[] bloque = {
                new Transaccion(i, 0, i, 10)
            };
            berretacoin.agregarBloque(bloque);
        }

        assertEquals(1, berretacoin.maximoTenedor());
    }

    @Test
    public void hackeoHastaVaciarBloque() {
        Transaccion[] bloque = {
            new Transaccion(0, 0, 1, 5),
            new Transaccion(1, 1, 2, 3),
            new Transaccion(2, 2, 3, 2)
        };

        berretacoin.agregarBloque(bloque);

        berretacoin.hackearTx();
        assertEquals(2, berretacoin.txUltimoBloque().length);

        berretacoin.hackearTx();
        assertEquals(1, berretacoin.txUltimoBloque().length);

        berretacoin.hackearTx();
        assertEquals(0, berretacoin.txUltimoBloque().length);
    }

    @Test
    public void agregarBloqueConUsuariosAltos() {
        Transaccion[] bloque = {
            new Transaccion(0, 0, 10, 2),
            new Transaccion(1, 10, 9, 1),
            new Transaccion(2, 9, 8, 1),
        };

        berretacoin.agregarBloque(bloque);

        assertEquals(8, berretacoin.maximoTenedor());
        assertEquals(new Transaccion(0, 0, 10, 2), berretacoin.txMayorValorUltimoBloque());
    }

    @Test
    public void hackearConUnaSolaTransaccion() {
        Transaccion[] bloque = {
            new Transaccion(0, 0, 1, 5)
        };

        berretacoin.agregarBloque(bloque);
        berretacoin.hackearTx();

        assertEquals(0, berretacoin.txUltimoBloque().length);
    }

    @Test
    public void transaccionQueDejaSaldoEnCeroDebeSerValida() {
        Transaccion[] bloque = {
            new Transaccion(0, 0, 1, 1) // El creador tiene 1
        };

        berretacoin.agregarBloque(bloque);

        assertEquals(1, berretacoin.txUltimoBloque().length);
        assertEquals(1, berretacoin.maximoTenedor());
    }

    //  Testear algo de lista enlazada?

    // Testear el Heap corto

    // Testear iterador

}
