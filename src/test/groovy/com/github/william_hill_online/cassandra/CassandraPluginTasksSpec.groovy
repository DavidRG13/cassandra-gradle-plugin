package com.github.william_hill_online.cassandra

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class CassandraPluginTasksSpec extends Specification {

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    List pluginClasspath

    def setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle")
        pluginClasspath = getClass().classLoader.findResource('plugin-classpath.txt').readLines().collect { new File(it) }
    }

    def 'startCassandra task should start an embedded cassandra instance'() {
        given:
        buildFile << """
                    plugins {
                        id 'com.williamhill.cassandra'
                    }

                    cassandra {
                        timeout 200000
                        schemaFilePath "src/test/resources/schema.cql"
                    }
                    """

        when:
        def result = GradleRunner.create()
                .withDebug(true)
                .withProjectDir(testProjectDir.root)
                .withArguments("startCassandra", "stopCassandra")
                .withPluginClasspath(pluginClasspath)
                .build()

        then:
        result.getOutput().contains("Starting...")
        result.task(":startCassandra").getOutcome() == TaskOutcome.SUCCESS
    }

    def 'startCassandra task should fail if port already in use'() {
        given:
        buildFile << """
                    plugins {
                        id 'com.williamhill.cassandra'
                    }

                    cassandra {
                        port 9043
                        schemaFilePath "src/test/resources/schema.cql"
                    }
                    """

        def socket = new ServerSocket(9043)

        when:
        def result = GradleRunner.create()
                .withDebug(true)
                .withProjectDir(testProjectDir.root)
                .withArguments("startCassandra")
                .withPluginClasspath(pluginClasspath)
                .buildAndFail()

        then:
        result.getOutput().contains("Port 9043 already in use")
        result.task(":startCassandra").getOutcome() == TaskOutcome.FAILED
        socket.close()
    }

    def "startCassandra task should fail if schema isn't provided"() {
        given:
        buildFile << """
                    plugins {
                        id 'com.williamhill.cassandra'
                    }

                    cassandra {
                        timeout 200000
                    }
                    """

        when:
        def result = GradleRunner.create()
                .withDebug(true)
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath(pluginClasspath)
                .withArguments("startCassandra")
                .buildAndFail()

        then:
        result.getOutput().contains("SchemaFilePath has to be provided")
        result.task(":startCassandra").getOutcome() == TaskOutcome.FAILED
    }
}
