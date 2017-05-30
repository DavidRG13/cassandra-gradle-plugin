package com.williamhill.gradle

import spock.lang.Specification

class GradleCassandraPluginTest extends Specification {

    def "dd"() {
        given:
        def dd = "dd"

        expect:
        new GradleCassandraPlugin().startCassandra(9042, 20000)
    }
}
