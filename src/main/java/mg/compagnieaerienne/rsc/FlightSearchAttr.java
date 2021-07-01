/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.rsc;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author lacha
 */
public class FlightSearchAttr {

    private int id_town_origin;
    private int id_town_destination;
    private String option;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date_flight_departure;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private Date date_flight_arriving;
    private int id_placeType;

    private int adultCount;
    private int childCount;
    private int babyCount;

    private String townOrigin;
    private String townDest;

    public String getTownOrigin() {
        return townOrigin;
    }

    public void setTownOrigin(String townOrigin) {
        this.townOrigin = townOrigin;
    }

    public String getTownDest() {
        return townDest;
    }

    public void setTownDest(String townDest) {
        this.townDest = townDest;
    }

    public int getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(int adultCount) {
        this.adultCount = adultCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getBabyCount() {
        return babyCount;
    }

    public void setBabyCount(int babyCount) {
        this.babyCount = babyCount;
    }

    public int getId_town_origin() {
        return id_town_origin;
    }

    public void setId_town_origin(int id_town_origin) {
        this.id_town_origin = id_town_origin;
    }

    public int getId_town_destination() {
        return id_town_destination;
    }

    public void setId_town_destination(int id_town_destination) {
        this.id_town_destination = id_town_destination;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Date getDate_flight_departure() {
        return date_flight_departure;
    }

    public void setDate_flight_departure(Date date_flight_departure) {
        this.date_flight_departure = date_flight_departure;
    }

    public Date getDate_flight_arriving() {
        return date_flight_arriving;
    }

    public void setDate_flight_arriving(Date date_flight_arriving) {
        this.date_flight_arriving = date_flight_arriving;
    }

    public int getId_placeType() {
        return id_placeType;
    }

    public void setId_placeType(int id_placeType) {
        this.id_placeType = id_placeType;
    }

}
