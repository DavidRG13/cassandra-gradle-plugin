package com.williamhill.gradle

import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class GradleCassandraPlugin implements Plugin<Project> {

    static final String TASK_GROUP_NAME = 'Cassandra'

    @Override
    void apply(final Project project) {
        addStartEmbeddedCassandraTask(project)

        project.afterEvaluate {
            configureTasksRequiringMongoDb(project)
        }
    }

    private static void addStartEmbeddedCassandraTask(Project project) {
        project.task(group: TASK_GROUP_NAME, description: 'Start an embedded Cassandra instance', 'startCassandra').doFirst {
            startCassandra()
        }
    }

    private static Iterable<Task> configureTasksRequiringMongoDb(Project project) {
        project.tasks.each {
            def task = it
            if (task.runWithCassandra) {
                task.doFirst {
                    startCassandra()
                }
            }
        }
    }

    private static startCassandra() {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(120_000L)
    }
}
