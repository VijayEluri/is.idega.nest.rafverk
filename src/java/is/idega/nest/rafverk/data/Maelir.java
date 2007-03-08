/*
 * $Id: Maelir.java,v 1.3 2007/03/08 15:54:40 thomas Exp $
 * Created on Feb 12, 2007
 *
 * Copyright (C) 2007 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.nest.rafverk.data;

import java.util.List;


/**
 * 
 *  Last modified: $Date: 2007/03/08 15:54:40 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.3 $
 */
public class Maelir {
	
	public static Maelir addInvalidInstance(List myList) {
		Maelir maelir = new Maelir();
		myList.add(maelir);
		maelir.myList = myList;
		maelir.setValid(false);
		return maelir;
	}
	
	List myList = null;
	
	private boolean valid = true;
	
	private String fasa = null;
	
	private String numer = null;
	
	private String ampere = null;
	
	private String taxti = null;
	
	private String hjalpataeki = null;
	
	private String stadur = null;
	
	/**
	 * Set this instance to valid and 
	 * add an invalid instance at the end
	 * of myList
	 *
	 */
	public void add() {
		valid = true;
		Maelir.addInvalidInstance(myList);
	}
	
	public void delete() {
		myList.remove(this);
	}

	
	public String getAmpere() {
		return ampere;
	}

	
	public void setAmpere(String ampere) {
		this.ampere = ampere;
	}

	
	public String getNumer() {
		return numer;
	}

	
	public void setNumer(String numer) {
		this.numer = numer;
	}

	
	public String getTaxti() {
		return taxti;
	}

	
	public void setTaxti(String taxti) {
		this.taxti = taxti;
	}


	
	public String getFasa() {
		return fasa;
	}


	
	public void setFasa(String fasa) {
		this.fasa = fasa;
	}

	
	public boolean isValid() {
		return valid;
	}

	
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	
	public String getHjalpataeki() {
		return hjalpataeki;
	}

	
	public void setHjalpataeki(String hjalparTaeki) {
		this.hjalpataeki = hjalparTaeki;
	}

	
	public String getStadur() {
		return stadur;
	}

	
	public void setStadur(String stadur) {
		this.stadur = stadur;
	}
}
