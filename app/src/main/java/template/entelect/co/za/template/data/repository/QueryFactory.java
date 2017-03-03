package template.entelect.co.za.template.data.repository;

import java.util.List;

import template.entelect.co.za.template.data.QueryResult;
import za.co.cporm.model.query.Select;

/**
 * Created by hennie.brink on 2016/05/11.
 */
public interface QueryFactory<T> {

    QueryResult<T> getById(long id);

    QueryResult<T> getForEntity(String joinColumn, long parentId);

    QueryResult<T> getAll();

    List<String> getSearchColumns();

    //Todo: implement own user logic
    QueryResult<T> searchForUser(long userId, String query);
}
