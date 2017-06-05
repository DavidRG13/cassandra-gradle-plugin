package com.williamhill.gradle;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;

public class CassandraUnit {

    private static final String CASSANDRA_UNIT = "cassandra-unit-3.1.4.0-SNAPSHOT";
    private static final String CASSANDRA_STARTER = CASSANDRA_UNIT + "/bin/cu-starter";
    private static final String BINARY_FILE = "cassandra-unit-3.1.4.0-SNAPSHOT-bin.tar.gz";

    public static void  startCassandra(final int port, final long timeout, final String schemaFilePath, final String cassandraUnit) {
        File temp = new File("temp");
        if (temp.exists()) {
            File[] tempFiles = temp.listFiles();
            if (tempFiles != null) {
                for (File file : tempFiles) {
                    file.delete();
                }
            }
        } else {
            temp.mkdir();
        }

        try {
            File cu = new File(CASSANDRA_UNIT);
            if (!cu.exists()) {
                if (cassandraUnit == null || cassandraUnit.isEmpty()) {
                    String downloadLink = "https://github.com/William-Hill-Online/cassandra-unit/releases/download/SNAPSHOT/" + BINARY_FILE;
                    downloadCassandraUnitFrom(downloadLink);
                } else {
                    downloadCassandraUnitFrom(cassandraUnit);
                }
                new ProcessBuilder("tar", "-xvf", BINARY_FILE).start().waitFor();
            }

            String[] strings = {"sh", CASSANDRA_STARTER, "-p", String.valueOf(port), "-t", String.valueOf(timeout), "-s", schemaFilePath, "-d", CASSANDRA_UNIT};
            new ProcessBuilder(strings).redirectErrorStream(true).redirectOutput(new File(temp, "log")).start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void stopCassandra() {
        try {
            new ProcessBuilder("/bin/bash", "-c", "ps -ef | grep \"[c]u-loader\" | awk '{print $2}' |xargs kill").redirectErrorStream(true).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadCassandraUnitFrom(final String downloadLink) throws IOException {
        FileUtils.copyURLToFile(new URL(downloadLink), new File(BINARY_FILE));
    }

    static boolean portIsNotListening(final int port) {
        try(Socket socket = new Socket("localhost", port)) {
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
