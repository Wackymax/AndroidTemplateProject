package template.entelect.co.za.template.domain;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import template.entelect.co.za.template.common.log.Logger;
import template.entelect.co.za.template.domain.model.AbstractDataModel;
import za.co.cporm.model.CPDefaultRecord;
import za.co.cporm.model.annotation.Column.Column;
import za.co.cporm.model.annotation.References;
import za.co.cporm.model.generate.TableView;
import za.co.cporm.model.query.DataFilterClause;
import za.co.cporm.model.query.DataFilterCriterion;
import za.co.cporm.model.query.Select;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class ReferencedRecordsFinder {


    public static <T extends CPDefaultRecord> List<Select<T>> findReferencedRecords(CPDefaultRecord modelObject) {

        List<Select<T>> selects = new ArrayList<>();

        Class modelClass = modelObject.getClass();
        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
        for (Class<?> dbTable : databaseConfiguration.getDataModelObjects()) {

            if (!CPDefaultRecord.class.isAssignableFrom(dbTable))
                continue;

            if (TableView.class.isAssignableFrom(dbTable))
                continue;

            try {
                List<Field> referenceFields = findReferenceFields(dbTable, modelClass);

                if (referenceFields != null && !referenceFields.isEmpty()) {

                    Select<T> select = (Select<T>) Select.from(dbTable);
                    for (Field referencedField : referenceFields) {

                        String filterColumn = referencedField.getAnnotation(Column.class).columnName();
                        if (TextUtils.isEmpty(filterColumn)) {
                            Logger.e("No column name specified for field " + referencedField.getName() + " on class " + dbTable.getSimpleName());
                            continue;
                        }
                        select.addClause(new DataFilterCriterion(filterColumn, DataFilterCriterion.DataFilterOperator.EQUAL, modelObject.getId()), DataFilterClause.DataFilterConjunction.OR);
                    }

                    selects.add(select);
                }
            } catch (Exception e) {
                Logger.e("Failed to create select for class " + dbTable.getSimpleName(), e);
            }
        }

        return selects;
    }

    private static List<Field> findReferenceFields(Class dbTable, Class referenceClass) {

        List<Field> referencedFields = new ArrayList<>();
        for (Field dbField : dbTable.getDeclaredFields()) {

            if (!dbField.isAnnotationPresent(References.class))
                continue;

            if (dbField.getAnnotation(References.class).value() == referenceClass && dbField.isAnnotationPresent(Column.class))
                referencedFields.add(dbField);
        }

        Class superclass = dbTable.getSuperclass();
        if (superclass != null && AbstractDataModel.class.isAssignableFrom(superclass))
            referencedFields.addAll(findReferenceFields(superclass, referenceClass));


        return referencedFields;
    }
}
