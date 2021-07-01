/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.bl;

import java.sql.Connection;
import java.util.List;

/**
 *
 * @author lacha
 */
public interface DaoInterface {
    public void insert() throws Exception;
    public void insert(Connection conn) throws Exception;
    public List<BaseModel> findAll(Connection conn) throws Exception;
    public List<BaseModel> findAll(Object criteria, Connection conn) throws Exception;
    public List<BaseModel> findAll(String req, Connection conn) throws Exception;
    public List<BaseModel> findAll(String req) throws Exception;
    public List<BaseModel>  findAll() throws Exception;
    public BaseModel findById(Object id, Connection conn) throws Exception;
    public BaseModel findById(Object id) throws Exception;
    public void update() throws Exception;
    public void update(Connection conn) throws Exception;
}
