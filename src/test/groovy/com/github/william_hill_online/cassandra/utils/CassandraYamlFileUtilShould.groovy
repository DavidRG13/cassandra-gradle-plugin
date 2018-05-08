package com.github.william_hill_online.cassandra.utils

import spock.lang.Shared
import spock.lang.Specification

class CassandraYamlFileUtilShould extends Specification {

    @Shared def tempDir = File.createTempDir()

    def "overwritePort function should replace the port when it matches"() {
        expect:
        "native_transport_port: 9042" == CassandraYamlFileUtil.overwritePort.apply("native_transport_port: 0033", 9042)
    }

    def "overwritePort function should not replace the port when it doesnt match"() {
        expect:
        "native_transport_enabled: true" == CassandraYamlFileUtil.overwritePort.apply("native_transport_enabled: true", 9042)
    }

    def "create a cassandra yaml with the desired port"() {
        given:
        def port = 0001

        when:
        def yamlFile = CassandraYamlFileUtil.createYamlWithPortInWorkingDirectory(port, tempDir.getAbsolutePath())

        then:
        new BufferedReader(new FileReader(yamlFile)).lines().anyMatch {line -> line.contains(String.valueOf(port))}
    }
}
