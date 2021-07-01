/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author lacha
 */
public class User {
    private int id;
    private String name;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //password = passwordEncoder().encode(password);
        System.out.println(password);
        this.password = password;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }
    
    public static User findByUsername(Connection conn, String username) throws Exception {
        User user = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        try {
            String sql = "select id, name, password from users where name = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            res = pstmt.executeQuery();
            if(res.next()) {
                user = new User();
                user.setId(res.getInt(1));
                user.setName(res.getString(2));
                user.setPassword(res.getString(3));
            }
        } catch(Exception ex) {
            throw ex;
        } finally {
            if(res != null) res.close();
            if(pstmt != null) pstmt.close();
        }
        
        
        return user;
    }
    
}
