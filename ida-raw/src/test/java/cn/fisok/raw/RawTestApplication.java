package cn.fisok.raw;


import cn.fisok.raw.kit.LogKit;
import cn.fisok.raw.kit.NetKit;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class RawTestApplication {
    public static void main(String[] args){
        SpringApplication app = new SpringApplication(RawTestApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        ConfigurableApplicationContext appCtx = app.run(args);

        Environment env = appCtx.getEnvironment();
        String ip = NetKit.getHostAddress();
        String port = env.getProperty("server.port");
        String ctxPath = env.getProperty("server.servlet.context-path");

        StringBuffer consoleText = new StringBuffer();
        consoleText.append("\n---------------------------------------------\n");
        consoleText.append("启动成功，访问地址: http://").append(ip).append(":").append(port).append(ctxPath);
        consoleText.append("\n---------------------------------------------\n");

        LogKit.info(consoleText.toString());

    }
}
