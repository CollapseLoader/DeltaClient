package aethereal.core;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTarget {
    byte a() default 2;
}
