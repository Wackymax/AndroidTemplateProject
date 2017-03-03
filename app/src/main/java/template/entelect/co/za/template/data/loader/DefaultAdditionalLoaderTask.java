package template.entelect.co.za.template.data.loader;

import org.codejargon.feather.Feather;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Provider;

import template.entelect.co.za.template.MainApplication;


/**
 * Created by hennie.brink on 2016/07/25.
 */
public class DefaultAdditionalLoaderTask implements AdditionalLoaderTask<Object> {

    protected final Feather feather;

    public DefaultAdditionalLoaderTask() {
        feather = MainApplication.getRootContext().getFeather();
    }

    @Override
    public void loadInBackground(Object data) {

        if (data == null) {
            return;
        }

        injectFields(data, data.getClass());
    }

    private void injectFields(Object data, Class clazz) {
        for (Method method : clazz.getMethods()) {
            if (!method.isAnnotationPresent(Inject.class))
                continue;

            if (method.getParameterTypes().length < 1 || method.getParameterTypes().length > 1)
                continue;

            for (Class<?> parameter : method.getParameterTypes()) {

                Provider<?> provider = feather.provider(parameter);

                try {
                    method.invoke(data, provider.get());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        if (clazz.getSuperclass() != null) {
            injectFields(data, clazz.getSuperclass());
        }
    }
}
