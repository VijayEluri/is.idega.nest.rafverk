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
<builder:page id="builderpage_581" template="93">
<builder:region id="left" label="left">

<h:form id="form1" styleClass="citizenForm">
<f:verbatim>
<script type="text/javascript" src="/dwr/interface/NestService.js"><!-- dont remove --></script>

<script type="text/javascript" src="/dwr/engine.js"><!-- dont remove --></script>

<script type='text/javascript' src='/dwr/util.js'><!-- dont remove --></script>

<script type="text/javascript">

var image1 = new Image();
image1.src = '/idegaweb/bundles/com.idega.core.bundle/resources/loading_notext.gif';

var image2 = new Image();
image2.src='/idegaweb/bundles/com.idega.core.bundle/resources/style/images/transparent.png';

var image3 = new Image();
image3.src='/idegaweb/bundles/com.idega.core.bundle/resources/style/images/whitetransparent.png';


function useLoadingMessage(message) {
var loadingMessage;
if (message) loadingMessage = message;
else loadingMessage = "please wait";

dwr.engine.setPreHook(function() {
var disabledZone = $('busybuddy');
if (!disabledZone) {
var outer = document.createElement('div');
outer.setAttribute('id', 'busybuddy');
outer.setAttribute('class', 'LoadLayer');
//IE class workaround:
outer.setAttribute('className', 'LoadLayer');

var middle = document.createElement('div');
middle.setAttribute('id', 'busybuddy-middle');
middle.setAttribute('class', 'LoadLayerMiddle');
//IE class workaround:
middle.setAttribute('className', 'LoadLayerMiddle');
outer.appendChild(middle);

var inner = document.createElement('div');
inner.setAttribute('id', 'busybuddy-contents');
inner.setAttribute('class', 'LoadLayerContents');
//IE class workaround:
inner.setAttribute('className', 'LoadLayerContents');
middle.appendChild(inner);

var image = document.createElement('img');
image.setAttribute('id', 'loadingimage');
image.setAttribute('src',image1.src);
image.src=image1.src;
inner.appendChild(image);

var span = document.createElement('span');
span.setAttribute('id', 'loadingtext');
inner.appendChild(span);

var text = document.createTextNode("Loading");
span.appendChild(text);

var bodyArray = document.getElementsByTagName('body');
var bodyTag = bodyArray[0];
bodyTag.appendChild(outer);
//alert('bodyTag:'+bodyTag);
}
else {

disabledZone.style.visibility = 'visible';
}
});

dwr.engine.setPostHook(function() {
$('busybuddy').style.visibility = 'hidden';
});
}



function init() {
useLoadingMessage();
}

if (window.addEventListener) {
window.addEventListener("load", init, false);
}
else if (window.attachEvent) {
window.attachEvent("onload", init);
}
else {
window.onload = init;
}



function updateStreets() {
var postalCodeDrop = document.getElementById("form1:postnumerDrop");
NestService.getStreetsByPostalCode(postalCodeDrop.options[postalCodeDrop.selectedIndex].value, {
callback:changeStreets,
errorHandler:function(message) { alert("Error: " + message); }
});
}

function changeStreets(data) {
dwr.util.removeAllOptions("form1:gotuDrop");
dwr.util.addOptions("form1:gotuDrop", data);
var streetNumberFreeTextLabel = document.getElementById("form1:gotunumerLabel");
var streetNumberFreeText = document.getElementById("form1:gotunumer")
var streetNumberDropDownLabel = document.getElementById("form1:streetNumberDropLabel");
var streetNumberDropDown = document.getElementById("form1:streetNumberDrop");
var streetDrop = document.getElementById("form1:gotuDrop");
if (streetDrop.options.length == 1) {
var valueStreet = streetDrop.options[0].value;
streetNumberFreeTextLabel.style.display="block"
streetNumberFreeText.style.display="block"
streetNumberDropDownLabel.style.display="none";
streetNumberDropDown.style.display="none";
<!--
if (valueStreet != "") {
streetNumberLabel.innerHTML = "Frjáls texti"
}
else {
streetNumberLabel.innerHTML = ""
}
-->
}
else {
streetDrop.selectedIndex = 1;
streetNumberFreeTextLabel.style.display="none"
streetNumberFreeText.style.display="none"
streetNumberDropDownLabel.style.display="block";
streetNumberDropDown.style.display="block";
}
}

