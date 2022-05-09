package ape;

import ape.master.entity.common.ApeApplicationEnum;
import ape.master.security.SecurityUtils;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the * and some desktop browsers.
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EntityScan
@EnableJpaRepositories
@PWA(name = "PICCAPE页面性能告警后台服务", shortName = "告警后台服务", manifestPath = "manifest.json")
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        SecurityUtils.application = ApeApplicationEnum.告警后台服务;
        applicationContext = SpringApplication.run(Application.class, args);
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        Application.applicationContext = applicationContext;
    }

    @Override
    public void configurePage(AppShellSettings settings) {
    }
}
