package etu1748.framework;

public class FileUpload {
    String name;
    String path;
    byte[] file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public FileUpload() {
    }

    public FileUpload(String name, String path, byte[] file) {
        setName(name);
        setPath(path);
        setFile(file);
    }

}
