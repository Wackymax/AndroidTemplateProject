package template.entelect.co.za.template.data.repository;

import com.squareup.otto.Bus;

import java.util.List;

import template.entelect.co.za.template.data.QueryResult;
import template.entelect.co.za.template.domain.model.BaseDataModel;
import template.entelect.co.za.template.domain.model.SyncModel;

/**
 * Created by Rushil on 3/12/2016.
 */
public interface BaseQueryFactory<T extends BaseDataModel> extends QueryFactory<T> {

    long validateAndSave(Bus bus, T... items);

    long validateAndSave(TransactionContext transactionContext, T... items);

    long delete(final long id, Bus bus);

    T getExistingItem(T item);

    T getItemToSave(T item);
}
