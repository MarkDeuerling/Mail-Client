package de.bht.fpa.mail.s791814.model.applicationLogic.xml;

import de.bht.fpa.mail.s791814.model.datas.Email;
import de.bht.fpa.mail.s791814.model.datas.Folder;
import java.io.File;
import javax.xml.bind.JAXB;

/**
 *
 * @author Mark Deuerling
 */
public class XMLEmailManager implements EmailManagerIF {

    @Override
    public void loadEmails(Folder f) {
        File file = new File(f.getPath());

        if (f.getEmails().isEmpty()) {

            Folder topFolder = new Folder(file, true);

            for (File fl : file.listFiles()) {
                String xmlS = fl.getName();

                if (xmlS.endsWith("xml")) {
                    Email eml = JAXB.unmarshal(fl, Email.class);
                    topFolder.addEmail(eml);
                }
            }
           
            for(Email email : topFolder.getEmails()){
                f.addEmail(email);
            }
        }
    }

}
