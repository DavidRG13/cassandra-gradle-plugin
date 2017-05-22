package com.williamhill.gradle

import spock.lang.Specification

class GradleCassandraPluginExtensionSpec extends Specification {

    def pluginExtension = new GradleCassandraPluginExtension()

    def 'timeout can be supplied as a number in seconds'() {
        given:
        def desiredTimeout = 120
        pluginExtension.timeout = desiredTimeout

        expect:
        desiredTimeout == pluginExtension.timeout
    }

    def 'timeout can be supplied as a string number in seconds'() {
        given:
        def desiredTimeout = 120
        def desiredTimeoutString = '120'
        pluginExtension.timeout = desiredTimeoutString

        expect:
        desiredTimeout == pluginExtension.timeout
    }
}
