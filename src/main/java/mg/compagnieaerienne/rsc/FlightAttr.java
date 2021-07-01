/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.rsc;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author lacha
 */
public class FlightAttr {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date_flight_departure;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date_flight_arriving;
    private int id_plane;
    private int id_route;

    public Date getDate_flight_departure() {
        return date_flight_departure;
    }

    public void setDate_flight_departure(Date date_flight_departure) {
        this.date_flight_departure = date_flight_departure;
    }

    public Date getDate_flight_arriving() {
        return date_flight_arriving;
    }

    public void setDate_flight_arriving(Date date_flight_arriving) {
        this.date_flight_arriving = date_flight_arriving;
    }

    public int getId_plane() {
        return id_plane;
    }

    public void setId_plane(int id_plane) {
        this.id_plane = id_plane;
    }

    public int getId_route() {
        return id_route;
    }

    public void setId_route(int id_route) {
        this.id_route = id_route;
    }
    
    
}
