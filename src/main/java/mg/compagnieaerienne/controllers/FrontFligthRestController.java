/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.controllers;

import mg.compagnieaerienne.bl.Flight;
import mg.compagnieaerienne.bl.Town;
import mg.compagnieaerienne.rsc.FlightSearchAttr;
import mg.compagnieaerienne.rsc.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RestController
public class FrontFligthRestController {

    @PostMapping("/flight-search-rest")
    public ResponseBody getFlight(@RequestBody FlightSearchAttr attr) {
        ResponseBody response = new ResponseBody();
        try {
            new Flight().setIdTowns(attr);
            //response.getData().add(attr);
            response.getData().add(new Flight().getFlightsDepartureCriteria(attr));
            response.getData().add(new Flight().getFlightsArrivalCriteria(attr));
        } catch (Exception ex) {
            response.getStatus().setMessage(ex.getMessage());
        }

        return response;
    }

    @GetMapping("/town-search")
    public ResponseBody getFindTownByName(
            @RequestParam(name = "town-name", required = false, defaultValue = "") String townName) {
        ResponseBody response = new ResponseBody();
        try {
            response.getData().add(new Town().findTownByName(townName));
        } catch (Exception ex) {
            response.getStatus().setCode(400);
            response.getStatus().setMessage(ex.getMessage());
        }
        return response;
    }
}
