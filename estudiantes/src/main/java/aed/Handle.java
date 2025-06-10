package aed;

// Clase reutilizable
public class Handle<T, Ref> {
    private Ref refInterna;
    private boolean activo;

    public Handle(Ref refInterna) {
        this.refInterna = refInterna;
        this.activo = true;
    }

    public Ref getRef() {
        return refInterna;
    }

    protected void setRef(Ref nuevaRef) {
        this.refInterna = nuevaRef;
    }

    public boolean estaActivo() {
        return activo;
    }

    protected void invalidar() {
        this.activo = false;
        this.refInterna = null;
    }
}


