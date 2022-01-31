/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.controllers;

import java.util.List;
import mg.compagnieaerienne.bl.BaseModel;
import mg.compagnieaerienne.bl.Flight;
import mg.compagnieaerienne.bl.FlightSummary;
import mg.compagnieaerienne.bl.PlaceType;
import mg.compagnieaerienne.bl.Plane;
import mg.compagnieaerienne.bl.Pricing;
import mg.compagnieaerienne.bl.Route;
import mg.compagnieaerienne.bl.Ticket;
import mg.compagnieaerienne.rsc.FlightAttr;
import mg.compagnieaerienne.rsc.PricingAttr;
import mg.compagnieaerienne.security.User;
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
public class AdminFlightController {

    Logger logger = LoggerFactory.getLogger(AdminFlightController.class);
    
    @PostMapping("/users-update")
    public String updateUser( @ModelAttribute User user, RedirectAttributes redirect) {
        try {
            user.update();
            return "redirect:/users";
        } catch(Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/users/"+ user.getId();
        }
    }
    
    @GetMapping("/users/{id}")
    public String updateUserForm(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("userAttr", new User().findById(id));
        model.addAttribute("isupdate", true);
        return "user-form";
    }
    
    @PostMapping("/users")
    public String postUser(@ModelAttribute User user,RedirectAttributes redirect) {
        try {
            user.insert();
            return "redirect:/users";
        } catch(Exception ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/users-form";
        }
        
    }
    
    @GetMapping("/users-form")
    public String getUsersForm(Model model) {
        model.addAttribute("userAttr", new User());
        return "user-form";
    }
    
    @GetMapping("/users")
    public String getUsers(Model model) throws Exception {
        model.addAttribute("users", new User().findAll());
        return "user-list";
    }

    @GetMapping("/flight-details/{id}")
    public String getFlightDetails(@PathVariable("id") int id, Model model) throws Exception {
        model.addAttribute("flightDetails", new Ticket().findFlightDetails(id));
        return "flight-list-details-back";
    }

    @GetMapping("/stats-payments")
    public String getStatsPayments(Model model) {

        return "stats-payments";
    }
    
    /* FOR PAGINATION */
    @GetMapping("/flight-booking-info")
    public String getFlightBookingInfo(Model model,@RequestParam(name="page", required=false, defaultValue="0") int page) throws Exception {
        List<BaseModel> all = new FlightSummary().findAllPage(page, count_per_page);
        model.addAttribute("flights", all);
        model.addAttribute("numberpages", new FlightSummary().numberPage(count_per_page));
        model.addAttribute("page", page);
        return "flight-booking-info";
    }
    /* END FOR PAGINATION */

    @GetMapping("/pricing-form")
    public String getPricingForm(Model model) throws Exception {
        model.addAttribute("pricingAttr", new PricingAttr());
        model.addAttribute("routes", new Route().findAll());
        model.addAttribute("placetypes", new PlaceType().findAll());
        return "pricing-form";
    }

    @PostMapping("/pricing")
    public String postPricing(@ModelAttribute PricingAttr priceAttr, RedirectAttributes redirectAttr) {
        try {
            new Pricing(priceAttr).insert();
        } catch (Exception ex) {
            if (ex.getMessage() == null || ex.getMessage().equals("null")) {
                redirectAttr.addFlashAttribute("error", "Null pointer");
            } else {
                redirectAttr.addFlashAttribute("error", ex.getMessage());
            }
            return "redirect:/pricing-form";
        }
        return "redirect:/pricing";
    }

    @GetMapping("/pricing")
    public String getPricingList(Model model) throws Exception {
        model.addAttribute("pricings", new Pricing().findAll());
        logger.info(String.valueOf(new Pricing().findAll().size()));
        return "pricing-list-back";
    }

    @PostMapping("/flight")
    public String postFlight(@ModelAttribute FlightAttr flightAttr, Model model, RedirectAttributes redirectAttributes) {
        try {
            Flight ff = new Flight(flightAttr);
            ff.insert();
            return "redirect:/flight";
        } catch (Exception ex) {
            if (ex.getMessage() == null || ex.getMessage().equals("null")) {
                redirectAttributes.addFlashAttribute("error", "Null pointer");
            } else {
                redirectAttributes.addFlashAttribute("error", ex.getMessage());
            }
            return "redirect:/flight-form";
        }

    }
    
    private int count_per_page=  5;

    @GetMapping("/flight")
    public String getAllFlights(Model model,  @RequestParam(name="page", required=false, defaultValue="0") int page) throws Exception {
        model.addAttribute("flights", new Flight().findAllPage(page, count_per_page));
        model.addAttribute("numberpages", new Flight().numberPage(count_per_page));
        model.addAttribute("page", page);
        return "flight-list";
    }

    @GetMapping("/flight-form")
    public String getFormFlights(Model model) throws Exception {
        model.addAttribute("flighAttr", new FlightAttr());
        model.addAttribute("routes", new Route().findAll());
        model.addAttribute("planes", new Plane().findAll());
        return "flight-form";
    }
}
