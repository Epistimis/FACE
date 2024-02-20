package com.epistimis.face;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;

import com.epistimis.face.face.UoPClientServerRole;
import com.epistimis.face.face.UopClientServerConnection;
import com.epistimis.face.face.UopCompositeTemplate;
import com.epistimis.face.face.UopConnection;
import com.epistimis.face.face.UopMessageExchangeType;
import com.epistimis.face.face.UopMessageType;
import com.epistimis.face.face.UopQueuingConnection;
import com.epistimis.face.face.UopSingleInstanceMessageConnection;
import com.epistimis.face.face.UopTemplate;
import com.epistimis.face.face.UopTemplateComposition;
import com.epistimis.face.util.QueryUtilities;
//import com.epistimis.face.validation.StructureChecks;
import com.epistimis.uddl.CLPExtractors;
import com.epistimis.uddl.QueryProcessor;
import com.epistimis.uddl.UddlQNP;
import com.epistimis.uddl.exceptions.CharacteristicNotFoundException;
import com.epistimis.uddl.uddl.UddlElement;
import com.google.inject.Inject;


/**
 * This collection of methods extracts info about connections - returning the info at any C/L/P level.
 * TODO: We can follow realizes early or late. Folling realizes early means we get everything at the realized level, not at the realizing level.
 * If we do not realize every characteristic, or if we have multiple realizations at the realizing level, we miss that if we follow realization
 * early.  Following realization shows the extent of what is possible. Following realization late (after we have characteristics) shows what is
 * actually there now.
 * 
 * @param <Characteristic>
 * @param <Entity>
 * @param <Composition>
 * @param <Participant>
 * @param <View>
 * @param <Query>
 * @param <CompositeQuery>
 * @param <QueryComposition>
 * @param <QProcessor>
 */

