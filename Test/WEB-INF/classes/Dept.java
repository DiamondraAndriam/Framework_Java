package modele;

import etu1748.framework.ModelView;
import etu1748.framework.annotation.*;

public class Dept {

    @Urls("dept-view")
    public ModelView getDept() {
        return new ModelView("test_singleton2.jsp");
    }
}
