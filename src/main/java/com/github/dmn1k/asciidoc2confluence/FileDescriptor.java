package com.github.dmn1k.asciidoc2confluence;

import lombok.Builder;

import java.io.File;

@Builder(builderClassName = "Builder")
public class FileDescriptor {
    String file;
    String ancestorId;
    String preambleTitle;
}
