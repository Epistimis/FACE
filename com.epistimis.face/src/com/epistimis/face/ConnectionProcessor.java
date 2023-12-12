package com.epistimis.face;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

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
import com.epistimis.face.generator.QueryUtilities;
//import com.epistimis.face.validation.StructureChecks;
import com.epistimis.uddl.CLPExtractors;
import com.epistimis.uddl.ConceptualQueryProcessor;
import com.epistimis.uddl.LogicalQueryProcessor;
import com.epistimis.uddl.PlatformQueryProcessor;
import com.epistimis.uddl.UddlQNP;
import com.epistimis.uddl.exceptions.CharacteristicNotFoundException;
import com.epistimis.uddl.query.query.QueryStatement;
import com.epistimis.uddl.uddl.ConceptualCharacteristic;
import com.epistimis.uddl.uddl.ConceptualCompositeQuery;
import com.epistimis.uddl.uddl.ConceptualEntity;
import com.epistimis.uddl.uddl.ConceptualQuery;
import com.epistimis.uddl.uddl.ConceptualView;
import com.epistimis.uddl.uddl.PlatformCharacteristic;
import com.epistimis.uddl.uddl.PlatformComposition;
import com.epistimis.uddl.uddl.PlatformParticipant;
import com.google.inject.Inject;

public class ConnectionProcessor {
	@Inject
	ConceptualQueryProcessor cqp;// = new ConceptualQueryProcessor();
	@Inject
	LogicalQueryProcessor lqp;// = new LogicalQueryProcessor();
	@Inject
	PlatformQueryProcessor pqp; // = new PlatformQueryProcessor();

	@Inject
	UddlQNP qnp;// = new UddlQNP();

	@Inject
	CLPExtractors clp;

	@Inject
	QueryUtilities qu;

	static Logger logger = Logger.getLogger(ConnectionProcessor.class);

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
	 * Get all the ConceptualEntities referenced by this connection.
	 * 
	 * @param conn
	 * @return
	 */
	public Map<String, ConceptualEntity> getReferencedConceptualEntities(UopConnection conn) {
		Map<String, ConceptualEntity> entities = new HashMap<String, ConceptualEntity>();
		// Figure out which Entities are referenced by this component
		Map<String, ConceptualQuery> referencedQueries = qu.conceptualQueriesMap(conn);

		/**
		 * Now get all the QuerySpecifications from these queries and, from those, get
		 * all the referenced Entities
		 */
		for (Map.Entry<String, ConceptualQuery> entry : referencedQueries.entrySet()) {
			ConceptualQuery query = entry.getValue();
			entities.putAll(getReferencedConceptualEntities(query));
		}

		return entities;
	}

	/**
	 * Get all the ConceptualEntities referenced by this Query.
	 * 
	 * @param conn
	 * @return
	 */
	public Map<String, ConceptualEntity> getReferencedConceptualEntities(ConceptualQuery query) {
		Map<String, ConceptualEntity> entities = new HashMap<String, ConceptualEntity>();
		QueryStatement spec = cqp.parseQuery(query);
		entities.putAll(cqp.matchQuerytoUDDL(query, spec));

		return entities;
	}

	/**
	 * Get all the ConceptualEntities referenced by this CompositeQuery.
	 * 
	 * @param conn
	 * @return
	 */
	public Map<String, ConceptualEntity> getReferencedConceptualEntities(ConceptualCompositeQuery query) {
		Map<String, ConceptualEntity> entities = new HashMap<String, ConceptualEntity>();
		Map<ConceptualQuery, QueryStatement> specs = cqp.parseCompositeQuery(query);
		for (Map.Entry<ConceptualQuery, QueryStatement> entry: specs.entrySet()) {
			entities.putAll(cqp.matchQuerytoUDDL(entry.getKey(), entry.getValue()));		
		}

		return entities;
	}
	
