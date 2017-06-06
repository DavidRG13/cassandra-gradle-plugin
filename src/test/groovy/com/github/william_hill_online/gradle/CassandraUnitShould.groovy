package com.github.william_hill_online.gradle

import spock.lang.Specification

class CassandraUnitShould extends Specification {

    def 'return true if port is listening'() {
        given:
        def port = 9042
        def socket = new ServerSocket(port)

        expect:
        CassandraUnit.portIsListening(port)
        socket.close()
    }

    def 'return false if port is not listening'() {
        expect:
        CassandraUnit.portIsNotListening(9042)
    }
}
