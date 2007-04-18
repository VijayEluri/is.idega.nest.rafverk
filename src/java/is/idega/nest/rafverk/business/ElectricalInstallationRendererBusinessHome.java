package is.idega.nest.rafverk.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface ElectricalInstallationRendererBusinessHome extends IBOHome {

	public ElectricalInstallationRendererBusiness create() throws CreateException, RemoteException;
}