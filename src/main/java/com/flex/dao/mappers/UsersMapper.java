package com.flex.dao.mappers;

import com.flex.models.UserModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersMapper implements RowMapper<UserModel> {

    @Override
    public UserModel mapRow(ResultSet resultSet, int i) throws SQLException {
        Long userId = resultSet.getLong("id");
        String login = resultSet.getString("auth_login");
        String userName = resultSet.getString("user_name");
        String encryptedPassword = resultSet.getString("encrypted_password");
        Boolean enabled = resultSet.getBoolean("enabled");
        return new UserModel(userId, login, encryptedPassword, userName, enabled);
    }
}
