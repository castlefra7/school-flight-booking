/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import mg.compagnieaerienne.conn.ConnGen;
import mg.compagnieaerienne.gen.FctGen;

/**
 *
 * @author lacha
 */
public class Ticket extends BaseModel {

    private String first_name;
    private String last_name;
    private Timestamp date_flight_arriving;
    private Timestamp date_flight_departure;
    private Timestamp date_booking;
    private String passenger_categ;
    private double price;
    private String name_placetype;
    private String name;
    private String origin_name;
    private String dest_name;

    private String full_name;
    private String email;
    private String phone_number;
    private int booking_number;

    public List<BaseModel> findFlightDetails(int _id_flight) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            String req = "select * from all_flights_passengers where id_flight = " + _id_flight;
            return this.findAll(req);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public boolean isPaid(int _booking_number) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            String req = "select * from payments where booking_number = " + _booking_number;
            return FctGen.exists(req, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<BaseModel> findAllTickets(int bookingNumber) throws Exception {
        int[] bookingNumbers = new int[1];
        bookingNumbers[0] = bookingNumber;
        return this.findAllTickets(bookingNumbers);
    }

    public List<BaseModel> findAllTickets(int[] bookingNumbers) throws Exception {
        String req = "select * from all_flights_passengers where ";
        for (int bookingNumb : bookingNumbers) {
            req += " booking_number = " + bookingNumb + " or ";
        }
        req = req.substring(0, req.length() - 4);
        return this.findAll(req);
    }

    @Override
    public void insert(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {

        String columns = "first_name;last_name;date_flight_arriving;date_flight_departure;date_booking;passenger_categ;"
                + "price;name_placetype;name;origin_name;dest_name;full_name;email;phone_number;booking_number";
        //String req = "select * from all_flights_passengers where "
        return (List<BaseModel>) (List<?>) FctGen.findAll(this, req, columns, conn);
    }

    @Override
    public void update(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public Timestamp getDate_flight_arriving() {
        return date_flight_arriving;
    }

    public void setDate_flight_arriving(Timestamp date_flight_arriving) {
        this.date_flight_arriving = date_flight_arriving;
    }

    public Timestamp getDate_flight_departure() {
        return date_flight_departure;
    }

    public void setDate_flight_departure(Timestamp date_flight_departure) {
        this.date_flight_departure = date_flight_departure;
    }

    public Timestamp getDate_booking() {
        return date_booking;
    }

    public void setDate_booking(Timestamp date_booking) {
        this.date_booking = date_booking;
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

    public String getName_placetype() {
        return name_placetype;
    }

    public void setName_placetype(String name_placetype) {
        this.name_placetype = name_placetype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getBooking_number() {
        return booking_number;
    }

    public void setBooking_number(int booking_number) {
        this.booking_number = booking_number;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public String getDest_name() {
        return dest_name;
    }

    public void setDest_name(String dest_name) {
        this.dest_name = dest_name;
    }

}
