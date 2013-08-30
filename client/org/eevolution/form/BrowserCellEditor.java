/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.eevolution.form;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.util.logging.Logger;

import org.adempiere.model.MBrowseField;
import org.compiere.minigrid.MiniCellEditor;
import org.compiere.model.GridField;


/**
 *  Browser Cell Editor extends of MiniTable Cell Editor based on class add listen funtionality
 *
 *  @author     Carlos Parada
 *  @version    $Id: BrowserCellEditor.java,v 1.0 2013/08/30 00:51:28 
 */
public class BrowserCellEditor extends MiniCellEditor implements VetoableChangeListener
{

	/** 
	 * *** Builder of Class***
	 * @author Carlos Parada 30/08/2013, 05:27:09
	 * @param c
	 * @param displayType
	 * @param table
	 */
	public BrowserCellEditor(Class c, int displayType,MBrowseField field,BrowserTable table) {
		super(c, displayType);
		// TODO Auto-generated constructor stub
		this.table=table;
		this.field=field;
		m_editor.addVetoableChangeListener(this);
	}
	
	/**
	 * 
	 * *** Builder of Class***
	 * @author Carlos Parada 30/08/2013, 05:27:36
	 * @param c
	 * @param table
	 */
	public BrowserCellEditor(Class c,MBrowseField field,BrowserTable table) {
		super(c);
		
		// TODO Auto-generated constructor stub
		this.table=table;
		this.field=field;
		m_editor.addVetoableChangeListener(this);
	}

	/**
	 * Listener for implements Callouts
	 */
	@Override
	public void vetoableChange(PropertyChangeEvent evt)
			throws PropertyVetoException {
		// TODO Auto-generated method stub
		if (!field.isReadOnly())
		{
			
			GridField field= new GridField(table.getTablemodel().getGridFieldVO(table.getBrowse().p_WindowNo, "", 3)); 
			field.setValue(evt.getNewValue(),true);
					
			table.processCallout(field);
		}
				
	}

	
	/** Serial ID	 */
	private static final long serialVersionUID = 4431508736596874253L;
	
	/** Browser Table*/
	private BrowserTable table;
	
	/** Browser Field*/
	private MBrowseField field;

	/**	Logger			*/
	private static Logger log = Logger.getLogger(BrowserCellEditor.class.getName());
	
}   //  BrowserCellEditor
