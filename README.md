[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.william-hill-online/cassandra-gradle-plugin/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.github.william-hill-online/cassandra-gradle-plugin)
## Gradle Cassandra Plugin ##

The Gradle Cassandra Plugin allows you to run an embedded instance of Cassandra from your gradle build. It is based on the [Cassandra Unit](https://github.com/jsevellec/cassandra-unit).

Plugin is available on the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/com.williamhill.cassandra).

### Usage ###

Enable the plugin in your gradle build:

```groovy
plugins {
  id 'com.williamhill.cassandra' version '0.4'
}
```

You can now declare a dependency on a running Cassandra instance from any of your tasks:

```groovy
task integrationTest(type: Test) {
    runWithCassandra = true
}

```

### Configuration ###

Configure your Cassandra instance inside a ```cassandra``` block:

```
cassandra {
    port 9042
    schemaFilePath "src/test/resources/schema.cql"
    ...
}
```

The `cassandra` configuration block can be declared at either the project or the task level. Task-level configuration inherits from any project-level configuration provided.

The following properties are configurable:

* ```timeout``` Timeout for Cassandra to start (defaults to **'20000'**)
* ```port```: The port where Cassandra will be listening (defaults to **'9042'**)
* ```cassandraUnit```: The URL from where [CassandraUnit](https://github.com/jsevellec/cassandra-unit) binary artifact will be downloaded (defaults to **'https://github.com/William-Hill-Online/cassandra-unit/releases/download/SNAPSHOT/cassandra-unit-3.1.4.0-SNAPSHOT-bin.tar.gz'**). Useful of you are in a net with restricted access.
* ```schemaFilePath```: Schema that will be created when Cassandra is up. **'This is required'**. If you need to create more than one, separate them with a ",".

### Tasks ###

For your convenience the plugin also adds the following tasks to your buildscript:

```
$ gradle tasks
...
Cassandra tasks
-----------
startCassandra - Starts a local Cassandra instance
stopCassandra - Stops the local Cassandra instance
...
```

## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
