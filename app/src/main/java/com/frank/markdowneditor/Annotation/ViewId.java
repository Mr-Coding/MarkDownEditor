package com.frank.markdowneditor.Annotation;

import java.lang.annotation.*;

/**
 * 替代findViewById的注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ViewId {
    int id()default -1;
}
