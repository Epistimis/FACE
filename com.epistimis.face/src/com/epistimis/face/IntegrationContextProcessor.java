package com.epistimis.face;

import java.util.HashSet;
import java.util.Set;

import com.epistimis.face.face.IntegrationIntegrationContext;
import com.epistimis.face.face.IntegrationTSNodeConnection;
import com.epistimis.face.face.IntegrationUoPInputEndPoint;
import com.epistimis.face.face.IntegrationUoPOutputEndPoint;
import com.epistimis.face.face.UopConnection;
import com.epistimis.uddl.ConceptualQueryProcessor;
import com.epistimis.uddl.LogicalQueryProcessor;
import com.epistimis.uddl.PlatformQueryProcessor;
import com.epistimis.uddl.UddlQNP;
import com.google.inject.Inject;

public class IntegrationContextProcessor {

	@Inject ConceptualQueryProcessor cqp;
	@Inject LogicalQueryProcessor lqp;
	@Inject PlatformQueryProcessor pqp; 

	@Inject  UddlQNP qnp;

	public IntegrationContextProcessor() {
		// TODO Auto-generated constructor stub
	}

	public Set<UopConnection> connections(IntegrationIntegrationContext ic) {
		Set<UopConnection> result = new HashSet<>();
		for (IntegrationTSNodeConnection tconn: ic.getConnection()) {
			if (tconn.getSource() instanceof IntegrationUoPOutputEndPoint) {
				IntegrationUoPOutputEndPoint output = (IntegrationUoPOutputEndPoint)tconn.getSource();
				result.add(output.getConnection());
			}
			if (tconn.getDestination() instanceof IntegrationUoPInputEndPoint) {
				IntegrationUoPInputEndPoint input = (IntegrationUoPInputEndPoint)tconn.getDestination();
				result.add(input.getConnection());
			}
		}
		return result;
	}
}
