package etu1748.framework;

import java.util.List;

public class Token {
    private String token;
    private List<String> method;
    private String profile;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getMethod() {
        return method;
    }

    public void setMethod(List<String> method) {
        this.method = method;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
