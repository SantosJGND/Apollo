package org.bbop.apollo

import org.bbop.apollo.sequence.DownloadFile
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import java.util.zip.GZIPOutputStream
import org.springframework.http.HttpStatus
import org.bbop.apollo.gwt.shared.PermissionEnum

class IOServiceController extends AbstractApolloController {
    
    def sequenceService
    def featureService
    def gff3HandlerService
    def fastaHandlerService
    def preferenceService
    def permissionService

    //
    // this is a map of uuid / filename
    // see #464
    private Map<String,DownloadFile> fileMap = new HashMap<>()

    def index() { }
    
    def handleOperation(String track, String operation) {
        log.debug "Requested parameterMap: ${request.parameterMap.keySet()}"
        log.debug "upstream params: ${params}"
        JSONObject postObject = findPost()
        //operation = postObject.get(REST_OPERATION)
        //TODO: Currently not using the findPost()
        def mappedAction = underscoreToCamelCase(operation)
        forward action: "${mappedAction}", params: params
    }
    
    def write() {
        try {
            log.debug("params to IOService::write(): ${params}")
            log.debug "export sequences ${request.JSON} -> ${params}"
            JSONObject dataObject = (request.JSON ?: params) as JSONObject
            if(params.data) dataObject=JSON.parse(params.data)
            log.debug "data ${dataObject}"
            if(!permissionService.hasPermissions(dataObject, PermissionEnum.READ)){
                render status: HttpStatus.UNAUTHORIZED
                return
            }
            String typeOfExport = dataObject.type
            String sequenceType = dataObject.seqType
            String exportAllSequences = dataObject.exportAllSequences
            String exportGff3Fasta = dataObject.exportGff3Fasta
            String output = dataObject.output
            String format = dataObject.format
            log.debug "${dataObject.sequences}"
            def sequences = dataObject.sequences // can be array or string
            Organism organism = dataObject.organism?Organism.findByCommonName(dataObject.organism):preferenceService.getCurrentOrganismForCurrentUser()
            log.debug "JERE ${typeOfExport} ${output} ${sequences}"

            def sequenceList
            if (exportAllSequences == "true") {
                // HQL for all sequences
                sequenceList = Sequence.executeQuery("select distinct s from Sequence s join s.featureLocations fl where s.organism = :organism order by s.name asc ",[organism: organism])
            } else {
                // HQL for a single sequence or selected sequences
                sequenceList = Sequence.executeQuery("select distinct s from Sequence s join s.featureLocations fl where s.organism = :organism and s.name in (:sequenceNames) order by s.name asc ", [sequenceNames: sequences,organism: organism])
            }
            log.debug "# of sequences to export ${sequenceList.size()}"

            List<String> ontologyIdList = [Gene.class.name]
            List<String> alterationTypes = [Insertion.class.canonicalName, Deletion.class.canonicalName, Substitution.class.canonicalName]
            List<Feature> listOfFeatures = new ArrayList<>()
            List<Feature> listOfSequenceAlterations = new ArrayList<>()

            if(sequenceList){
                listOfFeatures.addAll(Feature.executeQuery("select distinct f from FeatureLocation fl join fl.sequence s join fl.feature f where s in (:sequenceList) and fl.feature.class in (:ontologyIdList) order by f.name asc", [sequenceList: sequenceList, ontologyIdList: ontologyIdList]))
            }
            else{
                log.warn "There are no annotations to be exported in this list of sequences ${sequences}"
            }
            File outputFile = File.createTempFile("Annotations", "." + typeOfExport.toLowerCase())
            String fileName

            if (typeOfExport == "GFF3") {
                // adding sequence alterations to list of features to export
                listOfSequenceAlterations = Feature.executeQuery("select f from Feature f join f.featureLocations fl join fl.sequence s where s in :sequenceList and f.class in :alterationTypes", [sequenceList: sequenceList, alterationTypes: alterationTypes])
                listOfFeatures.addAll(listOfSequenceAlterations)
                if(exportAllSequences!="true"&&sequences!=null&&!(sequences.class == JSONArray.class)) {
                    fileName = "Annotations-" + sequences + "." + typeOfExport.toLowerCase() + (format=="gzip"?".gz":"")
                }
                else {
                    fileName = "Annotations" + "." + typeOfExport.toLowerCase() + (format=="gzip"?".gz":"")
                }
                // call gff3HandlerService
                if (exportGff3Fasta == "true") {
                    gff3HandlerService.writeFeaturesToText(outputFile.path, listOfFeatures, grailsApplication.config.apollo.gff3.source as String, true, sequenceList)
                } else {
                    gff3HandlerService.writeFeaturesToText(outputFile.path, listOfFeatures, grailsApplication.config.apollo.gff3.source as String)
                }
            } else if (typeOfExport == "FASTA") {
                if(exportAllSequences!="true"&&sequences!=null&&!(sequences.class == JSONArray.class)) {
                    fileName = "Annotations-" + sequences + "." + sequenceType + "." + typeOfExport.toLowerCase() + (format=="gzip"?".gz":"")
                }
                else {
                    fileName = "Annotations" + "." + sequenceType + "." + typeOfExport.toLowerCase() + (format=="gzip"?".gz":"")
                }

                // call fastaHandlerService
                fastaHandlerService.writeFeatures(listOfFeatures, sequenceType, ["name"] as Set, outputFile.path, FastaHandlerService.Mode.WRITE, FastaHandlerService.Format.TEXT)
            }


            //generating a html fragment with the link for download that can be rendered on client side
            String uuidString = UUID.randomUUID().toString()
            DownloadFile downloadFile = new DownloadFile(
                    uuid: uuidString
                    ,path: outputFile.path
                    ,fileName: fileName
            )
            log.debug "${uuidString}"
            fileMap.put(uuidString,downloadFile)

            if(output=="file") {

                def jsonObject = [
                    "uuid":uuidString,
                    "exportType": typeOfExport,
                    "seqType": sequenceType,
                    "format": format,
                    "filename": fileName
                ]
                render jsonObject as JSON
            }
            else {
                render text: outputFile.text
            }
        }
        catch(Exception e) {
            def error=[error: e.message]
            e.printStackTrace()
            render error as JSON
        }
    }
    
    def download() {
        String uuid = params.uuid
        DownloadFile downloadFile = fileMap.remove(uuid)
        def file
        if(downloadFile) {
            file = new File(downloadFile.path)
            if (!file.exists()) {
                render text: "Error: file does not exist"
                return
            }
        }
        else {
            render text: "Error: uuid did not map to file. Please try to re-download"
            return
        }

        response.setHeader("Content-disposition", "attachment; filename=${downloadFile.fileName}")
        if(params.format=="gzip") {
            new GZIPOutputStream(response.outputStream).withWriter{ it << file.text }
        }
        else {
            def outputStream = response.outputStream
            outputStream << file.text
            outputStream.flush()
            outputStream.close()
        }

        file.delete()
    }
}
