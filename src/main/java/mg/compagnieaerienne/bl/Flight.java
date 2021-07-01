/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import mg.compagnieaerienne.conn.ConnGen;
import mg.compagnieaerienne.gen.FctGen;
import mg.compagnieaerienne.rsc.CustomerAttr;
import mg.compagnieaerienne.rsc.FlightAttr;
import mg.compagnieaerienne.rsc.FlightSearchAttr;
import mg.compagnieaerienne.rsc.PassengerAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lacha
 */
public class Flight extends BaseModel {

    public static int ID_NO_FLIGHT = -1;

    Logger logger = LoggerFactory.getLogger(Flight.class);

    private Timestamp date_flight_arriving;
    private Timestamp date_flight_departure;
    private int id_plane;
    private int id_route;

    private Route route;
    private Plane plane;

    private List<Pricing> pricings;

    public Flight() {
    }

    public Flight(FlightAttr _attr) {

        setId_plane(_attr.getId_plane());
        setId_route(_attr.getId_route());
        this.setDate_flight_arriving(new Timestamp(_attr.getDate_flight_arriving().getTime()));
        this.setDate_flight_departure(new Timestamp(_attr.getDate_flight_departure().getTime()));
    }

    public Flight(int _id, int _id_plane, int _id_route, Timestamp _date_arr, Timestamp _date_dep) {
        setId(_id);
        setId_plane(_id_plane);
        setId_route(_id_route);
        this.setDate_flight_arriving(_date_arr);
        this.setDate_flight_departure(_date_dep);
    }

