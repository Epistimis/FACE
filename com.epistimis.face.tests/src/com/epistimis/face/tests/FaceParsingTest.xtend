/*
 * generated by Xtext 2.30.0
 */
/*
 * Copyright (c) 2022, 2023 Epistimis LLC (http://www.epistimis.com).
 */
package com.epistimis.face.tests

import com.epistimis.face.face.ArchitectureModel
import com.google.inject.Inject
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith

@ExtendWith(InjectionExtension)
@InjectWith(FaceInjectorProvider)
class FaceParsingTest {
	@Inject
	ParseHelper<ArchitectureModel> parseHelper
	
	@Test
	def void loadModel() {
		val result = parseHelper.parse('''
am CommonCo ""{
	dm DataModel {
		cdm Structures {
	
			observable Question "A question that needs to be addressed";
			observable Answer  "The answer to a question";
			
			/**
			 * TODO: Can a CommonCo customer be someone other than the insured party? If so, what do we need to track to 
			 * determine that relationship and if appropriate authorizations are in place?
			 */
			centity Customer "The CommonCo customer. This may not be the insured party. " : PPT.Conceptual.NaturalPerson  {
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.NonPhysicalAddress 	phoneForText[0:1] 		"This specific phone used for texting";
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.NonPhysicalAddress 	phones[0:-1] 			"All phones. Any 'phoneForText' shoudl be here also";
				Privacy.General.AccessKey  															password[1:5] 			"password (history) - first is current, others are just so people don't repeat passwords";
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.Time.CalendarTime 	dob[1:1] 				"Date of birth";
				Privacy.General.Decision															privacyConsents[1:-1] 	"History of privacy consents";
				Privacy.General.Decision															termsCondsConsents[1:-1] "History of terms & conditions consents";				
				Insurance																			insurance[0:-1] 		"All the insurances the Customer has";
				QA																					qsAndAs[0:-1]			"All the questions asked by the customer (and their answers)";
			};

			/**
			 * Question content could be anything. Some content will be sensitive.
			 * 
			 * TODO: Do these questions have unique IDs? Or are they just addressed in list order? Generally, how do we deal with UniqueIDs -
			 * which are really an implementation detail
			 */
			centity QA "A customer question" {
				Question 																					question[1:1] 		"The question to answer";
				Privacy.People.Health																		medicalCondition[0:-1] "The medical condition this relates to, if any. Could be more than one";
				Answer   																					answer[0:-1]  		"The set of answers to the question";
				Privacy.General.Decision 																	decision[0:1] 		"If the question results in a decision, that is stored here.";
				QA																							subquestions[0:-1]  "One question may spur others";

				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.State.ConfigurationState 	sensitivity[0:1] 	"The sensitivity of this content. If not specified, this content is sensitive";
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.State.ValidityState 			validity[0:1] 		"How valid is this content. If not specified, the content is assumed to be valid";			
			};
			
			centity Advocate "A CommonCo advocate" : PPT.Conceptual.NaturalPerson {
				Customer																					customers[0:-1]		"All the customers for this Advocate";	
			};
			
			cassoc Insurance "Insurance Info" {
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.Identifier.Identifier group[1:1] "The group this is part of";
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.Identifier.Identifier member[1:1] "The member of the group";
				
				participants: [
				  PPT.Conceptual.LegalOrganization 	insuranceOrganization[1:1] "The insurance company" {src: [1:1]} ;
				  PPT.Conceptual.NaturalPerson 		insured[1:1] "The insured" {src: [1:1]} ;
				  PPT.Conceptual.NaturalPerson 		dependents[0:-1] "Dependents, if any. " {src: [1:1]} ;
				]														
			};
			

		}
		
		cdm Queries "The queries used for this model" {
			cquery FullCustomerRecord "All the Customer Data" {
				spec:"SELECT * FROM Customer"
			};
			cquery Login "Info needed for to log into an existing account" {
				spec:"SELECT Customer.name, Customer.password FROM Customer"
			};
			cquery BillingInfo "Info needed for billing" {
				spec: "SELECT Customer.name, Customer.address  FROM Customer"
				};
	 
			cquery Ack "Info needed for acknowledgement" {
				spec: "SELECT Customer.name FROM Customer"
			};

			cquery NewCustomerQuestion "Info for a question from Customer" {
				spec: "SELECT Customer.name, QA.question, QA.medicalCondition FROM Customer JOIN QA ON Customer.qsAndAs"
			};


			cquery RequestMore "Info needed for request" {
				spec: "SELECT QA.question as startingQ, QA.question as newQ FROM QA JOIN QA ON startingQ.subquestions = newQ"
			};
			
//			cquery AdvocateResponse "Question answered by Advocate" {
//				spec: "SELECT Customer.name, QA.* FROM Customer JOIN QA ON Customer.qsAndAs"
//			};
					
  			cquery RequestCustomerData "Info needed for customer Dashboard" {
				spec: "SELECT Customer.name FROM Customer"
			};

			/**
			 * Because this is just the answer associated with a question, it could be the main question or a subquestion -
			 * no matter who asks it.
			 */
			cquery NewAnswer "Info answering a question" {
				spec: "SELECT QA.question, QA.answer FROM QA "
			};
  
			cquery RequestListOfCustomers "All the customers associated with this Advocate" {
				spec: "SELECT Advocate.name FROM Advocate"
			};

			cquery RetrieveListOfCustomers "All the customers associated with this Advocate" {
				spec: "SELECT Customer.name FROM Customer JOIN Advocate ON Advocate.customers"
			};
			
			cquery RequestCustomerQuestionsList "Get all the questions associated with this Customer" {
				spec: "SELECT Customer.name FROM Customer"
			};
					
			cquery RetrieveCustomerQuestionsList "All info related to a specific customer question" {
				spec: "SELECT Customer.name, QA.question FROM Customer JOIN QA on Customer.qsAndAs"
			};

			cquery RequestQuestionDetail "Request detail on a question" {
				spec: "SELECT QA.question FROM QA"
			};
	
			cquery RetrieveQuestionDetail "All info related to a specific question" {
				spec: "SELECT QA.* FROM QA"
			};

		}
		
	}
}
		''')
		Assertions.assertNotNull(result)
		val errors = result.eResource.errors
		Assertions.assertTrue(errors.isEmpty, '''Unexpected errors: «errors.join(", ")»''')
	}
	
