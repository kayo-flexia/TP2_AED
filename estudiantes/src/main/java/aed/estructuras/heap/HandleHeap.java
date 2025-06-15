package aed.estructuras.heap;
//Un Handle encapsula una referencia directa al nodo del heap que contiene el dato. Es como tener un puntero controlado


// Clase reutilizable
//este es un handle de heap. Hay que crear OTRO handle de Listas enlazadas
public class HandleHeap<T> {
    //T es el tipo de objeto al que eventualmente se refiere (por ejemplo, un `Usuario` o `Transaccion`).  

    private Integer refInterna; //es la clave interna que el sistema (por ejemplo, el Heap) usa para encontrar el objeto real.
    private boolean activo; //indica si este handle todavía es válido. Es útil para evitar que se use un handle que ya fue eliminado o que apunta a un objeto inválido.

    public HandleHeap(int refInterna) {
        //cuando se crea el handle, se le asigna su referencia y se marca como activo.
        this.refInterna = refInterna;
        this.activo = true;
    }

    public int getRef() {
        //Este método permite obtener la referencia interna. Sirve para acceder al objeto real dentro de un ArrayList.
        return refInterna;
    }

    protected void setRef(int nuevaRef) {
        //Este método permite actualizar la referencia interna. Por ejemplo, si el objeto se mueve dentro del heap (porque cambió de prioridad), actualizamos el handle para que siga apuntando al nuevo lugar. Es `protected` porque solo estructuras internas (como el `Heap`) deberían usarlo.
        this.refInterna = nuevaRef;
    }

    public boolean estaActivo() {
        //Este método sirve para preguntar si el handle todavía es válido. Es útil para detectar errores o estados inconsistentes, como querer usar un handle que ya fue invalidado.
        return activo;
    }

    protected void invalidar() {
        //Este método marca el handle como inactivo, y borra su referencia interna. Es útil si, por ejemplo, el objeto al que apuntaba fue eliminado. Así se evita que el sistema (o el usuario) intente acceder a algo que ya no existe.
        this.activo = false;
        this.refInterna = null;
    }
}