public abstract class ConnectionProcessor<Characteristic extends EObject,Entity extends UddlElement, Composition extends Characteristic,
			Participant extends Characteristic, View extends UddlElement,
			Query extends View, CompositeQuery extends View, QueryComposition extends EObject, 
			QProcessor extends QueryProcessor<?,Characteristic,Entity,?,Composition,Participant,View, Query, CompositeQuery,QueryComposition,?,?,?>> {
//	@Inject
//	ConceptualQueryProcessor cqp;// = new QueryProcessor();
//	@Inject
//	LogicalQueryProcessor lqp;// = new LogicalQueryProcessor();
//	@Inject
//	PlatformQueryProcessor pqp; // = new QueryProcessor();

	@Inject QProcessor qp;
	
	@Inject
	UddlQNP qnp;// = new UddlQNP();

	@Inject
	CLPExtractors clp;

	// Cannot reference QueryUtilities - because it references ConnectionProcessor - that creates a circular dependency
//	@Inject
//	QueryUtilities qu;

	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	protected abstract Query getQuery(UopTemplate templ); // get the appropriate query type
	

	public static String getDefinedRole(UopConnection conn) {
		if (conn == null) {
			return "";
		}
		return conn.getName();

		/**
		 * TODO: Whenever the privacy.xtext rules for these types are updated to add
		 * definedRole then this code should be uncommented.
		 */
//		String name = conn.getName();
//		if ((name != null) && (name.trim().length()> 0)) {
//			return name;
//		}
//		RoleBase rb = null;
//		if (conn instanceof UopQueuingConnection) {
//			rb = ((UopQueuingConnection) conn).getDefinedRole();
//		}
//		else if (conn instanceof UopSingleInstanceMessageConnection) {
//			rb = ((UopSingleInstanceMessageConnection) conn).getDefinedRole();
//		}
//		else if (conn instanceof UopQueuingConnection) {
//			rb = ((UopClientServerConnection) conn).getDefinedRole();
//		} else {
//			// If we get here, it's an error
//			logger.error("Unsupported Connection type: " + conn.getClass().toString());
//			return new String();			
//		}
//		
//		if (rb == null) {
//			return "";
//		} else {
//			// The define role will have a qualified name. We 
//			return rb.getName();
//		}

	}

//	/**
//	 * Get all the Entities referenced by this connection.
//	 * 
//	 * @param conn
//	 * @return
//	 */
//	public Map<String, Entity> getReferencedEntities(UopConnection conn) {
//		Map<String, Entity> entities = new HashMap<String,Entity>();
//		// Figure out which Entities are referenced by this component
//		Map<String, Query> referencedQueries = qu.queriesMap(conn);
//
//		/**
//		 * Now get all the QuerySpecifications from these queries and, from those, get
//		 * all the referenced Entities
//		 */
//		for (Map.Entry<String, Query> entry : referencedQueries.entrySet()) {
//			Query query = entry.getValue();
//			entities.putAll(qp.getReferencedEntities(query));
//		}
//
//		return entities;
//	}

//	/**
//	 * Get all the Entities referenced by this Query.
//	 * 
//	 * @param conn
//	 * @return
//	 */
//	public Map<String, Entity> getReferencedEntities(Query query) {
//		Map<String, Entity> entities = new HashMap<String, Entity>();
//		QueryStatement spec = cqp.parseQuery(query);
//		entities.putAll(cqp.matchQuerytoUDDL(query, spec));
//
//		return entities;
//	}

//	/**
//	 * Get all the Entities referenced by this CompositeQuery.
//	 * 
//	 * @param conn
//	 * @return
//	 * @deprecated Use cqp.getEntitiesComposite() instead
//	 */
//	@Deprecated
//	public Map<String, Entity> getReferencedEntities(CompositeQuery query) {
//		Map<String, Entity> entities = new HashMap<String, Entity>();
//		Map<Query, QueryStatement> specs = cqp.parseCompositeQuery(query);
//		for (Map.Entry<Query, QueryStatement> entry: specs.entrySet()) {
//			entities.putAll(cqp.matchQuerytoUDDL(entry.getKey(), entry.getValue()));		
//		}
//
//		return entities;
//	}
	
	/**
	 * Get all the Characteristics used on this connection
	 * 
	 * @param conn
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this UopConnection
	 * @ throws CharacteristicNotFoundException 
	 */
	public Map<String, Characteristic> getSelectedCharacteristicsMap(UopConnection conn) /* throws CharacteristicNotFoundException */ {
		if (conn instanceof UopQueuingConnection) {
			return getSelectedCharacteristicsMap((UopQueuingConnection) conn);
		}
		if (conn instanceof UopSingleInstanceMessageConnection) {
			return getSelectedCharacteristicsMap((UopSingleInstanceMessageConnection) conn);
		}
		if (conn instanceof UopClientServerConnection) {
			return getSelectedCharacteristicsMap((UopClientServerConnection) conn);
		}
		// If we get here, it's an error
		logger.error("Unsupported Connection type: " + conn.getClass().toString());
		return new HashMap<>();
	}

	/**
	 * Process a Queueing Connection
	 * 
	 * @param conn
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this UopConnection
	 * @ throws CharacteristicNotFoundException 
	 */
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(UopQueuingConnection conn) /* throws CharacteristicNotFoundException */ {
		if (conn.getMessageExchangeType() == UopMessageExchangeType.INBOUND_MESSAGE) {
			return getSelectedCharacteristicsMap(conn, conn.getMessageType());
		}
		// We don't care about Outbound connections
		return new HashMap<>();

	}

	/**
	 * Process a SingleInstanceMessageConnection
	 * 
	 * @param conn
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this UopConnection
	 * @throws CharacteristicNotFoundException 
	 */
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(
			UopSingleInstanceMessageConnection conn) /* throws CharacteristicNotFoundException */ {
		if (conn.getMessageExchangeType() == UopMessageExchangeType.INBOUND_MESSAGE) {
			return getSelectedCharacteristicsMap(conn, conn.getMessageType());
		}
		// We don't care about Outbound connections
		return new HashMap<>();
	}

	/**
	 * Process a ClientServerConnection
	 * 
	 * @param conn
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this UopConnection
	 * @throws CharacteristicNotFoundException 
	 */
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(
			UopClientServerConnection conn) /* throws CharacteristicNotFoundException */ {
		/**
		 * We only care about the inbound direction, so look at the connection to
		 * determine which to use
		 */
		if (conn.getRole() == UoPClientServerRole.CLIENT) {
			return getSelectedCharacteristicsMap(conn, conn.getRequestType());
		} else {
			return getSelectedCharacteristicsMap(conn, conn.getResponseType());
		}

	}

	/**
	 * Here's where we do all the actual processing
	 * 
	 * NOTE: This assumes that only one of the three values will be set. NOTE: Only
	 * inbound direction of connection is examined
	 * 
	 * @param context
	 * @param msgType
	 * @param cce
	 * @param cv
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this UopConnection as specified by this UopMessageType,
	 *         Entity or View
	 * @throws CharacteristicNotFoundException 
	 */
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(UopConnection context,
			UopMessageType msgType) /* throws CharacteristicNotFoundException */ {
		if (msgType != null) {
			return getSelectedCharacteristicsMap(msgType);
		}

		return new HashMap<>();
	}

	/**
	 * 
	 * @param msgType
	 * @return A map of (rolename, characteristic) for all the characteristics
	 *         specified by this UopMessageType
	 * @throws CharacteristicNotFoundException 
	 */
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(UopMessageType msgType) /* throws CharacteristicNotFoundException */ {
		// This is the standard approach - so extract the query from the template and
		// then process the query
		if (msgType instanceof UopTemplate) {
			return getSelectedCharacteristicsMap((UopTemplate) msgType);
		}
		if (msgType instanceof UopCompositeTemplate) {
			/**
			 * We have a choice here: We can drill down into each composition element of the
			 * CompositeTemplate and process that or we can process the CompositeQuery at
			 * this level. Processing the CompositeQuery means we pick up everything, even
			 * if the template doesn't use it. Processing individual compositions and the
			 * queries they contain is a tighter fit.
			 */
			Map<String, Characteristic> result = new HashMap<>();
			UopCompositeTemplate uct = (UopCompositeTemplate) msgType;
			for (UopTemplateComposition utc : uct.getComposition()) {
				result.putAll(getSelectedCharacteristicsMap(utc.getType()));
			}
			return result;
		}
		// Should never get here
		return new HashMap<>();
	}

	/**
	 * 
	 * @param templ
	 * @return A map of (rolename, characteristic) for all the characteristics
	 *         selected by this template
	 * @throws CharacteristicNotFoundException 
	 */
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(UopTemplate templ) /* throws CharacteristicNotFoundException */ {
		Map<String, Characteristic> chars = new HashMap<>();
		// If we get here, we have a single template
		if (templ.getBoundQuery() != null) {
			chars = qp.getSelectedCharacteristics(getQuery(templ));
//			// Convert these to Characteristics
//			chars.forEach((k, v) -> {
//				cchars.put(k, isComposition(v) ? (conv2Composition(v).getRealizes().getRealizes()
//						: (conv2Participant(v).getRealizes().getRealizes());
//			});
		}
		return chars;
	}

	/**
	 * 
	 * @param ce
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this Entity
	 */
	protected Map<String, Characteristic> getCharacteristicsMap(Entity ce) {
		Map<String, Characteristic> results = clp.getCharacteristics(ce);
		return results;
	}

	protected abstract boolean isQuery(View v);
	protected abstract boolean isCompositeQuery(View v);
	protected abstract Query	conv2Query(View v);
	protected abstract CompositeQuery conv2CompositeQuery(View v);
	/**
	 * 
	 * @param cv
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this View
	 * @throws CharacteristicNotFoundException 
	 */
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(View cv) /* throws CharacteristicNotFoundException */ {

		if (isQuery(cv)) {
			return qp.getSelectedCharacteristics(conv2Query(cv));
		}
		if (isCompositeQuery(cv)) {
			return qp.getCompositeQuerySelectedCharacteristics(conv2CompositeQuery(cv));
		}
		return new HashMap<>();
	}

}
