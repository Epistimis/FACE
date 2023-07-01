package com.epistimis.face.design;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import com.epistimis.face.face.IntegrationTSNodeConnection;
import com.epistimis.face.validation.StructureChecks;

/**
 * The services class used by VSM.
 */
public class Services {


	/**
	 * See
	 * http://help.eclipse.org/neon/index.jsp?topic=%2Forg.eclipse.sirius.doc%2Fdoc%2Findex.html&cp=24
	 * for documentation on how to write service methods.
	 */
	public EObject myService(EObject self, String arg) {
		System.out.println("myService called ");
		return self;
	}
	

	/** Label provider */
	protected static final AdapterFactoryLabelProvider LABEL_PROVIDER = new AdapterFactoryLabelProvider(
			new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));

	public String getPropertiesTitle(EObject eObj) {
		String msg = "";// Messages.NamingService_0;
		msg = msg + LABEL_PROVIDER.getText(eObj);
		return msg;
	}

	public boolean doConnectionsMatch(IntegrationTSNodeConnection conn) {
		return StructureChecks.doConnectionsMatch(conn);
	}

}