	@Test
	def void loadModel2() {
		val result = parseHelper.parse('''
/*
 * TODO: 
 */
 
am CommonCo ""{
	dm DataModel {
		cdm Structures {
	
			observable Question "A question that needs to be addressed";
			observable Answer  "The answer to a question";
			
			/**
			 * TODO: Can a CommonCo customer be someone other than the insured party? If so, what do we need to track to 
			 * determine that relationship and if appropriate authorizations are in place?
			 */
			centity Customer "The CommonCo customer. This may not be the insured party. " : PPT.Conceptual.NaturalPerson  {
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.NonPhysicalAddress 	phoneForText[0:1] 		"This specific phone used for texting";
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.NonPhysicalAddress 	phones[0:-1] 			"All phones. Any 'phoneForText' shoudl be here also";
				Privacy.General.AccessKey  															password[1:5] 			"password (history) - first is current, others are just so people don't repeat passwords";
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.Time.CalendarTime 	dob[1:1] 				"Date of birth";
				Privacy.General.Decision															privacyConsents[1:-1] 	"History of privacy consents";
				Privacy.General.Decision															termsCondsConsents[1:-1] "History of terms & conditions consents";				
				Insurance																			insurance[0:-1] 		"All the insurances the Customer has";
				QA																					qsAndAs[0:-1]			"All the questions asked by the customer (and their answers)";
			};

			/**
			 * Question content could be anything. Some content will be sensitive.
			 * 
			 * TODO: Do these questions have unique IDs? Or are they just addressed in list order? Generally, how do we deal with UniqueIDs -
			 * which are really an implementation detail
			 */
			centity QA "A customer question" {
				Question 																					question[1:1] 		"The question to answer";
				Privacy.People.Health																		medicalCondition[0:-1] "The medical condition this relates to, if any. Could be more than one";
				Answer   																					answer[0:-1]  		"The set of answers to the question";
				Privacy.General.Decision 																	decision[0:1] 		"If the question results in a decision, that is stored here.";
				QA																							subquestions[0:-1]  "One question may spur others";

				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.State.ConfigurationState 	sensitivity[0:1] 	"The sensitivity of this content. If not specified, this content is sensitive";
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.State.ValidityState 			validity[0:1] 		"How valid is this content. If not specified, the content is assumed to be valid";			
			};
			
			centity Advocate "A CommonCo advocate" : PPT.Conceptual.NaturalPerson {
				Customer																					customers[0:-1]		"All the customers for this Advocate";	
			};
			
			cassoc Insurance "Insurance Info" {
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.Identifier.Identifier group[1:1] "The group this is part of";
				Conceptual_Model.FACE_Shared_Data_Model_Conceptual.Observables.Identifier.Identifier member[1:1] "The member of the group";
				
				participants: [
				  PPT.Conceptual.LegalOrganization 	insuranceOrganization[1:1] "The insurance company" {src: [1:1]} ;
				  PPT.Conceptual.NaturalPerson 		insured[1:1] "The insured" {src: [1:1]} ;
				  PPT.Conceptual.NaturalPerson 		dependents[0:-1] "Dependents, if any. " {src: [1:1]} ;
				]														
			};
			

		}
		
		pdm Queries "The queries used for this model" {
			pquery FullCustomerRecord "All the Customer Data" {
				spec:"SELECT * FROM Customer"
			};
			pquery Login "Info needed for to log into an existing account" {
				spec:"SELECT Customer.name, Customer.password FROM Customer"
			};
			pquery BillingInfo "Info needed for billing" {
				spec: "SELECT Customer.name, Customer.address  FROM Customer"
				};
	 
			pquery Ack "Info needed for acknowledgement" {
				spec: "SELECT Customer.name FROM Customer"
			};

			pquery NewCustomerQuestion "Info for a question from Customer" {
				spec: "SELECT Customer.name, QA.question, QA.medicalCondition FROM Customer JOIN QA ON Customer.qsAndAs"
			};


			pquery RequestMore "Info needed for request" {
				spec: "SELECT QA.question as startingQ, QA.question as newQ FROM QA JOIN QA ON startingQ.subquestions = newQ"
			};
			
//			pquery AdvocateResponse "Question answered by Advocate" {
//				spec: "SELECT Customer.name, QA.* FROM Customer JOIN QA ON Customer.qsAndAs"
//			};
					
  			pquery RequestCustomerData "Info needed for customer Dashboard" {
				spec: "SELECT Customer.name FROM Customer"
			};

			/**
			 * Because this is just the answer associated with a question, it could be the main question or a subquestion -
			 * no matter who asks it.
			 */
			pquery NewAnswer "Info answering a question" {
				spec: "SELECT QA.question, QA.answer FROM QA "
			};
  
			pquery RequestListOfCustomers "All the customers associated with this Advocate" {
				spec: "SELECT Advocate.name FROM Advocate"
			};

			pquery RetrieveListOfCustomers "All the customers associated with this Advocate" {
				spec: "SELECT Customer.name FROM Customer JOIN Advocate ON Advocate.customers"
			};
			
			pquery RequestCustomerQuestionsList "Get all the questions associated with this Customer" {
				spec: "SELECT Customer.name FROM Customer"
			};
					
			pquery RetrieveCustomerQuestionsList "All info related to a specific customer question" {
				spec: "SELECT Customer.name, QA.question FROM Customer JOIN QA on Customer.qsAndAs"
			};

			pquery RequestQuestionDetail "Request detail on a question" {
				spec: "SELECT QA.question FROM QA"
			};
	
			pquery RetrieveQuestionDetail "All info related to a specific question" {
				spec: "SELECT QA.* FROM QA"
			};

		}
		
	}
	um Components "Software Components Used" {
		
		templ patientRegistration "This is the message where patients register for an appointment" {
			spec: " main (Patient) { Patient.name; Patient.id; Patient.phone; Patient.address; Patient.health; Patient.religion;}"
			bound: DataModel.Queries.BillingInfo
		};
		templ patientRegAck "This acknowledges the registration" {
			spec: " main (Patient) { Patient.name;  }"
			bound: DataModel.Queries.Ack 		
		};
		templ patientExternalActionLogging "The info used when logging a new appointment" {
			spec: "main (Patient) { Patient.name; Patient.id }"
			bound: DataModel.Queries.Ack
		};

		templ systemAck "The info used when ack'ing a xaction" {
			spec: "main (Patient) { Patient.name; Patient.id }"
			bound: DataModel.Queries.Ack
		};

		templ patientReview "This is the message where doctors review a patient record" {
			spec: " main (Patient) { Patient.name; Patient.id; Patient.health;  }"
			bound: DataModel.Queries.BillingInfo
		};

		templ patientReviewAck "This acknowledges a patient record review" {
			spec: " main (Patient) { Patient.name; Patient.id; Patient.health;  }"
			bound: DataModel.Queries.Ack
		};

		templ patientReviewLogging "The info used when logging doctor's/ chaplain's review" {
			spec: "main (Patient, Employee) { Patient.name; Patient.id; Patient.health; }"
			bound: DataModel.Queries.FullCustomerRecord 
		}; 
		
		templ patientChaplainReview "This is the message where chaplains review a patient record" {
			spec: " main (Patient) { Patient.name; Patient.id; Patient.health; Patient.religion }"
			bound: DataModel.Queries.BillingInfo
		};

		templ DBLogging "General DB Logging" {
			spec: "main (DBLogging) { DBLogging.timestamp, DBLogging.processID, DBLogging.version, DBLogging.status }"
			bound: DataModel.Queries.FullCustomerRecord
		};	
			
		pc CreateAnAccount "This is where new Customers sign up" {
			lang: Ada
			
			conn: [ 
					csconn NewAccountInfo "Send new accnt info / ack back" {
						msg:  [ patientRegistration  /  patientRegAck ] 
						role: client 
					} 
			]  
		};   
		   
		pc LogIn "This is where existing Customers sign in" {
			lang: Java
			conn: [ 
					csconn LoginInfo "Send login info / ack back" {
						msg:  [ patientReview /  systemAck ] 
						role: client 
					} 
			]  
		}; 
			     
		pc SubmitNewCustomerForm "This is where existing Customers create their initial  info record" {
			lang: C
			conn: [ 
					/** This could also be the billing info because of how the new customer is set up */
					simconn Login "Receiving a successful login" {
						msg: InboundMessage  patientRegistration 
					}
					csconn CustomerInfo "Send base customer info / ack back" {
						msg:  [ patientReview / systemAck ] 
						role: client 
					} 
			]  
		}; 
			     
		pc SubmitCustomerRequestForm "This is where existing Customers add a new request" {
			lang: Scala
			conn: [ 
					simconn Login "Receiving a successful login" {
						msg: InboundMessage  patientRegistration
					}
					csconn RequestInfo "Send new request / ack back" {
						msg:  [ patientReview / patientReviewAck ] 
						role: client 
					} 
			]  
		};      

		pc AdvocateReview "Advocate reviews requests - " {
			lang: Rust
			conn: [ 
					simconn Advocate "Send customer rec and request info " {
						msg: InboundMessage    patientChaplainReview   
					} 

					csconn RequestListOfCustomers "Get the outstanding list of customers for this advocate" {
						msg: [ patientChaplainReview / DataModel.Queries.RetrieveCustomerQuestionsList ]
						role: client
					}
					
					csconn RequestCustomerInfo "Get the outstanding list of questions for a specific customer" {
						msg: [  DataModel.Queries.RequestCustomerData /  DataModel.Queries.RetrieveCustomerQuestionsList ]
						role: client
					}

					csconn RequestQuestionList "Get my list of questions/ send list back back" {
						msg:  [  DataModel.Queries.RequestCustomerData  /  DataModel.Queries.RetrieveCustomerQuestionsList ] 
						role: client 
					} 

					csconn RequestQuestionDetails "Get the details on a specific question" {
						msg: [  DataModel.Queries.RequestQuestionDetail  /  DataModel.Queries.RetrieveQuestionDetail ]
						role:  client
					}

					simconn MoreInfoRequest "Request more info from customer - this creates a new subquestion" {
						msg: OutboundMessage  DataModel.Queries.RequestMore 
					}
					
					simconn AdvResponse "Advocate sends response back to customer" {
						msg: OutboundMessage  DataModel.Queries.NewAnswer
					}
				]  
			};      

		pc CustomerDashboard "Where the customer can see everything" {
			lang: Typescript
			conn: [ 
					csconn RequestCustomerInfo "Request my customer info  / send it back  - used for account mgt" {
						msg:  [  DataModel.Queries.RequestCustomerData  /  DataModel.Queries.FullCustomerRecord ] 
						role: client 
					} 
					csconn RequestQuestionList "Get my list of questions/ send list back back" {
						msg:  [  DataModel.Queries.RequestCustomerData  /  DataModel.Queries.RetrieveCustomerQuestionsList ] 
						role: client 
					} 

					csconn RequestQuestionDetails "Get details for a specific question/ send it back" {
						msg:  [  DataModel.Queries.RequestQuestionDetail  /  DataModel.Queries.RetrieveQuestionDetail ] 
						role: client 
					} 

					simconn SendAnswer "Send the  answer" {
							msg: OutboundMessage  DataModel.Queries.NewAnswer  	
					}

			]  
		};	
		
		pc Datastore "The component that handles persistent storage" {
			lang: Unspecified
			conn: [
					csconn StoreNewLogin "store received new customer billing info (full record not there yet)" {
							msg:  [  DataModel.Queries.BillingInfo  /  DataModel.Queries.Ack ] 
							role: server 		
					}
					csconn RetrieveLogin "retrieve login info " {
							msg:  [  DataModel.Queries.Ack  /  DataModel.Queries.Login ] 
							role: server 		
					}

					csconn StoreCustomerInfo "store received customer info" {
							msg:  [  DataModel.Queries.FullCustomerRecord  /  DataModel.Queries.Ack ] 
							role: server 		
					}
	
					csconn StoreCustomerQ "store received customer question" {
							msg:  [  DataModel.Queries.NewCustomerQuestion  /  DataModel.Queries.Ack ] 
							role: server 		
					}

					csconn RequestListOfCustomers "Get the outstanding list of customers for this advocate" {
						msg: [  DataModel.Queries.RequestListOfCustomers /  DataModel.Queries.RetrieveCustomerQuestionsList ]
						role: server
					}
					
					csconn RequestCustomerInfo "Get the outstanding list of questions for a specific customer" {
						msg: [  DataModel.Queries.RequestCustomerData /  DataModel.Queries.RetrieveCustomerQuestionsList ]
						role: server
					}

					csconn RequestQuestionList "Get my list of questions/ send list back back" {
						msg:  [  DataModel.Queries.RequestCustomerData  /  DataModel.Queries.RetrieveCustomerQuestionsList ] 
						role: client 
					} 

					csconn RequestQuestionDetails "Get the details on a specific question" {
						msg: [  DataModel.Queries.RequestQuestionDetail  /  DataModel.Queries.RetrieveQuestionDetail ]
						role:  server
					}

					simconn StoreAnswer "store received answer" {
							msg: InboundMessage  DataModel.Queries.NewAnswer  	
					}
				
			]
		};
	}

	/**
	 * TODO: Need a mechanism to easily create one instance of each PC/PSC in associated UM automatically
	 * so we don't have to manually create them. That should also default to use all the inputs and outputs
	 * defined in the component. (Its easier to delete stuff you don't want than add it)
	 * 
	 * TODO: Each Integration creates a business flow. So this should be associated with a business type.
	 * 
	 * TODO: Are there standard functions (like logging) that we should ask about? That might depend on business type
	 * (e.g. HIPAA may require certain auditing functions.)
	 * 
	 * TODO: Do we attempt to detect if some flows require synchronization?
	 */
	im Integration "CommonCo Integration / wiring diagram" {
		/**
		 * The primary instances will run in California
		 */
		uinst CreateAnAccount1 "An Instance of the CreateAnAccount Page" -> Components.CreateAnAccount {
			output: [ Components.CreateAnAccount.NewAccountInfo]
		};
	
		uinst Login1 "An Instance of the Login Page" -> Components.LogIn {
			output: [ Components.LogIn.LoginInfo ]
		};
	
		uinst SubmitNewCustomerForm1 "An Instance of the New Customer Info page" -> Components.SubmitNewCustomerForm {
			input: [ Components.SubmitNewCustomerForm.Login]
			output: [ Components.SubmitNewCustomerForm.CustomerInfo ]
		};
	
		uinst SubmitCustomerRequestForm1 "An Instance of the Customer New Request form" -> Components.SubmitCustomerRequestForm {
			input: [Components.SubmitCustomerRequestForm.Login]
			output: [ Components.SubmitCustomerRequestForm.RequestInfo ]
		};
	
		uinst AdvocateReview1 "An Instance of the Advocate Review Dashboard" -> Components.AdvocateReview {
			input: [ Components.AdvocateReview.Advocate  ]
			output: [ 
				Components.AdvocateReview.RequestListOfCustomers
				Components.AdvocateReview.RequestCustomerInfo
				Components.AdvocateReview.RequestQuestionDetails			
				Components.AdvocateReview.MoreInfoRequest
				Components.AdvocateReview.AdvResponse 
				Components.AdvocateReview.RequestQuestionList			
			]
		};

		uinst CustomerDashboard1 "An Instance of the Customer Dashboard" -> Components.CustomerDashboard {
			output: [ 
				Components.CustomerDashboard.RequestCustomerInfo
				Components.CustomerDashboard.RequestQuestionList
				Components.CustomerDashboard.RequestQuestionDetails			
				Components.CustomerDashboard.SendAnswer			
			]
		};

		uinst Datastore1 "An Instance of the Datastore" -> Components.Datastore {
			/**
			 * TODO: verify that C/S can be on both input and output
			 */
			input: [
				Components.Datastore.StoreNewLogin
				Components.Datastore.RetrieveLogin
				Components.Datastore.StoreCustomerInfo 
				Components.Datastore.StoreCustomerQ
				Components.Datastore.RequestListOfCustomers 
				Components.Datastore.RequestCustomerInfo 				
				Components.Datastore.RequestQuestionList 				
				Components.Datastore.RequestQuestionDetails 	
				Components.Datastore.StoreAnswer 	// This last one is a simconn, so no corresponding output						
			]
		};
	
		ic NorthAmericanWiring "The wiring diagram for NA" {
			conn: [
				( U2U CommonCo.Integration.CreateAnAccount1.output0 -> CommonCo.Integration.Datastore1.input0)
				( U2U CommonCo.Integration.CreateAnAccount1.output0 -> CommonCo.Integration.SubmitNewCustomerForm1.input0)
				
				( U2U CommonCo.Integration.Login1.output0 -> CommonCo.Integration.SubmitCustomerRequestForm1.input0)
				
				( U2U CommonCo.Integration.SubmitNewCustomerForm1.output0 -> CommonCo.Integration.Datastore1.input2)
				
				( U2U CommonCo.Integration.SubmitCustomerRequestForm1.output0 -> CommonCo.Integration.Datastore1.input2)
				( U2U CommonCo.Integration.SubmitCustomerRequestForm1.output0 -> CommonCo.Integration.AdvocateReview1.input0)
				
				( U2U CommonCo.Integration.AdvocateReview1.output0 -> CommonCo.Integration.Datastore1.input4)
				( U2U CommonCo.Integration.AdvocateReview1.output1 -> CommonCo.Integration.Datastore1.input5)
				( U2U CommonCo.Integration.AdvocateReview1.output2 -> CommonCo.Integration.Datastore1.input7)
				( U2U CommonCo.Integration.AdvocateReview1.output3 -> CommonCo.Integration.Datastore1.input3)
				( U2U CommonCo.Integration.AdvocateReview1.output4 -> CommonCo.Integration.Datastore1.input8)
				( U2U CommonCo.Integration.AdvocateReview1.output5 -> CommonCo.Integration.Datastore1.input6)

				( U2U CommonCo.Integration.CustomerDashboard1.output0 -> CommonCo.Integration.Datastore1.input5)
				( U2U CommonCo.Integration.CustomerDashboard1.output1 -> CommonCo.Integration.Datastore1.input6)
				( U2U CommonCo.Integration.CustomerDashboard1.output2 -> CommonCo.Integration.Datastore1.input7)
				( U2U CommonCo.Integration.CustomerDashboard1.output3 -> CommonCo.Integration.Datastore1.input8)
				
			]
		}
			
	
		/**
		 * A second set of instances that can be used as backups - if we choose to create them
		 */
/*		 
		uinst CreateAnAccount2 "An Backup Instance of the CreateAnAccount Page" -> Components.CreateAnAccount {
			location: Privacy.General.CommonJurisdictions.EuropeanUnion.Ireland
			output:  [ Components.CreateAnAccount.NewAccountInfo ] 
		};
 */

	}
}

		''')
		Assertions.assertNotNull(result)
		val errors = result.eResource.errors
		Assertions.assertTrue(errors.isEmpty, '''Unexpected errors: «errors.join(", ")»''')
	}
	
}
