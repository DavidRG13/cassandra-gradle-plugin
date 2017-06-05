package com.williamhill.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import static com.williamhill.gradle.CassandraUnit.startCassandra

class GradleCassandraPlugin implements Plugin<Project> {

    private static final String CASSANDRA_UNIT = "cassandra-unit-3.1.4.0-SNAPSHOT"
    private static final String CASSANDRA_STARTER = "sh " + CASSANDRA_UNIT + "/bin/cu-starter"
    static final String PLUGIN_EXTENSION_NAME = 'cassandra'
    static final String TASK_GROUP_NAME = 'Cassandra'
    static private Process cassandraProcess

    @Override
    void apply(final Project project) {
        configureTaskProperties(project)
        addStartEmbeddedCassandraTask(project)
        addStopEmbeddedCassandraTask(project)

        extendAllTasksWithCassandraOptions(project)

        project.afterEvaluate {
            configureTasksRequiringCassandra(project)
        }
    }

    private static void configureTaskProperties(Project project) {
        project.extensions.create(PLUGIN_EXTENSION_NAME, GradleCassandraPluginExtension)
    }

    private static void addStartEmbeddedCassandraTask(Project project) {
        project.task(group: TASK_GROUP_NAME, description: 'Start an embedded Cassandra instance', 'startCassandra2').doFirst {
            startCassandraFromProject(project)
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
                    startCassandraFromProject(project)
                }
            }
        }
    }

    private static startCassandraFromProject(final Project project) {
        def pluginExt = project[PLUGIN_EXTENSION_NAME] as GradleCassandraPluginExtension
        def port = pluginExt.port
        if (pluginExt.schemaFilePath == null || pluginExt.schemaFilePath.isEmpty()) {
            System.out.println("SchemaFilePath has to be provided");
        } else if (CassandraUnit.portIsNotListening(port)) {
            startCassandra(port, pluginExt.timeout, pluginExt.schemaFilePath, pluginExt.cassandraUnit)

            while (CassandraUnit.portIsNotListening(port)) {
                System.out.println("Starting...")
                try {
                    Thread.sleep(750)
                } catch (InterruptedException e) {
                    e.printStackTrace()
                }
            }
        }
    }

    private static stopCassandra() {
        CassandraUnit.stopCassandra()
    }
}
