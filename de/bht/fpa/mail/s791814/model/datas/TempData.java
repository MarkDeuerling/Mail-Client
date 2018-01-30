/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.bht.fpa.mail.s791814.model.datas;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mark Deuerling
 */
public class TempData {
    protected static TempData tmp = new TempData();
    protected List<String> tmpL = new ArrayList<>();
    
    private TempData(){        
        //Factory Class
    }
    
    public static TempData getTempData(){
        return tmp;
    }
    
    public List<String> getData(){
        return tmpL;
    }
    
    public void addData(String data){
        tmpL.add(data);
    }
    
}
