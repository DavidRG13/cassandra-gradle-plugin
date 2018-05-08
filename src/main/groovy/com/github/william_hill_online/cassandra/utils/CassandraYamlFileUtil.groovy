package com.github.william_hill_online.cassandra.utils

import java.util.function.BiFunction

class CassandraYamlFileUtil {

    public static BiFunction<String, Integer, String> overwritePort = { String line, Integer port -> line.contains("native_transport_port:") ? "native_transport_port: $port" :line }
    private static final String CASSANDRA_UNIT_YAML_FILE_NAME = "cu-cassandra.yaml"

    static def createYamlWithPortInWorkingDirectory(final int port, final String workingDirectory) {
        def cu = new File(workingDirectory, "cu.yaml")
        def writer = new BufferedWriter(new FileWriter(cu))

        def resource = ClassLoader.getSystemResourceAsStream(CASSANDRA_UNIT_YAML_FILE_NAME)
        new BufferedReader(new InputStreamReader(resource)).lines()
                .map { line -> overwritePort.apply(line, port)}
                .forEach {String line -> writer.write line }

        writer.close()
        return cu
    }
}
