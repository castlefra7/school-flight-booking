/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mg.compagnieaerienne.bl.Flight;
import mg.compagnieaerienne.bl.Payment;
import mg.compagnieaerienne.bl.PlaceType;
import mg.compagnieaerienne.bl.Ticket;
import mg.compagnieaerienne.bl.Town;
import mg.compagnieaerienne.rsc.CustomerAttr;
import mg.compagnieaerienne.rsc.FlightSearchAttr;
import mg.compagnieaerienne.rsc.PassengerAttr;
import mg.compagnieaerienne.rsc.PaymentAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author lacha
 */
@Controller
public class FrontFlightController {

    private static final String SESSION_PASSENGERS = "passengers";
    private static final String SESSION_SEARCH = "flightSearch";
    private static final String SESSION_ID_FLIGHT_DEP = "idFlightDep";
    private static final String SESSION_ID_FLIGHT_ARR = "idFlightArr";

    Logger logger = LoggerFactory.getLogger(FrontFlightController.class);
    
    @GetMapping("/flight-front-rest")
    public String getFlightSearchFormRest(Model model) throws Exception {
        FlightSearchAttr flightAttr = new FlightSearchAttr();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("01/07/2021");
        flightAttr.setDate_flight_departure(date);
        date = sdf.parse("01/08/2021");
        flightAttr.setDate_flight_arriving(date);
        flightAttr.setAdultCount(3);
        flightAttr.setId_placeType(2);
        flightAttr.setId_town_origin(1);
        flightAttr.setId_town_destination(2);
        //flightAttr.setOption("2");
        flightAttr.setTownOrigin("antananarivo");
        flightAttr.setTownDest("new york");

        model.addAttribute("flighAttr", flightAttr);
        model.addAttribute("towns", new Town().findAll());
        model.addAttribute("placetypes", new PlaceType().findAll());
        return "flight-search-form-rest";
    }

    @GetMapping("/tickets/{id}")
    public String getTickets(Model model, @PathVariable("id") int id, RedirectAttributes redirectAttr) throws Exception {
        Ticket ticket = new Ticket();
        if (ticket.isPaid(id) == false) {
            redirectAttr.addFlashAttribute("error", "Veuillez effectuer le paiement pour cette réservation. Code: " + id);
            return "redirect:/payment";
        } else {
            model.addAttribute("tickets", ticket.findAllTickets(id));
        }

        return "ticket.html";
    }

    @PostMapping("/payment")
    public String postPaymentForm(Model model, RedirectAttributes redirectAttr, @ModelAttribute PaymentAttr attr) {
        try {
            new Payment(attr).insert();
            redirectAttr.addFlashAttribute("success", "Paiement effectué avec succès");
            return "redirect:/tickets/" + attr.getBooking_number();
        } catch (Exception ex) {
            if (ex.getMessage() == null || ex.getMessage().equals("null")) {
                redirectAttr.addFlashAttribute("error", "Null pointer");
            } else {
                redirectAttr.addFlashAttribute("error", ex.getMessage());
            }
            return "redirect:/payment";
        }

    }

    @GetMapping("/payment")
    public String getPaymentForm(Model model) {
        PaymentAttr attr = new PaymentAttr();
        attr.setDate_payment(new Date());
        model.addAttribute("paymentAttr", attr);
        return "payment-form";
    }

    @PostMapping("/flight-book")
    public String postValidateBooking(HttpServletRequest request, RedirectAttributes redirectAttr, @ModelAttribute CustomerAttr custAttr) {
        try {
            HttpSession session = request.getSession();
            Object ob = session.getAttribute(SESSION_PASSENGERS);
            Object flightSearch = session.getAttribute(SESSION_SEARCH);
            Object idFlightDep = session.getAttribute(SESSION_ID_FLIGHT_DEP);
            Object idFlightArr = session.getAttribute(SESSION_ID_FLIGHT_ARR);
            
            if (ob != null && flightSearch != null && idFlightDep != null && idFlightArr != null) {
                int[] id_flights = new int[2];
                id_flights[0] = (Integer) idFlightDep;
                id_flights[1] = (Integer) idFlightArr;
                List<Integer> bookingNumbers = new Flight().book((List<PassengerAttr>) (List<?>) ob, custAttr, (FlightSearchAttr) flightSearch, id_flights);
                String message = "Réservation effectuée avec succès.";
                if (bookingNumbers.size() <= 0) {
                    throw new Exception("Nous n'avons pas pu vous envoyer votre code de réservation");
                } else if (bookingNumbers.size() <= 1) {
                    message += " Votre code de réservation est " + bookingNumbers.get(0)
                            + ". Veuillez vous rappeler de ce code pour effectuer le paiement.";
                } else {
                    message += "Vos codes de réservation sont ";
                    for (Integer bookingNumber : bookingNumbers) {
                        message += bookingNumber + ", ";
                    }
                    message = message.substring(0, message.length() - 2);
                    message += ". Veuillez vous rappeler de ces codes pour effectuer les paiements.";
                }
                redirectAttr.addFlashAttribute("success", message);

                session.removeAttribute(SESSION_SEARCH);
                session.removeAttribute(SESSION_ID_FLIGHT_DEP);
                session.removeAttribute(SESSION_ID_FLIGHT_ARR);
                session.removeAttribute(SESSION_PASSENGERS);
            } else {
                redirectAttr.addFlashAttribute("error", "Une erreur inconnue s'est produite");
            }

        } catch (Exception ex) {
            redirectAttr.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/";
    }

    @GetMapping("/flight-customer-info")
    public String getCustomerFormInfo(HttpServletRequest request, Model model, RedirectAttributes redirectAttr) {
        HttpSession session = request.getSession();
        Object ob = session.getAttribute(SESSION_PASSENGERS);
        if (ob == null) {
            redirectAttr.addFlashAttribute("error", "Veuillez entrer au moins un passager");
            return "redirect:/flight-passenger-info";
        }
        model.addAttribute("custAttr", new CustomerAttr());
        return "flight-form-customer";
    }

    @PostMapping("/flight-passenger-info")
    public String postPassengerInfo(HttpServletRequest request, @ModelAttribute PassengerAttr passAttr) {
        HttpSession session = request.getSession();
        Object ob = session.getAttribute(SESSION_PASSENGERS);
        if (ob != null) {
            List<PassengerAttr> passengers = (List<PassengerAttr>) (List<?>) ob;
            passengers.add(passAttr);
        } else {
            List<PassengerAttr> passengers = new ArrayList();
            passengers.add(passAttr);
            session.setAttribute(SESSION_PASSENGERS, passengers);
        }// $_session["ff"] = "moi";
        // request.getSession().setAttribute("type", "admin");
        // request.getSession().setAttribute("type", "user");
        return "redirect:/flight-passenger-info";
    }

    @GetMapping("/flight-passenger-info")
    public String getPassengerFormInfo(Model model) throws Exception {
        model.addAttribute("passAttr", new PassengerAttr());
        return "flight-form-passenger";
    }

    // Flight.ID_NO_FLIGHT
    @GetMapping("/flight-begin-booking")
    public String setupBooking(HttpServletRequest request,
            @RequestParam(name = "id-flight-dep", required = false, defaultValue = "-1") int id_flight_dep,
            @RequestParam(name = "id-flight-arr", required = false, defaultValue = "-1") int id_flight_arr) {
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_ID_FLIGHT_DEP, id_flight_dep);
        session.setAttribute(SESSION_ID_FLIGHT_ARR, id_flight_arr);

        return "redirect:/flight-passenger-info";
    }
    

