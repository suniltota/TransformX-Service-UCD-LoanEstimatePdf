package com.actualize.mortgage.leform;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.actualize.mortgage.mismodao.Deal;
import com.actualize.mortgage.mismodao.DocumentClass;
import com.actualize.mortgage.mismodao.DocumentClassification;
import com.actualize.mortgage.mismodao.LoanIdentifier;
import com.actualize.mortgage.mismodao.MISMODocument;
import com.actualize.mortgage.mismodao.TermsOfLoan;
import com.actualize.mortgage.pdferector.Page;

public class LoanEstimate {
	static public final Encoder encoder = Base64.getEncoder();
	static public final Decoder decoder = Base64.getDecoder();
	
	public Document run(InputStream in, boolean validate) throws ParserConfigurationException, IOException, SAXException {
		// Create the results document and root element ("Results")
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document xmldoc = builder.newDocument();
		Element resultsElement = xmldoc.createElement("Results");
        xmldoc.appendChild(resultsElement);
        Element statusElement = xmldoc.createElement("Status");
        resultsElement.appendChild(statusElement);
        
        // Add the start and end times
		Element startElement = xmldoc.createElement("StartTime");
		resultsElement.appendChild(startElement);
		Element endElement = xmldoc.createElement("EndTime");
		resultsElement.appendChild(endElement);

		// Stamp the start time
		startElement.appendChild(xmldoc.createTextNode(new Date().toString()));

    	// Save input stream into ByteArrayOutputStream (needed for multiple purposes, e.g. validation and input)
    	ByteArrayOutputStream baos = inputStreamToByteArrayOutputStream(in);
		in.close();

    	// Validate incoming xml
		// TODO implement validate
//		try {
//			if (validate)
//				MISMOValidation.validateXML(new ByteArrayInputStream(baos.toByteArray()));
//		} catch (Exception e) { // Oops, ERROR!
//			insertError(e, xmldoc, resultsElement, statusElement);
//		}

		// Read data
		MISMODocument data = new MISMODocument(new ByteArrayInputStream(baos.toByteArray()));
	    insertPdfResults(data, xmldoc, resultsElement);

		// Stamp the end time
    	endElement.appendChild(xmldoc.createTextNode(new Date().toString()));

    	// Set status to success
    	statusElement.appendChild(xmldoc.createTextNode("Success"));
    	return xmldoc;
	}

	public void run(MISMODocument data, OutputStream out) throws COSVisitorException, IOException {
		ArrayList<Page> pages = new ArrayList<Page>();
		Page page = null;

		// Grab deal and loan identifier
		Deal deal = null;
		NodeList nodes = data.getElementsAddNS("//DEAL");
		NodeList documentClassificationNodes = data.getElementsAddNS("//DOCUMENT_CLASSIFICATION");
		
		if (nodes.getLength() > 0)
			deal = new Deal(Deal.NS, (Element)nodes.item(0));
		LoanIdentifier loanIdentifier = new LoanIdentifier((Element)deal.getElementAddNS("LOANS/LOAN/LOAN_IDENTIFIERS/LOAN_IDENTIFIER[LoanIdentifierType='LenderLoan']"));
		TermsOfLoan termsOfLoan = new TermsOfLoan((Element)deal.getElementAddNS("LOANS/LOAN/TERMS_OF_LOAN[LoanPurposeType='Refinance']"));

		DocumentClassification documentClassification = null;
		if(documentClassificationNodes.getLength()>0)
		    documentClassification = new DocumentClassification(DocumentClassification.NS, (Element)documentClassificationNodes.item(0));
		
		DocumentClass documentClass = new DocumentClass((Element)documentClassification.getElementAddNS("DOCUMENT_CLASSES/DOCUMENT_CLASS[DocumentType='Other']"));
		
		// Create document
		PDDocument doc = new PDDocument();
		
		try {
	
			// Add page 1
			page = new Page(doc);
			pages.add(page);
			page.addSection(new LoanEstimateSection());
			page.addSection(new LoanTermsSection());
			page.addSection(new ProjectedPaymentsSection());
			page.addSection(new CostsAtClosingSection(page, deal));
			page.addSection(new FooterSection(page, 1, loanIdentifier.LoanIdentifier));
	
			// Add page 2
			page = new Page(doc);
			pages.add(page);
			page.addSection(new LoanCostsSection(page, null));
			page.addSection(new APTableSection(page, null));
			page.addSection(new OtherCostsSection(page, null));
			
			// Logic to identify the refinance transaction
			// As per USB UCD-144 changing logic to render alternate form based on document type
			/*if("Refinance".equalsIgnoreCase(termsOfLoan.LoanPurposeType))
			    page.addSection(new CashToCloseSectionRefinance(page, deal));
			else
			    page.addSection(new CashToCloseSection(page, null));*/
			if("LoanEstimate:AlternateForm".equalsIgnoreCase(documentClass.documentTypeOtherDescription))
			    page.addSection(new CashToCloseSectionRefinance(page, deal));
			else
			    page.addSection(new CashToCloseSection(page, null));
			
			page.addSection(new AIRTableSection(page, null));
			page.addSection(new FooterSection(page, 2, loanIdentifier.LoanIdentifier));
	
			// Add page 3
			page = new Page(doc);
			pages.add(page);
			page.addSection(new AdditionalInformationSection(page, null));
			page.addSection(new ComparisonsSection(page, null));
			page.addSection(new OtherConsiderationsSection(page, null));
			page.addSection(new ConfirmReceiptSection(page, null));
			page.addSection(new FooterSection(page, 3, loanIdentifier.LoanIdentifier));
	
			// Add addendum
			if (!LoanEstimateSection.additionalApplicants(deal).isEmpty()) {
				page = new Page(doc);
				pages.add(page);
				page.addSection(new AddendumSection(page, null));
				page.addSection(new AddendumFooterSection(page, 3, loanIdentifier.LoanIdentifier));
			}
	
			// Render and save
			for (int i = 0; i < pages.size(); i++)
				pages.get(i).render(doc, deal);
			doc.save(out);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			doc.close();
		}
	}
	
