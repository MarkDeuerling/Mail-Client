package de.bht.fpa.mail.s791814.model.applicationLogic.imap;

import de.bht.fpa.mail.s791814.model.applicationLogic.xml.EmailManagerIF;
import de.bht.fpa.mail.s791814.model.datas.Account;
import de.bht.fpa.mail.s791814.model.datas.Email;
import de.bht.fpa.mail.s791814.model.datas.Folder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 *
 * @author Mark Deuerling
 */
public class IMapEmailManager implements EmailManagerIF{
    
    protected Store store;
    protected Account acc;
    
    public IMapEmailManager(Account acc){
        //https://www.google.com/settings/security/lesssecureapps //activate security
        this.acc = acc;
        
    }

    @Override
    public void loadEmails(Folder f) {
        try {
            store = IMapConnectionHelper.connect(acc);
            if(f.getEmails().isEmpty() && store != null){
                javax.mail.Folder subFolder = store.getFolder(f.getPath()); //
                if(subFolder.exists()){
                    subFolder.open(javax.mail.Folder.READ_ONLY);
                    if(subFolder.isOpen()){
                        Message[] messages = subFolder.getMessages();

                        for(Message m : messages){
                            Email e = IMapEmailConverter.convertMessage(m);
                            f.addEmail(e);
                        }
                    }
                }
            }
        } catch (MessagingException ex) {
            Logger.getLogger(IMapEmailManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                store.close();
            } catch (MessagingException ex) {
                Logger.getLogger(IMapEmailManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
