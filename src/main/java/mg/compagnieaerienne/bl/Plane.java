/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lacha
 */
public class Plane extends BaseModel {
    
    private String name;
    private int number_places;
    
    @Override
    public void insert(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List<BaseModel> result = new ArrayList();
        try {
            String sql = "select id,name, number_places from planes ";
            pstmt = conn.prepareStatement(sql);
            res = pstmt.executeQuery();
            while (res.next()) {
                Plane plane = new Plane();
                plane.setId(res.getInt(1));
                plane.setName(res.getString(2));
                plane.setNumber_places(res.getInt(3));
                result.add(plane);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (res != null) {
                res.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return result;
    }
    
    @Override
    public void update(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getNumber_places() {
        return number_places;
    }
    
    public void setNumber_places(int number_places) {
        this.number_places = number_places;
    }

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
