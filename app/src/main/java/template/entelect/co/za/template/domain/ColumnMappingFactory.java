package template.entelect.co.za.template.domain;

import org.joda.time.DateTime;

import template.entelect.co.za.template.domain.mapping.DateTimeColumnMapping;
import za.co.cporm.model.map.SqlColumnMapping;
import za.co.cporm.model.map.SqlColumnMappingFactory;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class ColumnMappingFactory extends SqlColumnMappingFactory {

    private DateTimeColumnMapping dateTimeColumnMapping = new DateTimeColumnMapping();

    @Override
    public SqlColumnMapping findColumnMapping(Class<?> fieldType) {

        if (DateTime.class.isAssignableFrom(fieldType)) {
            return dateTimeColumnMapping;
        }

        return super.findColumnMapping(fieldType);
    }
}

