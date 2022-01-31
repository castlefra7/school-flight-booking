/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.starter.starter_boilerplate;

import mg.compagnieaerienne.conn.ConnGen;
import java.sql.Connection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mg.compagnieaerienne.bl.BaseModel;
import mg.compagnieaerienne.bl.StatPayment;

/**
 *
 * @author lacha
 */
public class MainTest {

    public static void main(String[] args) throws Exception {
        List<BaseModel> result = new  StatPayment().findAll();
        for(BaseModel m: result) {
            StatPayment st = (StatPayment)m;
            //System.out.println(st.getDate() + " " + st.getTotal_amount());
        }
        
        // PDFGen
        
        // ExcelGen

    }

}
