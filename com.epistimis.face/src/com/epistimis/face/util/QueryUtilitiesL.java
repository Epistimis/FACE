/**
 * 
 */
package com.epistimis.face.util;

import org.eclipse.emf.common.util.EList;

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
public class QueryUtilitiesL extends
		QueryUtilities<LogicalCharacteristic, LogicalEntity, LogicalComposition, LogicalParticipant, LogicalView, LogicalQuery, LogicalCompositeQuery, LogicalQueryComposition, LogicalQueryProcessor
//		, ConnectionProcessorL
		> {

	// @Override
	public LogicalQuery getQuery(UopTemplate templ) {
		return templ.getBoundQuery().getRealizes(); // .getRealizes();
	}

	// @Override
	public boolean isCompositeQuery(LogicalView query) {
		return (query instanceof LogicalCompositeQuery);
	}

	// @Override
	public EList<LogicalQueryComposition> getCompositions(LogicalView query) {
		assert(query instanceof LogicalCompositeQuery);
		return ((LogicalCompositeQuery) query).getComposition();
	}

	// @Override
	public LogicalView getCompositionType(LogicalQueryComposition qc) {
		return qc.getType();
	}

	@Override
	public boolean isQuery(LogicalView v) {
		// TODO Auto-generated method stub
		return (v instanceof LogicalView);
	}

	@Override
	public LogicalQuery conv2Query(LogicalView v) {
		// TODO Auto-generated method stub
		return (LogicalQuery)v;
	}

	@Override
	public LogicalCompositeQuery conv2CompositeQuery(LogicalView v) {
		// TODO Auto-generated method stub
		return (LogicalCompositeQuery)v;
	}

}
