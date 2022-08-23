package com.github.yinguoqing96.doraserver.controller;

import com.github.yinguoqing96.doraserver.common.AjaxResult;
import com.github.yinguoqing96.doraserver.rsa.RSAEncryptUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yin Guoqing
 * @date 2022/8/20
 */
@RestController
@RequestMapping("/api")
public class ServerController {

    @Value("${rsa.privateKey}")
    private String privateKey;

    @PostMapping("/hello")
    public AjaxResult helloWorld() {
        String helloWorldString = "已连接到目标 VM, 地址: ''127.0.0.1:56577'，传输: '套接字''\n" +
                "\n" +
                "  .   ____          _            __ _ _\n" +
                " /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\\n" +
                "( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\\n" +
                " \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )\n" +
                "  '  |____| .__|_| |_|_| |_\\__, | / / / /\n" +
                " =========|_|==============|___/=/_/_/_/\n" +
                " :: Spring Boot ::                (v2.7.3)\n" +
                "\n" +
                "2022-08-23 16:53:11.916  INFO 27167 --- [           main] c.g.y.doraserver.DoraServerApplication   : Starting DoraServerApplication using Java 11.0.14.1 on MacBook.local with PID 27167 (/Users/yinguoqing/IdeaProjects/dora-server/target/classes started by yinguoqing in /Users/yinguoqing/IdeaProjects/dora-server)\n" +
                "2022-08-23 16:53:11.917  INFO 27167 --- [           main] c.g.y.doraserver.DoraServerApplication   : No active profile set, falling back to 1 default profile: \"default\"\n" +
                "2022-08-23 16:53:12.262  INFO 27167 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 9902 (http)\n" +
                "2022-08-23 16:53:12.266  INFO 27167 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]\n" +
                "2022-08-23 16:53:12.266  INFO 27167 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.65]\n" +
                "2022-08-23 16:53:12.302  INFO 27167 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext\n" +
                "2022-08-23 16:53:12.302  INFO 27167 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 361 ms\n" +
                "2022-08-23 16:53:12.450  INFO 27167 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 9902 (http) with context path ''\n" +
                "2022-08-23 16:53:12.457  INFO 27167 --- [           main] c.g.y.doraserver.DoraServerApplication   : Started DoraServerApplication in 0.708 seconds (JVM running for 1.015)\n" +
                "2022-08-23 16:53:16.836  INFO 27167 --- [nio-9902-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'\n" +
                "2022-08-23 16:53:16.836  INFO 27167 --- [nio-9902-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'\n" +
                "2022-08-23 16:53:16.839  INFO 27167 --- [nio-9902-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 3 ms";
        // 加密
        String helloWorld = RSAEncryptUtil.privateKeyEncrypt(helloWorldString, privateKey);
        return AjaxResult.success(helloWorld);
    }
}