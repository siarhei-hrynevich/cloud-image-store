package com.flex.dao.mappers;

import com.flex.models.ImageModel;
import com.flex.viewModels.ImageViewModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageViewModelMapper implements RowMapper<ImageViewModel> {

    @Override
    public ImageViewModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        ImageViewModel model = new ImageViewModel();
        model.setName(rs.getString("name"));
        model.setUserID(rs.getLong("user_id"));
        model.setId(rs.getLong("id"));
        model.setWidth(rs.getInt("width"));
        model.setHeight(rs.getInt("height"));
        model.setUrl(rs.getString("small_url"));
        model.setDownloads(rs.getLong("downloads"));
        return model;
    }
}
