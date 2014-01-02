/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file 
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License. 
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */

package com.amazon.advertising.api.sample;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/*
 * This class shows how to make a simple authenticated ItemLookup call to the
 * Amazon Product Advertising API.
 * 
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */

/*
 * 1. Create AWS_SimpleQuery project
 * 2. Resolve apache-commons-codec dependece 
 * 3. Replace parameters 
 * 4. Test first request on: http://associates-amazon.s3.amazonaws.com/signed-requests/helper/index.html
 */

public class ItemLookupSample {
    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    //private static final String AWS_ACCESS_KEY_ID = "YOUR_ACCESS_KEY_ID_HERE";
	private static final String AWS_ACCESS_KEY_ID = "AKIAIC4DC6HWCUTJ3TBQ";
	
    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    //private static final String AWS_SECRET_KEY = "YOUR_SECRET_KEY_HERE";
	private static final String AWS_SECRET_KEY = "D77OdtKT8vEifBoEZenj0Gw7Mo09U7RuRsKtF7IN";
	
    /*
     * Use one of the following end-points, according to the region you are
     * interested in:
     * 
     *      US: ecs.amazonaws.com 
     *      CA: ecs.amazonaws.ca 
     *      UK: ecs.amazonaws.co.uk 
     *      DE: ecs.amazonaws.de 
     *      FR: ecs.amazonaws.fr 
     *      JP: ecs.amazonaws.jp
     * 
     */
    private static final String ENDPOINT = "ecs.amazonaws.com";

    /*
     * The Item ID to lookup. The value below was selected for the US locale.
     * You can choose a different value if this value does not work in the
     * locale of your choice.
     */
    private static final String ITEM_ID = "0545010225";

    public static void main_old(String[] args) {
        /*
         * Set up the signed requests helper 
         */
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
        String requestUrl = null;
        String title = null;

        /* The helper can sign requests in two forms - map form and string form */
        
        /*
         * Here is an example in map form, where the request parameters are stored in a map.
         */
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
        params.put("Operation", "ItemSearch");
        params.put("SearchIndex", "Books");
        params.put("Keywords", "harry+potter");
        params.put("Timestamp", "2014-01-02T14:41:11.000Z");
        params.put("AWSAccessKeyId", "AKIAIC4DC6HWCUTJ3TBQ");
        //
        
        requestUrl = helper.sign(params);
        System.out.println("Signed Request is \"" + requestUrl + "\"");

        title = fetchTitle(requestUrl);
        System.out.println("Signed Title is \"" + title + "\"");
        System.out.println();

        /* Here is an example with string form, where the requests parameters have already been concatenated
         * into a query string. */
        System.out.println("String form example:");
        String queryString = "Service=AWSECommerceService&Version=2009-03-31&Operation=ItemLookup&ResponseGroup=Small&ItemId="
                + ITEM_ID;
        requestUrl = helper.sign(queryString);
        System.out.println("Request is \"" + requestUrl + "\"");

        title = fetchTitle(requestUrl);
        System.out.println("Title is \"" + title + "\"");
        System.out.println();

    }

    /*
     * Utility function to fetch the response from the service and extract the
     * title from the XML.
     */
    private static String fetchTitle(String requestUrl) {
        String title = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);
            Node titleNode = doc.getElementsByTagName("Title").item(0);
            title = titleNode.getTextContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return title;
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
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return responseText;
    }    

    // New main-execution
    public static void main(String[] args) {
        /*
         * Set up the signed requests helper 
         */
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
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
    
}
