/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import mg.compagnieaerienne.gen.FctGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lacha
 */
public class Passenger extends BaseModel {

    Logger logger = LoggerFactory.getLogger(Passenger.class);
    private int id_customer;
    private String first_name;
    private String last_name;
    private String passenger_categ;
    private int id_place_type;
    private double price;

    public double findPrice(int _id_route, Connection conn) throws SQLException {
        String req = String.format("select price from pricings where id_place_type = %d and  id_route = %d and passenger_categ = '%s'",
                this.getId_place_type(), _id_route, this.getPassenger_categ());
        logger.info(req);
        return FctGen.getDouble("price", req, conn);
    }

    @Override
    public void insert(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        try {
            String sql = "insert into passengers (id_customer, first_name, last_name, passenger_categ, id_place_type, price) "
                    + "values (?,?,?, ?::passengerCateg, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.getId_customer());
            pstmt.setString(2, this.getFirst_name());
            pstmt.setString(3, this.getLast_name());
            pstmt.setString(4, this.getPassenger_categ());
            pstmt.setInt(5, this.getId_place_type());
            pstmt.setDouble(6, this.getPrice());
            pstmt.execute();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassenger_categ() {
        return passenger_categ;
    }

    public void setPassenger_categ(String passenger_categ) {
        this.passenger_categ = passenger_categ;
    }

    public int getId_place_type() {
        return id_place_type;
    }

    public void setId_place_type(int id_place_type) {
        this.id_place_type = id_place_type;
    }

}
