package lessons;

import lessons.services.CommandManager;
import lessons.services.GreetingService;
import lessons.services.GreetingServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;


/**
 * Конфигурационный класс Spring IoC контейнера
 */
@Configuration
@ComponentScan(basePackages = "lessons.services")
@Import(AnotherConfiguration.class)
public class LessonsConfiguration {

    @Bean
    MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Bean
    @Scope("prototype")
    public Object asyncCommand() {
        return new Object();
    }

    @Bean
    public CommandManager commandManager() {
        // возвращаем новую анонимную реализацию CommandManager с
        // с новым объектом Command
        return new CommandManager() {
            protected Object createCommand() {
                return asyncCommand();
            }
        };
    }

//    @Bean
//    BeanWithDependency beanWithDependency() {
//        return new BeanWithDependency(greetingService());
//    }

    @Bean
    GreetingService greetingService() {
        return new GreetingServiceImpl();
    }

//    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
//    @Description("Текстовое описание бина greetingService")
//    GreetingService greetingService() {
//        return new GreetingServiceImpl();
//    }

//    @Bean(name = {"gServiceName", "gServiceAnotherNamed"})
//    @Scope("prototype")
//    GreetingService gService() {
//        return new GreetingServiceImpl();
//    }
}
