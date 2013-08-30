/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2003-2013 e-Evolution Consultants. All Rights Reserved.      *
 * Copyright (C) 2003-2013 Victor Pérez Juárez 								  * 
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Contributor(s): Victor Pérez Juárez  (victor.perez@e-evolution.com)		  *
 * Sponsors: e-Evolution Consultants (http://www.e-evolution.com/)            *
 *****************************************************************************/
package org.eevolution.form;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.util.CLogger;

/**
 * Browser Callout Engine.
 * @author eEvolution author Victor Perez<victor.perez@e-evolution.com>
 */
public class BrowserCalloutEngine extends CalloutEngine implements BrowserCallout {

	
	/** Logger */
	protected CLogger log = CLogger.getCLogger(getClass());
	private BrowserRows m_mRow;
	private GridField m_mField;
	

	/**
	 * 
	 * @return Browser Row
	 */
	public BrowserRows getBrowserRow() {
		return m_mRow;
	}

	/**
	 * 
	 * @return gridField
	 */
	public GridField getGridField() {
		return m_mField;
	}

	@Override
	/**
	 *	Start BrowserCallout.
	 *  <p>
	 *	Callout's are used for cross field validation and setting values in other fields
	 *	when returning a non empty (error message) string, an exception is raised
	 *  <p>
	 *	When invoked, the Tab model has the new value!
	 *  @param ctx      Context
	 *  @param method   Method name
	 *  @param WindowNo current Window No
	 *  @param mRow  	Row Browser
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @param oldValue The old value
	 *  @return Error message or ""
	 */
	public String start(Properties ctx, String methodName, int WindowNo,
			BrowserRows mRow, GridField mField, Object value, Object oldValue) {
		if (methodName == null || methodName.length() == 0)
			throw new IllegalArgumentException("No Method Name");

		m_mRow = mRow;
		m_mField = mField;

		//
		String retValue = "";
		StringBuffer msg = new StringBuffer(methodName).append(" - ")
				.append(mField.getColumnName()).append("=").append(value)
				.append(" (old=").append(oldValue).append(") {active=")
				.append(isCalloutActive()).append("}");
		if (!isCalloutActive())
			log.info(msg.toString());

		// Find Method
		Method method = getMethod(methodName);
		if (method == null)
			throw new IllegalArgumentException("Method not found: "
					+ methodName);
		int argLength = method.getParameterTypes().length;
		if (!(argLength == 5 || argLength == 6))
			throw new IllegalArgumentException("Method " + methodName
					+ " has invalid no of arguments: " + argLength);

		// Call Method
		try {
			Object[] args = null;
			if (argLength == 6)
				args = new Object[] { ctx, new Integer(WindowNo), mRow, mField,
						value, oldValue };
			else
				args = new Object[] { ctx, new Integer(WindowNo), mRow, mField,
						value };
			retValue = (String) method.invoke(this, args);
		} catch (Exception e) {
			Throwable ex = e.getCause(); // InvocationTargetException
			if (ex == null)
				ex = e;
			log.log(Level.SEVERE, "start: " + methodName, ex);
			retValue = ex.getLocalizedMessage();
			if (retValue == null) {
				retValue = ex.toString();
			}
		} finally {
			m_mRow = null;
			m_mField = null;
		}
		return retValue;
	} // start
	
	/**
	 * 	Get Method
	 *	@param methodName method name
	 *	@return method or null
	 */
	private Method getMethod (String methodName)
	{
		Method[] allMethods = getClass().getMethods();
		for (int i = 0; i < allMethods.length; i++)
		{
			if (methodName.equals(allMethods[i].getName()))
				return allMethods[i];
		}
		return null;
	}	//	getMethod
	
} // BrowserCalloutEngine