    @GetMapping("/")
    public String getFlightSearchForm(Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        session.removeAttribute(SESSION_PASSENGERS);

        FlightSearchAttr flightAttr = new FlightSearchAttr();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("01/07/2021");
        flightAttr.setDate_flight_departure(date);
        date = sdf.parse("01/08/2021");
        flightAttr.setDate_flight_arriving(date);
        flightAttr.setAdultCount(3);
        flightAttr.setId_placeType(2);
        flightAttr.setId_town_origin(1);
        flightAttr.setId_town_destination(2);
        //flightAttr.setOption("2");
        flightAttr.setTownOrigin("antananarivo");
        flightAttr.setTownDest("new york");

        model.addAttribute("flighAttr", flightAttr);
        model.addAttribute("towns", new Town().findAll());
        model.addAttribute("placetypes", new PlaceType().findAll());
        return "flight-search-form";
    }

    @GetMapping("/flight-front-booking")
    public String postFlightSearchForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        try {
            // echo $_session["userid"];
            if (session.getAttribute(SESSION_SEARCH) != null) {
                FlightSearchAttr flightAttr = (FlightSearchAttr) session.getAttribute(SESSION_SEARCH);
                if (flightAttr.getOption() == null || flightAttr.getOption().equals("2")) {
                    // aller simple
                    model.addAttribute("flightsGo", new Flight().getFlightsDepartureCriteria(flightAttr));
                } else {
                    // aller retour
                    model.addAttribute("flightsGo", new Flight().getFlightsDepartureCriteria(flightAttr));
                    model.addAttribute("flightsBack", new Flight().getFlightsArrivalCriteria(flightAttr));
                }
            } else {
                logger.info("No " + SESSION_SEARCH);
            }

        } catch (Exception ex) {
            if (ex.getMessage() == null || ex.getMessage().equals("null")) {
                model.addAttribute("error", "Nul pointer");
            } else {
                model.addAttribute("error", ex.getMessage());
            }

        }

        return "flight-list-front";
    }

    @PostMapping("/flight-front")
    public String postFlightSearchForm(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute FlightSearchAttr flightAttr, Model model, RedirectAttributes redirectAttr) {
        try {
            new Flight().setIdTowns(flightAttr);
            HttpSession session = request.getSession();
            session.setAttribute(SESSION_SEARCH, flightAttr);
            return "redirect:/flight-front-booking";
        } catch (Exception ex) {
            redirectAttr.addFlashAttribute("error", ex.getMessage());
            return "redirect:/flight-front";
        }

    }

    @GetMapping("/calculate-price")
    public String getCalculatePrice(HttpServletRequest request, RedirectAttributes redirectAttributes,
            @RequestParam(name = "id-flight-dep", required = false, defaultValue = "-1") int id_flight_dep,
            @RequestParam(name = "id-flight-arr", required = false, defaultValue = "-1") int id_flight_arr) {
        HttpSession session = request.getSession();
        try {
            if (session.getAttribute(SESSION_SEARCH) != null) {
                FlightSearchAttr flightAttr = (FlightSearchAttr) session.getAttribute(SESSION_SEARCH);

                // redirect the price to flight-front-booking
                redirectAttributes.addFlashAttribute("id_flight_dep", id_flight_dep);
                redirectAttributes.addFlashAttribute("id_flight_arr", id_flight_arr);
                int[] id_flights = new int[2];
                id_flights[0] = id_flight_dep;
                id_flights[1] = id_flight_arr;
                redirectAttributes.addFlashAttribute("price", new Flight().calculateFlightPrice(id_flights, flightAttr));
            } else {
                logger.info("No " + SESSION_SEARCH);
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/flight-front-booking";
    }
}
