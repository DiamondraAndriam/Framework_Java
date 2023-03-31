package etu1748.framework;

public class ModelView{
    String view;

    public String getView(){
        return this.view;
    }
    public void setView(String view){
        this.view = view;
    }

    public ModelView(String view){
        setView(view);
    }
}