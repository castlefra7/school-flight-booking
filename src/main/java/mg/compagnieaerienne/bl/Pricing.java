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
import mg.compagnieaerienne.controllers.AdminFlightController;
import mg.compagnieaerienne.rsc.PricingAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lacha
 */
public final class Pricing extends BaseModel {

    private int id_place_type;
    private int id_route;
    private String passenger_categ;
    private double price;

    private PlaceType placeType;
    private Route route;

    public Pricing(PricingAttr attr) {
        this.setId_place_type(attr.getId_place_type());
        this.setId_route(attr.getId_route());
        this.setPassenger_categ(attr.getPassenger_categ());
        this.setPrice(attr.getPrice());
    }

    public Pricing() {
    }

    @Override
    public List<BaseModel> findAll(Object criteria, Connection conn) throws Exception {
        String req = String.format("select * from all_pricings where id_route = %d",
                criteria);
        return this.findAll(req, conn);
    }

    @Override
    public void insert(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        try {
            String req = "insert into pricings (id_place_type, id_route, passenger_categ, price) values  (?,?,?::passengerCateg,?)";
            pstmt = conn.prepareStatement(req);
            pstmt.setInt(1, this.getId_place_type());
            pstmt.setInt(2, this.getId_route());
            pstmt.setString(3, this.getPassenger_categ());
            pstmt.setDouble(4, this.getPrice());

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
        String req = "select * from all_pricings";
        return this.findAll(req, conn);
    }
    Logger logger = LoggerFactory.getLogger(Pricing.class);

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List<BaseModel> result = new ArrayList();
        try {
            pstmt = conn.prepareStatement(req);
            //pstmt.setInt(1, Integer.valueOf(criteria.toString()));
            res = pstmt.executeQuery();
            while (res.next()) {
                Pricing newPricing = new Pricing();
                PlaceType _placeType = new PlaceType();
                _placeType.setId(res.getInt("id_place_type"));
                _placeType.setName(res.getString("name"));
                newPricing.setPlaceType(_placeType);
                newPricing.setId(res.getInt("id"));
                newPricing.setId_place_type(res.getInt("id_place_type"));
                newPricing.setId_route(res.getInt("id_route"));
                newPricing.setPassenger_categ(res.getString("passenger_categ"));
                newPricing.setPrice(res.getDouble("price"));

                Route _route = new Route();
                _route.setId(res.getInt("id_route"));
                _route.setTownOrigin(new Town(res.getInt("id_town_origin"), res.getString("origin_name")));
                _route.setTownDest(new Town(res.getInt("id_town_destination"), res.getString("dest_name")));
                newPricing.setRoute(_route);
                result.add(newPricing);
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

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getId_place_type() {
        return id_place_type;
    }

    public void setId_place_type(int id_place_type) {
        this.id_place_type = id_place_type;
    }

    public int getId_route() {
        return id_route;
    }

    public void setId_route(int id_route) {
        this.id_route = id_route;
    }

    public String getPassenger_categ() {
        return passenger_categ;
    }

    public void setPassenger_categ(String passenger_categ) {
        this.passenger_categ = passenger_categ;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }

}
