package de.bht.fpa.mail.s791814.model.account;

import de.bht.fpa.mail.s791814.model.datas.Account;
import java.util.List;

/**
 *
 * @author Mark Deuerling
 */
public class AccountManager implements AccountManagerIF{
    
    protected Account account;
    protected AccountDAOIF dao;
    
    public AccountManager(){
        dao = new AccountFileDAO();
//        dao = new AccountDBDAO();
    }

    @Override
    public Account getAccount(String name) {
        for(Account acc : dao.getAllAccounts()){
            if(acc.getName().equals(name))
                account = acc;
        }
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        return dao.getAllAccounts();
    }

    @Override
    public boolean saveAccount(Account acc) {
        account = dao.saveAccount(acc);
        
        return true; //
    }

    @Override
    public boolean updateAccount(Account account) {
        return dao.updateAccount(account);
    }
    
}
