package com.actualize.mortgage.api;

import java.io.ByteArrayInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.actualize.mortgage.domainmodels.PDFResponse;
import com.actualize.mortgage.mismodao.MISMODocument;
import com.actualize.mortgage.services.LoanEstimatePDFServices;
/**
 * This class is the rest controller which defines the all the APIs associated for generation of Loan Estimate PDF
 * @author sboragala
 *
 */
@RestController
@RequestMapping(value = "/actualize/transformx/documents/ucd/le/")
public class LoanEstimatePDFAPIimpl {
	
	@Autowired
	private LoanEstimatePDFServices loanEstimatePDFServices;

	private static final Logger LOG = LogManager.getLogger(LoanEstimatePDFAPIimpl.class);
	
	@ResponseBody
	@RequestMapping(value = "{version}/pdf", method = { RequestMethod.POST })
    public PDFResponse loanEstimatePdf(@PathVariable String version, @RequestBody String xmldoc) throws Exception {
		LOG.info("user "+SecurityContextHolder.getContext().getAuthentication().getName()+" used Service: LE MISMO XML to LE PDF");
        MISMODocument mismoDocument = new MISMODocument(new ByteArrayInputStream(xmldoc.getBytes("utf-8")));
        return loanEstimatePDFServices.generateLoanEstimatePDF(mismoDocument);
    }
	
	 @RequestMapping(value = "{version}/ping", method = { RequestMethod.GET })
    public String checkStatus(@PathVariable String version) throws Exception {
		 LOG.info("user "+SecurityContextHolder.getContext().getAuthentication().getName()+" used Service: ping LE PDF service");
        return "The service for generating PDF for Loan Estimate is running and ready to accept your requests";
    }

}
