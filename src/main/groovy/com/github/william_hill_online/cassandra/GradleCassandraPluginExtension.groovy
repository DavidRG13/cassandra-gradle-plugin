package com.github.william_hill_online.cassandra

class GradleCassandraPluginExtension {

    private int timeout = 120_000L
    private int port = 9042
    private String schemaFilePath;
    private String cassandraUnit;

    int getTimeout() {
        timeout
    }

    void setTimeout(Object timeout) {
        if (timeout instanceof String) {
            this.timeout = Integer.parseInt(timeout)
        } else {
            this.timeout = timeout as Integer
        }
    }

    int getPort() {
        return port
    }

    void setPort(final int port) {
        this.port = port
    }

    void setTimeout(final int timeout) {
        this.timeout = timeout
    }

    String getSchemaFilePath() {
        return schemaFilePath
    }

    void setSchemaFilePath(final String schemaFilePath) {
        this.schemaFilePath = schemaFilePath
    }

    String getCassandraUnit() {
        return cassandraUnit
    }

    void setCassandraUnit(final String cassandraUnit) {
        this.cassandraUnit = cassandraUnit
    }
}
