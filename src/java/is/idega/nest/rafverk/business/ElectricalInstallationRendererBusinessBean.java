/*
 * $Id: ElectricalInstallationRendererBusinessBean.java,v 1.6 2007/06/21 15:11:24 thomas Exp $
 * Created on Apr 11, 2007
 *
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.nest.rafverk.business;

import is.idega.nest.rafverk.bean.InitialData;
import is.idega.nest.rafverk.bean.constants.FieldID;
import is.idega.nest.rafverk.bean.validation.ElectricalInstallationValidator;
import is.idega.nest.rafverk.bean.validation.ValidationRules;
import is.idega.nest.rafverk.data.Maelir;
import is.idega.nest.rafverk.data.MaelirList;
import is.idega.nest.rafverk.domain.ElectricalInstallation;
import is.idega.nest.rafverk.util.DataConverter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import com.idega.business.IBOLookup;
import com.idega.business.IBOService;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.RealEstate;
import com.idega.core.location.data.Street;
import com.idega.fop.data.Property;
import com.idega.fop.data.PropertyImpl;
import com.idega.fop.data.PropertyTree;
import com.idega.fop.tools.PropertyWriter;
import com.idega.presentation.IWContext;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2007/06/21 15:11:24 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.6 $
 */
public class ElectricalInstallationRendererBusinessBean extends IBOServiceBean implements ElectricalInstallationRendererBusiness {
	
	private static final String OHM = "\u2126";
	
	private static final String MEGA_OHM="M\u2126";
	
	private static final String VOLT = "V";
	
	private static final String AMPERE = "A";
	
	private static final String MILLI_SECONDS = "ms";
	
	private UserBusiness userBusiness = null;
	
	private ElectricalInstallationBusiness electricalInstallationBusiness = null;
	
	private ValidationRules validationRules = null; 
	
	public String getPDFApplication(ElectricalInstallation electricalInstallation) throws IOException {
		Property prop = getApplicationProperty(electricalInstallation);
		return getPDFOutput(prop);
	}	

	public String getXMLApplication(ElectricalInstallation electricalInstallation) throws IOException {
		Property prop = getApplicationProperty(electricalInstallation);
		return getXMLOutput(prop);
	}		
	
	public String getPDFReport(ElectricalInstallation electricalInstallation) throws IOException {
		Property prop = getReportProperty(electricalInstallation);
		return getPDFOutput(prop);
	}
	
	public String getXMLReport(ElectricalInstallation electricalInstallation) throws IOException {
		Property prop = getReportProperty(electricalInstallation);
		return getXMLOutput(prop);
	}

	
	public Map validateApplication(ElectricalInstallation electricalInstallation) {
		Property property = getApplicationProperty(electricalInstallation);
		ElectricalInstallationValidator handler = new ElectricalInstallationValidator(getValidationRules());
		property.accept(handler);
		return handler.getResults();
	}
	
	private Property getApplicationProperty(ElectricalInstallation electricalInstallation) {
		PropertyTree prop = new PropertyTree("thjonustubeidni", "Þjónustubeiðni");
		prop.add(getHeadApplication(electricalInstallation))
		.add(getBodyApplication(electricalInstallation));
		return prop;
	}
	
	private Property getReportProperty(ElectricalInstallation electricalInstallation) {
		PropertyTree prop = new PropertyTree("skyrsla", "Skýrsla um neysluveitu");		
		prop.add(getHeadReport(electricalInstallation))
		.add(getBodyReport(electricalInstallation))
		.add(getBottomReport(electricalInstallation));
		return prop;
	}
	
	private String getPDFOutput(Property property) throws IOException{
		return getOutput(property, PropertyWriter.PDF_RENDERER);
	}

	private String getXMLOutput(Property property) throws IOException{
		return getOutput(property, PropertyWriter.XML_DATA_RENDERER);
	}
	
