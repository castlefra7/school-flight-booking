/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import mg.compagnieaerienne.gen.FctGen;

/**
 *
 * @author lacha
 */
public class Booking extends BaseModel {

    private Timestamp date_booking;
    private int id_flight;
    private int id_customer;
    private int booking_number;

    public int getBooking_number() {
        return booking_number;
    }

    public void setBooking_number(int booking_number) {
        this.booking_number = booking_number;
    }

    public boolean isBookingNumberExist(int _bookingNumber, Connection conn) throws SQLException {
        String req = "select * from bookings where booking_number = " + _bookingNumber;
        return FctGen.exists(req, conn);
    }

    public double totalPrice(int _bookingNumber, Connection conn) throws SQLException {
        String req = String.format("select total_price  from all_bookings_total_price where booking_number  = %d", _bookingNumber);
        return FctGen.getDouble("total_price", req, conn);
    }

    public int getLastUniqueBookingNumber(Connection conn) throws SQLException {
        String req = "select last_value from bookingNumber";
        return FctGen.getInt("last_value", req, conn);
    }

    public int getUniqueBookingNumber(Connection conn) throws SQLException {
        String req = "select nextval('bookingNumber') as seq";
        return FctGen.getInt("seq", req, conn);
    }

    @Override
    public void insert(Connection conn) throws Exception {
        String columns = "date_booking;id_flight;id_customer;booking_number";
        FctGen.insert(this, columns, "bookings", conn);
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

    public Timestamp getDate_booking() {
        return date_booking;
    }

    public void setDate_booking(Timestamp date_booking) {
        this.date_booking = date_booking;
    }

    public int getId_flight() {
        return id_flight;
    }

    public void setId_flight(int id_flight) {
        this.id_flight = id_flight;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

}
