/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.util.List;
import mg.compagnieaerienne.gen.FctGen;
import org.slf4j.Logger;

/**
 *
 * @author lacha
 */
public class FlightSummary extends Flight {

    private int id_flight;
    private int id_place_type;
    private int remain_places;
    private String name_placeType;
    private String id_town_destination;
    private String dest_name;
    private String name;
    private String origin_name;
    private int number_places;

    @Override
    public void insert(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        String req = "select remain_places, id_place_type, id_flight, date_flight_departure,"
                + "date_flight_arriving, origin_name, dest_name, name, name_placetype, number_places from all_flights_summary_with_details";
        String columns = "id_flight;id_place_type;remain_places;name;number_places;origin_name;"
                + "date_flight_arriving;date_flight_departure;dest_name;name_placetype";
        return (List<BaseModel>) (List<?>) FctGen.findAll(this, req, columns, conn);
    }

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public int getId_flight() {
        return id_flight;
    }

    public void setId_flight(int id_flight) {
        this.id_flight = id_flight;
        this.setId(id_flight);
    }

    public int getId_place_type() {
        return id_place_type;
    }

    public void setId_place_type(int id_place_type) {
        this.id_place_type = id_place_type;
    }

    public int getRemain_places() {
        return remain_places;
    }

    public void setRemain_places(int remain_places) {
        this.remain_places = remain_places;
    }

    public String getName_placeType() {
        return name_placeType;
    }

    public void setName_placeType(String name_placeType) {
        this.name_placeType = name_placeType;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getId_town_destination() {
        return id_town_destination;
    }

    public void setId_town_destination(String id_town_destination) {
        this.id_town_destination = id_town_destination;
    }

    public String getDest_name() {
        return dest_name;
    }

    public void setDest_name(String dest_name) {
        this.dest_name = dest_name;
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

}
