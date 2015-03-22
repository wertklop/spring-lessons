package lessons.validator;

import lessons.entity.Address;
import lessons.entity.Person;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PersonValidator implements Validator {

    private Validator addressValidator;

    public PersonValidator(Validator addressValidator) {
        if (addressValidator == null || !addressValidator.supports(Address.class)) {
            throw new IllegalArgumentException("The supplied [Validator] is required and must not be null.");
        }
        this.addressValidator = addressValidator;
    }

    //Проверяет, применима данная проверка классу Person
    @Override
    public boolean supports(Class clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        Person person = (Person) target;
        if (person.getAge() < 0) {
            errors.rejectValue("age", "negativevalue");
        } else if (person.getAge() > 110) {
            errors.rejectValue("age", "too.darn.old");
        }
        try {
            errors.pushNestedPath("address");
            ValidationUtils.invokeValidator(addressValidator, person.getAddress(), errors);
        } finally {
            errors.popNestedPath();
        }
    }
}