function updateStreetNumberLabel() {
var streetNumberFreeTextLabel = document.getElementById("form1:gotunumerLabel");
var streetNumberFreeText = document.getElementById("form1:gotunumer")
var streetNumberDropDownLabel = document.getElementById("form1:streetNumberDropLabel");
var streetNumberDropDown = document.getElementById("form1:streetNumberDrop");
var streetDrop = document.getElementById("form1:gotuDrop");
var selection = streetDrop.options[streetDrop.selectedIndex].value;
if (selection == "none_street") {
streetNumberFreeTextLabel.style.display="block"
streetNumberFreeText.style.display="block"
streetNumberDropDownLabel.style.display="none";
streetNumberDropDown.style.display="none";
}
else {
streetNumberFreeTextLabel.style.display="none"
streetNumberFreeText.style.display="none"
streetNumberDropDownLabel.style.display="block";
streetNumberDropDown.style.display="block";
}
}

function updateRealEstates() {
var postalCodeDrop = document.getElementById("form1:postnumerDrop");
var streetDrop = document.getElementById("form1:gotuDrop");
var freetext = document.getElementById("form1:gotunumer");
var streetNumberDrop = document.getElementById("form1:streetNumberDrop");
var postalCodeSelection = postalCodeDrop.options[postalCodeDrop.selectedIndex].value;
var streetSelection = streetDrop.options[streetDrop.selectedIndex].value;
var freetextValue = freetext.value;
var streetNumber = streetNumberDrop.options[streetNumberDrop.selectedIndex].value;
NestService.getRealEstatesByPostalCodeStreetStreetNumber(postalCodeSelection, streetSelection, streetNumber, freetextValue,
{callback:changeRealEstates,
errorHandler:function(message) { alert("Error: " + message); }
});
}

function changeRealEstates(data) {
dwr.util.removeAllOptions("form1:fasteignirDrop");
dwr.util.addOptions("form1:fasteignirDrop", data);
}

function updateEnergyConsumerFields() {
var realEstateDrop = document.getElementById("form1:fasteignirDrop");
NestService.getEnergyConsumerFields(realEstateDrop.options[realEstateDrop.selectedIndex].value,
{callback:changeEnergyConsumerFields,
errorHandler:function(message) { alert("Error: " + message); }
});
}

function changeEnergyConsumerFields(data) {
var realEstateDisplay = document.getElementById("form1:veitustadurDisplay");
var energyConsumer = document.getElementById("form1:orkukaupandi");
var personalIDEnergyConsumer = document.getElementById("form1:kennitalaOrkukaupanda");
var currentWorkingPlaceErrorMessage = document.getElementById("form1:currentWorkingPlaceErrorMessage");
var changeElectrician = document.getElementById("form1:changeElectrician");
var checkOutWorkingPlaceButton = document.getElementById("form1:checkOutWorkingPlaceButton");
realEstateDisplay.innerHTML = data[0];
energyConsumer.value = data[1];
personalIDEnergyConsumer.value = data[2];
currentWorkingPlaceErrorMessage.innerHTML = data[3];
checkOutWorkingPlaceButton.disabled=(data[3]!='' || data[0]=='');
changeElectrician.style.display=data[4];
}

</script>
</f:verbatim>
<!--div class="generalContent"-->

<f:verbatim><h1 class="applicationHeading">Þjónustubeiðni</h1></f:verbatim>

<wf:container styleClass="header">

<f:verbatim><h1>1. Upplýsingar um neysluveitu
</h1></f:verbatim>

<!-- phases -->
<wf:container styleClass="phases">

<f:verbatim>
<!-- ul -->
<ul>
<li class="current">1
</li>
<li>2
</li>
<li>3
</li>
</ul>
<!-- end of ul -->
</f:verbatim>

