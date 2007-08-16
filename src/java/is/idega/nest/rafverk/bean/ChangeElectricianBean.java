/*
 * $Id: ChangeElectricianBean.java,v 1.2 2007/08/16 17:47:47 thomas Exp $
 * Created on Aug 13, 2007
 *
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.nest.rafverk.bean;

import is.idega.nest.rafverk.business.ElectricalInstallationBusiness;
import is.idega.nest.rafverk.domain.ElectricalInstallation;
import is.idega.nest.rafverk.domain.Fasteign;
import is.idega.nest.rafverk.domain.Orkukaupandi;
import is.idega.nest.rafverk.domain.Rafverktaka;
import is.idega.nest.rafverk.domain.Rafverktaki;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.FinderException;
import javax.faces.context.FacesContext;

import com.idega.business.IBOLookup;
import com.idega.business.IBOService;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.util.StringHandler;


/**
 * 
 *  Last modified: $Date: 2007/08/16 17:47:47 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public class ChangeElectricianBean extends RealEstateBean {
	
	private Map electricalInstallationList = null;
	
	private Map electricalInstallationMap = null; 
	
	private String electricalInstallationIDSelection = null;
	
	private ElectricalInstallationBusiness electricalInstallationBusiness = null;
	
	public ChangeElectricianBean() {
    	initialize();
    }
	
	public void initialize() {
		super.initializeForm();
		electricalInstallationList = new TreeMap(Collections.reverseOrder());
		electricalInstallationMap = new HashMap();
		initializeElectricalInstallationList(null);
	}
	
	// might be overwritten by subclasses
	void changedRealEstate(Fasteign fasteign) {
		String realEstateNumber = fasteign.getFastaNumer();
		initializeElectricalInstallationList(realEstateNumber);
	}
	
	private void initializeElectricalInstallationList(String realEstateNumber) {
		electricalInstallationList.clear(); 
		electricalInstallationMap.clear();
		if (realEstateNumber == null || InitialData.NONE_REAL_ESTATE_SELECTION.equals(realEstateNumber)) {
			return;
		}
		Collection verktokur = null;
		try {
			verktokur = getElectricalInstallationBusiness().getElectricalInstallationByRealEstateNumber(realEstateNumber);
		}
		catch (RemoteException e) {
			throw new RuntimeException(e.getMessage());
		}
		catch (FinderException e) {
			verktokur = null;
		}
		if (verktokur == null || verktokur.isEmpty()) {
			electricalInstallationList.put(InitialData.NONE_ELECTRIC_INSTALLATION_SELECTION, "ekkert fannst");
		}
		else {
			electricalInstallationList.put(InitialData.NONE_ELECTRIC_INSTALLATION_SELECTION, "Veldu rafverktöku");
			Iterator iterator = verktokur.iterator();
			while (iterator.hasNext()) {
				ElectricalInstallation electricalInstallation = (ElectricalInstallation) iterator.next();
				Rafverktaka verktaka = new Rafverktaka(electricalInstallation, getElectricalInstallationBusiness());
				String id = verktaka.getId();
				String label = getElectricalInstallationLabel(verktaka);
				electricalInstallationList.put(id, label);	
				electricalInstallationMap.put(id, verktaka);
			}
		}
	}
	
	private String getElectricalInstallationLabel(Rafverktaka rafverktaka) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Rafverktaki: ");
		Rafverktaki rafverktaki = rafverktaka.getRafverktaki();
		if (rafverktaki != null) {
			add(buffer, rafverktaki.getNafn());
		}
		
		buffer.append(", Verknúmer: ");
		add(buffer, rafverktaka.getExternalProjectID());
		
		buffer.append(", Orkukaupandi: ");
		Orkukaupandi orkukaupandi = rafverktaka.getOrkukaupandi();
		if (orkukaupandi != null) {
			add(buffer, orkukaupandi.getNafn());
		}
		
		buffer.append(", Stada: ");
		add(buffer, rafverktaka.getStadaDisplay());
		
		return buffer.toString();
	}
	
	private void add(StringBuffer buffer, String value) {
		buffer.append(StringHandler.getStringOrDash(value));
	}
	
	public ElectricalInstallationBusiness getElectricalInstallationBusiness() {
		if (electricalInstallationBusiness == null) {
			electricalInstallationBusiness = (ElectricalInstallationBusiness) getSeviceBean(ElectricalInstallationBusiness.class);
		}
		return electricalInstallationBusiness;
	}
	
	private IBOService getSeviceBean(Class serviceBeanClass) {
		IBOService myServiceBean = null;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			IWContext iwContext = IWContext.getIWContext(context);
			IWApplicationContext iwac = iwContext.getApplicationContext();
			myServiceBean = IBOLookup.getServiceInstance(iwac, serviceBeanClass);
		}
		catch (RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		return myServiceBean;
	}
	
	public Rafverktaka getCurrentElectricalInstallationSelection() {
		String id = getElectricalInstallationIDSelection();
		if (id == null ||InitialData.NONE_ELECTRIC_INSTALLATION_SELECTION.equals(id)) {
			return null;
		}
		return (electricalInstallationMap == null) ? null : (Rafverktaka) electricalInstallationMap.get(id);
	}

	public Map getElectricalInstallationList() {
		return electricalInstallationList;
	}
	
	public List getElectricalInstallationListiSelects() {
		return getListiSelects(getElectricalInstallationList());
	}
	
	// getter for second step...
	
	public String getNafnOrkukaupanda() {
		Orkukaupandi orkukaupandi = getCurrentElectricalInstallationSelection().getOrkukaupandi();
		if (orkukaupandi != null) {
			return orkukaupandi.getNafn();
		}
		return StringHandler.EMPTY_STRING;
	}
	
	public String getKennitalaOrkukaupanda() {
		Orkukaupandi orkukaupandi = getCurrentElectricalInstallationSelection().getOrkukaupandi();
		if (orkukaupandi != null) {
			return orkukaupandi.getKennitala();
		}
		return StringHandler.EMPTY_STRING;
	}
	
	public String getVeitustadurDisplay() {
		Rafverktaka currentElectricalInstallation = getCurrentElectricalInstallationSelection();
		if (currentElectricalInstallation == null) {
			return super.getVeitustadurDisplay();
		}
		String display = currentElectricalInstallation.getVeitustadurDisplay();
		return StringHandler.replaceIfEmpty(display, StringHandler.EMPTY_STRING);
	}
	
	public String getStadaDisplay() {
		return getCurrentElectricalInstallationSelection().getStadaDisplay();
	}
	
	// ...getter for second step
	
	// action method second step 
	public String changeElectrician() {
		Rafverktaka currentElectricalInstallation = getCurrentElectricalInstallationSelection();
		ElectricalInstallation electricalInstallation = currentElectricalInstallation.getElectricalInstallation();
		return "next";
	}
	
	// message for third step
	public String getMessageStoring() {
		return "Sent";
	}
	
	
	/**
	 * @return Returns the electricalInstallationIDSelection.
	 */
	public String getElectricalInstallationIDSelection() {
		return electricalInstallationIDSelection;
	}

	
	/**
	 * @param electricalInstallationIDSelection The electricalInstallationIDSelection to set.
	 */
	public void setElectricalInstallationIDSelection(String electricalInstallationIDSelection) {
		this.electricalInstallationIDSelection = electricalInstallationIDSelection;
	}

}
