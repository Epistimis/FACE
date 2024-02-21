package com.epistimis.face.util;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.IQualifiedNameProvider;

import com.epistimis.face.exceptions.MissingRealizationException;
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
import com.epistimis.face.face.UopUnitOfPortability;
import com.epistimis.uddl.CLPExtractors;
import com.epistimis.uddl.QueryProcessor;
import com.epistimis.uddl.exceptions.CharacteristicNotFoundException;
import com.epistimis.uddl.uddl.UddlElement;
import com.google.inject.Inject;

/**
 * These are utilities that are used to handle the transition between Face ->
 * Query -> Uddl
 */
public abstract class QueryUtilities<Characteristic extends EObject, 
										Entity extends UddlElement, 
										Composition extends Characteristic, 
										Participant extends Characteristic, 
										View extends UddlElement, 
										Query extends View, 
										CompositeQuery extends View, 
										QueryComposition extends EObject, 
			QProcessor extends QueryProcessor<?, Characteristic, Entity, ?, Composition, Participant, View, Query, CompositeQuery, QueryComposition, ?, ?, ?>
//			,CProcessor extends ConnectionProcessor<Characteristic,Entity, Composition, Participant, View, Query, CompositeQuery, QueryComposition, QProcessor>
> {

	/**
	 * NOTE: The qnp is not protected because derived classes will have their own
	 */
	@Inject
	IQualifiedNameProvider qnp;

	@Inject
	CLPExtractors clp;

//	@Inject protected extension PlatformQueryProcessor pqp;
//
//	@Inject protected extension ConceptualQueryProcessor cqp;
//
//	@Inject protected  CProcessor cp;

	@Inject
	protected QProcessor qp;

	private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Depending on C/L/P, the query will be obtained in different ways from a
	 * template
	 */
	public abstract Query getQuery(UopTemplate templ);

	public abstract boolean isQuery(View v);

	public abstract boolean isCompositeQuery(View v);

	public abstract EList<QueryComposition> getCompositions(View v);

	public abstract View getCompositionType(QueryComposition qc);

	public abstract Query conv2Query(View v);

	public abstract CompositeQuery conv2CompositeQuery(View v);

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

	/**
	 * Get all the Characteristics used on this connection
	 * 
	 * @param conn
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this UopConnection @ throws CharacteristicNotFoundException
	 */
	public Map<String, Characteristic> getSelectedCharacteristicsMap(
			UopConnection conn) /* throws CharacteristicNotFoundException */ {
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
	 *         this UopConnection @ throws CharacteristicNotFoundException
	 */
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(
			UopQueuingConnection conn) /* throws CharacteristicNotFoundException */ {
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
	 *         this UopConnection as specified by this UopMessageType, Entity or
	 *         View
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
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(
			UopMessageType msgType) /* throws CharacteristicNotFoundException */ {
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
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(
			UopTemplate templ) /* throws CharacteristicNotFoundException */ {
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

	/**
	 * 
	 * @param cv
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this View
	 * @throws CharacteristicNotFoundException
	 */
	protected Map<String, Characteristic> getSelectedCharacteristicsMap(
			View cv) /* throws CharacteristicNotFoundException */ {

		if (isQuery(cv)) {
			return qp.getSelectedCharacteristics(conv2Query(cv));
		}
		if (isCompositeQuery(cv)) {
			return qp.getCompositeQuerySelectedCharacteristics(conv2CompositeQuery(cv));
		}
		return new HashMap<>();
	}

	// ======== getReferencedEntities ========

	public Map<String, Entity> getReferencedEntities(UopUnitOfPortability comp) {
		Map<String, Entity> entities = new HashMap<String, Entity>();
		for (UopConnection conn : comp.getConnection()) {
			entities.putAll(getReferencedEntities(conn));
		}
//		// Figure out which Entities are referenced by this component
//		var referencedQueries = new TreeMap<String, CononceptualQuery>();
//		for (conn : comp.connection) {
//			referencedQueries.putAll(conn.platformQueriesMap);
//		}
//		/**
//		 * Now get all the QuerySpecifications from these queries and, from those, get all the referenced Entities
//		 */
//		for (Map.Entry<String,Query> entry : referencedQueries.entrySet) {
//			val Query query = entry.value
//			val QuerySpecification spec = cqp.processQuery(query);
//			entities.addAll(cqp.matchQuerytoUDDL(query, spec));
//		}
		return entities;
	}

	/**
	 * Get all the ConceptualEntities referenced by this connection.
	 * 
	 * @param conn
	 * @return
	 */
	public Map<String, Entity> getReferencedEntities(UopConnection conn) {
		Map<String, Entity> entities = new HashMap<String, Entity>();
		// Figure out which Entities are referenced by this component
		Map<String, Query> referencedQueries = queriesMap(conn);

		/**
		 * Now get all the QuerySpecifications from these queries and, from those, get
		 * all the referenced Entities
		 */
		for (Map.Entry<String, Query> entry : referencedQueries.entrySet()) {
			Query query = entry.getValue();
			entities.putAll(qp.getReferencedEntities(query));
		}

		return entities;
	}

	// ======== queriesMap =======
	public SortedMap<String, Query> queriesMap(UopMessageType elem) {
		if (elem instanceof UopTemplate) {
			return queriesMap((UopTemplate) elem);
		}
		if (elem instanceof UopCompositeTemplate) {
			return queriesMap((UopCompositeTemplate) elem);
		}
		return new TreeMap<String, Query>();
	}

	/**
	 * Get all the queries referenced by this Template/Connection. Recurses down
	 * through Composites to find everything, keeping only a single reference,
	 * ordered by the FQN of the query. Note that this returns only ConceptualQuery,
	 * not ConceptualCompositeQuery - effectively flattening the list.
	 * 
	 * TODO: this does not account for differences between
	 * Platform/Logical/Conceptual in terms of fields selected. So the
	 * ConceptualQuery returned could contain more fields than the PlatformQuery
	 * actually uses. Or, if Logical/Platform realizes the same Conceptual field
	 * multiple ways, it could be missing something - or it could have the wrong
	 * composition name
	 */
	public SortedMap<String, Query> queriesMap(UopTemplate elem) {
		SortedMap<String, Query> result = new TreeMap<String, Query>();
		Query cq = getQuery(elem); // .boundQuery?.realizes?.realizes;
		if (cq != null) {
			result.put(qnp.getFullyQualifiedName(cq).toString(), cq);
		} else {
			String msg = String.format(
					"Attempt to find Query associated with Template %s failed. Is a realization missing?",
					qnp.getFullyQualifiedName(elem).toString());
			throw new MissingRealizationException(msg);
		}
		return result;
	}

	public SortedMap<String, Query> queriesMap(UopCompositeTemplate elem) {
		SortedMap<String, Query> result = new TreeMap<String, Query>();
		for (UopTemplateComposition comp : elem.getComposition()) {
			result.putAll(queriesMap(comp.getType()));
		}
		return result;
	}

	public SortedMap<String, Query> queriesMap(UopConnection elem) {
		if (elem instanceof UopQueuingConnection) {
			return queriesMap((UopQueuingConnection) elem);
		}
		if (elem instanceof UopSingleInstanceMessageConnection) {
			return queriesMap((UopSingleInstanceMessageConnection) elem);
		}
		if (elem instanceof UopClientServerConnection) {
			return queriesMap((UopClientServerConnection) elem);
		}
		// Should never get here
		return new TreeMap<String, Query>();

	}

	public SortedMap<String, Query> queriesMap(UopQueuingConnection elem) {
		SortedMap<String, Query> result = queriesMap(elem.getMessageType());
		if (result != null) {
			return result;
		} else {
			return new TreeMap<String, Query>();
		}
	}

	public SortedMap<String, Query> queriesMap(UopSingleInstanceMessageConnection elem) {
		SortedMap<String, Query> result = queriesMap(elem.getMessageType());
		if (result != null) {
			return result;
		} else {
			return new TreeMap<String, Query>();
		}
	}

	public SortedMap<String, Query> queriesMap(UopClientServerConnection elem) {
		SortedMap<String, Query> result = new TreeMap<String, Query>();
		SortedMap<String, Query> reqs = queriesMap(elem.getRequestType());
		if (reqs != null) {
			result.putAll(reqs);
		}
		SortedMap<String, Query> resp = queriesMap(elem.getResponseType());
		if (resp != null) {
			result.putAll(resp);
		}

		return result;
	}

	public SortedMap<String, Query> queriesMap(View elem) {
		SortedMap<String, Query> result = new TreeMap<String, Query>();
		if (isCompositeQuery(elem)) {
			EList<QueryComposition> comps = getCompositions(elem);
			for (QueryComposition comp : comps) {
				View qcType = getCompositionType(comp);
				result.putAll(queriesMap(qcType));
			}
		} else {
			result.put(qnp.getFullyQualifiedName(elem).toString(), conv2Query(elem));
		}
		return result;
	}

//	// ======== getReferencedPlatformEntities ========
//	def dispatch Map<String, PlatformEntity> getReferencedPlatformEntities(UopUnitOfPortability comp) {
//		var Map<String, PlatformEntity> entities = new HashMap<String, PlatformEntity>();
//		for (conn : comp.connection) {
//			entities.putAll(conn.getReferencedPlatformEntities);
//		}
////		// Figure out which Entities are referenced by this component
////		var referencedQueries = new TreeMap<String, PlatformQuery>();
////		for (conn : comp.connection) {
////			referencedQueries.putAll(conn.platformQueriesMap);
////		}
////		/**
////		 * Now get all the QuerySpecifications from these queries and, from those, get all the referenced Entities
////		 */
////		for (Map.Entry<String,PlatformQuery> entry : referencedQueries.entrySet) {
////			val PlatformQuery query = entry.value
////			val QuerySpecification spec = pqp.processQuery(query);
////			entities.addAll(pqp.matchQuerytoUDDL(query, spec));
////		}
//		return entities;
//	}

//	def dispatch Map<String, PlatformEntity> getReferencedPlatformEntities(UopConnection conn) {
//		var Map<String, PlatformEntity> entities = new HashMap<String, PlatformEntity>();
//		// Figure out which Entities are referenced by this component
//		var referencedQueries = conn.platformQueriesMap;
//
//		/**
//		 * Now get all the QuerySpecifications from these queries and, from those, get all the referenced Entities
//		 */
//		for (Map.Entry<String,PlatformQuery> entry : referencedQueries.entrySet) {
//			val PlatformQuery query = entry.value
//			val QueryStatement spec = qp.parseQuery(query);
//			entities.putAll(pqp.matchQuerytoUDDL(query, spec));
//		}
//
//		return entities;
//	}
	// ======== platformQueriesMap =================================
	/**
	 * Get all the queries referenced by this Template/Connection. Recurses down
	 * through Composites to find everything, keeping only a single reference,
	 * ordered by the FQN of the query. Note that this returns only PlatformQuery,
	 * not PlatformCompositeQuery - effectively flattening the list.
	 */
//	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(UopTemplate templ) {
//		var result = new TreeMap<String, PlatformQuery>();
//		result.put(qnp.getFullyQualifiedName(templ.boundQuery).toString, templ.boundQuery);
//		return result;
//	}

//	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(UopCompositeTemplate templ) {
//		var result = new TreeMap<String, PlatformQuery>();
//		for (UopTemplateComposition comp : templ.composition) {
//			result.putAll(comp.type.platformQueriesMap);
//		}
//		return result;
//	}

//	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(UopQueuingConnection conn) {
//		val result = conn.messageType?.platformQueriesMap;
//		if (result !== null) {
//			return result;
//		} else {
//			new TreeMap<String, PlatformQuery>();
//		}
//	}

//	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(UopSingleInstanceMessageConnection conn) {
//		val result = conn.messageType?.platformQueriesMap;
//		if (result !== null) {
//			return result;
//		} else {
//			return new TreeMap<String, PlatformQuery>();
//		}
//	}

//	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(UopClientServerConnection conn) {
//		var result = new TreeMap<String, PlatformQuery>();
//		val reqs = conn.requestType?.platformQueriesMap;
//		if (reqs !== null) {
//			result.putAll(reqs);
//		}
//		val resp = conn.responseType?.platformQueriesMap;
//		if (resp !== null) {
//			result.putAll(resp);
//		}
//
//		return result;
//	}

//	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(PlatformQuery query) {
//		var result = new TreeMap<String, PlatformQuery>();
//		result.put(qnp.getFullyQualifiedName(query).toString, query);
//		return result;
//	}
//
//	def dispatch SortedMap<String, PlatformQuery> platformQueriesMap(PlatformCompositeQuery query) {
//		var result = new TreeMap<String, PlatformQuery>();
//		for (PlatformQueryComposition comp : query.composition) {
//			result.putAll(comp.type.platformQueriesMap);
//		}
//		return result;
//	}

}
