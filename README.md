# TransformX-Service-UCD-LoanEstimatePdf

This project defines the code for generating the PDF for Loan Estimate 

This service runs on port :9012

To run the server ,enter into project folder and run

mvn spring-boot:run (or) java -jar *location of the jar file*

The above line will start the server at port 9012

If you want to change the port .Please start th server as mentioned below 

syntax : java -jar *location of the jar file* --server.port= *server port number*
 
example: java -jar target/LoanEstimatePdf.jar --server.port=9090

API to generate Loan Estimate PDF(/actualize/transformx/documents/ucd/le/pdf) with input as Loan Estimate XML 

syntax : *server address with port*/actualize/transformx/documents/ucd/le/pdf; method :POST; Header: Content-Type:application/xml

example: http://localhost:9012/actualize/transformx/documents/ucd/le/pdf ; method: POST; Header: Content-Type:application/xml
