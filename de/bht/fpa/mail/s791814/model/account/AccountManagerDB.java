package de.bht.fpa.mail.s791814.model.account;

/**
 *
 * @author Mark Deuerling
 */
public class AccountManagerDB extends AccountManager{

    public AccountManagerDB() {
       dao = new AccountDBDAO();
       
    }
    
    
}
