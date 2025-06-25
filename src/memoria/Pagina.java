package memoria;

/**
 * Simula uma página de memória virtual.
 */
public class Pagina {
    private final int id;
    private final int processoId;
    private boolean referenciada;
    private boolean modificada;
    private boolean presente;
    private int moldura;

    public Pagina(int id, int processoId) {
        this.id = id;
        this.processoId = processoId;
        this.referenciada = false;
        this.modificada   = false;
        this.presente     = false;
        this.moldura      = -1;
    }

    public int getId() { return id; }
    public int getProcessoId() { return processoId; }
    public boolean isReferenciada() { return referenciada; }
    public boolean isModificada()   { return modificada; }
    public boolean isPresente()     { return presente; }
    public int getMoldura()         { return moldura; }

    public void setReferenciada(boolean r) { this.referenciada = r; }
    public void setModificada(boolean m)   { this.modificada   = m; }
    public void setPresente(boolean p)     { this.presente     = p; }
    public void setMoldura(int m)          { this.moldura      = m; }

    @Override
    public String toString() {
        return "P" + processoId + "P" + id;
    }
}
