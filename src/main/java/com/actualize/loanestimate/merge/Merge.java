package com.actualize.loanestimate.merge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.SyncFailedException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.actualize.loanestimate.mismodao.Fee;
import com.actualize.loanestimate.mismodao.FeeDetail;
import com.actualize.loanestimate.mismodao.Fees;
import com.actualize.loanestimate.mismodao.MISMODocument;
import com.actualize.loanestimate.mismodao.PrepaidItem;
import com.actualize.loanestimate.mismodao.PrepaidItemDetail;
import com.actualize.loanestimate.mismodao.PrepaidItems;

public class Merge {
    private static final Logger LOGGER = Logger.getLogger(Merge.class.getName());

    class IgnoreList {
        private Set<String> ignoreSet = new TreeSet<String>();
        
        private String key(String container, String type, String typeOtherDescription, String section) {
        	String str = container + "|" + type + "|" + typeOtherDescription + "|" + section;
        	return str.replaceAll("\\s+","");
        }
        
        public void add(String container, String type, String typeOtherDescription, String section) {
        	ignoreSet.add(key(container, type, typeOtherDescription, section));
        }
        
        public boolean contains(String container, String type, String typeOtherDescription, String section) {
        	return ignoreSet.contains(key(container, type, typeOtherDescription, section));
        }
    }
    private IgnoreList ignoreList = new IgnoreList();
    
    public Merge() {
    }
    
    public Merge(String ignorefile) {
    	BufferedReader br = null;
    	String line = "";
    	int linenum = 1;
    	try {
    		br = new BufferedReader(new FileReader(ignorefile));
    		while ((line = br.readLine()) != null) {
    			String[] tokens = line.split(",", -1);
    			if (tokens.length < 4)
    				System.err.println("Warning: Problem with merge ignore file line " + linenum + ". Only " + tokens.length + " fields.");
    			else
    				ignoreList.add(tokens[0], tokens[1], tokens[2], tokens[3]);
    			linenum++;
    		}
    	} catch (FileNotFoundException e) {
    		LOGGER.log(Level.WARNING, "Merge ignore file not found", e);
    	} catch (IOException e) {
    		LOGGER.log(Level.WARNING, "Merge ignore file error", e);
    	} finally {
    		if (br != null) {
    			try {
    				br.close();
    			} catch (IOException e) {
    	    		LOGGER.log(Level.WARNING, "Merge ignore file close error", e);
    			}
    		}
    	}
    }
    
//    private boolean addEscrow(EscrowItems escrows, EscrowItem escrow) {
//    	EscrowItemDetail detail_2 = escrow.escrowItemDetail;
//    	if (ignoreList.contains("ESCROW_ITEM", detail_2.EscrowItemType, detail_2.EscrowItemTypeOtherDescription, detail_2.IntegratedDisclosureSectionType))
//    		return false;
//    	for (int i = 0; i < escrows.escrowItems.length; i++) {
//    		EscrowItemDetail detail_1 = escrows.escrowItems[i].escrowItemDetail;
//    		if (!detail_1.EscrowItemType.equals(detail_2.EscrowItemType))
//    			continue;
//    		if (detail_1.EscrowItemType.equals("Other") && !detail_1.EscrowItemTypeOtherDescription.equals(detail_2.EscrowItemTypeOtherDescription))
//    			continue;
//    		if (!detail_1.IntegratedDisclosureSectionType.equals(detail_2.IntegratedDisclosureSectionType))
//    			continue;
//    		return false;
//    	}
//    	return true;
//    }
    
    private boolean addFee(Fees fees, Fee fee) {
		FeeDetail detail_2 = fee.feeDetail;
    	if (ignoreList.contains("FEE", detail_2.FeeType, detail_2.FeeTypeOtherDescription, detail_2.IntegratedDisclosureSectionType))
    		return false;
//    	for (int i = 0; i < fees.fees.length; i++) {
//    		FeeDetail detail_1 = fees.fees[i].feeDetail;
//    		if (!detail_1.DisplayLabelText.equals(detail_2.DisplayLabelText))
//    			continue;
//    		if (!detail_1.FeeType.equals(detail_2.FeeType))
//    			continue;
//    		if (detail_1.FeeType.equals("Other") && !detail_1.FeeTypeOtherDescription.equals(detail_2.FeeTypeOtherDescription))
//    			continue;
//    		if (!detail_1.IntegratedDisclosureSectionType.equals(detail_2.IntegratedDisclosureSectionType))
//    			continue;
//    		return false;
//    	}
    	return true;
    }
    
