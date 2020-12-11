package com.flex.services.implementation;

import com.flex.dao.RolesDao;
import com.flex.dao.UsersDao;
import com.flex.models.ExtendedUserDetails;
import com.flex.models.UserModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImplementation implements UserDetailsService {
    private final UsersDao appUserDAO;

    private final RolesDao appRoleDAO;

    public UserDetailsServiceImplementation(UsersDao appUserDAO, RolesDao appRoleDAO) {
        this.appUserDAO = appUserDAO;
        this.appRoleDAO = appRoleDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserModel appUser = this.appUserDAO.findUserAccount(userName);

        if (appUser == null) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }
        return createDetails(appUser);
    }

    public UserDetails loadUserById(Long id) {
        UserModel user = appUserDAO.findUserAccountById(id);
        return createDetails(user);
    }

    private ExtendedUserDetails createDetails(UserModel model) {
        if(model == null)
            return null;
        List<String> roleNames = this.appRoleDAO.getUserRoleNames(model.getId());
        return new ExtendedUserDetails(model.getId(), model.getName(), //
                model.getEncryptedPassword(), roleNames);

    }
}
