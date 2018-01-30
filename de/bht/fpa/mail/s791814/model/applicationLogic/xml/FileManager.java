package de.bht.fpa.mail.s791814.model.applicationLogic.xml;

import de.bht.fpa.mail.s791814.model.datas.*;
import java.io.File;


/*
 * This class manages a hierarchy of folders and their content which is loaded 
 * from a given directory path.
 * 
 * @author Simone Strippgen
 */
public class FileManager implements FolderManagerIF {

    //top Folder of the managed hierarchy
    Folder topFolder;

    /**
     * Constructs a new FileManager object which manages a folder hierarchy,
     * where file contains the path to the top directory. The contents of the
     * directory file are loaded into the top folder
     *
     * @param file File which points to the top directory
     */
    public FileManager(File file) {

        topFolder = new Folder(file, true);
        
//        for (File f : file.listFiles()) {
//            if (f.isDirectory()) { 
//                
//                boolean deepDirs = false;
//                for(File deepF : f.listFiles()){
//                    if(deepF.isDirectory()){
//                        deepDirs = true;
//                        break;
//                    }else{
//                        deepDirs = false;
//                    }
//                }                
//                if(deepDirs){
//                    topFolder.addComponent(new Folder(f, true));
//                }else{
//                    topFolder.addComponent(new Folder(f, false));
//                }
//            }else{
//              // topFolder.addComponent(new FileElement(f));
//            }
//        }
    }

    /**
     * Loads all relevant content in the directory path of a folder object into
     * the folder.
     *
     * @param f the folder into which the content of the corresponding directory
     * should be loaded
     */
    @Override
    public void loadContent(Folder f) {
    File file = new File(f.getPath());
        
        for (File assistFile : file.listFiles()) {
            if (assistFile.isDirectory()) { 
                
                boolean deepDirs = false;
                for(File deepF : assistFile.listFiles()){
                    if(deepF.isDirectory()){
                        deepDirs = true;
                        break;
                    }else{
                        deepDirs = false;
                    }
                }                
                if(deepDirs){
                    f.addComponent(new Folder(assistFile, true));
                }else{
                    f.addComponent(new Folder(assistFile, false));
                }
            }else{
              // topFolder.addComponent(new FileElement(f));
            }
        }
    }

    @Override
    public Folder getTopFolder() {
        return topFolder;
    }
}