    private boolean addPrepaid(PrepaidItems prepaids, PrepaidItem prepaid) {
    	PrepaidItemDetail detail_2 = prepaid.prepaidItemDetail;
    	if (ignoreList.contains("PREPAID_ITEM", detail_2.PrepaidItemType, detail_2.PrepaidItemTypeOtherDescription, detail_2.IntegratedDisclosureSectionType))
    		return false;
    	for (int i = 0; i < prepaids.prepaidItems.length; i++) {
    		PrepaidItemDetail detail_1 = prepaids.prepaidItems[i].prepaidItemDetail;
    		if (!detail_1.PrepaidItemType.equals(detail_2.PrepaidItemType))
    			continue;
    		if (detail_1.PrepaidItemType.equals("Other") && !detail_1.PrepaidItemTypeOtherDescription.equals(detail_2.PrepaidItemTypeOtherDescription))
    			continue;
    		return false;
    	}
    	return true;
    }

    void merge(MISMODocument doc_1, MISMODocument doc_2) {
    	mergeEstimatedPropertyCost(doc_1, doc_2.getElementAddNS("//ESTIMATED_PROPERTY_COST"));
    	mergeCashToClose(doc_1, doc_2.getElementsAddNS("//CASH_TO_CLOSE_ITEM"));
    	mergeEscrows(doc_1, doc_2);
    	mergeFees(doc_1.getElementAddNS("//FEES"), doc_2.getElementsAddNS("//FEE"));
    	mergePrepaids(doc_1.getElementAddNS("//PREPAID_ITEMS"), doc_2.getElementsAddNS("//PREPAID_ITEM"));
    	mergeContactInfo(doc_1, doc_2);
    	mergeDocumemtType(doc_1, doc_2);
    }
    
    void mergeCashToClose(MISMODocument doc_1, NodeList cashToCloseItems) {
		Node integratedDisclosure = doc_1.getElementAddNS("//INTEGRATED_DISCLOSURE");
		Document document = integratedDisclosure.getOwnerDocument();
		Node cashToClose = doc_1.getElementAddNS("//INTEGRATED_DISCLOSURE/CASH_TO_CLOSE_ITEMS");
		if (cashToClose != null)
			cashToClose.getParentNode().removeChild(cashToClose);
		Element elem = document.createElementNS("http://www.mismo.org/residential/2009/schemas", "CASH_TO_CLOSE_ITEMS");
		cashToClose = integratedDisclosure.insertBefore(elem, integratedDisclosure.getFirstChild());
		for (int i = 0; i < cashToCloseItems.getLength(); i++) {
			Node cashToCloseItem = cashToCloseItems.item(i);
			cashToClose.appendChild(document.importNode(cashToCloseItem, true));
		}
    }

//    private void mergeEscrow(EscrowItems escrows, EscrowItem escrow) {
//    	if (addEscrow(escrows, escrow)) {
//			Node escrowNode = (Node)escrows.element;
//			Document document = escrowNode.getOwnerDocument();
//			escrowNode.appendChild(document.importNode((Node)escrow.element, true));
//		}
//    }
    
    void mergeEstimatedPropertyCost(MISMODocument doc_1, Node doc2estimatedPropertyCost) {
		Node integratedDisclosure = doc_1.getElementAddNS("//INTEGRATED_DISCLOSURE");
		Document document = integratedDisclosure.getOwnerDocument();
		Node tmp = doc_1.getElementAddNS("//ESTIMATED_PROPERTY_COST");
		if (doc2estimatedPropertyCost != null)
			if (tmp == null)
				integratedDisclosure.appendChild(document.importNode(doc2estimatedPropertyCost, true));
			else
				integratedDisclosure.replaceChild(document.importNode(doc2estimatedPropertyCost, true), tmp);
    }

