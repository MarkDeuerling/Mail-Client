package de.bht.fpa.mail.s791814.model.applicationLogic;

import de.bht.fpa.mail.s791814.model.applicationLogic.xml.EmailManagerIF;
import de.bht.fpa.mail.s791814.model.applicationLogic.xml.XMLEmailManager;
import de.bht.fpa.mail.s791814.model.applicationLogic.xml.FolderManagerIF;
import de.bht.fpa.mail.s791814.model.applicationLogic.xml.FileManager;
import de.bht.fpa.mail.s791814.model.account.AccountManagerDB;
import de.bht.fpa.mail.s791814.model.account.AccountManagerIF;
import de.bht.fpa.mail.s791814.model.applicationLogic.imap.IMapEmailManager;
import de.bht.fpa.mail.s791814.model.applicationLogic.imap.IMapFolderManager;
import de.bht.fpa.mail.s791814.model.datas.Account;
import de.bht.fpa.mail.s791814.model.datas.Email;
import de.bht.fpa.mail.s791814.model.datas.Folder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXB;

/**
 *
 * @author Mark Deuerling
 */
public class Facade implements ApplicationLogicIF{

    protected EmailManagerIF emailManagerIF;
    protected FolderManagerIF folderManagerIF;
    protected Folder emailFolder;
    protected AccountManagerIF accountManagerIF;
    
    
    public Facade(File file){
        folderManagerIF = new FileManager(file);
        emailManagerIF  = new XMLEmailManager();
        
//        accountManagerIF = new AccountManager() //<-- local save
        accountManagerIF = new AccountManagerDB(); //<-- db save
        
    }
    
    @Override
    public Folder getTopFolder() {

        return folderManagerIF.getTopFolder();
    }

    @Override
    public void loadContent(Folder folder) {
        folderManagerIF.loadContent(folder);
    }

    @Override
    public List<Email> search(String pattern) {
        List<Email> al = new ArrayList<>();
        for(Email email : emailFolder.getEmails()){
            if(email.toString().toLowerCase().contains(pattern) || 
                    email.getText().toLowerCase().contains(pattern))
            {
                al.add(email);
            }
        }
        return al;
    }

    @Override
    public void loadEmails(Folder folder) {
        emailFolder = folder;
        emailManagerIF.loadEmails(folder);
    }

    @Override
    public void changeDirectory(File file) {
        folderManagerIF = new FileManager(file);
        emailManagerIF  = new XMLEmailManager();
    }

    @Override
    public void saveEmails(File file) {  
        int i = 0;
        for(Email email : emailFolder.getEmails()){
            File f = new File(file.getPath() +  "/xml_mail" + i + ".xml");
            JAXB.marshal(email, f);
            i++;
        }
    }

    @Override
    public void openAccount(String name) {
        Account acc = accountManagerIF.getAccount(name);
        folderManagerIF = new IMapFolderManager(acc);
        emailManagerIF  = new IMapEmailManager(acc);
    }

    @Override
    public List<String> getAllAccounts() {
        List<String> allStringAccounts = new ArrayList<>();
        for(Account acc : accountManagerIF.getAllAccounts()){
            allStringAccounts.add(acc.getName());
        }
        
        return allStringAccounts;
    }

    @Override
    public Account getAccount(String name) {
        return accountManagerIF.getAccount(name);
    }

    @Override
    public boolean saveAccount(Account account) {
        return accountManagerIF.saveAccount(account);
    }

    @Override
    public void updateAccount(Account account) {
        accountManagerIF.updateAccount(account);
    }
    
}
