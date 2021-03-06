<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
xmlns="http://www.w3.org/1999/xhtml"
xmlns:h="http://java.sun.com/jsf/html"
xmlns:f="http://java.sun.com/jsf/core"
xmlns:builder="http://xmlns.idega.com/com.idega.builder"
xmlns:x="http://myfaces.apache.org/tomahawk"
xmlns:wf="http://xmlns.idega.com/com.idega.webface"
version="1.2">
<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>

<f:view>
<builder:page id="builderpage_424242" template="93">
<builder:region id="left" label="left">
<h:form id="form1" styleClass="rafverk">

<script type="text/javascript">

function submitFilter() {
document.getElementById("form1:filterVerktakaStatusesButton").click();
}
</script>


<wf:container styleClass="formSection">
<!--h:outputText value="Mínar rafverktökur:"/-->
<f:verbatim><h2>Mínar rafverktökur:</h2></f:verbatim>
</wf:container>

<wf:container styleClass="formSection">
<h:commandButton
id="welcome_1"
action="#{TilkynningVertakaBean.startTilkynningVertaka}"
immediate="true"
value="Stofna þjónustubeiðni"/>

<h:commandButton
id="welcome_2"
action="#{TilkynningVertakaBean.startTilkynningLokVerks}"
immediate="true"
value="Skrá skýrslu um neysluveitu"/>

<h:commandButton
id="welcome_3"
action="rafverktakaskipti"
immediate="true"
value="Ósk um rafverktakaskipti"/>
</wf:container>

<wf:container styleClass="formSection">
<wf:container styleClass="formItem">
<h:outputLabel for="filterVerktakaStatuses" value="Staða verks"/>

<h:selectOneMenu id="filterVerktakaStatuses" value="#{RafverktokuListi.selectedStatus}" 
onchange="submitFilter()"
valueChangeListener="#{RafverktokuListi.resetList}">
<f:selectItems value="#{RafverktokuListi.possibleStatusesSelects}"/>
</h:selectOneMenu>

<wf:container>
<h:outputText value="Hægt er að nota algildistákn (*,?) í leitarskilyrðum"/>
</wf:container>

<h:outputLabel for="verknumer" value="Verknúmer"/>
<h:inputText id="verknumer"
valueChangeListener="#{RafverktokuListi.resetList}"
value="#{RafverktokuListi.searchForExternalProjectID}"/>

<h:outputLabel for="orkukaupandi" value="Orkukaupanði"/>
<h:inputText id="orkukaupandi"
valueChangeListener="#{RafverktokuListi.resetList}"
value="#{RafverktokuListi.searchForEnergyConsumer}"/>

<h:outputLabel for="veitustadur" value="Veitustaður"/>
<h:inputText id="veitustadur"
valueChangeListener="#{RafverktokuListi.resetList}"
value="#{RafverktokuListi.searchForRealEstate}"/>
</wf:container>

<h:commandButton id="filterVerktakaStatusesButton"
action="#{RafverktokuListi.doFilter}"
value="Finna"/>

</wf:container>
<wf:container styleClass="formSection">

<h:selectOneMenu value="#{RafverktokuListi.numberOfRowsPerPage}" onchange="submit()">
<f:selectItems
value="#{InitialData.numberOfRowsPerPageSelects}" />
</h:selectOneMenu>

<h:dataTable id="myTable" rows="#{RafverktokuListi.numberOfRowsPerPageAsInt}" value="#{RafverktokuListi.rafverktokur}" var="verktaka" columnClasses="oddRow evenRow" styleClass="caseTable ruler" >
<h:column>
<f:facet name="header">
<h:outputText value=""/>
</f:facet>
<h:selectOneMenu onchange="submit()" valueChangeListener="#{RafverktokuListi.populateFormsForForms}">
<f:selectItems value="#{InitialData.optionsPerElectricalInstallationListiSelects}">
</f:selectItems>
</h:selectOneMenu>
</h:column>
<h:column>
<f:facet name="header">
<h:outputText value="Verknúmer"/>
</f:facet>
<h:commandLink action="yfirlit" actionListener="#{RafverktokuListi.populateFormsForOverview}"
value="#{verktaka.externalProjectID}"/>
</h:column>
<h:column>
<f:facet name="header">
<h:outputText value="Orkukaupandi"/>
</f:facet>
<h:commandLink action="yfirlit" actionListener="#{RafverktokuListi.populateFormsForOverview}"
value="#{verktaka.orkukaupandi.nafn}"/>
</h:column>
<h:column>
<f:facet name="header">
<h:outputText value="Veitustaður"/>
</f:facet>
<h:commandLink action="yfirlit" actionListener="#{RafverktokuListi.populateFormsForOverview}"
value="#{verktaka.veitustadurDisplay}"/>
</h:column>
<h:column>
<f:facet name="header">
<h:outputText value="Staða"/>
</f:facet>
<h:commandLink action="yfirlit" actionListener="#{RafverktokuListi.populateFormsForOverview}"
value="#{verktaka.stadaDisplay}"/>
</h:column>
</h:dataTable>
<x:dataScroller
binding="#{RafverktokuListi.dataScroller}"
for="myTable"
fastStep="10"
pageIndexVar="hello1"
pageCountVar="hello2"
paginator="true"
paginatorMaxPages="9">
<f:facet name="first" ><h:outputText value="fyrsti"/>
</f:facet>
<f:facet name="last"><h:outputText value="seinasti"/>
</f:facet>
<f:facet name="previous"><h:outputText value="fyrri"/>
</f:facet>
<f:facet name="next"><h:outputText value="næsti"/>
</f:facet>
<f:facet name="fastforward"><h:outputText value="spóla áfram"/>
</f:facet>
<f:facet name="fastrewind"><h:outputText value="spóla til baka"/>
</f:facet>

</x:dataScroller>

</wf:container>

</h:form>
</builder:region>
</builder:page>
</f:view>

</jsp:root>