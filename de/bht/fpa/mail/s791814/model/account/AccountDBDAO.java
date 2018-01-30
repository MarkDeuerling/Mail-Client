package de.bht.fpa.mail.s791814.model.account;

import static de.bht.fpa.mail.s791814.model.account.AccountFileDAO.ACCOUNT_FILE;
import de.bht.fpa.mail.s791814.model.datas.Account;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Mark Deuerling
 */
public class AccountDBDAO implements AccountDAOIF{

    protected EntityManagerFactory emf;
    protected EntityManager em;
    
    public AccountDBDAO(){
        emf = Persistence.createEntityManagerFactory("fpa_mysql");
        em = emf.createEntityManager();
//        saveAccounts(TestAccountProvider.createAccounts());
        
        
    }
    
    @Override
    public List<Account> getAllAccounts() {
        em = emf.createEntityManager();
        Query q = em.createQuery("SELECT accounts FROM Account accounts");
        
        List<Account> accountList = q.getResultList();
        em.close();
        return accountList;
    }

    @Override
    public Account saveAccount(Account acc) {
        em = emf.createEntityManager();
        
        EntityTransaction trans = em.getTransaction();
        trans.begin();
      
        em.persist(acc);
        
        trans.commit();
        em.close();
        
        return acc;
    }

    @Override
    public boolean updateAccount(Account acc) {
        em = emf.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        Query q = em.createQuery("DELETE FROM Account accounts WHERE accounts.name LIKE '"+acc.getName()+"%' ");
        
        trans.begin();
        q.executeUpdate();
        em.merge(acc);
        trans.commit();
        em.close();
        
        
        return true;
    }
    
    private void saveAccounts(List<Account> accList) {
        try {
            EntityManager em = emf.createEntityManager();
            EntityTransaction trans = em.getTransaction();
            
            
            
            File accountFile = ACCOUNT_FILE;
            System.out.println(accountFile);
            boolean deleted = accountFile.delete();
            FileOutputStream fileOutput = new FileOutputStream(accountFile);
            ObjectOutputStream os = new ObjectOutputStream(fileOutput);
            for (Account a : accList) {
                trans.begin();
                em.persist(a);
                trans.commit();
            }
            
            em.close();
            os.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
