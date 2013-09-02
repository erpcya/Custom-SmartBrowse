/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Copyright (C) 2003-2013 E.R.P. Consultores y Asociados.                    *
 * All Rights Reserved.                                                       *
 * Contributor(s): Carlos Parada www.erpconsultoresyasociados.com             *
 *****************************************************************************/
package org.eevolution.form;

import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.swing.DefaultCellEditor;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import org.adempiere.model.MBrowseField;
import org.compiere.grid.ed.VCellRenderer;
import org.compiere.grid.ed.VHeaderRenderer;
import org.compiere.minigrid.CheckRenderer;
import org.compiere.minigrid.IDColumn;
import org.compiere.minigrid.IDColumnEditor;
import org.compiere.minigrid.IDColumnRenderer;
import org.compiere.minigrid.MiniTable;
import org.compiere.minigrid.ROCellEditor;
import org.compiere.model.GridField;
import org.compiere.model.MRule;
import org.compiere.swing.CCheckBox;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 * 
 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013
 *
 */
public class BrowserTable extends MiniTable

{
	/**
	 * 
	 * *** Build Of Class ***
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 06:47:42
	 * @param browse
	 */
	public BrowserTable(VBrowser browse) {
		// TODO Auto-generated constructor stub
		this.browse=browse;
	}
	
	/**
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 06:46:50
	 * Sum Amts Selected
	 */
	@Override
	public boolean editCellAt(int row, int column, EventObject e) {
		// TODO Auto-generated method stub			
		if (browse.getFieldKey().getName().equals(getColumn(column).getHeaderValue()))
			if (getValueAt(row, column) instanceof IDColumn)
				browse.create_Amts(row, !((IDColumn) getValueAt(row, column)).isSelected());
		
		return super.editCellAt(row, column, e);
	}
	
	/**
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 06:46:50
	 * @param col
	 * @param field
	 * @return void
	 */
	public void addBrowseField(int col , MBrowseField field)
	{
		tablemodel.addBrowseField(col, field);
		
	}
	