</wf:container>
<!-- end of phases -->

</wf:container>
<!-- end of header -->

<!-- form section -->
<wf:container styleClass="info">

<wf:container styleClass="personInfo" id="name">
<h:outputText value="#{TilkynningVertakaBean.rafverktaka.rafverktaki.nafn}"/>
</wf:container>

</wf:container>
<!-- end of formsection-->

<f:verbatim>
<h1 class="subHeader topSubHeader">Grunnskráning veitu
</h1>
</f:verbatim>

<!-- form section -->
<wf:container styleClass="formSection">

<wf:container styleClass="helperText">
<f:verbatim>
Upplysingar um rafveitu/orkuveitu
</f:verbatim>
</wf:container>

<wf:container
rendered="#{TilkynningVertakaBean.newOwnerOfCase != null}"
styleClass="formItem">
<h:outputText
rendered="#{TilkynningVertakaBean.newOwnerOfCase != null}"
value="Þetta mál er núna í vinnslu hjá: "/>
</wf:container>
<wf:container styleClass="formItem">
<h:outputText
rendered="#{TilkynningVertakaBean.newOwnerOfCase != null}"
id="newOwner" value="#{TilkynningVertakaBean.newOwnerOfCase.nafn}"/>
</wf:container>

<wf:container styleClass="formItem required">
<h:outputLabel for="orkuveituDrop" value="Orkuveitufyrirtæki"/>
<h:selectOneMenu
disabled="#{! TilkynningVertakaBean.applicationStorable}"
id="orkuveituDrop" value="#{TilkynningVertakaBean.orkuveitufyrirtaeki}">
<f:selectItems value="#{TilkynningVertakaBean.rafveituListiSelects}">
<!-- f:selectItem itemLabel="Orkuveita Reykjavíkur" itemValue="OR"/>
<f:selectItem itemLabel="Hitaveita Sudurnesja" itemValue="HS"/-->
</f:selectItems>
</h:selectOneMenu>
</wf:container>

</wf:container>
<!-- end of formsection-->

<!-- form section -->
<wf:container styleClass="formSection">

<wf:container styleClass="helperText">
<f:verbatim>
Ytri gögn
</f:verbatim>
</wf:container>

<wf:container styleClass="formItem">
<h:outputLabel for="externalProjectID" value="Verknúmer"/>
<h:inputText
disabled="#{! TilkynningVertakaBean.applicationStorable}"
id="externalProjectID" value="#{TilkynningVertakaBean.externalProjectID}"/>
</wf:container>

<wf:container styleClass="formItem">
<h:outputLabel for="personInCharge" value="Umsjónarmaður"/>
<h:inputText
disabled="#{! TilkynningVertakaBean.applicationStorable}"
id="personInCharge" value="#{TilkynningVertakaBean.personInCharge}"/>
</wf:container>

</wf:container>
<!-- end of formsection -->

<!-- form section -->
<wf:container styleClass="formSection">

<wf:container styleClass="helperText">
<f:verbatim>
Upplysingar um rafverktaka
</f:verbatim>
</wf:container>

<wf:container styleClass="formItem floated">
<h:outputLabel for="rafverktakaFyrirtaeki" value="Rafverktakafyrirtæki"/>
<h:inputText
disabled="#{! TilkynningVertakaBean.applicationStorable}"
id="rafverktakaFyrirtaeki" value="#{TilkynningVertakaBean.rafverktaka.rafverktaki.nafnFyrirtaekis}"/>
</wf:container>

<wf:container styleClass="formItem required floated">
<h:outputLabel for="loggilturRafverktaki" value="Löggiltur rafverktaki"/>
<h:inputText
id="loggilturRafverktaki" value="#{TilkynningVertakaBean.rafverktaka.rafverktaki.nafn}"
disabled="true"/>
</wf:container>

<wf:container styleClass="formItem required floated">
<h:outputLabel for="heimilisfangRafverktaka" value="Heimilisfang"/>
<h:inputText id="heimilisfangRafverktaka"
value="#{TilkynningVertakaBean.rafverktaka.rafverktaki.heimilisfang.display}"
disabled="true"/>
</wf:container>

