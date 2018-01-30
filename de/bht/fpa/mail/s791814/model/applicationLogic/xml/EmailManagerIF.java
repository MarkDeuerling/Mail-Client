/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791814.model.applicationLogic.xml;

import de.bht.fpa.mail.s791814.model.datas.Folder;

/**
 *
 * @author admin
 */
public interface EmailManagerIF {
    public void loadEmails(Folder f);
    
}
