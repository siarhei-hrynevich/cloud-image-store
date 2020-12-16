package com.flex.dao;

import com.flex.dao.mappers.ImageMapper;
import com.flex.dao.mappers.ImageViewModelMapper;
import com.flex.models.ImageModel;
import com.flex.viewModels.ImageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@Transactional
public class ImageDao extends JdbcDaoSupport {
    @Autowired
    public ImageDao(DataSource dataSource) {
        this.setDataSource(dataSource);
    }


    public List<String> getAllTags() {
        String query = "SELECT text FROM tags;";
        return getJdbcTemplate().queryForList(query, String.class);
    }

    public String getDownloadLink(Long id) {
        String query = "SELECT url FROM images WHERE id = ?";
        return getJdbcTemplate().queryForObject(query, String.class, id);
    }

    public void incrementDownloadsOfImage(Long id) {
        String query = "UPDATE images SET downloads = downloads + 1 WHERE id = ?";
        getJdbcTemplate().update(query, id);
    }

    public ImageModel create(ImageModel model) {
        if (model == null)
            return null;
        model.makeShortUrl();
        insertImage(model);
        String getQuery = "SELECT id FROM images WHERE url = ?;";
        long imageId = getJdbcTemplate().queryForObject(getQuery, long.class, model.getUrl());
        model.setId(imageId);
        if(model.getTags() != null)
            insertImageTags(model);
        return model;
    }

    public List<ImageViewModel> findByName(String name) {
        String sql = "SELECT * FROM images WHERE name LIKE ?;";
        RowMapper<ImageViewModel> mapper = new ImageViewModelMapper();
        String nameForQuery = String.format("%%%s%%", name);
        List<ImageViewModel> models = getJdbcTemplate().query(sql, mapper, nameForQuery);
        models.forEach(m -> m.setTags(selectTags(m.getId())));
        return models;
    }

    public List<ImageViewModel> findByNameWithTag(String name, String tag) {
        String query =
                "WITH tagged_images_ids AS (WITH selected_tags AS (SELECT id FROM tags WHERE text LIKE ?) " +
                        "SELECT image_id FROM image_tags it INNER JOIN selected_tags st ON it.tag_id = st.id) " +
                        "SELECT DISTINCT * FROM images imgs INNER JOIN tagged_images_ids tg_ids ON tg_ids.image_id = imgs.id WHERE name LIKE ?";
        String nameForQuery = String.format("%%%s%%", name);
        String tagForQuery = String.format("%%%s%%", tag);
        List<ImageViewModel> models = getJdbcTemplate().query(query, new ImageViewModelMapper(), tagForQuery, nameForQuery);
        models.forEach(m -> m.setTags(selectTags(m.getId())));
        return models;
    }

    public List<ImageViewModel> getLastImages(int count) {
        String sql = "SELECT * FROM images LIMIT ?;";
        RowMapper<ImageViewModel> mapper = new ImageViewModelMapper();
        List<ImageViewModel> models = getJdbcTemplate().query(sql, mapper, count);
        models.forEach(m -> m.setTags(selectTags(m.getId())));
        return models;
    }

    public List<ImageViewModel> findByUserId(long id) {
        String sql = "SELECT * FROM images WHERE user_id = ?";
        RowMapper<ImageViewModel> mapper = new ImageViewModelMapper();
        List<ImageViewModel> models = getJdbcTemplate().query(sql, mapper, id);
        models.forEach(m -> m.setTags(selectTags(m.getId())));
        return models;
    }

    public ImageModel findById(long id) {
        String sql = "SELECT * FROM images WHERE id = ?";
        ImageModel model = getJdbcTemplate().queryForObject(sql, new ImageMapper(), id);
        model.setTags(selectTags(model.getId()));
        return model;
    }

    public void deleteImage(ImageModel model) {
        String deleteImageQuery = "DELETE FROM images WHERE id = ?;";
        String deleteImageTagsQuery = "DELETE FROM image_tags WHERE image_id = ?";
        getJdbcTemplate().update(deleteImageTagsQuery, model.getId());
        getJdbcTemplate().update(deleteImageQuery, model.getId());
    }

    public Long count() {
        String query = "SELECT COUNT(id) FROM images";
        return getJdbcTemplate().queryForObject(query, Long.class);
    }

    private void insertImage(ImageModel model) {

        String sql = "INSERT INTO images " +
                "(name, user_id, width, height, url, small_url) " +
                "VALUES (?,?,?,?,?,?);";
        Object[] params = new Object[]{
                model.getName(),
                model.getUserID(),
                model.getWidth(),
                model.getHeight(),
                model.getUrl(),
                model.getSmallUrl()
        };
        getJdbcTemplate().update(sql, params);
        if(model.getTags() != null)
            insertTags(model.getTags());
    }

    private void insertTags(List<String> tags) {
        try {
            insertQueryTags(tags);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertImageTags(ImageModel model) {
        try {
            insertQueryImageTags(model.getTags(), model.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertQueryImageTags(List<String> tags, long imageId) {
        String sql = "INSERT INTO image_tags (image_id, tag_id)" +
                "VALUES (?, (SELECT id FROM tags WHERE text = ?))";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                ps.setString(1, String.valueOf(imageId));
                ps.setString(2, tags.get(i));
            }

            @Override
            public int getBatchSize() {
                return tags.size();
            }
        });
    }

    private void insertQueryTags(List<String> tags) {
        String sql = "INSERT INTO tags (text) VALUES (?); ";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                ps.setString(1, tags.get(i));
            }

            @Override
            public int getBatchSize() {
                return tags.size();
            }
        });
    }

    private List<String> selectTags(long imageId) {
        String query = "WITH selected_tags AS (SELECT tag_id FROM image_tags WHERE image_id = ?) SELECT text FROM selected_tags st INNER JOIN tags t ON st.tag_id = t.id;";
        return getJdbcTemplate().queryForList(query, String.class, imageId);
    }
}
