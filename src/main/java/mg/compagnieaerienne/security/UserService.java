/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.compagnieaerienne.security;

import java.sql.Connection;
import java.sql.SQLException;
import mg.compagnieaerienne.conn.ConnGen;
import mg.compagnieaerienne.security.User;
import mg.compagnieaerienne.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author lacha
 */
@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Connection conn = null;
        UserPrincipal userPrinc = null;
        try {
            conn = ConnGen.getConn();
            User user = User.findByUsername(conn, username);
            if(user == null) {
                throw new UsernameNotFoundException(username);
            }
            userPrinc = new UserPrincipal(user);
        } catch(Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                if(conn!=null) conn.close();
            } catch(SQLException ex) {
                System.out.println(ex);
            }
        }
        return userPrinc;
    }
    
}
