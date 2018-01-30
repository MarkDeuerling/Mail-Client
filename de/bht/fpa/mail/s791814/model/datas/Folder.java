package de.bht.fpa.mail.s791814.model.datas;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 *
 * @author Simone Strippgen
 *
 */

@Entity
public class Folder extends Component implements Serializable{

    @Transient
    transient private boolean expandable;
    
    @Transient
    transient private ArrayList<Component> content;
    
    @Transient
    transient private ArrayList<Email> emails;

    public Folder(){
        expandable = true;
        content = new ArrayList<>();
        emails = new ArrayList<>();
        
    }
    
    public Folder(File path, boolean expandable) {
        super(path);
        this.expandable = expandable;
        content = new ArrayList<Component>();
        emails = new ArrayList<Email>();
    }

    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public void addComponent(Component comp) {
        content.add(comp);
    }

    @Override
    public List<Component> getComponents() {
        return content;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void addEmail(Email message) {
        emails.add(message);
    }
 }
