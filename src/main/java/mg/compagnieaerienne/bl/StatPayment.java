/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import mg.compagnieaerienne.gen.FctGen;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author lacha
 */
public class StatPayment extends BaseModel {

    private Timestamp date;
    private double total_amount;

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {

        this.date = date;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    @Override
    public void insert(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        String req = "select * from stats_payments_by_date";
        return (List<BaseModel>) (List<?>) FctGen.findAll(this, req, "total_amount;date", conn);
    }

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
