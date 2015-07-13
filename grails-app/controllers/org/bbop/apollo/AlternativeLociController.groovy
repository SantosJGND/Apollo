package org.bbop.apollo


import grails.converters.JSON
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
class AlternativeLociController {


    def addLoci() {
        log.debug "addLoci ${params}"

        AlternativeLoci al = new AlternativeLoci(
            name: params.name,
            start: params.start,
            end: params.end
        ).save()
        render ([test: "create loci success"] as JSON)
    }


        static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def permissionService

    def beforeInterceptor = {
        if(!permissionService.isAdmin()){
            forward action: "notAuthorized" ,controller: "annotator"
            return
        }
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond AlternativeLoci.list(params), model:[alternativeLociInstanceCount: AlternativeLoci.count()]
    }

    def show(AlternativeLoci alternativeLociInstance) {
        respond alternativeLociInstance
    }

    def create() {
        respond new AlternativeLoci(params)
    }

    @Transactional
    def save(AlternativeLoci alternativeLociInstance) {
        if (alternativeLociInstance == null) {
            notFound()
            return
        }

        if (alternativeLociInstance.hasErrors()) {
            respond alternativeLociInstance.errors, view:'create'
            return
        }

        alternativeLociInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'alternativeLoci.label', default: 'AlternativeLoci'), alternativeLociInstance.id])
                redirect alternativeLociInstance
            }
            '*' { respond alternativeLociInstance, [status: CREATED] }
        }
    }

    def edit(AlternativeLoci alternativeLociInstance) {
        respond alternativeLociInstance
    }

    @Transactional
    def update(AlternativeLoci alternativeLociInstance) {
        if (alternativeLociInstance == null) {
            notFound()
            return
        }

        if (alternativeLociInstance.hasErrors()) {
            respond alternativeLociInstance.errors, view:'edit'
            return
        }

        alternativeLociInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AlternativeLoci.label', default: 'AlternativeLoci'), alternativeLociInstance.id])
                redirect alternativeLociInstance
            }
            '*'{ respond alternativeLociInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(AlternativeLoci alternativeLociInstance) {

        if (alternativeLociInstance == null) {
            notFound()
            return
        }

        alternativeLociInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'AlternativeLoci.label', default: 'AlternativeLoci'), alternativeLociInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'availableStatus.label', default: 'AlternativeLoci'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
