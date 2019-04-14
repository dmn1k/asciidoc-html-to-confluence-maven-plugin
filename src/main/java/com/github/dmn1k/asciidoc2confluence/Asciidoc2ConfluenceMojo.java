package com.github.dmn1k.asciidoc2confluence;


import groovy.lang.GroovyClassLoader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.xerces.impl.dv.util.Base64;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "publish", defaultPhase = LifecyclePhase.INSTALL)
public class Asciidoc2ConfluenceMojo extends AbstractMojo {
    @Parameter(property = "file", required = true)
    private File file;

    @Parameter(property = "ancestorId", required = true)
    private String ancestorId;

    @Parameter(property = "preambleTitle")
    private String preambleTitle;

    @Parameter(property = "api", required = true)
    private String api;

    @Parameter(property = "spaceKey", required = true)
    private String spaceKey;

    @Parameter(property = "pagePrefix")
    private String pagePrefix;

    @Parameter(property = "pageSuffix")
    private String pageSuffix;

    @Parameter(property = "credentials", required = true)
    private String credentials;

    @Parameter(property = "extraPageContent")
    private String extraPageContent;

    @Parameter(property = "createSubpages", defaultValue = "false")
    private boolean createSubpages;

    public void execute() throws MojoExecutionException {
        Config config = Config.builder()
                .api(api)
                .createSubpages(createSubpages)
                .credentials(Base64.encode(credentials.getBytes()))
                .extraPageContent(extraPageContent)
                .pagePrefix(pagePrefix == null ? "" : pagePrefix)
                .pageSuffix(pageSuffix == null ? "" : pageSuffix)
                .spaceKey(spaceKey)
                .singleInput(FileDescriptor.builder().file(file.getAbsolutePath()).ancestorId(ancestorId).preambleTitle(preambleTitle).build())
                .build();

        try (InputStream fis = getClass().getClassLoader().getResourceAsStream("ConfluencePublisher.groovy");
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            Class publisherClass = new GroovyClassLoader().parseClass(reader, "ConfluencePublisher.groovy");
            Object publisherInstance = publisherClass.getConstructor(Config.class).newInstance(config);
            publisherClass.getDeclaredMethod("publish", new Class[]{}).invoke(publisherInstance);
        } catch (Exception e) {
            throw new MojoExecutionException("publish to confluence failed", e);
        }
    }
}