<wf:container styleClass="formItem required floated">
<h:outputLabel for="kennitalaRafverktaka" value="Kennitala rafverktaka"/>
<h:inputText id="kennitalaRafverktaka" value="#{TilkynningVertakaBean.rafverktaka.rafverktaki.kennitala}"
disabled="true"/>
</wf:container>

<wf:container styleClass="formItem required floated">
<h:outputLabel for="heimasimiRafverktaka" value="Heimasími"/>
<h:inputText id="heimasimiRafverktaka" value="#{TilkynningVertakaBean.rafverktaka.rafverktaki.heimasimi}"
disabled="true"/>
</wf:container>

<wf:container styleClass="formItem required floated">
<h:outputLabel for="vinnusimiRafverktaka" value="Vinnusími"/>
<h:inputText id="vinnusimiRafverktaka" value="#{TilkynningVertakaBean.rafverktaka.rafverktaki.vinnusimi}"
disabled="true"/>
</wf:container>


</wf:container>
<!-- end of formsection-->

<!-- form section -->
<wf:container styleClass="formSection">

<wf:container styleClass="helperText">
<f:verbatim>
Upplysingar um veitustað
</f:verbatim>
</wf:container>

<wf:container styleClass="formItem"
rendered="#{TilkynningVertakaBean.invalid['workingPlace'] != null}">
<h:outputText
style="#{TilkynningVertakaBean.invalid['workingPlace'] == null ? 'color:black' : 'color:red'}"
value="Veldu veitustað"/>
</wf:container>

<wf:container styleClass="formItem required">
<h:outputLabel for="postnumerDrop" value="Póstnúmer"/>
<h:selectOneMenu
disabled="#{! TilkynningVertakaBean.workingPlaceChangeable}"
id="postnumerDrop" value="#{TilkynningVertakaBean.postnumer}" onchange="updateStreets();">
<f:selectItems value="#{RafverktakaInitialdata.postnumeraListiSelects}"/>
</h:selectOneMenu>
<h:outputLabel for="gotuDrop" value="Gata" />
<h:selectOneMenu
disabled="#{! TilkynningVertakaBean.workingPlaceChangeable}"
id="gotuDrop" value="#{TilkynningVertakaBean.gata}" onchange="updateStreetNumberLabel();">
<f:selectItems value="#{TilkynningVertakaBean.gotuListiSelects}"/>
</h:selectOneMenu>
<h:outputLabel
style="#{TilkynningVertakaBean.showFreetextGotunumer}"
id="gotunumerLabel" for="gotunumer" value="Frjáls texti" />
<h:inputText
style="#{TilkynningVertakaBean.showFreetextGotunumer}"
disabled="#{! TilkynningVertakaBean.workingPlaceChangeable}"
id="gotunumer" value="#{TilkynningVertakaBean.freeText}"/>
<h:outputLabel
style="#{TilkynningVertakaBean.showStreetNumberSelects}"
id="streetNumberDropLabel" for="streetNumberDrop" value="Götunúmer" />
<h:selectOneMenu
style="#{TilkynningVertakaBean.showStreetNumberSelects}"
disabled="#{! TilkynningVertakaBean.workingPlaceChangeable}"
id="streetNumberDrop" value="#{TilkynningVertakaBean.streetNumber}">
<f:selectItems value="#{RafverktakaInitialdata.streetNumberSelects}"/>
</h:selectOneMenu>
</wf:container>

