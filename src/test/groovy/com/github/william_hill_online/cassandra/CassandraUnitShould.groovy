package com.github.william_hill_online.cassandra

import spock.lang.Specification

class CassandraUnitShould extends Specification {

    def 'return true if port is listening'() {
        given:
        def port = 9044
        def socket = new ServerSocket(port)

        expect:
        CassandraUnit.portIsListening(port)
        socket.close()
    }

    def 'return false if port is not listening'() {
        expect:
        CassandraUnit.portIsNotListening(9043)
    }
}
