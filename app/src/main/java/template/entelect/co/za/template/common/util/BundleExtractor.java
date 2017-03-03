package template.entelect.co.za.template.common.util;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.LruCache;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import template.entelect.co.za.template.common.annotation.FromBundle;
import template.entelect.co.za.template.common.log.Logger;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class BundleExtractor  {

    private static final LruCache<Class, MarkedField[]> classInformationCache = new LruCache<>(50);

    public static void injectFromBundle(Object object, Bundle bundle) {

        if (bundle == null)
            return;

        MarkedField[] markedFieldsForClass = getMarkedFieldsForClass(object.getClass());

        for (MarkedField markedField : markedFieldsForClass) {

            if (bundle.containsKey(markedField.annotation.value())) {

                try {
                    setFieldValue(bundle, markedField, object);
                } catch (IllegalAccessException e) {
                    Logger.e("Failed to set field value for field " + markedField.field.getName(), e);
                }
            }
        }

    }

    private static MarkedField[] getMarkedFieldsForClass(Class clazz) {

        MarkedField[] markedFields = classInformationCache.get(clazz);

        if (markedFields == null) {

            List<MarkedField> scannedFields = scanClassForMarkedFields(clazz);
            markedFields = scannedFields.toArray(new MarkedField[scannedFields.size()]);
            classInformationCache.put(clazz, markedFields);
        }

        return markedFields;
    }


    private static List<MarkedField> scanClassForMarkedFields(Class clazz) {

        List<MarkedField> markedFields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {

            if (field.isAnnotationPresent(FromBundle.class)) {
                markedFields.add(new MarkedField(field, field.getAnnotation(FromBundle.class)));
            }
        }

        if (clazz.getSuperclass() != null)
            markedFields.addAll(scanClassForMarkedFields(clazz.getSuperclass()));

        return markedFields;
    }

    private static void setFieldValue(Bundle bundle, MarkedField markedField, Object object) throws IllegalAccessException {

        Field field = markedField.field;
        field.setAccessible(true);
        Class<?> fieldType = field.getType();

        String key = markedField.annotation.value();

        if (fieldType.isAssignableFrom(Long.class) || fieldType.isAssignableFrom(long.class)) {

            field.set(object, bundle.getLong(key, -1));
        } else if (fieldType.isAssignableFrom(Integer.class) || fieldType.isAssignableFrom(int.class)) {

            field.set(object, bundle.getInt(key, -1));
        } else if (fieldType.isAssignableFrom(Double.class) || fieldType.isAssignableFrom(double.class)) {

            field.set(object, bundle.getInt(key, -1));
        } else if (fieldType.isAssignableFrom(Boolean.class) || fieldType.isAssignableFrom(boolean.class)) {

            field.set(object, bundle.getBoolean(key, false));
        } else if (fieldType.isAssignableFrom(String.class)) {

            field.set(object, bundle.getString(key, null));
        } else if (Parcelable.class.isAssignableFrom(fieldType)) {

            field.set(object, bundle.getParcelable(key));
        } else if (Serializable.class.isAssignableFrom(fieldType)) {

            field.set(object, bundle.getSerializable(key));
        } else throw new RuntimeException("Unsupported field type: " + fieldType.getSimpleName());
    }

    private static class MarkedField {

        private final Field field;
        private final FromBundle annotation;

        private MarkedField(Field field, FromBundle annotation) {
            this.field = field;
            this.annotation = annotation;
        }
    }
}