	private String getOutput(Property property, String renderer) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		IWContext iwContext = IWContext.getIWContext(context);
		PropertyWriter writerToFile = new PropertyWriter(property, iwContext);
		writerToFile.setRenderer(renderer);
		return writerToFile.createContainer();
	}
		
	// head application +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
	private Property getHeadApplication(ElectricalInstallation electricalInstallation) {
		PropertyTree prop = new PropertyTree("head", "Upplýsingar um neysluveitu");
		prop.add(getEnergyCompany(electricalInstallation));
		addHeadData(prop, electricalInstallation);
		String type = electricalInstallation.getType();
		prop.addWithValueDescription(FieldID.TYPE, "Notkunarflokkur", type, DataConverter.lookup(InitialData.NOTKUNARFLOKKUR, type));
		String currentLineModification = electricalInstallation.getCurrentLineModification();
		prop.addWithValueDescription(FieldID.CURRENT_LINE_MODIFICATION, "Heimtaug", currentLineModification,DataConverter.lookup(InitialData.HEIMTAUG, currentLineModification));
		String currentLineConnectionModification = electricalInstallation.getCurrentLineConnectionModification();
		prop.addWithValueDescription(FieldID.CURRENT_LINE_CONNECTION_MODIFICATION, "Heimtaug tengist í", currentLineConnectionModification, DataConverter.lookup(InitialData.HEIMTAUG_TENGIST, currentLineConnectionModification));
		prop.add(FieldID.HOME_LINE, 
				"Stofn/kvisl",
				electricalInstallation.getHomeLineA(),
				"x",
				electricalInstallation.getHomeLineB(),
				"+",
				electricalInstallation.getHomeLineC(),
				("mm\u00b2"));
		String switchPanelModification = electricalInstallation.getSwitchPanelModification();
		prop.addWithValueDescription(FieldID.SWITCH_PANEL_MODIFICATION, "Aðaltafla/Mælatafla", switchPanelModification, DataConverter.lookup(InitialData.ADALTAFLA, switchPanelModification))
		.add(getElectronicalProtectiveMeasures(electricalInstallation));
		return prop;
	}
	
	private void addHeadData(PropertyTree propertyTree, ElectricalInstallation electricalInstallation) {
		propertyTree.add(getElectricianData(electricalInstallation))
		.add(getExternalData(electricalInstallation))
		.add(getWorkingplaceData(electricalInstallation))
		.add(getEnergyConsumer(electricalInstallation));
	}
	
	private Property getEnergyCompany(ElectricalInstallation electricalInstallation) {
		Group energyCompany = electricalInstallation.getEnergyCompany();
		String energyCompanyString = null;
		if (energyCompany != null) {
			energyCompanyString = energyCompany.getName();
		}
		return new PropertyImpl("energyCompany","Orkuveitu",energyCompanyString);
	}
	
	private Property getExternalData(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("externalData", "Ytri gögn");
		String externalProjectID = electricalInstallation.getExternalProjectID();
		String personInCharge = electricalInstallation.getPersonInCharge();
		propertyTree.add("externalProjectID", "Verknúmer", externalProjectID);
		propertyTree.add("personInCharge", "Ábyrgðarmaður", personInCharge);
		return propertyTree;
	}
	
	private Property getElectricianData(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("electrician", "Rafverktaki");
		// user name
		User electrician = electricalInstallation.getElectrician();
		String electricianName = null, 
		electricianPersonalID = null;
		if (electrician != null) {
			electricianName = electrician.getName();
			electricianPersonalID = electrician.getPersonalID();
		}
		propertyTree.add("electricianName", "Löggiltur rafverktaki",electricianName)
		.add("personalIDElectrician", "Kennitala", electricianPersonalID)
		.add("electricianCompany", "Rafverktakafyrirtæki", electricalInstallation.getElectricianCompany());
		// user address
		PropertyTree addressProp = new PropertyTree("address", "Heimilisfang");
		UserBusiness userBusinessTemp = getUserBusiness();
		Address address = null;
		try {
			address = userBusinessTemp.getUsersCoAddress(electrician);
		}
		catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		String streetName = null,
		streetNumber = null,
		postalCodeName = null,
		postalCodeNumber = null;
		if (address != null) {
			streetName = address.getStreetAddress();
			streetNumber = address.getStreetNumber();
			PostalCode postalCode = address.getPostalCode();
			if (postalCode != null) {
				postalCodeName = postalCode.getName();
				postalCodeNumber = postalCode.getPostalCode();
			}
		}
		addressProp.add("street", "Gata", streetName)
		.add("streetNumber", "Númer", streetNumber)
		.add("postalCodeName", "Sveitarfélag", postalCodeName)
		.add("postalCodeNumber", "Póstnúmer", postalCodeNumber)
		.add(addressProp);
		// phones
		Phone homePhone = null;
		String homePhoneString = null;
		try {
			homePhone = userBusinessTemp.getUsersHomePhone(electrician);
			homePhoneString = (homePhone == null) ? null : homePhone.getNumber();
			
		}
		catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		catch (NoPhoneFoundException e) {
			homePhoneString = null;
		}
		propertyTree.add("homePhone", "Heimasimi", homePhoneString);
		Phone workPhone = null;
		String workPhoneString = null;
		try {
			workPhone = userBusinessTemp.getUsersWorkPhone(electrician);
			workPhoneString = (workPhone == null) ? null : workPhone.getNumber();
			
		}
		catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		catch (NoPhoneFoundException e) {
			workPhoneString = null;
		}
		propertyTree.add("workPhone", "Vinnusimi", workPhoneString);
		return propertyTree;
	}
	
	private Property getWorkingplaceData(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("workingplace", "Veitustaður");
		// user name
		String name = null,
		use = null,
		comment = null,
		streetName = null,
		streetNumber = null,
		postalCodeNumber = null,
		postalCodeName = null; 
		RealEstate realEstate = electricalInstallation.getRealEstate();
		if (realEstate != null) {
			name = realEstate.getName();
			use = realEstate.getUse();
			comment = realEstate.getComment();
			Street street = realEstate.getStreet();
			streetNumber = realEstate.getStreetNumber();
			if (street != null) {
				streetName = street.getName();
				PostalCode postalCode = street.getPostalCode();
				if (postalCode != null) {
					postalCodeNumber = postalCode.getPostalCode();
					postalCodeName = postalCode.getName();
				}
			}
		}	
		propertyTree.add("name", "Veitstaður", name)
		.add("use", "Nóta",  use)
		.add("comment","Skýringar", comment)
		.add("streetName", "Gata", streetName)
		.add("streetNumber", "Gatanúmer", streetNumber)
		.add("postalCodeNumber", "Póstnúmer", postalCodeNumber)
		.add("postalCodeName", "Sveitarfélag", postalCodeName);
		return propertyTree;
	}
			
	private Property getEnergyConsumer(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("energyConsumer", "Orkukaupandi");	
		propertyTree.add("name", "Orkukaupandi", electricalInstallation.getEnergyConsumerName())
		.add("energyConsumerPersonalId", "Kennitala", electricalInstallation.getEnergyConsumerPersonalID())
		.add("energyConsumerHomePhone", "Heimasími", electricalInstallation.getEnergyConsumerHomePhone())
		.add("energyConsumerWorkPhone", "Vinnusími", electricalInstallation.getEnergyConsumerWorkPhone());
		return propertyTree;
	}
	
	private Property getElectronicalProtectiveMeasures(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree(FieldID.ELECTRONICAL_PROTECTIVE_MEASURES, "Varnarráðstöfun");
		Iterator iterator = electricalInstallation.getElectronicalProtectiveMeasures().iterator();
		while (iterator.hasNext()) {
			String value = (String) iterator.next();
			propertyTree.addWithValueDescription("item", "", value, DataConverter.lookup(InitialData.VARNARRADSTOEFUN, value));
		}
		return propertyTree;
	}

	
	// body application ++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	private Property getBodyApplication(ElectricalInstallation electricalInstallation) {
		MaelirList maelirList = getMaelirList(electricalInstallation);
		PropertyTree prop = new PropertyTree("body", "Þjónustubeiðni");
		prop.add(getApplication(electricalInstallation));
		prop.add(FieldID.PLACE_METER, "Staður mælis", maelirList.getStadurMaelir().getStadur());
		prop.add(FieldID.SWITCH_PANEL_NUMBER , "Númer töflu", electricalInstallation.getSwitchPanelNumber());
		prop.add(getVoltageSystem(electricalInstallation));
		addAllMaelir(prop, maelirList);
		prop.add("applicationRemarks", "Skýringar", electricalInstallation.getApplicationRemarks());
		return prop;
	}
	
	private MaelirList getMaelirList(ElectricalInstallation electricalInstallation) {
		try {
			return getElectricalInstallationBusiness().getMaelirList(electricalInstallation);
		}
		catch (RemoteException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
		
		
	private Property getApplication(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("applicationGroup", "Beiðni um");
		PropertyTree items = new PropertyTree(FieldID.APPLICATION, "Beiðni");
		Iterator iterator = electricalInstallation.getApplication().iterator();
		while (iterator.hasNext()) {
			String value = (String) iterator.next();
			items.addWithValueDescription("item", "", value, DataConverter.lookup(InitialData.BEIDNI, value));
		}
		propertyTree.add(items);
		propertyTree.addWithUnit(FieldID.POWER, "Uppsett afl", electricalInstallation.getPower(), "kW");
		return propertyTree;
	}
	
	private Property getVoltageSystem(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("voltageSystemGroup", "Spennukerfi");
		String voltageSystem = electricalInstallation.getVoltageSystem();
		propertyTree.addWithValueDescription(FieldID.VOLTAGE_SYSTEM, "Spennukerfi", voltageSystem, DataConverter.lookup(InitialData.SPENNUKERFI, voltageSystem))
		.add(FieldID.VOLTAGE_SYSTEM_OTHER, "Annað", electricalInstallation.getVoltageSystemOther());
		return propertyTree;
	}
	
	private void addAllMaelir(PropertyTree propertyTree, MaelirList maelirList) {
		propertyTree.add(getMaelirForContextShowNumber(InitialData.TAKA,"Taka mæli", maelirList))
		.add(getMaelirForContextShowNumber(InitialData.FYRIR, "Fyrir er", maelirList))
		.add(getMaelirForContextShowPhaseAmpere(InitialData.SETJA, "Setja mæli", maelirList))
		.add(getMaelirForContextShowNumber(InitialData.FLUTT_A, "Flutt á", maelirList))
		.add(getMaelirForContextShowNumber(InitialData.FLUTT_AF, "Flutt af", maelirList))
		.add(getMaelirForContextShowDevice(InitialData.HJALPATAEKI, "Setja hjálpatæki", maelirList))
		.add(getMaelirForContextShowAmpere(InitialData.STRAUMSPENNA, "Setja straumspennamæli", maelirList));
	}

	private Property getMaelirForContextShowNumber(String context, String description, MaelirList maelirList) {
		return getMaelirForContextShowNumberDevicePhaseAmpereRate(context, description, "", true, false, false, false, false,  maelirList);
	}
	
	private Property getMaelirForContextShowDevice(String context, String description, MaelirList maelirList) {
		return getMaelirForContextShowNumberDevicePhaseAmpereRate(context, description, "", false, true, false, false, false, maelirList);
	}
	
	private Property getMaelirForContextShowAmpere(String context, String description, MaelirList maelirList) {
		return getMaelirForContextShowNumberDevicePhaseAmpereRate(context, description, "", false, false, false, true, true, maelirList);
	}
	
	private Property getMaelirForContextShowPhaseAmpere(String context, String description, MaelirList maelirList) {
		return getMaelirForContextShowNumberDevicePhaseAmpereRate(context, description, "Mælir", false, false, true, true, true, maelirList);
	}
	
	private Property getMaelirForContextShowNumberDevicePhaseAmpereRate(String context, 
			String description, 
			String itemName,
			boolean showNumber, 
			boolean showDevice, 
			boolean showPhase,
			boolean showAmpere, 
			boolean showRate,
			MaelirList maelirList) {  
		PropertyTree propertyTree = new PropertyTree(context, description);
		Map map = maelirList.getMaelirListMap();
		List list = (List) map.get(context);
		if (list == null) {
			return propertyTree;
		}
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Maelir maelir = (Maelir) iterator.next();
			if (maelir.isValid()) {
				PropertyTree maelirData = new PropertyTree("item", itemName);
				if (showNumber) {
					maelirData.add(FieldID.NUMBER, "Mæli númer", maelir.getNumer());
				}
				if (showDevice){
					maelirData.add(FieldID.DEVICE, "Hjálpatæki", maelir.getHjalpataeki());
				}
				if (showPhase) {
					String fasa = maelir.getFasa();
					maelirData.addWithValueDescription(FieldID.PHASE, "Fasa", fasa, DataConverter.lookup(InitialData.MAELI, fasa));
				}
				if (showAmpere) {
					maelirData.addWithUnit(FieldID.AMPERE, "Stærð", maelir.getAmpere(), AMPERE);
				}
				if (showRate) {
					maelirData.add(FieldID.RATE, "Taxti", maelir.getTaxti());
				}
				propertyTree.add(maelirData);
			}
		}
		return propertyTree;
	}
	
	// report rendering -------------------------------------------------------------------------------------------------------------------------------------------
	// head report ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	private Property getHeadReport(ElectricalInstallation electricalInstallation) {
		PropertyTree prop = new PropertyTree("head", "");
		addHeadData(prop, electricalInstallation);
		prop.add(getAnnouncement(electricalInstallation));
		String typeInReport = electricalInstallation.getTypeInReport();
		prop.addWithValueDescription("typeInreport", "Notkun húsnæðis",typeInReport ,DataConverter.lookup(InitialData.NOTKUNARFLOKKUR, typeInReport));
		String announcementRemarks = electricalInstallation.getAnnouncementRemarks();
		prop.add("announcementRemarks","Nánari skýring á því sem tilkynnt er",announcementRemarks);
		return prop;
	}
		
		
	private Property getAnnouncement(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("announcementGroup", "Tilkynnt er");
		String announcement = electricalInstallation.getAnnouncement();
		propertyTree.addWithValueDescription("announcement", "Tilkynnt", announcement ,DataConverter.lookup(InitialData.TILKYNNT, announcement));
		propertyTree.add("announcementOther", "Annað", electricalInstallation.getAnnouncementOther());
		return propertyTree;
	}
	
	// body report ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	private Property getBodyReport(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("bodyReport", "Upplýsingar um neysluveitu");
		propertyTree.add(getVoltageSystemInReport(electricalInstallation))
		.add(getElectronicalProtectiveMeasuresInReport(electricalInstallation))
		.add(getGrounding(electricalInstallation));
		MaelirList maelirList = getMaelirList(electricalInstallation);
		propertyTree.add(getMeter(maelirList))
		.add("remarks", "Skýringar",electricalInstallation.getRemarks());
		return propertyTree;
	}
	
	private Property getVoltageSystemInReport(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("voltageSystemInReportGroup", "Veitukerfi");
		String voltageSystemInReport = electricalInstallation.getVoltageSystemInReport();
		propertyTree.addWithValueDescription("voltageSystemInReport", "Veitukerfi",voltageSystemInReport ,DataConverter.lookup(InitialData.SPENNUKERFI, voltageSystemInReport));
		propertyTree.add("voltageSystemInReportOther", "Annað", electricalInstallation.getVoltageSystemOtherInReport());
		return propertyTree;
	}
	
	private Property getElectronicalProtectiveMeasuresInReport(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("electronicalProtectiveMeasuresInReport", "Varnarráðstöfun");
		Iterator iterator = electricalInstallation.getElectronicalProtectiveMeasuresInReport().iterator();
		while (iterator.hasNext()) {
			String value = (String) iterator.next();
			propertyTree.addWithValueDescription("item", "", value, DataConverter.lookup(InitialData.VARNARRADSTOEFUN, value));
		}
		return propertyTree;
	}
	
	private Property getGrounding(ElectricalInstallation electricalInstallation) {
		PropertyTree propTree = new PropertyTree("groundingGroup", "Jarðskaut/sp.jöfnun");
		PropertyTree propertyTree = new PropertyTree("grounding", "Jarðskaut/sp.jöfnun");
		Iterator iterator = electricalInstallation.getGrounding().iterator();
		while (iterator.hasNext()) {
			String value = (String) iterator.next();
			propertyTree.addWithValueDescription("item", "", value, DataConverter.lookup(InitialData.JARDSKAUT, value));
		}
		propTree.add(propertyTree);
		propTree.add("groundingOther", "Annað", electricalInstallation.getGroundingOther());
		return propertyTree;
	}
	
	private Property getMeter(MaelirList maelirList) {
		PropertyTree propertyTree = new PropertyTree("meterGroup", "Mælir");
		Maelir maelir = maelirList.getStadurMaelir();
		propertyTree.add("meterNumber", "Númer mælis rafveitu", maelir.getNumer())
		.add("meterPlace","Staður mælis",maelir.getStadur());
		return propertyTree;
	}
	
	// bottom report ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	private Property getBottomReport(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("bottomReport", "Mælingar og prófanir");
		propertyTree.add(getSwitchPanel(electricalInstallation))
		.add(getInsulation(electricalInstallation))
		.add(getShortCircuit(electricalInstallation))
		.add(getVoltage(electricalInstallation))
		.add(isFuseAttached(electricalInstallation))
		.add("measurementRemarks", "Skýringar", electricalInstallation.getMeasurementRemarks());
		return propertyTree;
	}
	
	
	private Property getSwitchPanel(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("switchPanel", "");
		propertyTree.addWithUnit("switchPanelResistence", "Hringrásarviðnám aðal-/greinitöflu",electricalInstallation.getSwitchPanelResistence(),OHM)
		.addWithUnit("switchPanelAmpere", "Skammhlaupsstraumur aðal-/greinitöflu",electricalInstallation.getSwitchPanelAmpere(),AMPERE);
		return propertyTree;
	}
	
	private Property getInsulation(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("insulation", "");
		propertyTree.addWithUnit("insulationResistence", "Einangrun neysluveitu",electricalInstallation.getInsulationResistence(),MEGA_OHM)
		.addWithUnit("groundingResistence", "Hringrásarviðnámsmæling jarðskauts/sp.jöfnunar",electricalInstallation.getGroundingResistence(),AMPERE);
		return propertyTree;
	}
	
	private Property getShortCircuit(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("shortCircuit", "");
		propertyTree.addWithUnit("ShortCircuitAmpere", "Skammhlaupsstraumur neysluveitu",electricalInstallation.getShortCircuitAmpere(),AMPERE)
		.addWithUnit("resistence", "Hringrásarviðnámsmæling neysluveitu",electricalInstallation.getResistence(),OHM);
		return propertyTree;
	}
	
	private Property getVoltage(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("voltage", "");
		propertyTree.addWithUnit("voltagePhaseN", "Maeld spenna Fasi-N",electricalInstallation.getVoltagePhaseN(),VOLT)
		.addWithUnit("voltagePhasePhase", "Maeld spenna Fasi-Fasi",electricalInstallation.getVoltagePhasePhase(),VOLT);
		return propertyTree;
	}
	
	private Property isFuseAttached(ElectricalInstallation electricalInstallation) {
		PropertyTree propertyTree = new PropertyTree("fuse","");
		String key = (electricalInstallation.isFuseAttached()) ? InitialData.LEKASTRAUMSROFI_I_LAGI_KEY : InitialData.LEKASTRAUMSROFI_EKKI_TIL_STADAR_KEY;
		String description = DataConverter.lookup(InitialData.LEKASTRAUMSROFI, key);
		propertyTree.addWithValueDescription("fuseAttached", "Lekastraumsrofi", key, description)
		.addWithUnit("fuseVoltage","",electricalInstallation.getFuseVoltage(),VOLT)
		.addWithUnit("fuseTime", "", electricalInstallation.getFuseTime(),MILLI_SECONDS);
		return propertyTree;
	}
	
	private UserBusiness getUserBusiness() {
		if (userBusiness == null) {
			userBusiness = (UserBusiness) getServiceBean(UserBusiness.class);
		}
		return userBusiness;
	}
	
	private ElectricalInstallationBusiness getElectricalInstallationBusiness() {
		if (electricalInstallationBusiness == null) {
			electricalInstallationBusiness = (ElectricalInstallationBusiness) getServiceBean(ElectricalInstallationBusiness.class);
		}
		return electricalInstallationBusiness;
	}
	
	private ValidationRules getValidationRules() {
		if (validationRules == null) {
			validationRules = new ValidationRules();
		}
		return validationRules;
	}
	
	private IBOService getServiceBean(Class serviceClass ) {
		IBOService service = null;
		try {
			service = IBOLookup.getServiceInstance(getIWApplicationContext(), serviceClass);
		}
		catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		return service;
	}

}