    public void setIdTowns(FlightSearchAttr searchAttr) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            Town town = new Town();
            searchAttr.setId_town_origin(town.findOneTownByName(searchAttr.getTownOrigin(), conn).getId());
            searchAttr.setId_town_destination(town.findOneTownByName(searchAttr.getTownDest(), conn).getId());
        } catch (Exception ex) {
            throw ex;
        }
    }
    
      public List<Integer> book(List<PassengerAttr> _passengers,
            CustomerAttr _custAttr,
            FlightSearchAttr fligthSearch,
            int[] _id_flights) throws Exception {
        List<Integer> result = new ArrayList();
        for (int iD = 0; iD < _id_flights.length; iD++) {
            if(_id_flights[iD] == ID_NO_FLIGHT) continue;
            result.add(book(_passengers, _custAttr, fligthSearch, _id_flights[iD]));
        }
        return result;
    }

    public int book(List<PassengerAttr> _passengers,
            CustomerAttr _custAttr,
            FlightSearchAttr fligthSearch,
            int _id_flight) throws Exception {
        Connection conn = null;
        int bookingNumber = -1;
        try {
            conn = ConnGen.getConn();
            conn.setAutoCommit(false);

            // Get flight
            Flight desiredFlight = (Flight) this.findById(_id_flight, conn);

            // insert customer
            Customer customer = new Customer();
            customer.setAddress(_custAttr.getAddress());
            customer.setEmail(_custAttr.getEmail());
            customer.setFull_name(_custAttr.getFull_name());
            customer.setPhone_number(_custAttr.getPhone_number());
            customer.insert(conn);

            int lastCustId = customer.findLastId(conn);
            // insert booking
            Booking _booking = new Booking();
            _booking.setDate_booking(new Timestamp(_custAttr.getDate_booking().getTime()));
            _booking.setId_customer(lastCustId);
            _booking.setId_flight(_id_flight);
            _booking.setBooking_number(_booking.getUniqueBookingNumber(conn));
            bookingNumber = _booking.getBooking_number();
            _booking.insert(conn);

            // insert passenger
            // for each passenger find it's corresponding price
            for (PassengerAttr passAttr : _passengers) {
                Passenger pass = new Passenger();
                pass.setId_customer(lastCustId);
                pass.setFirst_name(passAttr.getFirst_name());
                pass.setLast_name(passAttr.getLast_name());
                pass.setPassenger_categ(passAttr.getPassenger_categ());
                pass.setId_place_type(fligthSearch.getId_placeType());
                pass.setPrice(pass.findPrice(desiredFlight.getRoute().getId(), conn));
                pass.insert(conn);
            }

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return bookingNumber;
    }

    public double calculateFlightPrice(int[] _id_flight, FlightSearchAttr _search) throws Exception {
        double result = 0;
        for (int iD = 0; iD < _id_flight.length; iD++) {
            if (_id_flight[iD] == ID_NO_FLIGHT) {
                continue;
            }
            Flight _flight = (Flight) findById(_id_flight[iD]);
            logger.info(String.valueOf(_id_flight[iD]));
            double totalPassenger = _search.getAdultCount() + _search.getBabyCount() + _search.getChildCount();
            int id_place_type = _search.getId_placeType();
            for (Pricing _pricing : _flight.getPricings()) {
                logger.info(String.valueOf(_pricing.getPrice()));
                if (_pricing.getId_place_type() == id_place_type) {
                    if (_pricing.getPassenger_categ().equals("bébé")) {
                        result += _pricing.getPrice() * _search.getBabyCount();
                    }
                    if (_pricing.getPassenger_categ().equals("enfant")) {
                        result += _pricing.getPrice() * _search.getChildCount();
                    }
                    if (_pricing.getPassenger_categ().equals("adulte")) {
                        result += _pricing.getPrice() * _search.getAdultCount();
                    }

                }
            }
        }

        return result;
    }

    public List<BaseModel> getFlightsArrivalCriteria(FlightSearchAttr _search) throws Exception {
        logger.info("ato");
        if (_search.getDate_flight_arriving() == null) {
            throw new Exception("Veuillez spécifier une d'arrivée");
        }

        int numberDays = 3;
        int totalSum = _search.getAdultCount() + _search.getBabyCount() + _search.getChildCount();
        LocalDate desiredDate = FctGen.convertToLocalDateViaSqlDate(_search.getDate_flight_arriving());
        return this.getFlightsCriteria(_search.getId_town_destination(), _search.getId_town_origin(),
                _search.getId_placeType(),
                numberDays, desiredDate, totalSum);
    }

    public List<BaseModel> getFlightsDepartureCriteria(FlightSearchAttr _search) throws Exception {

        if (_search.getDate_flight_departure() == null) {
            throw new Exception("Veuillez spécifier une de départ");
        }
        int numberDays = 3;
        int totalSum = _search.getAdultCount() + _search.getBabyCount() + _search.getChildCount();
        LocalDate desiredDate = FctGen.convertToLocalDateViaSqlDate(_search.getDate_flight_departure());
        return this.getFlightsCriteria(_search.getId_town_origin(), _search.getId_town_destination(),
                _search.getId_placeType(),
                numberDays, desiredDate, totalSum);
    }

    public List<BaseModel> getFlightsCriteria(
            int origin,
            int destination,
            int placeType,
            int numberDays, LocalDate desiredDate,
            int totalSum) throws Exception {

        String sql = String.format(""
                + "select * from all_flights where id_town_origin = %d "
                + "and id_town_destination = %d "
                + "and date_flight_departure between "
                + "'%s' and '%s' "
                + "and id_flight in (select id_flight from all_flights_summary "
                + "where id_place_type = %d and remain_places >= %d) order by date_flight_departure",
                origin, destination,
                FctGen.minusDay(numberDays, desiredDate).atTime(0, 0),
                FctGen.addDay(numberDays, desiredDate).atTime(23, 59),
                placeType,
                totalSum);
        logger.info(sql);
        return findAll(sql);
    }

    @Override
    public BaseModel findById(Object id, Connection conn) throws Exception {
        String req = String.format("select * from all_flights where id_flight = %d", id);
        List<BaseModel> desiredFlight = this.findAll(req);
        if (desiredFlight.size() <= 0) {
            throw new Exception("Ce vol n'existe pas");
        }
        return desiredFlight.get(0);
    }

    @Override
    public void insert(Connection conn) throws Exception {
        String columns = "date_flight_arriving;date_flight_departure;id_plane;id_route";
        logger.info("eo)");
        FctGen.insert(this, columns, "flights", conn);
    }

    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        String sql = "select id_flight,date_flight_departure, "
                + "date_flight_arriving,id_route,id_town_origin, "
                + "origin_name,id_town_destination, dest_name, "
                + "id_plane, name from all_flights";
        return this.findAll(sql, conn);
    }

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List<BaseModel> result = new ArrayList();
        try {

            pstmt = conn.prepareStatement(req);
            res = pstmt.executeQuery();
            while (res.next()) {
                Flight _flight = new Flight();
                _flight.setId(res.getInt("id_flight"));
                _flight.setId_plane(res.getInt("id_plane"));
                _flight.setId_route(res.getInt("id_route"));
                _flight.setDate_flight_arriving(res.getTimestamp("date_flight_arriving"));
                _flight.setDate_flight_departure(res.getTimestamp("date_flight_departure"));

                Route _route = new Route();
                _route.setId(res.getInt("id_route"));
                _route.setTownOrigin(new Town(res.getInt("id_town_origin"), res.getString("origin_name")));
                _route.setTownDest(new Town(res.getInt("id_town_destination"), res.getString("dest_name")));
                Plane _plane = new Plane();
                _plane.setId(res.getInt("id_plane"));
                _plane.setName(res.getString("name"));

                _flight.setRoute(_route);
                _flight.setPlane(_plane);
                Pricing pricing = new Pricing();
                _flight.setPricings((List<Pricing>) (List<?>) pricing.findAll(_route.getId(), conn));
                result.add(_flight);
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

    public Timestamp getDate_flight_arriving() {
        return date_flight_arriving;
    }

    public void setDate_flight_arriving(Timestamp date_flight_arriving) {
        this.date_flight_arriving = date_flight_arriving;
    }

    public java.util.Date getDate_flight_departure() {
        return date_flight_departure;
    }

    public void setDate_flight_departure(Timestamp date_flight_departure) {
        this.date_flight_departure = date_flight_departure;
    }

    public int getId_plane() {
        return id_plane;
    }

    public final void setId_plane(int id_plane) {
        this.id_plane = id_plane;
    }

    public int getId_route() {
        return id_route;
    }

    public final void setId_route(int id_route) {
        this.id_route = id_route;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public List<Pricing> getPricings() {
        return pricings;
    }

    public void setPricings(List<Pricing> pricings) {
        this.pricings = pricings;
    }

}
