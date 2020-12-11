package com.flex.dao;


import com.flex.dao.mappers.UsersMapper;
import com.flex.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;

@Repository
@Transactional
public class UsersDao extends JdbcDaoSupport {

    @Autowired
    public UsersDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public void addUser(UserModel user) {
        String sql = "INSERT INTO users (auth_login, user_name, encrypted_password, enabled) VALUES (?,?,?,?);";
        this.getJdbcTemplate().update(sql, user.getLogin(), user.getName(), user.getEncryptedPassword(), user.isEnabled());
        Object[] params = new Object[] { user.getLogin() };
        user.setId(this.getJdbcTemplate().queryForObject("SELECT id FROM users WHERE auth_login = ?;", params, Long.class));
    }

    public UserModel findUserAccount(String userName) {
        String sql = "SELECT * FROM users WHERE auth_login = ? ";
        Object[] params = new Object[] { userName };
        try {
            return this.getJdbcTemplate().queryForObject(sql, params, new UsersMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public UserModel findUserAccountById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?;";
        try {
            return this.getJdbcTemplate().queryForObject(sql, new UsersMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
