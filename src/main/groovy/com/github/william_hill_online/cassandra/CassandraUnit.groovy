package com.github.william_hill_online.cassandra

class CassandraUnit {

    private static final String CASSANDRA_UNIT = "cassandra-unit-3.1.4.0-SNAPSHOT"
    private static final String CASSANDRA_STARTER = CASSANDRA_UNIT + "/bin/cu-starter"
    private static final String BINARY_FILE = "cassandra-unit-3.1.4.0-SNAPSHOT-bin.tar.gz"

    static void  startCassandra(final int port, final long timeout, final String schemaFilePath, final String cassandraUnit, String workingDirectory) {
        new File(workingDirectory).mkdir()

        try {
            File cu = new File(workingDirectory, CASSANDRA_UNIT)
            if (!cu.exists()) {
                if (cassandraUnit == null || cassandraUnit.isEmpty()) {
                    String downloadLink = "https://github.com/William-Hill-Online/cassandra-unit/releases/download/Snapshot2/" + BINARY_FILE
                    downloadCassandraUnitFrom(downloadLink, workingDirectory)
                } else {
                    downloadCassandraUnitFrom(cassandraUnit, workingDirectory)
                }
                def string = "$workingDirectory/$BINARY_FILE"
                println string
                new ProcessBuilder("tar", "-xf", string, "-C", "$workingDirectory").start().waitFor()
            }

            String[] strings = ["sh", "$workingDirectory/$CASSANDRA_STARTER", "-p", String.valueOf(port), "-t", String.valueOf(timeout), "-s", schemaFilePath, "-d", "$workingDirectory/$CASSANDRA_UNIT"]
            new ProcessBuilder(strings).redirectErrorStream(true).redirectOutput(new File(cu, "cassandra-unit.log")).start()
        } catch (IOException | InterruptedException e) {
            e.printStackTrace()
        }
    }

    static void stopCassandra() {
        try {
            new ProcessBuilder("/bin/bash", "-c", "ps -ef | grep \"[c]u-loader\" | awk '{print \$2}' |xargs kill").redirectErrorStream(true).start()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }

    private static void downloadCassandraUnitFrom(final String downloadLink, final String workingDirectory) throws IOException {
        new File(workingDirectory, BINARY_FILE) << new URL(downloadLink).getBytes()
    }

    static boolean portIsNotListening(final int port) {
        return !portIsListening(port)
    }

    static boolean portIsListening(final int port) {
        try {
            Socket socket = new Socket("localhost", port)
            socket.close()
            return true
        } catch (IOException e) {
            return false
        }
    }
}
