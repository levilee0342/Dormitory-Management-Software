package com.ht.qlktx.annotations;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.enums.Role;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
public class RoleAspect {

    @Around("@annotation(requiredRole)")
    public Object checkRole(ProceedingJoinPoint pjp, RequiredRole requiredRole) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String userRole = (String) requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST);
        Role[] roles = requiredRole.value();
        for (Role role : roles) {
            if (role.name().equals(userRole)) {
                return pjp.proceed();
            }
        }
        throw new HttpException("Bạn không có quyền thực hiện hành động này", HttpStatus.FORBIDDEN);
    }
}
