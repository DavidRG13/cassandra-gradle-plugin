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

    def 'individual tasks can declare a dependency on a running cassandra instance'() {
        given:
        buildFile << """
                    apply plugin: ${GradleCassandraPlugin.name}

                    task testTask {
                        runWithCassandra = true
                    }
                    """

        when:
        def result = GradleRunner.create()
                .withDebug(true)
                .withProjectDir(testProjectDir.root)
                .withArguments("testTask")
                .build()
        println(buildFile.text)


        then:
        result.getOutput().contains("")
        result.task(":testTask").getOutcome() == TaskOutcome.SUCCESS
    }
}
