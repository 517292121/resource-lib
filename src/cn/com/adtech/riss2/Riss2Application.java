package cn.com.adtech.riss2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import cn.com.adtech.riss2.filter.AccessFilter;
import cn.com.adtech.riss2.filter.MyXssFilter;
import cn.com.adtech.riss2.fileservice.FileServiceSettings;
import cn.com.adtech.riss2.listener.AccessCounterListener;
import cn.com.adtech.riss2.servlet.CreateLuceneIndex;
import cn.com.adtech.riss2.servlet.UpdateAllCache;
import cn.com.adtech.riss2.servlet.UpdateGroupCount;
import com.metaparadigm.jsonrpc.JSONRPCServlet;
import uk.ltd.getahead.dwr.DWRServlet;

/**
 * Spring Boot entry point for RISS2. The original XML bean graph, JSP views,
 * URL mappings, DWR and JSON-RPC endpoints remain in use so existing clients
 * do not need to change their URLs during the platform upgrade.
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@ImportResource({
        "classpath:/legacy-config/applicationContext.xml",
        "classpath:/legacy-config/bsoBean.xml",
        "classpath:/legacy-config/riss2-servlet.xml"
})
public class Riss2Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Riss2Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Riss2Application.class);
    }

    /**
     * JSPs cannot be rendered directly from a nested executable-JAR resource.
     * Use the source WebRoot in development, or unpack the WebRoot archive
     * bundled in the JAR to a temporary document root for packaged runs.
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> legacyWebRoot() {
        return factory -> {
            try {
                // The source WebRoot contains the legacy web.xml, which
                // duplicates servlet/filter/listener registrations already
                // supplied by Spring Boot. Prefer the sanitized bundle in
                // development and packaged runs alike.
                File webRoot = unpackBundledWebRoot();
                if (webRoot == null) {
                    webRoot = new File("WebRoot");
                }
                if (webRoot != null && webRoot.isDirectory()) {
                    factory.setDocumentRoot(webRoot);
                }
            } catch (IOException e) {
                throw new IllegalStateException("Unable to prepare the bundled JSP web root", e);
            }
        };
    }

    private File unpackBundledWebRoot() throws IOException {
        InputStream archive = getClass().getClassLoader().getResourceAsStream("webroot.zip");
        if (archive == null) {
            return null;
        }

        Path destination = Files.createTempDirectory("MCProject-WebRoot-");
        try (ZipInputStream zip = new ZipInputStream(archive)) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                Path output = destination.resolve(entry.getName()).normalize();
                if (!output.startsWith(destination)) {
                    throw new IOException("Invalid path in bundled WebRoot archive: " + entry.getName());
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(output);
                } else {
                    Path parent = output.getParent();
                    if (parent != null) {
                        Files.createDirectories(parent);
                    }
                    try (FileOutputStream file = new FileOutputStream(output.toFile())) {
                        byte[] buffer = new byte[8192];
                        int length;
                        while ((length = zip.read(buffer)) != -1) {
                            file.write(buffer, 0, length);
                        }
                    }
                }
                zip.closeEntry();
            }
        }
        return destination.toFile();
    }

    @Bean
    public ServletContextInitializer legacyContextParameters() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) {
                servletContext.setInitParameter("downLoadPath", FileServiceSettings.property("file.dir", ""));
                servletContext.setInitParameter("fileDe", "views/common/adlib.key");
            }
        };
    }

    @Bean
    public FilterRegistrationBean<Filter> accessFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>(new AccessFilter());
        registration.addUrlPatterns("/console/data/*");
        registration.setName("accessFilter");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> xssFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>(new MyXssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssfilter");
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<AccessCounterListener> accessCounterListener() {
        return new ServletListenerRegistrationBean<AccessCounterListener>(new AccessCounterListener());
    }

    @Bean
    public ServletRegistrationBean<Servlet> jsonRpcServlet() {
        return servlet("JsonRpc", new JSONRPCServlet(), "/Json_Rpc");
    }

    @Bean
    public ServletRegistrationBean<Servlet> dwrServlet() {
        ServletRegistrationBean<Servlet> registration = servlet("dwr-invoker", new DWRServlet(), "/dwr/*");
        registration.addInitParameter("config", "WEB-INF/config/dwr.xml");
        registration.addInitParameter("debug", "true");
        registration.addInitParameter("crossDomainSessionSecurity", "false");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<Servlet> createLuceneIndexServlet() {
        return servlet("createLuceneIndex", new CreateLuceneIndex(), "/servlet/createLuceneIndex");
    }

    @Bean
    public ServletRegistrationBean<Servlet> updateAllCacheServlet() {
        ServletRegistrationBean<Servlet> registration = servlet("UpdateAllCache", new UpdateAllCache(), "/servlet/updateAllCache");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public ServletRegistrationBean<Servlet> updateGroupCountServlet() {
        return servlet("UpdateGroupCount", new UpdateGroupCount(), "/servlet/updateGroupCount");
    }

    private ServletRegistrationBean<Servlet> servlet(String name, Servlet servlet, String mapping) {
        ServletRegistrationBean<Servlet> registration = new ServletRegistrationBean<Servlet>(servlet, mapping);
        registration.setName(name);
        return registration;
    }
}
