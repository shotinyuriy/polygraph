package kz.aksay.polygraph.validation;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateEquationsValidator implements ConstraintValidator<DateEquations, Object> {

	private DateEquation[] dateEquations;
	
	@Override
	public void initialize(DateEquations constraintAnnotation) {
		dateEquations = constraintAnnotation.dateEquations();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		
		Set<String> messages = new HashSet<String>();
		if(value == null) return false;
		else {
			boolean isValid = true;	
			for(DateEquation dateEquation : dateEquations) {
				SimpleDateEquationValidator validator = new SimpleDateEquationValidator(dateEquation);
				if(validator.isSimpleValid(value) == false) {
					messages.add(dateEquation.message());
					isValid = false;
				}
			}
			if(isValid == false) {
				context.disableDefaultConstraintViolation();
				for(String s : messages) {
					context.buildConstraintViolationWithTemplate(s)
						.addConstraintViolation();
				}
			}
			return isValid;
		}
	}
}
