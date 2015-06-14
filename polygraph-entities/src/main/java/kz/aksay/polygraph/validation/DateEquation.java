package kz.aksay.polygraph.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy=DateEquationValidator.class)
public @interface DateEquation {
	
	String message() default "";
	
	Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    Comparison comparison();
    
    String date1Name();
    
    String date2Name();
}
