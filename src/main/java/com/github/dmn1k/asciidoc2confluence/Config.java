package com.github.dmn1k.asciidoc2confluence;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder(builderClassName = "Builder")
public class Config {
    private String api;
    private String spaceKey;

    @lombok.Builder.Default
    private String pagePrefix = "";

    @lombok.Builder.Default
    private String pageSuffix = "";

    private String credentials;

    @lombok.Builder.Default
    private String extraPageContent = "";

    private boolean createSubpages;

    @Singular("singleInput")
    private List<FileDescriptor> input;
}
