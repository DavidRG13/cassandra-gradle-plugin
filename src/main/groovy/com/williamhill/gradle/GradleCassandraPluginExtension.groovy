package com.williamhill.gradle

class GradleCassandraPluginExtension {

    private int timeout = 120_000L
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
}
