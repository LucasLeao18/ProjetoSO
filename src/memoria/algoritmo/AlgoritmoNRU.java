package memoria.algoritmo;

import memoria.Pagina;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Implementa a política NRU (Not Recently Used).
 */
public class AlgoritmoNRU implements Substituicao {

    @Override
    public Pagina substituir(List<Pagina> paginas) {
        List<List<Pagina>> classes = new ArrayList<>();
        for (int i = 0; i < 4; i++) classes.add(new ArrayList<>());

        for (Pagina p : paginas) {
            int idx = (!p.isReferenciada() && !p.isModificada()) ? 0
                    : (!p.isReferenciada() && p.isModificada()) ? 1
                    : (p.isReferenciada() && !p.isModificada()) ? 2
                    : 3;
            classes.get(idx).add(p);
        }

        for (List<Pagina> cls : classes) {
            if (!cls.isEmpty()) {
                Collections.shuffle(cls);
                return cls.get(0);
            }
        }
        return paginas.get(0);
    }
}
