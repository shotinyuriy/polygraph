package kz.aksay.polygraph.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateEquationValidator implements ConstraintValidator<DateEquation, Object> {

	private SimpleDateEquationValidator simpleDateEquationValidator;
	
	@Override
	public void initialize(DateEquation constraintAnnotation) {
		simpleDateEquationValidator = new SimpleDateEquationValidator(constraintAnnotation);
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return simpleDateEquationValidator.isSimpleValid(value);
	}
}
