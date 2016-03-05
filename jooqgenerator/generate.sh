#!/bin/bash

# Download all classpath jars first
java -cp jooq-3.7.2.jar:jooq-meta-3.7.2.jar:jooq-codegen-3.7.2.jar:postgresql-9.1-901-1.jdbc4.jar org.jooq.util.GenerationTool test-schema.xml