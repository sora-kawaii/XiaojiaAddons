package com.xiaojia.xiaojiaaddons.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
   int step() default 1;

   String parent() default "";

   String suffix() default "";

   String description() default "";

   String[] options() default {};

   String name();

   Type type();

   int max() default Integer.MAX_VALUE;

   boolean illegal() default false;

   int min() default 0;

   String prefix() default "";

   public static enum Type {
   
      BOOLEAN,
   
      NUMBER,
   
      SELECT,
   
      FOLDER,
   
      TEXT;
   }
}
