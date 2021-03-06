package is.idega.nest.rafverk.domain;


public class Orkukaupandi extends BaseBean {
	
	String id;
	String nafn;
	String kennitala;
	Heimilisfang heimilisfang;
	String vinnusimi;
	String heimasimi;
	String email;
	
	public Orkukaupandi() {
	}
	
	public Orkukaupandi(ElectricalInstallation electricalInstallation) {
		nafn = electricalInstallation.getEnergyConsumerName();
		kennitala = electricalInstallation.getEnergyConsumerPersonalID();
		vinnusimi = electricalInstallation.getEnergyConsumerWorkPhone();
		heimasimi = electricalInstallation.getEnergyConsumerHomePhone();
		email = electricalInstallation.getEnergyConsumerEmail();
		
	}
	
	public String getHeimasimi() {
		return heimasimi;
	}
	public void setHeimasimi(String heimasimi) {
		this.heimasimi = heimasimi;
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	public Heimilisfang getHeimilisfang() {
		return heimilisfang;
	}
	
	/**
	 * @deprecated
	 * @param heimilisfang
	 */
	public void setHeimilisfang(Heimilisfang heimilisfang) {
		this.heimilisfang = heimilisfang;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKennitala() {
		return kennitala;
	}
	public void setKennitala(String kennitala) {
		this.kennitala = kennitala;
	}
	public String getNafn() {
		return nafn;
	}
	public void setNafn(String nafn) {
		this.nafn = nafn;
	}
	public String getVinnusimi() {
		return vinnusimi;
	}
	public void setVinnusimi(String vinnusimi) {
		this.vinnusimi = vinnusimi;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}