    public ByteArrayOutputStream getPDFDocument(MISMODocument data) throws COSVisitorException, IOException {
        ArrayList<Page> pages = new ArrayList<Page>();
        Page page = null;

        // Grab deal and loan identifier
        Deal deal = null;
        NodeList nodes = data.getElementsAddNS("//DEAL");
        NodeList documentClassificationNodes = data.getElementsAddNS("//DOCUMENT_CLASSIFICATION");

        if (nodes.getLength() > 0)
            deal = new Deal(Deal.NS, (Element) nodes.item(0));
        LoanIdentifier loanIdentifier = new LoanIdentifier(
                (Element) deal.getElementAddNS("LOANS/LOAN/LOAN_IDENTIFIERS/LOAN_IDENTIFIER[LoanIdentifierType='LenderLoan']"));
        TermsOfLoan termsOfLoan = new TermsOfLoan((Element) deal.getElementAddNS("LOANS/LOAN/TERMS_OF_LOAN[LoanPurposeType='Refinance']"));

        DocumentClassification documentClassification = null;
        if (documentClassificationNodes.getLength() > 0)
            documentClassification = new DocumentClassification(DocumentClassification.NS, (Element) documentClassificationNodes.item(0));

        DocumentClass documentClass = new DocumentClass(
                (Element) documentClassification.getElementAddNS("DOCUMENT_CLASSES/DOCUMENT_CLASS[DocumentType='Other']"));

        // Create document
        PDDocument doc = new PDDocument();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {

            // Add page 1
            page = new Page(doc);
            pages.add(page);
            page.addSection(new LoanEstimateSection());
            page.addSection(new LoanTermsSection());
            page.addSection(new ProjectedPaymentsSection());
            page.addSection(new CostsAtClosingSection(page, deal));
            page.addSection(new FooterSection(page, 1, loanIdentifier.LoanIdentifier));

            // Add page 2
            page = new Page(doc);
            pages.add(page);
            page.addSection(new LoanCostsSection(page, null));
            page.addSection(new APTableSection(page, null));
            page.addSection(new OtherCostsSection(page, null));

            // Logic to identify the refinance transaction
            // As per USB UCD-144 changing logic to render alternate form based
            // on document type
            /*
             * if("Refinance".equalsIgnoreCase(termsOfLoan.LoanPurposeType))
             * page.addSection(new CashToCloseSectionRefinance(page, deal));
             * else page.addSection(new CashToCloseSection(page, null));
             */
            if ("LoanEstimate:AlternateForm".equalsIgnoreCase(documentClass.documentTypeOtherDescription))
                page.addSection(new CashToCloseSectionRefinance(page, deal));
            else
                page.addSection(new CashToCloseSection(page, null));

            page.addSection(new AIRTableSection(page, null));
            page.addSection(new FooterSection(page, 2, loanIdentifier.LoanIdentifier));

            // Add page 3
            page = new Page(doc);
            pages.add(page);
            page.addSection(new AdditionalInformationSection(page, null));
            page.addSection(new ComparisonsSection(page, null));
            page.addSection(new OtherConsiderationsSection(page, null));
            page.addSection(new ConfirmReceiptSection(page, null));
            page.addSection(new FooterSection(page, 3, loanIdentifier.LoanIdentifier));

            // Add addendum
            if (!LoanEstimateSection.additionalApplicants(deal).isEmpty()) {
                page = new Page(doc);
                pages.add(page);
                page.addSection(new AddendumSection(page, null));
                page.addSection(new AddendumFooterSection(page, 3, loanIdentifier.LoanIdentifier));
            }

            // Render and save
            for (int i = 0; i < pages.size(); i++)
                pages.get(i).render(doc, deal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        doc.save(output);
        doc.close();
        return output;
    }

	private static String getStackTrace(Throwable aThrowable) {
		StringBuilder result = new StringBuilder();
		for (StackTraceElement element : aThrowable.getStackTrace()) {
			result.append("at ");
			result.append(element);
			result.append("\n");
		}
		return result.toString();
	}
	
	private static ByteArrayOutputStream inputStreamToByteArrayOutputStream(InputStream in) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) > -1 ) {
		    baos.write(buffer, 0, len);
		}
		baos.flush();
		return baos;
	}

	private void insertError(Exception e, Document xmldoc, Element documentElement, Element statusElement) {
	
	    // Set UniformDisclosureResults/Status to Error
		statusElement.appendChild(xmldoc.createTextNode("Error"));
		
		// Create error element
	    Element errorElement = xmldoc.createElement("Error");
	    documentElement.appendChild(errorElement);
	    
	    // Insert error code into error element
	    Element codeElement  = xmldoc.createElement("Code");
	    errorElement.appendChild(codeElement);
	    codeElement.appendChild(xmldoc.createTextNode("004"));
	    
	    // Insert summary message into error element
	    Element messageElement  = xmldoc.createElement("Message");
	    errorElement.appendChild(messageElement);
	    messageElement.appendChild(xmldoc.createTextNode("Service failure"));
	    
	    // Insert message details into error element
	    Element detailsElement  = xmldoc.createElement("MessageDetails");
		errorElement.appendChild(detailsElement);
	    detailsElement.appendChild(xmldoc.createTextNode("Exception(" + e.toString() + ")"));
	    
	    // Insert stack trace into error element
	    Element stackElement  = xmldoc.createElement("StackTrace");
		errorElement.appendChild(stackElement);
		stackElement.appendChild(xmldoc.createCDATASection("\n" + getStackTrace(e)));
	}

	private void insertPdfResults(MISMODocument data, Document xmldoc, Element rootElement) {
	
		// Append UniformDisclosureResults and UniformDisclosureResults/Status elements
		Element documentElement = xmldoc.createElement("Document");
		rootElement.appendChild(documentElement);
	    Element statusElement = xmldoc.createElement("Status");
	    documentElement.appendChild(statusElement);
	
	    try {
	    	
	    	// Create PDF document
			ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
			run(data, pdfOutStream);
	    	
	    	// Encode the document in base64
	        String encodedPdf = encoder.encodeToString(pdfOutStream.toByteArray());
	        
	    	// Append UniformDisclosureResults/Document element with embedded pdf
	        Element pdfElement = xmldoc.createElement("PdfDocument");
	        documentElement.appendChild(pdfElement);
	        pdfElement.appendChild(xmldoc.createTextNode(encodedPdf));
	        
	        // Set UniformDisclosureResults/Status to Success
	        statusElement.appendChild(xmldoc.createTextNode("Success"));
			
		} catch (Exception e) { // Oops, ERROR!
			insertError(e, xmldoc, documentElement, statusElement);
		}
	}
/*
	public static void main(String[] args) throws COSVisitorException, IOException, ParserConfigurationException, SAXException {
		String inFile = args[0];
		String outFile = args[1];

		// Read MISMO document
		MISMODocument data = new MISMODocument(inFile);

		// While pdf document
		OutputStream out = new FileOutputStream(outFile);
		LoanEstimate leform = new LoanEstimate();
		leform.run(data, out);
	}*/
}