	/** 
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 06:46:11
	 * Remove All Rows For a Minitable and BrowseRows 
	 */
	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
		tablemodel.clear();
		super.removeAll();
	}
	
	/**
	 * 
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 06:46:11
	 * @param row
	 * @param col
	 * @param value
	 * @return void
	 */
	public void setValue(int row, int col, Object value)
	{
		//Set Value for BrowserRows and MiniTable
		tablemodel.setValue(row, col, value);
		setValueAt(value, row, col);
	}
	
	/**
	 *  Overwrite for support BrowseCellEditor
	 *  Set Column Editor & Renderer to Class
	 *  (after all columns were added)
	 *  Lauout of IDColumn depemds on multiSelection
	 *  @param index column index
	 *  @param c   class of column - determines renderere/editors supported:
	 *  @param DisplayType define Type Value
	 *  IDColumn, Boolean, Double (Quantity), BigDecimal (Amount), Integer, Timestamp, String (default)
	 *  @param readOnly read only flag
	 *  @param header optional header value
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setColumnClass (int index, Class c, int displayType ,boolean readOnly, String header)
	{
	//	log.config( "MiniTable.setColumnClass - " + index, c.getName() + ", r/o=" + readOnly);
		TableColumn tc = getColumnModel().getColumn(index);
		if (tc == null)
			return;
		//  Set R/O
		setColumnReadOnly(index, readOnly);

		//  Header
		if (header != null && header.length() > 0)
			tc.setHeaderValue(Util.cleanAmp(header));

		//  ID Column & Selection
		if (c == IDColumn.class)
		{
			tc.setCellRenderer(new IDColumnRenderer(isMultiSelection()));
			if (isMultiSelection())
			{
				tc.setCellEditor(new IDColumnEditor());
				setColumnReadOnly(index, false);
			}
			else
			{
				tc.setCellEditor(new ROCellEditor());
			}
			m_minWidth.add(new Integer(10));
			tc.setMaxWidth(20);
			tc.setPreferredWidth(20);
			tc.setResizable(false);
			
			tc.setHeaderRenderer(new VHeaderRenderer(DisplayType.Number));
		}
		//  Boolean
		else if (DisplayType.YesNo == displayType || c == Boolean.class )
		{
			tc.setCellRenderer(new CheckRenderer());
			if (readOnly)
				tc.setCellEditor(new ROCellEditor());
			else
			{
				CCheckBox check = new CCheckBox();
				check.setMargin(new Insets(0,0,0,0));
				check.setHorizontalAlignment(SwingConstants.CENTER);
				tc.setCellEditor(new DefaultCellEditor(check));
			}
			m_minWidth.add(new Integer(30));
			
			tc.setHeaderRenderer(new VHeaderRenderer(DisplayType.YesNo));
		}
		//  Date
		else if (DisplayType.Date == displayType || DisplayType.DateTime == displayType ||  c == Timestamp.class )
		{
			if(DisplayType.DateTime == displayType)
				tc.setCellRenderer(new VCellRenderer(DisplayType.DateTime));
			else 
				tc.setCellRenderer(new VCellRenderer(DisplayType.Date));
			
			if (readOnly)
				tc.setCellEditor(new ROCellEditor());
			else if (DisplayType.Date == displayType || DisplayType.DateTime == displayType)
				tc.setCellEditor(new BrowserCellEditor(c, displayType,tablemodel.getBrowseField(index),this));
			else 
				tc.setCellEditor(new BrowserCellEditor(c,tablemodel.getBrowseField(index),this));
			
			m_minWidth.add(new Integer(30));
			if (DisplayType.DateTime == displayType)
				tc.setHeaderRenderer(new VHeaderRenderer(DisplayType.DateTime));
			else 
				tc.setHeaderRenderer(new VHeaderRenderer(DisplayType.Date));
		}
		//  Amount
		else if (DisplayType.Amount == displayType || c == BigDecimal.class )
		{
			tc.setCellRenderer(new VCellRenderer(DisplayType.Amount));
			if (readOnly)
			{
				tc.setCellEditor(new ROCellEditor());
				m_minWidth.add(new Integer(70));
			}
			else
			{
				tc.setCellEditor(new BrowserCellEditor(c,tablemodel.getBrowseField(index),this));
				m_minWidth.add(new Integer(80));
			}
			
			tc.setHeaderRenderer(new VHeaderRenderer(DisplayType.Number));
		}
		//  Number
		else if (DisplayType.Number == displayType || c == Double.class)
		{
			tc.setCellRenderer(new VCellRenderer(DisplayType.Number));
			if (readOnly)
			{
				tc.setCellEditor(new ROCellEditor());
				m_minWidth.add(new Integer(70));
			}
			else
			{
				tc.setCellEditor(new BrowserCellEditor(c,tablemodel.getBrowseField(index),this));
				m_minWidth.add(new Integer(80));
			}
			
			tc.setHeaderRenderer(new VHeaderRenderer(DisplayType.Number));
		}
		//  Integer
		else if (DisplayType.Integer == displayType || c == Integer.class )
		{
			tc.setCellRenderer(new VCellRenderer(DisplayType.Integer));
			if (readOnly)
				tc.setCellEditor(new ROCellEditor());
			else
				tc.setCellEditor(new BrowserCellEditor(c,tablemodel.getBrowseField(index),this));
			m_minWidth.add(new Integer(30));
			
			tc.setHeaderRenderer(new VHeaderRenderer(DisplayType.Number));
		}
		//  String
		else
		{
			tc.setCellRenderer(new VCellRenderer(DisplayType.String));
			if (readOnly)
				tc.setCellEditor(new ROCellEditor());
			else
				tc.setCellEditor(new BrowserCellEditor(String.class,tablemodel.getBrowseField(index),this));
			m_minWidth.add(new Integer(30));
			
			tc.setHeaderRenderer(new VHeaderRenderer(DisplayType.String));
		}
	//	log.fine( "Renderer=" + tc.getCellRenderer().toString() + ", Editor=" + tc.getCellEditor().toString());
	}   //  setColumnClass
	
	
	/**************************************************************************
	 *  Adapted for Browse Callouts
	 *  Process Callout(s).
	 *  <p>
	 *  The Callout is in the string of
	 *  "class.method;class.method;"
	 * If there is no class name, i.e. only a method name, the class is regarded
	 * as CalloutSystem.
	 * The class needs to comply with the Interface Callout.
	 *
	 * For a limited time, the old notation of Sx_matheod / Ux_menthod is maintained.
	 *
	 * @param field field
	 * @return error message or ""
	 * @see org.compiere.model.Callout
	 */
	public String processCallout (GridField field)
	{
		String callout = field.getCallout();
		if (callout.length() == 0)
			return "";


		Object value = field.getValue();
		Object oldValue = field.getOldValue();
		log.fine(field.getColumnName() + "=" + value
			+ " (" + callout + ") - old=" + oldValue);

		StringTokenizer st = new StringTokenizer(callout, ";,", false);
		while (st.hasMoreTokens())      //  for each callout
		{
			String cmd = st.nextToken().trim();
			
			//detect infinite loop
			if (activeCallouts.contains(cmd)) continue;
			
			String retValue = "";
			// FR [1877902]
			// CarlosRuiz - globalqss - implement beanshell callout
			// Victor Perez  - vpj-cd implement JSR 223 Scripting
			if (cmd.toLowerCase().startsWith(MRule.SCRIPT_PREFIX)) {
				
				MRule rule = MRule.get(ctx, cmd.substring(MRule.SCRIPT_PREFIX.length()));
				if (rule == null) {
					retValue = "Callout " + cmd + " not found"; 
					log.log(Level.SEVERE, retValue);
					return retValue;
				}
				if ( !  (rule.getEventType().equals(MRule.EVENTTYPE_Callout) 
					  && rule.getRuleType().equals(MRule.RULETYPE_JSR223ScriptingAPIs))) {
					retValue = "Callout " + cmd
						+ " must be of type JSR 223 and event Callout"; 
					log.log(Level.SEVERE, retValue);
					return retValue;
				}

				ScriptEngine engine = rule.getScriptEngine();

				// Window context are    W_
				// Login context  are    G_
				MRule.setContext(engine, ctx, browse.p_WindowNo);
				// now add the callout parameters windowNo, tab, field, value, oldValue to the engine 
				// Method arguments context are A_
				engine.put(MRule.ARGUMENTS_PREFIX + "WindowNo", browse.p_WindowNo);
				engine.put(MRule.ARGUMENTS_PREFIX + "Tab", this);
				engine.put(MRule.ARGUMENTS_PREFIX + "Field", field);
				engine.put(MRule.ARGUMENTS_PREFIX + "Value", value);
				engine.put(MRule.ARGUMENTS_PREFIX + "OldValue", oldValue);
				engine.put(MRule.ARGUMENTS_PREFIX + "Ctx", ctx);

				try 
				{
					activeCallouts.add(cmd);
					retValue = engine.eval(rule.getScript()).toString();
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "", e);
					retValue = 	"Callout Invalid: " + e.toString();
					return retValue;
				}
				finally
				{
					activeCallouts.remove(cmd);
				}
				
			} else {

				BrowserCallout call = null;
				String method = null;
				int methodStart = cmd.lastIndexOf('.');
				try
				{
					if (methodStart != -1)      //  no class
					{
						Class<?> cClass = Class.forName(cmd.substring(0,methodStart));
						call = (BrowserCallout)cClass.newInstance();
						method = cmd.substring(methodStart+1);
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "class", e);
					return "Callout Invalid: " + cmd + " (" + e.toString() + ")";
				}

				if (call == null || method == null || method.length() == 0)
					return "Callout Invalid: " + method;

				try
				{
					activeCallouts.add(cmd);
					activeCalloutInstance.add(call);
					retValue = call.start(ctx, method, browse.p_WindowNo, tablemodel, field, value, oldValue);
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, "start", e);
					retValue = 	"Callout Invalid: " + e.toString();
					return retValue;
				}
				finally
				{
					activeCallouts.remove(cmd);
					activeCalloutInstance.remove(call);
				}
				
			}			
			
			if (!Util.isEmpty(retValue))		//	interrupt on first error
			{
				log.severe (retValue);
				return retValue;
			}
		}   //  for each callout
		return "";
	}	//	processCallout

	/**
	 * 
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 06:45:37
	 * @return
	 * @return BrowserRows
	 */
	public BrowserRows getTablemodel() {
		return tablemodel;
	}

	/**
	 * 
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 06:45:30
	 * @return
	 * @return VBrowser
	 */
	public VBrowser getBrowse() {
		return browse;
	}

	
	/** Serial Version*/
	private static final long serialVersionUID = 1L;
	
	/**VBrowse */
	private VBrowser browse;
	
	/** Browser Rows*/
	private BrowserRows tablemodel = new BrowserRows();
	
	/**	Logger			*/
	private static Logger log = Logger.getLogger(BrowserTable.class.getName());
	
	/** Active BrowseCallOuts **/
	private List<String> activeCallouts = new ArrayList<String>();
	
	/** Active BrowseCallOutsInstances **/
	private List<BrowserCallout> activeCalloutInstance = new ArrayList<BrowserCallout>();
	
	/** Context **/
	private Properties ctx =Env.getCtx();   
}



