package com.ht.qlktx.annotations;

import com.ht.qlktx.enums.Role;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredRole {
    Role[] value();
}