package com.williamhill.gradle

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
                            classpath "com.williamhill:cassandra-gradle-plugin:1.0-SNAPSHOT"
                            classpath "org.apache.cassandra:cassandra-all:3.10"
                            classpath "com.datastax.cassandra:cassandra-driver-core:3.2.0"
                        }
                    }

                    apply plugin: 'java'
                    apply plugin: com.williamhill.gradle.GradleCassandraPlugin
                    
                    repositories {
                        jcenter()
                    }
                    dependencies {
                        compile "org.apache.cassandra:cassandra-all:3.10"
                        compile "com.datastax.cassandra:cassandra-driver-core:3.2.0"
                    }   
                    """

        when:
        def result = GradleRunner.create()
                .withDebug(true)
                .withProjectDir(testProjectDir.root)
                .withArguments("startCassandra", "-S")
                .build()

        then:
        !result.getOutput().contains("NullPointerException")
        result.task(":startCassandra").getOutcome() == TaskOutcome.SUCCESS
    }

//    def 'individual tasks can declare a dependency on a running cassandra instance'() {
//        given:
//        buildFile << """
//                    buildscript {
//                        repositories {
//                            mavenLocal()
//                            jcenter()
//                        }
//                        dependencies {
//                            classpath "com.williamhill:cassandra-gradle-plugin:1.0-SNAPSHOT"
//                        }
//                    }
//
//                    apply plugin: "com.williamhill.gradle.GradleCassandraPlugin"
//
//                    task testTask {
//                        runWithCassandra = true
//                    }
//                    """
//
//        when:
//        def result = GradleRunner.create()
//                .withDebug(true)
//                .withProjectDir(testProjectDir.root)
//                .withArguments("testTask")
//                .build()
//        println(buildFile.text)
//
//
//        then:
//        result.getOutput().contains("")
//        result.task(":testTask").getOutcome() == TaskOutcome.SUCCESS
//    }
}