    private void mergeEscrows(MISMODocument doc_1, MISMODocument doc_2) {
	if (doc_1.getElementAddNS("//ESCROW/ESCROW_DETAIL/EscrowAggregateAccountingAdjustmentAmount") == null &&
			doc_2.getElementAddNS("//ESCROW/ESCROW_DETAIL/EscrowAggregateAccountingAdjustmentAmount") != null) {
		Node escrow = doc_1.getElementAddNS("//ESCROW");
		Node adjustment = doc_2.getElementAddNS("//ESCROW/ESCROW_DETAIL");
	    insertNode(escrow, adjustment);
	}
//    	EscrowItems escrowItems = new EscrowItems((Element)escrows);
//    	for (int i = 0; i < escrowlist.getLength(); i++) {
//    		Node node = escrowlist.item(i);
//    		String tagname = ((Element)node).getTagName();
//    		if (tagname.equals("ESCROW_ITEM") || tagname.endsWith(":ESCROW_ITEM"))
//    			mergeEscrow(escrowItems, new EscrowItem((Element)node));
//    	}
    }

    private void mergeFee(Fees fees, Fee fee) {
    	if (addFee(fees, fee)) {
			Node feeNode = (Node)fees.element;
			Document document = feeNode.getOwnerDocument();
			feeNode.appendChild(document.importNode((Node)fee.element, true));
		}
    }

    private void mergeFees(Node fees, NodeList feelist) {
    	for (int i = 0; i < feelist.getLength(); i++) {
    		Node node = feelist.item(i);
    		String tagname = ((Element)node).getTagName();
    		if (tagname.equals("FEE") || tagname.endsWith(":FEE"))
    			mergeFee(new Fees((Element)fees), new Fee((Element)node));
    	}
    }

    private void mergePrepaid(PrepaidItems prepaids, PrepaidItem prepaid) {
    	if (addPrepaid(prepaids, prepaid)) {
			Node prepaidNode = (Node)prepaids.element;
			Document document = prepaidNode.getOwnerDocument();
			prepaidNode.appendChild(document.importNode((Node)prepaid.element, true));
		}
    }

    private void mergePrepaids(Node prepaids, NodeList prepaidlist) {
    	for (int i = 0; i < prepaidlist.getLength(); i++) {
    		Node node = prepaidlist.item(i);
    		String tagname = ((Element)node).getTagName();
    		if (tagname.equals("PREPAID_ITEM") || tagname.endsWith(":PREPAID_ITEM"))
    			mergePrepaid(new PrepaidItems((Element)prepaids), new PrepaidItem((Element)node));
    	}
    }
    
    private void addNodes(Node node, NodeList nodesToBeAdded) {
    	if (node != null && nodesToBeAdded != null)
			for (int i = 0; i < nodesToBeAdded.getLength(); i++)
				node.appendChild(node.getOwnerDocument().importNode(nodesToBeAdded.item(i), true));
    }
    
    private void insertNode(Node node, Node nodeToBeAdded) {
    	if (node != null && nodeToBeAdded != null)
			node.insertBefore(node.getOwnerDocument().importNode(nodeToBeAdded, true), node.getFirstChild());
    }
    
    private void removeNodes(NodeList childrenToBeRemoved) {
    	if (childrenToBeRemoved != null)
    		for (int i = 0; i < childrenToBeRemoved.getLength(); i++)
    			childrenToBeRemoved.item(i).getParentNode().removeChild(childrenToBeRemoved.item(i));
    }
    
