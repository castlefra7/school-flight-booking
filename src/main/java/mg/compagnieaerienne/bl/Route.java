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
public class Route extends BaseModel {
    private int id_town_origin;
    private int id_town_destination;
    
    private Town townOrigin;
    private Town townDest;

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
            String sql = "select id_route, id_town_origin, origin_name, id_town_destination, dest_name from all_routes ";
            pstmt = conn.prepareStatement(sql);
            res = pstmt.executeQuery();
            while(res.next()) {
                Route route = new Route();
                route.setId(res.getInt(1));
                route.setTownOrigin(new Town(res.getInt(2), res.getString(3)));
                route.setTownDest(new Town(res.getInt(4), res.getString(5)));
                result.add(route);
            }
        } catch(Exception ex) {
            throw ex;
        } finally {
            if(res != null) res.close();
            if(pstmt != null) pstmt.close();
        }
        return result;
    }

    @Override
    public void update(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getId_town_origin() {
        return id_town_origin;
    }

    public void setId_town_origin(int id_town_origin) {
        this.id_town_origin = id_town_origin;
    }

    public int getId_town_destination() {
        return id_town_destination;
    }

    public void setId_town_destination(int id_town_destination) {
        this.id_town_destination = id_town_destination;
    }

    public Town getTownOrigin() {
        return townOrigin;
    }

    public void setTownOrigin(Town townOrigin) {
        this.townOrigin = townOrigin;
    }

    public Town getTownDest() {
        return townDest;
    }

    public void setTownDest(Town townDest) {
        this.townDest = townDest;
    }

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
