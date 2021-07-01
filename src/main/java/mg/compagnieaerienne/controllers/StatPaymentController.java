/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.controllers;

import java.util.List;
import mg.compagnieaerienne.bl.BaseModel;
import mg.compagnieaerienne.bl.StatPayment;
import mg.compagnieaerienne.rsc.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lacha
 */
@RestController
public class StatPaymentController {

    Logger logger = LoggerFactory.getLogger(StatPaymentController.class);

    @GetMapping("/stats-payment-by-date")
    public ResponseBody getStatsPaymentByDate() throws Exception {
        ResponseBody response = new ResponseBody();
        List<BaseModel> result = new StatPayment().findAll();
        for(BaseModel m: result) {
            StatPayment st = (StatPayment)m;
            logger.info(st.getDate().toString());
        }
        response.getData().add(result);
        return response;
    }
}
