package com.github.copilot.lang.fallback;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class VSCodeLanguageMap {
    public static final Map<String, String> INTELLIJ_VSCODE_MAP = Collections.unmodifiableMap(new ConcurrentHashMap<String, String>(){
		private static final long serialVersionUID = -6245267354191843399L;

		{
            this.put("CoffeeScript", "coffeescript");
            this.put("ObjectiveC", "c");
            this.put("CSS", "css");
            this.put("go", "go");
            this.put("Groovy", "groovy");
            this.put("Handlebars", "handlebars");
            this.put("HTML", "html");
            this.put("XHTML", "html");
            this.put("JAVA", "java");
            this.put("JavaScript", "javascript");
            this.put("TypeScript JSX", "jsx");
            this.put("LESS", "less");
            this.put("Makefile", "makefile");
            this.put("Markdown", "markdown");
            this.put("PHP", "php");
            this.put("Jade", "pug");
            this.put("Python", "python");
            this.put("R", "r");
            this.put("ruby", "ruby");
            this.put("Rust", "rust");
            this.put("SCSS", "scss");
            this.put("Shell Script", "shellscript");
            this.put("SQL", "sql");
            this.put("SQL92", "sql");
            this.put("Stylus", "stylus");
            this.put("TypeScript", "typescript");
            this.put("Vue", "vue");
            this.put("XML", "xml");
            this.put("yaml", "yaml");
        }
    });
    public static final Map<String, String> SHEBANG_MAP = Collections.unmodifiableMap(new ConcurrentHashMap<String, String>(){
		private static final long serialVersionUID = -4376366775698776171L;

		{
            this.put("shellscript", "#!/bin/sh");
            this.put("python", "#!/usr/bin/env python3");
            this.put("ruby", "#!/usr/bin/env ruby");
            this.put("html", "<!DOCTYPE html>");
            this.put("yaml", "# YAML data");
        }
    });

    private VSCodeLanguageMap() {
    }
}

