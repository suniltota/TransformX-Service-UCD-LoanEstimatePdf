package com.actualize.loanestimate.api;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.actualize.loanestimate.domainmodels.PDFResponse;
import com.actualize.loanestimate.mismodao.MISMODocument;
import com.actualize.loanestimate.sevices.LoanEstimateServices;

@RestController
@RequestMapping(value = "/actualize/transformx/documents/ucd/le")
public class LoanEstimatePDFAPIimpl {

	@Autowired
	private LoanEstimateServices loanEstimateServices;
	
	@ResponseBody
	@RequestMapping(value = "/pdf", method = { RequestMethod.POST })
    public PDFResponse loanEstimatePdf(@RequestBody String xmldoc) throws Exception {
        MISMODocument mismoDocument = new MISMODocument(new ByteArrayInputStream(xmldoc.getBytes("utf-8")));
        return loanEstimateServices.generateLoanEstimatePDF(mismoDocument);
    }
}
