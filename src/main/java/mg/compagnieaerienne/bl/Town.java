/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import mg.compagnieaerienne.conn.ConnGen;
import mg.compagnieaerienne.controllers.FrontFlightController;
import mg.compagnieaerienne.gen.FctGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lacha
 */
public class Town extends BaseModel {

    private String name;

    public Town() {
    }

    public Town(int _id, String _name) {
        setName(_name);
        setId(_id);
    }

    @Override
    public void insert(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Town findOneTownByName(String name) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return findOneTownByName(name, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Town findOneTownByName(String name, Connection conn) throws Exception {
        name = name.toLowerCase().trim();
        if (name.isEmpty()) {
            throw new Exception("Cette ville n'existe pas");
        }
        String req = String.format("select * from towns where name like '%s'", name);
        logger.info(req);
        List<BaseModel> result = this.findAll(req, conn);
        if (result.size() <= 0) {
            throw new Exception("Cette ville n'existe pas");
        } else {
            return (Town) result.get(0);
        }
    }

    public List<BaseModel> findTownByName(String name) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return findTownByName(name, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }
    Logger logger = LoggerFactory.getLogger(Town.class);

    public List<BaseModel> findTownByName(String name, Connection conn) throws Exception {
        name = name.toLowerCase().trim();
        List<BaseModel> empty = new ArrayList();
        if (name.isEmpty()) {
            return empty;
        }
        String req = String.format("select * from towns where name like '%%%s%%'", name);
        logger.info(req);
        List<BaseModel> result = this.findAll(req, conn);
        if (result.size() <= 0) {
            return empty;
        } else {
            return result;
        }
    }

    @Override
    public List<BaseModel> findAll(Connection conn) throws Exception {
        String req = "select id, name from towns";
        return this.findAll(req, conn);
    }

    @Override
    public void update(Connection conn) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<BaseModel> findAll(String req, Connection conn) throws Exception {
        String columns = "id;name";
        return (List<BaseModel>) (List<?>) FctGen.findAll(this, req, columns, conn);
    }

}
