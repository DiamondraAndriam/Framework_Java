import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class Main {
    public static void main(String[] args) {
        /*try {
            String json = "[{\"method\":[\"findAll\",\"update\",\"delete\",\"save\"],\"profil\":\"admin\",\"token\":\"ee13d8a7-985d-49fc-b27c-2f1ec8b4c047\"},{\"method\":[\"findAll\"],\"profil\":\"normal\",\"token\":\"ee13d8a7-985d-49fc-b27c-2f1ec8b4c047\"}]";
            Gson gson = new GsonBuilder().create();
            Type tokenListType = new TypeToken<List<Token>>(){}.getType();
            List<Token> token = gson.fromJson(json, tokenListType);
            System.out.println(token.get(0).method.size());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        System.out.println(UUID.randomUUID().toString());
    }

    static class Token {
        private String token;
        private List<String> method;
        private String profile;
    }

    static class Authorised {
        private List<Token> config;
    }
}

/*
    public class MonServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer le token d'autorisation de l'en-tête Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Enlever "Bearer " pour obtenir le token
            // Vérifier le token et effectuer l'action appropriée
            System.out.println("Token d'autorisation reçu : " + token);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Accès non autorisé");
        }
    }
}
 */