<wf:container
rendered="#{TilkynningVertakaBean.workingPlaceChangeable}"
styleClass="formItem required">
<f:verbatim><input type="button" name="Text 1" value="Fletta upp í Landskrá Fasteigna" onclick="updateRealEstates();"></input></f:verbatim>
<!--h:commandButton id="flettaUppIFasteignaskraButton" action="#{TilkynningVertakaBean.flettaUppIFasteignaskra}" value="Fletta upp í Landskrá Fasteigna"/-->
<!--h:selectOneMenu id="fasteignirDrop" rendered="#{TilkynningVertakaBean.availablefasteign}" value="#{TilkynningVertakaBean.fastanumer}" onchange="submit();"-->
<h:selectOneMenu
disabled="#{! TilkynningVertakaBean.applicationStorable}"
style="width:550px"
id="fasteignirDrop" value="#{TilkynningVertakaBean.fastanumer}" onclick="updateEnergyConsumerFields();" >
<f:selectItems value="#{TilkynningVertakaBean.fasteignaListiSelects}"/>
</h:selectOneMenu>
</wf:container>
<wf:container styleClass="formItem">
<h:outputText id="veitustadurDisplay" value="#{TilkynningVertakaBean.veitustadurDisplay}" />
</wf:container>

<wf:container styleClass="formItem">
<h:outputText
id="currentWorkingPlaceErrorMessage"
style="color:red" value="#{TilkynningVertakaBean.currentWorkingPlaceErrorMessage}"/>
</wf:container>

<wf:container styleClass="formItem">
<h:commandButton
style="#{TilkynningVertakaBean.showChangeElectricianOption}"
id="changeElectrician"
styleClass="buttonSpan"
actionListener="#{TilkynningVertakaBean.changeElectrician}"
action="rafverktakaskipti"
value="Change Electrician"/>
</wf:container>


<wf:container styleClass="formItem required floated">
<h:outputLabel style="#{TilkynningVertakaBean.invalid['name'] == null ? 'color:black' : 'color:red'}" for="orkukaupandi" value="Nafn orkukaupanda" />
<h:inputText
disabled="#{! TilkynningVertakaBean.applicationStorable}"
id="orkukaupandi" value="#{TilkynningVertakaBean.nafnOrkukaupanda}"/>
</wf:container>

<wf:container styleClass="formItem required floated">
<h:outputLabel style="#{TilkynningVertakaBean.invalid['energyConsumerPersonalId'] == null ? 'color:black' : 'color:red'}" for="kennitalaOrkukaupanda" value="Kennitala" />
<h:inputText
disabled="#{! TilkynningVertakaBean.applicationStorable}"
id="kennitalaOrkukaupanda" value="#{TilkynningVertakaBean.kennitalaOrkukaupanda}"/>
</wf:container>

<wf:container styleClass="formItem required floated">
<h:outputLabel style="#{TilkynningVertakaBean.invalid['energyConsumerHomePhone'] == null ? 'color:black' : 'color:red'}" for="heimasimiOrkukaupanda" value="Heimasími"/>
<h:inputText
disabled="#{! TilkynningVertakaBean.applicationStorable}"
id="heimasimiOrkukaupanda" value="#{TilkynningVertakaBean.heimasimiOrkukaupanda}"/>
</wf:container>

<wf:container styleClass="formItem required floated">
<h:outputLabel style="#{TilkynningVertakaBean.invalid['energyConsumerWorkPhone'] == null ? 'color:black' : 'color:red'}" for="vinnusimiOrkukaupanda" value="Vinnusími" />
<h:inputText
disabled="#{! TilkynningVertakaBean.applicationStorable}"
id="vinnusimiOrkukaupanda" value="#{TilkynningVertakaBean.vinnusimiOrkukaupanda}"/>
</wf:container>

</wf:container>
<!-- end form section -->

<wf:container styleClass="bottom">

<h:commandButton
styleClass="buttonSpan"
id="nextButton"
action="next"
value="Áfram"/>

<h:outputText value=" "/>

<h:commandButton
styleClass="buttonSpan"
id="storeButton"
disabled="#{! TilkynningVertakaBean.applicationStorable}"
action="#{TilkynningVertakaBean.store}"
value="Geyma"/>

<h:outputText value=" "/>

<h:commandButton
styleClass="buttonSpan"
id="checkOutWorkingPlaceButton"
disabled="#{! TilkynningVertakaBean.checkingOutWorkingPlaceAllowed}"
action="#{TilkynningVertakaBean.checkOutWorkingPlace}"
value="Taka verk"/>
</wf:container>
</h:form>



</builder:region>
</builder:page>
</f:view>
</jsp:root>