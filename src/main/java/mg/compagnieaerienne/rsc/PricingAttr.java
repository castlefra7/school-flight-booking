/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.rsc;

/**
 *
 * @author lacha
 */
public class PricingAttr {
    private int id_place_type;
    private int id_route;
    private String passenger_categ;
    private double price;

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
    
}
