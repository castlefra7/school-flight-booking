/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.controllers;

import mg.compagnieaerienne.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author lacha
 */
@Controller

public class SecurityController {
    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @GetMapping(value = "/username")
    @ResponseBody
    public String currentUserNameSimple() {
        Authentication authentication = authenticationFacade.getAuthentication();
        //authentication.getAuthorities().toString();
        return authentication.getName();
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required=false, defaultValue="") String error, Model model) {
        model.addAttribute("error", error);
        return "login";
    }
    
}
