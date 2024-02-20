/**
 * 
 */
package com.epistimis.face;

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
public class ConnectionProcessorP extends
		ConnectionProcessor<PlatformCharacteristic, PlatformEntity, PlatformComposition, PlatformParticipant, PlatformView, PlatformQuery, PlatformCompositeQuery, PlatformQueryComposition, PlatformQueryProcessor> {

	@Override
	protected PlatformQuery getQuery(UopTemplate templ) {
		return templ.getBoundQuery();
	}

	@Override
	protected boolean isQuery(PlatformView v) {
		return (v instanceof PlatformQuery);
	}

	@Override
	protected boolean isCompositeQuery(PlatformView v) {
		return (v instanceof PlatformCompositeQuery);
	}

	@Override
	protected PlatformQuery conv2Query(PlatformView v) {
		return (PlatformQuery)v;
	}

	@Override
	protected PlatformCompositeQuery conv2CompositeQuery(PlatformView v) {
		return (PlatformCompositeQuery)v;
	}

}
