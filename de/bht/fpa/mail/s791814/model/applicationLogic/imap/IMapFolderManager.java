package de.bht.fpa.mail.s791814.model.applicationLogic.imap;

import de.bht.fpa.mail.s791814.model.applicationLogic.xml.FolderManagerIF;
import de.bht.fpa.mail.s791814.model.datas.Account;
import de.bht.fpa.mail.s791814.model.datas.Folder;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Mark Deuerling
 */
public class IMapFolderManager implements FolderManagerIF{
    
    protected Account acc;
    protected Store store;
    protected javax.mail.Folder javaxFolder;
    protected Folder topFolder;
    protected boolean isFolder;
    
    public IMapFolderManager(Account acc){
        isFolder = true;
        this.acc = acc;
        
        store = IMapConnectionHelper.connect(acc);
        
        try {
            if(store != null){
                javaxFolder = store.getDefaultFolder();
                topFolder = new Folder(new File(acc.getName()), true);
            }else{
                topFolder = new Folder(new File("Account data not correct"), false);
            }
            
        } catch (MessagingException ex) {
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
//            try {
//                store.close();
//            } catch (MessagingException ex) {
//                Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
        
        
    }

    @Override
    public Folder getTopFolder() {
        
        return topFolder;
    }

    @Override
    public void loadContent(Folder f) {
        try {
            store = IMapConnectionHelper.connect(acc);
            if(f.getComponents().isEmpty() && store != null){ 
//                javax.mail.Folder javaxFolder;
                if(isFolder){
                    javaxFolder = store.getDefaultFolder();
                    isFolder = false;
                }else{
                    javaxFolder = store.getFolder(f.getName());
                }
                
                if(javaxFolder.exists() && store != null){
                    for(javax.mail.Folder jxFolder : javaxFolder.list()){
                        Folder folder;
                        if(jxFolder.list().length == 0){
                            folder = new Folder(new File(jxFolder.getName()), false);
                            folder.setPath(jxFolder.getFullName());
                            f.addComponent(folder);
                        } else {
                            folder = new Folder(new File(jxFolder.getName()), true);
                            folder.setPath(jxFolder.getFullName());
                            f.addComponent(folder);
                        }
                    }
                }
            }
        } catch (MessagingException ex) {
            Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
//            try {
//                store.close();
//            } catch (MessagingException ex) {
//                Logger.getLogger(IMapFolderManager.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }
    
}
