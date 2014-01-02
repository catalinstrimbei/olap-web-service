package org.aws.query.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class AWSQueryDOMService {

/*
 * 1. Create AWS_SimpleQuery project
 * 2. Resolve apache-commons-codec dependece 
 * 3. Replace parameters 
 * 4. Test first request on: http://associates-amazon.s3.amazonaws.com/signed-requests/helper/index.html
 */
	
	/*
	 * Request security args
	 */
	private static final String AWS_ACCESS_KEY_ID = "AKIAIC4DC6HWCUTJ3TBQ";
	private static final String AWS_SECRET_KEY = "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";
    private static final String ENDPOINT = "ecs.amazonaws.com";

    /*
     * Lookup parameter
     */
    private static final String ITEM_ID = "0545010225";

   
    
  

    // New main-execution
    public static void main(String[] args) {
        /*
         * Set up the signed requests helper 
         */
    	SignedRequestsJavaHelper helper;
        try {
            helper = SignedRequestsJavaHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        String requestUrl = null;

        /* The helper can sign requests in two forms - map form and string form */
        System.out.println("Map form example:");
        Map<String, String> params = new HashMap<String, String>();
        /*
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemLookup");
        params.put("ItemId", ITEM_ID);
        params.put("ResponseGroup", "Small");
		*/
        // Updated params
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2011-08-01");
        params.put("AssociateTag", "PutYourAssociateTagHere");
        params.put("Operation", "ItemLookup");        
//        params.put("Operation", "ItemSearch");
//        params.put("SearchIndex", "Books");
//        params.put("Keywords", "harry+potter");
        params.put("ItemId", ITEM_ID);
        params.put("Timestamp", "2014-01-02T14:41:11.000Z");
        params.put("AWSAccessKeyId", "AKIAIC4DC6HWCUTJ3TBQ");
        //
        
        requestUrl = helper.sign(params);
        System.out.println("Signed Request is \"" + requestUrl + "\"");

        String responseText = fetchResponse(requestUrl);
        System.out.println("RESPONSE \"" + responseText + "\"");
        System.out.println();

        /* Here is an example with string form, where the requests parameters have already been concatenated
         * into a query string. 
        System.out.println("String form example:");
        String queryString = "Service=AWSECommerceService&Version=2009-03-31&Operation=ItemLookup&ResponseGroup=Small&ItemId="
                + ITEM_ID;
        requestUrl = helper.sign(queryString);
        System.out.println("Request is \"" + requestUrl + "\"");

        title = fetchTitle(requestUrl);
        System.out.println("Title is \"" + title + "\"");
        System.out.println();
        */

    }
    
    // New fetching
    private static String fetchResponse(String requestUrl) {
        String responseText = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            //responseText = doc.getElementsByTagName("ItemSearchResponse").item(0).getTextContent();
            
            responseText = doc.getElementsByTagName("ItemLookupResponse").item(0).getTextContent();
            System.out.println("DOC: " + responseText);
            
            System.out.println("DOC_XML: " + getStringFromDocument(doc));
            saveStringFromDocument(doc);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return responseText;
    }      
    
  //method to convert Document to String
    private static String getStringFromDocument(Document doc)
    {
        try
        {
           DOMSource domSource = new DOMSource(doc);
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.transform(domSource, result);
           return writer.toString();
        }
        catch(TransformerException ex)
        {
           ex.printStackTrace();
           return null;
        }
    } 
    
    private static void saveStringFromDocument(Document doc){
    	String textContent = getStringFromDocument(doc);
    	
    	BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = 
            		"AWSResponse_" +
            		new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())
            		+".xml";
            File logFile = new File(timeLog);

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(textContent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }    	
    }
}
