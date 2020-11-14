package com.flex.dao.mappers;

import com.flex.models.UserModel;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RolesMapper implements RowMapper<GrantedAuthority> {
    @Override
    public GrantedAuthority mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
