package kz.aksay.polygraph.validation;

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
		
		StringBuffer messages = new StringBuffer();
		if(value == null) return false;
		else {
			boolean isValid = true;	
			for(DateEquation dateEquation : dateEquations) {
				SimpleDateEquationValidator validator = new SimpleDateEquationValidator(dateEquation);
				if(validator.isSimpleValid(value) == false) {
					if(messages.length() > 0) messages.append("\n");
					messages.append(dateEquation.message());
					isValid = false;
				}
			}
			if(isValid == false) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(messages.toString())
					.addConstraintViolation();
			}
			return isValid;
		}
	}
}
