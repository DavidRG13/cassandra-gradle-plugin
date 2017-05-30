package com.williamhill.gradle

class GradleCassandraPluginExtension {

    private int timeout = 120_000L
    private int port = 9042
    private String cassandraYamlLocation = '/cassandra.yaml'

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

    String getCassandraYamlLocation() {
        cassandraYamlLocation
    }

    void setCassandraYamlLocation(String location) {
        this.cassandraYamlLocation = location
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
}
