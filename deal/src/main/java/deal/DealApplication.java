package deal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"deal", "core"})
public class DealApplication {
    public static void main(String[] args) {
        SpringApplication.run(DealApplication.class, args);
    }
}
