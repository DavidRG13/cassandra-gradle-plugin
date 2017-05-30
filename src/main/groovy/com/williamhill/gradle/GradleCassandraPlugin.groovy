package com.williamhill.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class GradleCassandraPlugin implements Plugin<Project> {

    private static final String CASSANDRA_UNIT = "cassandra-unit-3.1.4.0-SNAPSHOT";
    private static final String CASSANDRA_STARTER = "sh " + CASSANDRA_UNIT + "/bin/cu-starter";
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
        def pluginExtension = project[PLUGIN_EXTENSION_NAME] as GradleCassandraPluginExtension
        startCassandra(pluginExtension.port, pluginExtension.timeout)
    }

    static startCassandra(final int port, final long timeout) {
        def path = getClass().getResource("/startCassandra.sh").path
        try {
            new ProcessBuilder("sh ${path}").start()
            // sh cassandra-unit-3.1.4.0-SNAPSHOT/bin/cu-starter -p 9042 -t 20000 -s cassandra-unit-3.1.4.0-SNAPSHOT/samples/schema.cql -d cassandra-unit-3.1.4.0-SNAPSHOT
            def command = "${CASSANDRA_STARTER} -p ${port} -t ${timeout} -s ${CASSANDRA_UNIT}/samples/schema.cql -d ${CASSANDRA_UNIT}"
            cassandraProcess = new ProcessBuilder(command).start()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }

    private static stopCassandra() {
        cassandraProcess.destroy()
    }
}
