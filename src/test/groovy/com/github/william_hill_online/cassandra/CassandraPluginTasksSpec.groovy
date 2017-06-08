package com.github.william_hill_online.cassandra

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class CassandraPluginTasksSpec extends Specification {

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    def setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle")
    }

    def 'startCassandra task should start an embedded cassandra instance'() {
        given:
        buildFile << """
                    buildscript {
                        repositories {
                            mavenLocal()
                            jcenter()
                        }
                        dependencies {
                            classpath "com.github.william_hill_online:cassandra-gradle-plugin:0.1"
                        }
                    }
                    
                    apply plugin: com.github.william_hill_online.cassandra.GradleCassandraPlugin
                    
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
                .withPluginClasspath([new File("META-INF/gradle-plugins/com.williamhill.cassandra.properties")])
                .build()

        then:
        result.getOutput().contains("Starting...")
        result.task(":startCassandra").getOutcome() == TaskOutcome.SUCCESS
    }

    def 'startCassandra task should fail if port already in use'() {
        given:
        buildFile << """
                    buildscript {
                        repositories {
                            mavenLocal()
                            jcenter()
                        }
                        dependencies {
                            classpath "com.github.william_hill_online:cassandra-gradle-plugin:0.1"
                        }
                    }
                    
                    apply plugin: com.github.william_hill_online.cassandra.GradleCassandraPlugin
                    
                    cassandra {
                        port 9042
                        schemaFilePath "src/test/resources/schema.cql"
                    }
                    """

        def socket = new ServerSocket(9042)

        when:
        def result = GradleRunner.create()
                .withDebug(true)
                .withProjectDir(testProjectDir.root)
                .withArguments("startCassandra")
                .buildAndFail()

        then:
        result.getOutput().contains("Port 9042 already in use")
        result.task(":startCassandra").getOutcome() == TaskOutcome.FAILED
        socket.close()
    }

    def "startCassandra task should fail if schema isn't provided"() {
        given:
        buildFile << """
                    buildscript {
                        repositories {
                            mavenLocal()
                            jcenter()
                        }
                        dependencies {
                            classpath "com.github.william_hill_online:cassandra-gradle-plugin:0.1"
                        }
                    }
                    
                    apply plugin: com.github.william_hill_online.cassandra.GradleCassandraPlugin
                    
                    cassandra {
                        timeout 200000
                    }
                    """

        when:
        def result = GradleRunner.create()
                .withDebug(true)
                .withProjectDir(testProjectDir.root)
                .withArguments("startCassandra")
                .buildAndFail()

        then:
        result.getOutput().contains("SchemaFilePath has to be provided")
        result.task(":startCassandra").getOutcome() == TaskOutcome.FAILED
    }
}