	/**
	 * Get all the Characteristics used on this connection
	 * 
	 * @param conn
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this UopConnection
	 * @ throws CharacteristicNotFoundException 
	 */
	public Map<String, ConceptualCharacteristic> getSelectedConceptualCharacteristicsMap(UopConnection conn) /* throws CharacteristicNotFoundException */ {
		if (conn instanceof UopQueuingConnection) {
			return getSelectedConceptualCharacteristicsMap((UopQueuingConnection) conn);
		}
		if (conn instanceof UopSingleInstanceMessageConnection) {
			return getSelectedConceptualCharacteristicsMap((UopSingleInstanceMessageConnection) conn);
		}
		if (conn instanceof UopClientServerConnection) {
			return getSelectedConceptualCharacteristicsMap((UopClientServerConnection) conn);
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
	protected Map<String, ConceptualCharacteristic> getSelectedConceptualCharacteristicsMap(UopQueuingConnection conn) /* throws CharacteristicNotFoundException */ {
		if (conn.getMessageExchangeType() == UopMessageExchangeType.INBOUND_MESSAGE) {
			return getSelectedConceptualCharacteristicsMap(conn, conn.getMessageType());
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
	protected Map<String, ConceptualCharacteristic> getSelectedConceptualCharacteristicsMap(
			UopSingleInstanceMessageConnection conn) /* throws CharacteristicNotFoundException */ {
		if (conn.getMessageExchangeType() == UopMessageExchangeType.INBOUND_MESSAGE) {
			return getSelectedConceptualCharacteristicsMap(conn, conn.getMessageType());
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
	protected Map<String, ConceptualCharacteristic> getSelectedConceptualCharacteristicsMap(
			UopClientServerConnection conn) /* throws CharacteristicNotFoundException */ {
		/**
		 * We only care about the inbound direction, so look at the connection to
		 * determine which to use
		 */
		if (conn.getRole() == UoPClientServerRole.CLIENT) {
			return getSelectedConceptualCharacteristicsMap(conn, conn.getRequestType());
		} else {
			return getSelectedConceptualCharacteristicsMap(conn, conn.getResponseType());
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
	 *         ConceptualEntity or ConceptualView
	 * @throws CharacteristicNotFoundException 
	 */
	protected Map<String, ConceptualCharacteristic> getSelectedConceptualCharacteristicsMap(UopConnection context,
			UopMessageType msgType) /* throws CharacteristicNotFoundException */ {
		if (msgType != null) {
			return getSelectedConceptualCharacteristicsMap(msgType);
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
	protected Map<String, ConceptualCharacteristic> getSelectedConceptualCharacteristicsMap(UopMessageType msgType) /* throws CharacteristicNotFoundException */ {
		// This is the standard approach - so extract the query from the template and
		// then process the query
		if (msgType instanceof UopTemplate) {
			return getSelectedConceptualCharacteristicsMap((UopTemplate) msgType);
		}
		if (msgType instanceof UopCompositeTemplate) {
			/**
			 * We have a choice here: We can drill down into each composition element of the
			 * CompositeTemplate and process that or we can process the CompositeQuery at
			 * this level. Processing the CompositeQuery means we pick up everything, even
			 * if the template doesn't use it. Processing individual compositions and the
			 * queries they contain is a tighter fit.
			 */
			Map<String, ConceptualCharacteristic> result = new HashMap<>();
			UopCompositeTemplate uct = (UopCompositeTemplate) msgType;
			for (UopTemplateComposition utc : uct.getComposition()) {
				result.putAll(getSelectedConceptualCharacteristicsMap(utc.getType()));
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
	protected Map<String, ConceptualCharacteristic> getSelectedConceptualCharacteristicsMap(UopTemplate templ) /* throws CharacteristicNotFoundException */ {
		Map<String, ConceptualCharacteristic> cchars = new HashMap<>();
		// If we get here, we have a single template
		if (templ.getBoundQuery() != null) {
			Map<String, PlatformCharacteristic> pchars = pqp.getSelectedCharacteristics(templ.getBoundQuery());
			// Convert these to ConceptualCharacteristics
			pchars.forEach((k, v) -> {
				cchars.put(k, v instanceof PlatformComposition ? ((PlatformComposition) v).getRealizes().getRealizes()
						: ((PlatformParticipant) v).getRealizes().getRealizes());
			});
		}
		return cchars;
	}

	/**
	 * 
	 * @param ce
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this ConceptualEntity
	 */
	protected Map<String, ConceptualCharacteristic> getCharacteristicsMap(ConceptualEntity ce) {
		Map<String, ConceptualCharacteristic> results = clp.getCharacteristics(ce);
		return results;
	}

	/**
	 * 
	 * @param cv
	 * @return A map of (rolename, characteristic) for all the characteristics on
	 *         this ConceptualView
	 * @throws CharacteristicNotFoundException 
	 */
	protected Map<String, ConceptualCharacteristic> getSelectedConceptualCharacteristicsMap(ConceptualView cv) /* throws CharacteristicNotFoundException */ {

		if (cv instanceof ConceptualQuery) {
			return cqp.getSelectedCharacteristics((ConceptualQuery) cv);
		}
		if (cv instanceof ConceptualCompositeQuery) {
			return cqp.getCompositeQuerySelectedCharacteristics((ConceptualCompositeQuery) cv);
		}
		return new HashMap<>();
	}

}
