package com.frank.markdowneditor.Annotation;

import android.app.Activity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * ViewId注解的接解析
 */
public class FindView {
    /**
     * @param activity Activity
     */
    public static void from(Activity activity){

        Class zlass = activity.getClass();
        Field[] fields = zlass.getDeclaredFields(); //获取所有字段
        Field.setAccessible(fields,true);      //设置可以访问私有字段

        for (Field field : fields){
            boolean b = field.isAnnotationPresent(ViewId.class); //该字段上是否有ViewId的注解
            if (b) {

                ViewId viewId = field.getAnnotation(ViewId.class); //获取ViewId的注解
                Object obj = null;
                try {

                    obj = zlass.getMethod("findViewById",int.class).invoke(activity,viewId.id());
                    field.set(activity,obj);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