    private void mergeContactInfo(MISMODocument doc_1, MISMODocument doc_2) {
    	Node parties = doc_1.getElementAddNS("//PARTIES");
		removeNodes(doc_2.getElementsAddNS("//PARTY[ROLES/ROLE/ROLE_DETAIL/PartyRoleType='Borrower']"));
    	if (doc_1.getElementsAddNS("//PARTY[INDIVIDUAL][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='NotePayTo']") != null)
    		removeNodes(doc_2.getElementsAddNS("//PARTY[INDIVIDUAL][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='NotePayTo']"));
    	if (doc_1.getElementsAddNS("//PARTY[LEGAL_ENTITY][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='NotePayTo']") != null)
    		removeNodes(doc_2.getElementsAddNS("//PARTY[LEGAL_ENTITY][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='NotePayTo']"));
    	if (doc_1.getElementsAddNS("//PARTY[INDIVIDUAL][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='ClosingAgent']") != null)
    		removeNodes(doc_2.getElementsAddNS("//PARTY[INDIVIDUAL][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='ClosingAgent']"));
    	if (doc_1.getElementsAddNS("//PARTY[LEGAL_ENTITY][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='ClosingAgent']") != null)
    		removeNodes(doc_2.getElementsAddNS("//PARTY[LEGAL_ENTITY][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='ClosingAgent']"));
    	if (doc_1.getElementsAddNS("//PARTY[INDIVIDUAL][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='MortgageBroker']") != null) {
    		if (doc_1.getElementAddNS("//PARTY[ROLES/ROLE/ROLE_DETAIL/PartyRoleType='MortgageBroker']/INDIVIDUAL/CONTACT_POINTS") == null) {
    			Node settlementAgent = doc_1.getElementAddNS("//PARTY[ROLES/ROLE/ROLE_DETAIL/PartyRoleType='MortgageBroker']/INDIVIDUAL");
    			Node contactPoints = doc_2.getElementAddNS("//PARTY[ROLES/ROLE/ROLE_DETAIL/PartyRoleType='MortgageBroker']/INDIVIDUAL/CONTACT_POINTS");
    		    insertNode(settlementAgent, contactPoints);
    		}
    		removeNodes(doc_2.getElementsAddNS("//PARTY[INDIVIDUAL][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='MortgageBroker']"));
    	}
    	if (doc_1.getElementsAddNS("//PARTY[LEGAL_ENTITY][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='MortgageBroker']") != null)
    		removeNodes(doc_2.getElementsAddNS("//PARTY[LEGAL_ENTITY][ROLES/ROLE/ROLE_DETAIL/PartyRoleType='MortgageBroker']"));
    	addNodes(parties, doc_2.getElementsAddNS("//PARTY"));
    }
    
    private void mergeDocumemtType(MISMODocument doc_1, MISMODocument doc_2) {
    	Node node = doc_1.getElementAddNS("//INTEGRATED_DISCLOSURE/INTEGRATED_DISCLOSURE_DETAIL");
    	Node nodeTypeOtherDescription = doc_2.getElementAddNS("//DOCUMENT/DOCUMENT_CLASSIFICATION/DOCUMENT_CLASSES/DOCUMENT_CLASS/DocumentTypeOtherDescription");
    	insertNode(node, nodeTypeOtherDescription);
    	Node nodeType = doc_2.getElementAddNS("//DOCUMENT/DOCUMENT_CLASSIFICATION/DOCUMENT_CLASSES/DOCUMENT_CLASS/DocumentType");
    	insertNode(node, nodeType);
    }
    
    private static String usage() {
    	return "Merge <infile1>.xml <infile2>.xml <outfile>.xml [<ignorefile>.txt]";
    }
	
	private static void writeDocument(Document document, String outfile) throws TransformerFactoryConfigurationError, TransformerException, SyncFailedException, IOException {
		FileOutputStream out = new FileOutputStream(outfile);
		Transformer tr = TransformerFactory.newInstance().newTransformer();
	    tr.setOutputProperty(OutputKeys.INDENT, "yes");
	    tr.setOutputProperty(OutputKeys.METHOD, "xml");
	    tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	    tr.transform(new DOMSource(document), new StreamResult(out));
		out.getFD().sync();
		out.close();
	}

	public static void main(String args[]) throws Exception {
		LOGGER.log(Level.FINE, "Start time: " + new Date().toString());

		// Parse command line arguments
		if (args.length < 3 || args.length > 4) {
			LOGGER.log(Level.ALL, usage());
			System.exit(0);
		}

		// Read MISMO document one
		LOGGER.log(Level.FINE, "Reading MISMO file 1 '" + args[0] + "'...");
		MISMODocument doc_1 = new MISMODocument(args[0]);
		
		// Read MISMO document two
		LOGGER.log(Level.FINE, "Reading MISMO file 2 '" + args[1] + "'...");
		MISMODocument doc_2 = new MISMODocument(args[1]);

		LOGGER.log(Level.FINE, "Merging documents");
		Merge merge = new Merge();
		if (args.length == 4)
			merge = new Merge(args[3]);
		merge.merge(doc_1, doc_2);

		LOGGER.log(Level.FINE, "Writing MISMO file '" + args[2] + "'...");
		Merge.writeDocument(((Node)doc_1.element).getOwnerDocument(), args[2]);
		
		LOGGER.log(Level.FINE, "End time: " + new Date().toString());
	}

}
