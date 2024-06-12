package login;

import etu1748.framework.ResponseAPI;
import etu1748.framework.annotation.*;
import etu1748.framework.util.Util;

@MVCController
public class LoginController {
    @Urls("login.do")
    @JSON("POST")
    public ResponseAPI login(Login login) {
        String token = null;
        boolean check = false;
        if (login.getUser().equals("user") && login.password().equals("1234")) {
            token = "f0185f9e-ba4f-4922-9fa5-4154baa754c4";
            Util.addConfig(token);
            check = true;
        }
        if (check) {
            return new ResponseAPI();
        } else {
            return new ResponseAPI(null, "Non connecté");
        }
    }

    @Urls("logout.do")
    @JSON("GET")
    @Auth
    public void logout() {
    }
}
