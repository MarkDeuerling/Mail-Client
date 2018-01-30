package de.bht.fpa.mail.s791814.model.datas;


import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;

/*
 * This is the component part of a composite pattern.
 * 
 * @author Simone Strippgen
 */

@Entity
@Inheritance
public abstract class Component implements Serializable{
    // absolute directory path to this component
    @Id
    @GeneratedValue
    private Long id;
 
    private String path;
    // name of the component (without path)
    private String name;

    public Component(){
        //standard constr.
    }
    public Component(File path) {
        this.path = path.getAbsolutePath();
        this.name = path.getName();
    }

    public void addComponent(Component comp) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Component> getComponents() {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // @return is the component expandable
    public abstract boolean isExpandable();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String p) {
        path = p;
    }

    public String getPath() {
        return path;
    }

    public String toString() {
        return name;
    }
}
