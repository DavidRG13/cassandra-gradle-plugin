package com.williamhill.gradle

import org.cassandraunit.utils.EmbeddedCassandraServerHelper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class GradleCassandraPlugin implements Plugin<Project> {

    static final String PLUGIN_EXTENSION_NAME = 'cassandra'
    static final String TASK_GROUP_NAME = 'Cassandra'

    @Override
    void apply(final Project project) {
        addStartEmbeddedCassandraTask(project)

        extendAllTasksWithCassandraOptions(project)

        project.afterEvaluate {
            configureTasksRequiringCassandra(project)
        }
    }

    private static void addStartEmbeddedCassandraTask(Project project) {
        project.task(group: TASK_GROUP_NAME, description: 'Start an embedded Cassandra instance', 'startCassandra').doFirst {
            startCassandra()
        }
    }

    private static void extendAllTasksWithCassandraOptions(Project project) {
        project.tasks.each {
            extend(it)
        }

        project.tasks.whenTaskAdded {
            extend(it)
        }
    }

    private static void extend(Task task) {
        task.ext.runWithCassandra = false
        task.extensions.add(PLUGIN_EXTENSION_NAME, GradleCassandraPluginExtension)
    }

    private static Iterable<Task> configureTasksRequiringCassandra(Project project) {
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
        try {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra('/cassandra.yaml', 120_000L)
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
