package com.flex.dao;

import com.flex.dao.mappers.ImageMapper;
import com.flex.models.ImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class ImageDao extends JdbcDaoSupport {
    @Autowired
    public ImageDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    public ImageModel create(ImageModel model) {
        model.makeShortUrl();
        String sql = "INSERT INTO images " +
                "(name, user_id, width, height, url) " +
                "VALUES (?,?,?,?,?);";
        Object[] params = new Object[]{
                model.getName(),
                model.getUserID(),
                model.getWidth(),
                model.getHeight(),
                model.getUrl()
        };
        getJdbcTemplate().update(sql, params);
        String getQuery = "SELECT id FROM images WHERE url = ?;";
        long imageId = getJdbcTemplate().queryForObject(getQuery, long.class, model.getUrl());
        model.setId(imageId);
        return model;
    }

    public List<ImageModel> findByName(String name) {
        String sql = "SELECT * FROM images WHERE name LIKE ?;";
        RowMapper<ImageModel> mapper = new ImageMapper();
        String nameForQuery = String.format("%%%s%%", name);
        List<ImageModel> images = getJdbcTemplate().query(sql, mapper, nameForQuery);
        return images;
    }

    public List<ImageModel> getLastImages(int count) {
        String sql = "SELECT * FROM images LIMIT ?;";
        RowMapper<ImageModel> mapper = new ImageMapper();
        return getJdbcTemplate().query(sql, mapper, count);
    }

    public ImageModel findById(long id) {
        String sql = "SELECT * FROM images WHERE id = ?";
        return getJdbcTemplate().queryForObject(sql, new ImageMapper(), id);
    }

}
