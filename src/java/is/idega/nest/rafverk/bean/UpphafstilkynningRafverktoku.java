package is.idega.nest.rafverk.bean;

import is.idega.nest.rafverk.domain.Fasteign;
import is.idega.nest.rafverk.domain.Rafverktaka;

import java.util.ArrayList;
import java.util.List;

public class UpphafstilkynningRafverktoku extends BaseBean {

	List possibleFasteignir;
	Rafverktaka rafverktaka;
	
	public UpphafstilkynningRafverktoku(){
		
		Rafverktaka verktaka = new Rafverktaka();
		verktaka.setRafverktaki(getInitialData().getRafverktakiJon());
		setRafverktaka(verktaka);
		
	}

	public Rafverktaka getRafverktaka() {
		return rafverktaka;
	}

	public void setRafverktaka(Rafverktaka verktaka) {
		this.rafverktaka = verktaka;
	}

	public String flettaUppIFasteignasrka() {
		
		List fasteignirSearchResults = new ArrayList();
		
		Fasteign result1 = getInitialData().getFasteignAdalstraeti13();
		
		//TODO Auto-generated method stub

		return null;
	}
	
	
	
	
}