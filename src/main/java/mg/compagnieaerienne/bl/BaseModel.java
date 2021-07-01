/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.util.List;
import mg.compagnieaerienne.conn.ConnGen;

/**
 *
 * @author lacha
 */
public abstract class BaseModel implements DaoInterface {

    private int id;

    public int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    @Override
    public void update() throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            this.update(conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void insert() throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            this.insert(conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<BaseModel> findAll() throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return this.findAll(conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<BaseModel> findAll(String req) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return this.findAll(req, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<BaseModel> findAll(Object criteria, Connection conn) throws Exception {
        return findAll(conn);
    }

    @Override
    public BaseModel findById(Object id) throws Exception {
        try (Connection conn = ConnGen.getConn()) {
            return this.findById(id, conn);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public BaseModel findById(Object id, Connection conn) throws Exception {
        return null;
    }

}
