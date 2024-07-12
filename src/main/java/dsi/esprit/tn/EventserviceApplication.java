package dsi.esprit.tn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients("dsi.esprit.tn")
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {"dsi.esprit.tn.repository"})
//@ComponentScan(basePackages = { "dsi.esprit.tn.*"})
@EntityScan("dsi.esprit.tn.Models")
@EnableScheduling
@SpringBootApplication
public class EventserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventserviceApplication.class, args);
    }

}
