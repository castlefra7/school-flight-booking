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
public class PassengerAttr {
    private String first_name;
    private String last_name;
    private String passenger_categ;
    private int id_place_type;

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
