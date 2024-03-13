package api;

public class ResponseAPI {
    private Object data;
    private String error;
    htt

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

    public ResponseAPI(Object data) {
        this.data = data;
    }

    public ResponseAPI(String error) {
        this.error = error;
    }
}
