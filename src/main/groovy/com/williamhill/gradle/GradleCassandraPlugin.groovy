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
//        project.dependencies.create("org.apache.cassandra:cassandra-all:3.9")
//        project.dependencies.create("com.datastax.cassandra:cassandra-driver-core:3.2.0")

//        project.configurations['compile'].dependencies.add(project.dependencies.create("com.datastax.cassandra:cassandra-driver-core:3.2.0"))
        addStartEmbeddedCassandraTask(project)
        addStopEmbeddedCassandraTask(project)

        extendAllTasksWithCassandraOptions(project)

        project.afterEvaluate {
            configureTasksRequiringCassandra(project)
        }
    }

    private static void addStartEmbeddedCassandraTask(Project project) {
        project.task(group: TASK_GROUP_NAME, description: 'Start an embedded Cassandra instance', 'startCassandra').doFirst {
            startCassandra(project)
        }
    }

    private static void addStopEmbeddedCassandraTask(Project project) {
        project.task(group: TASK_GROUP_NAME, description: 'Stop an embedded Cassandra instance', 'stopCassandra').doFirst {
            stopCassandra()
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
                    startCassandra(project)
                }
            }
        }
    }

    private static startCassandra(final Project project) {
        def pluginExtension = project[PLUGIN_EXTENSION_NAME] as GradleCassandraPluginExtension
        try {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra('/cassandra.yaml', pluginExtension.timeout)
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    private static stopCassandra() {
        try {
            EmbeddedCassandraServerHelper.stopEmbeddedCassandra()
        } catch (Exception e) {
            e.printStackTrace()
        }
    }
}
