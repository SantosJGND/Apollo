package org.bbop.apollo


import grails.converters.JSON

class AlternativeLociController {

    def index() {
        log.debug "altLoci controller index called"
        render ([error: "index call undefined"] as JSON)
    }


    def addLoci() {
        log.debug "addLoci ${params}"

        AlternativeLoci al = new AlternativeLoci(
            name: params.name,
            start: params.start,
            end: params.end
        )
        render ([test: "create loci success"] as JSON)
    }
}
