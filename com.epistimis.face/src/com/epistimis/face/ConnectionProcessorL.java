/**
 * 
 */
package com.epistimis.face;

import com.epistimis.face.face.UopTemplate;
import com.epistimis.uddl.LogicalQueryProcessor;
import com.epistimis.uddl.uddl.LogicalCharacteristic;
import com.epistimis.uddl.uddl.LogicalCompositeQuery;
import com.epistimis.uddl.uddl.LogicalComposition;
import com.epistimis.uddl.uddl.LogicalEntity;
import com.epistimis.uddl.uddl.LogicalParticipant;
import com.epistimis.uddl.uddl.LogicalQuery;
import com.epistimis.uddl.uddl.LogicalQueryComposition;
import com.epistimis.uddl.uddl.LogicalView;

/**
 * 
 */
public class ConnectionProcessorL extends
		ConnectionProcessor<LogicalCharacteristic, LogicalEntity, LogicalComposition, LogicalParticipant, LogicalView, LogicalQuery, LogicalCompositeQuery, LogicalQueryComposition, LogicalQueryProcessor> {

	@Override
	protected LogicalQuery getQuery(UopTemplate templ) {
		return templ.getBoundQuery().getRealizes();
	}

	@Override
	protected boolean isQuery(LogicalView v) {
		return (v instanceof LogicalQuery);
	}

	@Override
	protected boolean isCompositeQuery(LogicalView v) {
		return (v instanceof LogicalCompositeQuery);
	}

	@Override
	protected LogicalQuery conv2Query(LogicalView v) {
		return (LogicalQuery)v;
	}

	@Override
	protected LogicalCompositeQuery conv2CompositeQuery(LogicalView v) {
		return (LogicalCompositeQuery)v;
	}

}
