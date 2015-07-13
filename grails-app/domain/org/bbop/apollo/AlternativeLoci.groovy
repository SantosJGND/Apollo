package org.bbop.apollo

class AlternativeLoci {

    static constraints = {
    }


    String name
    long start
    long end

    static mapping = {
        end column: "sequence_end"
        start column: "sequence_start"
    }
}
