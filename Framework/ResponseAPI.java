package etu1748.framework;

public class ResponseAPI {
    private Object data;
    private String error;
    private int status = 200;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ResponseAPI() {
    }

    public ResponseAPI(Object data, String error) {
        this.data = data;
        this.error = error;
    }
}
