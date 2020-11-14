package com.flex.services.implementation;

import com.flex.dao.RolesDao;
import com.flex.dao.UsersDao;
import com.flex.exeptions.UserAlreadyExistException;
import com.flex.models.UserModel;
import com.flex.utils.PasswordEncrypt;
import com.flex.viewModels.RegisterViewModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;

@Service
public class UserService {
    private final UsersDao usersDao;
    private final RolesDao rolesDao;

    private PasswordEncoder passwordEncoder = PasswordEncrypt.passwordEncoder();

    public UserService(UsersDao usersDao, RolesDao rolesDao) {
        this.usersDao = usersDao;
        this.rolesDao = rolesDao;
    }

    public UserModel findByLogin(String login) { return usersDao.findUserAccount(login); }


    public void registerNewUser(RegisterViewModel user) throws UserAlreadyExistException {
        if(loginExist(user.getLogin())) {
            throw new UserAlreadyExistException(user.getLogin());
        }
        UserModel model = new UserModel
                (
                        0L,
                        user.getLogin(),
                        PasswordEncrypt.encrypt(user.getPassword()),
                        user.getName(),
                        true
                );
        usersDao.addUser(model);
        rolesDao.addDefaultRoleToUser(model.getId());
    }

    public UserModel findByLoginAndPassword(String login, String password) throws AccountException {
        UserModel user = findByLogin(login);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getEncryptedPassword())) {
                return user;
            } else {
                throw new AccountException("Incorrect password");
            }
        }
        throw new AccountException("Incorrect login");
    }


    private boolean loginExist(String login) {
        return false;
    }
}
