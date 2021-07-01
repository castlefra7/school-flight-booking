/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mg.compagnieaerienne.gen.FctGen;
import mg.compagnieaerienne.rsc.PaymentAttr;

/**
 *
 * @author lacha
 */
public class Payment extends BaseModel {

    private String email;
    private int booking_number;
    private Timestamp date_payment;
    private String bank_card;
    private double amount;
    private String card_name;

    public Payment() {
    }

    public Payment(PaymentAttr attr) throws Exception {
        this.setEmail(attr.getEmail());
        this.setDate_payment(new Timestamp(attr.getDate_payment().getTime()));
        this.setCard_name(attr.getCard_name());
        this.setBank_card(attr.getBank_card());
        this.setBooking_number(attr.getBooking_number());
        
    }

    public boolean isAlreadyPaid(int _bookingNumber, Connection conn) throws SQLException {
        String req = "select * from payments where booking_number = " + _bookingNumber;
        return FctGen.exists(req, conn);
    }

    @Override
    public void insert(Connection conn) throws Exception {
        // date payment should be >= date_booking
        Booking booking = new Booking();
        if (!booking.isBookingNumberExist(this.getBooking_number(), conn)) {
            throw new Exception("Veuillez vérifier votre code de réservation");
        }
        if (this.isAlreadyPaid(this.getBooking_number(), conn)) {
            throw new Exception("Cette réservation a été déjà payée");
        }
        this.setAmount(booking.totalPrice(this.getBooking_number(), conn));
        /// check bank card validity here

        String columns = "email;booking_number;date_payment;bank_card;amount;card_name";
        FctGen.insert(this, columns, "payments", conn);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBooking_number() {
        return booking_number;
    }

    public void setBooking_number(int booking_number) {
        this.booking_number = booking_number;
    }

    public Timestamp getDate_payment() {
        return date_payment;
    }

    public void setDate_payment(Timestamp date_payment) {
        this.date_payment = date_payment;
    }

    public String getBank_card() {
        return bank_card;
    }
    
    public static String CREDIT_CARD_PATTERN = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|"
                + "(?<mastercard>5[1-5][0-9]{14})|"
                + "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|"
                + "(?<amex>3[47][0-9]{13})|(?<diners>3(?:0[0-5]|[68][0-9])[0-9]{11})|(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";

    public final void setBank_card(String bank_card) throws Exception {
        if (bank_card == null || bank_card.isEmpty()) {
            throw new Exception("Veuillez vérifier votre carte bancaire");
        }
        Pattern pattern = Pattern.compile("\\D", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(bank_card);
        bank_card = matcher.replaceAll("");
        
        pattern = Pattern.compile(CREDIT_CARD_PATTERN, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(bank_card);
        
        boolean matchFound = matcher.find();
        if (!matchFound) {
            throw new Exception("Veuillez entrer un numéro de carte valide");
        } else {
            String _card_name = this.getCard_name().toLowerCase().trim();
            if(matcher.group(_card_name) == null) {
                throw new Exception("Veuillez vérifier nom de votre opérateur");
            }  
            this.bank_card = matcher.group(_card_name);
        }
        
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public static String getCREDIT_CARD_PATTERN() {
        return CREDIT_CARD_PATTERN;
    }

    public static void setCREDIT_CARD_PATTERN(String CREDIT_CARD_PATTERN) {
        Payment.CREDIT_CARD_PATTERN = CREDIT_CARD_PATTERN;
    }
    
    

}
