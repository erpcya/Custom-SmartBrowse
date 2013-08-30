package org.eevolution.form;

import java.util.Properties;

import org.compiere.model.GridField;


public class BrowserCallOutExample extends BrowserCalloutEngine {

	
	public String methodExample(Properties ctx,  int WindowNo,BrowserRows mRow, GridField mField, Object value, Object oldValue) 
	{
		System.out.println("Hi! this is a example of implementation callouts");
		System.out.println("This is a Value for GridField:"+value);
		return "";
	}
	
}
