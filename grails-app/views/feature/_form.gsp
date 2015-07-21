<%@ page import="org.bbop.apollo.Feature" %>



<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="feature.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${featureInstance?.name}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'uniqueName', 'error')} required">
	<label for="uniqueName">
		<g:message code="feature.uniqueName.label" default="Unique Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="uniqueName" required="" value="${featureInstance?.uniqueName}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'dbxref', 'error')} ">
	<label for="dbxref">
		<g:message code="feature.dbxref.label" default="Dbxref" />
		
	</label>
	<g:select id="dbxref" name="dbxref.id" from="${org.bbop.apollo.DBXref.list()}" optionKey="id" value="${featureInstance?.dbxref?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'sequenceLength', 'error')} ">
	<label for="sequenceLength">
		<g:message code="feature.sequenceLength.label" default="Sequence Length" />
		
	</label>
	<g:field name="sequenceLength" type="number" value="${featureInstance.sequenceLength}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'md5checksum', 'error')} ">
	<label for="md5checksum">
		<g:message code="feature.md5checksum.label" default="Md5checksum" />
		
	</label>
	<g:textField name="md5checksum" value="${featureInstance?.md5checksum}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'isAnalysis', 'error')} ">
	<label for="isAnalysis">
		<g:message code="feature.isAnalysis.label" default="Is Analysis" />
		
	</label>
	<g:checkBox name="isAnalysis" value="${featureInstance?.isAnalysis}" />

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'isObsolete', 'error')} ">
	<label for="isObsolete">
		<g:message code="feature.isObsolete.label" default="Is Obsolete" />
		
	</label>
	<g:checkBox name="isObsolete" value="${featureInstance?.isObsolete}" />

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'symbol', 'error')} ">
	<label for="symbol">
		<g:message code="feature.symbol.label" default="Symbol" />
		
	</label>
	<g:textField name="symbol" value="${featureInstance?.symbol}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="feature.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${featureInstance?.description}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'status', 'error')} ">
	<label for="status">
		<g:message code="feature.status.label" default="Status" />
		
	</label>
	<g:select id="status" name="status.id" from="${org.bbop.apollo.Status.list()}" optionKey="id" value="${featureInstance?.status?.id}" class="many-to-one" noSelection="['null': '']"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'childFeatureRelationships', 'error')} ">
	<label for="childFeatureRelationships">
		<g:message code="feature.childFeatureRelationships.label" default="Child Feature Relationships" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${featureInstance?.childFeatureRelationships?}" var="c">
    <li><g:link controller="featureRelationship" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="featureRelationship" action="create" params="['feature.id': featureInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'featureRelationship.label', default: 'FeatureRelationship')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'featureCVTerms', 'error')} ">
	<label for="featureCVTerms">
		<g:message code="feature.featureCVTerms.label" default="Feature CVT erms" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${featureInstance?.featureCVTerms?}" var="f">
    <li><g:link controller="featureCVTerm" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="featureCVTerm" action="create" params="['feature.id': featureInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'featureCVTerm.label', default: 'FeatureCVTerm')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'featureDBXrefs', 'error')} ">
	<label for="featureDBXrefs">
		<g:message code="feature.featureDBXrefs.label" default="Feature DBX refs" />
		
	</label>
	<g:select name="featureDBXrefs" from="${org.bbop.apollo.DBXref.list()}" multiple="multiple" optionKey="id" size="5" value="${featureInstance?.featureDBXrefs*.id}" class="many-to-many"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'featureGenotypes', 'error')} ">
	<label for="featureGenotypes">
		<g:message code="feature.featureGenotypes.label" default="Feature Genotypes" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${featureInstance?.featureGenotypes?}" var="f">
    <li><g:link controller="featureGenotype" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="featureGenotype" action="create" params="['feature.id': featureInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'featureGenotype.label', default: 'FeatureGenotype')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'featureLocations', 'error')} ">
	<label for="featureLocations">
		<g:message code="feature.featureLocations.label" default="Feature Locations" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${featureInstance?.featureLocations?}" var="f">
    <li><g:link controller="featureLocation" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="featureLocation" action="create" params="['feature.id': featureInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'featureLocation.label', default: 'FeatureLocation')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'featurePhenotypes', 'error')} ">
	<label for="featurePhenotypes">
		<g:message code="feature.featurePhenotypes.label" default="Feature Phenotypes" />
		
	</label>
	<g:select name="featurePhenotypes" from="${org.bbop.apollo.Phenotype.list()}" multiple="multiple" optionKey="id" size="5" value="${featureInstance?.featurePhenotypes*.id}" class="many-to-many"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'featureProperties', 'error')} ">
	<label for="featureProperties">
		<g:message code="feature.featureProperties.label" default="Feature Properties" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${featureInstance?.featureProperties?}" var="f">
    <li><g:link controller="featureProperty" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="featureProperty" action="create" params="['feature.id': featureInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'featureProperty.label', default: 'FeatureProperty')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'featurePublications', 'error')} ">
	<label for="featurePublications">
		<g:message code="feature.featurePublications.label" default="Feature Publications" />
		
	</label>
	<g:select name="featurePublications" from="${org.bbop.apollo.Publication.list()}" multiple="multiple" optionKey="id" size="5" value="${featureInstance?.featurePublications*.id}" class="many-to-many"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'featureSynonyms', 'error')} ">
	<label for="featureSynonyms">
		<g:message code="feature.featureSynonyms.label" default="Feature Synonyms" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${featureInstance?.featureSynonyms?}" var="f">
    <li><g:link controller="featureSynonym" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="featureSynonym" action="create" params="['feature.id': featureInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'featureSynonym.label', default: 'FeatureSynonym')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'owners', 'error')} ">
	<label for="owners">
		<g:message code="feature.owners.label" default="Owners" />
		
	</label>
	<g:select name="owners" from="${org.bbop.apollo.User.list()}" multiple="multiple" optionKey="id" size="5" value="${featureInstance?.owners*.id}" class="many-to-many"/>

</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'parentFeatureRelationships', 'error')} ">
	<label for="parentFeatureRelationships">
		<g:message code="feature.parentFeatureRelationships.label" default="Parent Feature Relationships" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${featureInstance?.parentFeatureRelationships?}" var="p">
    <li><g:link controller="featureRelationship" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="featureRelationship" action="create" params="['feature.id': featureInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'featureRelationship.label', default: 'FeatureRelationship')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: featureInstance, field: 'synonyms', 'error')} ">
	<label for="synonyms">
		<g:message code="feature.synonyms.label" default="Synonyms" />
		
	</label>
	<g:select name="synonyms" from="${org.bbop.apollo.Synonym.list()}" multiple="multiple" optionKey="id" size="5" value="${featureInstance?.synonyms*.id}" class="many-to-many"/>

</div>

