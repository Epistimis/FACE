/**
 * 
 */
package com.epistimis.face.util;

import org.eclipse.emf.common.util.EList;

import com.epistimis.face.face.UopTemplate;
import com.epistimis.uddl.PlatformQueryProcessor;
import com.epistimis.uddl.uddl.PlatformCharacteristic;
import com.epistimis.uddl.uddl.PlatformCompositeQuery;
import com.epistimis.uddl.uddl.PlatformComposition;
import com.epistimis.uddl.uddl.PlatformEntity;
import com.epistimis.uddl.uddl.PlatformParticipant;
import com.epistimis.uddl.uddl.PlatformQuery;
import com.epistimis.uddl.uddl.PlatformQueryComposition;
import com.epistimis.uddl.uddl.PlatformView;

/**
 * 
 */
public class QueryUtilitiesP extends
		QueryUtilities<PlatformCharacteristic, PlatformEntity, PlatformComposition, PlatformParticipant, PlatformView, PlatformQuery, PlatformCompositeQuery, PlatformQueryComposition, PlatformQueryProcessor
//		, ConnectionProcessorP
		> {

	// @Override
	public PlatformQuery getQuery(UopTemplate templ) {
		return templ.getBoundQuery(); // .getRealizes().getRealizes();
	}

	// @Override
	public boolean isCompositeQuery(PlatformView query) {
		return (query instanceof PlatformCompositeQuery);
	}

	// @Override
	public EList<PlatformQueryComposition> getCompositions(PlatformView query) {
		assert(query instanceof PlatformCompositeQuery);
		return ((PlatformCompositeQuery) query).getComposition();
	}

	// @Override
	public PlatformView getCompositionType(PlatformQueryComposition qc) {
		return qc.getType();
	}

	@Override
	public boolean isQuery(PlatformView v) {
		// TODO Auto-generated method stub
		return (v instanceof PlatformQuery);
	}

	@Override
	public PlatformQuery conv2Query(PlatformView v) {
		// TODO Auto-generated method stub
		return (PlatformQuery)v;
	}

	@Override
	public PlatformCompositeQuery conv2CompositeQuery(PlatformView v) {
		// TODO Auto-generated method stub
		return (PlatformCompositeQuery)v;
	}

}
