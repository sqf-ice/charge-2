package com.clouyun.charge;

import com.clouyun.boot.common.exception.BootException;
import com.clouyun.boot.modules.maven.MavenModel;
import com.clouyun.boot.modules.springboot.BaseApplication;
import com.clouyun.boot.modules.springboot.context.SpringBootContext;
import com.clouyun.boot.services.CacheService;
import com.clouyun.charge.common.utils.ReadXml;
import com.clouyun.randomcall.core.commservice.CommunicateServiceImpl;
import org.apache.commons.configuration.ConfigurationException;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60) //1分钟失效
@EnableAutoConfiguration(exclude = {WebSecurityConfigurer.class/*, RedisAutoConfiguration.class*/})
//@AutoConfigureAfter(PageHelperAutoConfiguration.class)k
//@AutoConfigureBefore(PageHelperAutoConfiguration.class)
@ComponentScan(basePackages = {"com.clouyun.cdzcache", "com.clouyun.charge","com.clouyun.cdz","com.clouyun.randomcall.core.commservice"})
@Import({CacheService.class})
@RestController
public class Application extends BaseApplication {

    public static Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private Environment env;
    @Autowired
	private CommunicateServiceImpl cs;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public MavenModel execute() throws Exception {
        MavenModel mavenModel = new MavenModel();
        mavenModel.setName(env.getProperty("info.name"));
        mavenModel.setVersion(env.getProperty("info.version"));
        //mavenModel.setEnvironment(env.getProperty("info.profile"));
        mavenModel.setEnvironment(env.getProperty("spring.profiles.active"));
        return mavenModel;
    }

    @Override
    public void before(SpringApplication application, String[] args) {
    }

    @Override
    public void after(SpringApplication application, ConfigurableApplicationContext applicationContext, String[] args) throws BootException {
    	try {
            ReadXml.read();
            if (cs == null)
                cs = SpringBootContext.getBean("communicateServiceImpl");
            cs.startService(0);
        } catch (JDOMException | IOException | ConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) throws BootException, IOException {
        Application app = new Application();
        app.enableTips(false);
        app.start(args);
    }
}
