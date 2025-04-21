package Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class Ann {
    @Retention(RetentionPolicy.RUNTIME)
    @interface Ok {};

    @Retention(RetentionPolicy.RUNTIME)
    @interface Ugly {
        int k() default 5;
    }
}
