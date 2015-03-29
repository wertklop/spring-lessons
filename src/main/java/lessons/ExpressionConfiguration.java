package lessons;

import lessons.spel.Inventor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ExpressionConfiguration {

    @Bean
    Inventor inventor() { return new Inventor();}
}
