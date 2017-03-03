package template.entelect.co.za.template.domain.mapping;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import org.joda.time.DateTime;

import za.co.cporm.model.map.SqlColumnMapping;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class DateTimeColumnMapping implements SqlColumnMapping {
    @Override
    public Class<?> getJavaType() {
        return DateTime.class;
    }

    @Override
    public String getSqlColumnTypeName() {
        return "LONG";
    }

    @Override
    public Object toSqlType(Object source) {
        return ((DateTime) source).getMillis();
    }

    @Override
    public Object getColumnValue(Cursor cursor, int columnIndex) {
        return new DateTime(cursor.getLong(columnIndex));
    }

    @Override
    public void setColumnValue(ContentValues contentValues, String key, Object value) {

        contentValues.put(key, (Long) toSqlType(value));
    }

    @Override
    public void setBundleValue(Bundle bundle, String key, Cursor cursor, int columnIndex) {

        bundle.putSerializable(key, (DateTime) getColumnValue(cursor, columnIndex));
    }

    @Override
    public Object getColumnValue(Bundle bundle, String columnName) {
        return bundle.getSerializable(columnName);
    }
}
