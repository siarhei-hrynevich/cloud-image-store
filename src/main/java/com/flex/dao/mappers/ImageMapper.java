package com.flex.dao.mappers;

import com.flex.models.ImageModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageMapper implements RowMapper<ImageModel> {
    @Override
    public ImageModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        ImageModel model = new ImageModel();
        model.setName(rs.getString("name"));
        model.setUserID(rs.getLong("user_id"));
        model.setId(rs.getLong("id"));
        model.setWidth(rs.getInt("width"));
        model.setHeight(rs.getInt("height"));
        model.setUrl(rs.getString("url"));
        return model;
    }
}
