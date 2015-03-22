package lessons.starter;

import lessons.entity.Person;
import lessons.validator.AddressValidator;
import lessons.validator.PersonValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.DataBinder;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

import java.util.Locale;


public class ValidationBindingConversionStarter {

    private static ValidationBindingConversionStarter starter;

    private static final Logger logger = LogManager.getLogger(ValidationBindingConversionStarter.class);

    public static void main(String[] args) {
        logger.info("Starting configuration...");

        starter = new ValidationBindingConversionStarter();
        starter.personValidate();
        starter.beanWrapperExample();
    }

    private void personValidate() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("ValidationMessages");

        Person person = new Person();
        person.setAge(150);
        PersonValidator personValidator = new PersonValidator(new AddressValidator());
        DataBinder dataBinder = new DataBinder(person);
        dataBinder.setValidator(personValidator);
        dataBinder.validate();
        logger.info("Count errors: " + dataBinder.getBindingResult().getErrorCount());

        ObjectError error = dataBinder.getBindingResult().getAllErrors().get(0);
        logger.info("Error \"" +error.getCode() + "\": " + messageSource.getMessage(error.getCode(), null, Locale.ROOT));

        error = dataBinder.getBindingResult().getAllErrors().get(1);
        MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
        String[] codes = codesResolver.resolveMessageCodes(error.getCode(), "age"); //указываем, по какому полю объекта мы хотим получить коды ошибок
        logger.info("Error \"" +codes[0] + "\": " + messageSource.getMessage(codes[0], null, Locale.ROOT));
    }

    private void beanWrapperExample() {
        Person person = new Person();
        BeanWrapper beanWrapper = new BeanWrapperImpl(person);
        beanWrapper.setPropertyValue("name", "Вася");
        PropertyValue propertyValue = new PropertyValue("age", "25");
        beanWrapper.setPropertyValue(propertyValue);
        logger.info("Person from Person - name: " + beanWrapper.getPropertyValue("name") + ", age: " + beanWrapper.getPropertyValue("age"));
        logger.info("Person from Person - name: " + person.getName() + ", age: " + person.getAge());
    }
}
