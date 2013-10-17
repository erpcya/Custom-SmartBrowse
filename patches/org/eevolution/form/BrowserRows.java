package org.eevolution.form;

import java.util.ArrayList;
import java.util.LinkedHashMap;


import org.adempiere.model.MBrowseField;
import org.compiere.model.GridField;
import org.compiere.model.GridFieldVO;

public class BrowserRows {

	MBrowseField m_browse_field = null;
	GridField m_grid_field = null;
	private Integer column = null;
	private Integer row = null;
	private Integer viewColumns=0;
	private LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> rows = new LinkedHashMap<Integer, LinkedHashMap<Integer, Object>>();
	private LinkedHashMap<Integer, MBrowseField> browser_head = new LinkedHashMap<Integer, MBrowseField>();
	private LinkedHashMap<Integer, Integer> display_indexes =new LinkedHashMap<Integer, Integer>(); 
	private LinkedHashMap<Integer, Integer> indexes_display =new LinkedHashMap<Integer, Integer>();
	
	
	public BrowserRows() {
	}
	
	public GridFieldVO getGridFieldVO(int windowNo,String title,int col)
	{
		MBrowseField field  = getBrowseField(col);
		GridFieldVO voBase = GridFieldVO.createStdField(field.getCtx(),
				windowNo, 0, 0, 0, false, false, false);
		
		String uniqueName =  field.getAD_View_Column().getColumnSQL();
		voBase.isProcess = true;
		voBase.IsDisplayed = field.isDisplayed();
		voBase.IsReadOnly = field.isReadOnly();
		voBase.IsUpdateable = true;
		voBase.WindowNo = windowNo;		
		voBase.AD_Column_ID = field.getAD_View_Column().getAD_Column_ID();
		voBase.AD_Table_ID = field.getAD_View_Column().getAD_Column()
				.getAD_Table_ID();
		voBase.ColumnName = field.getAD_View_Column().getAD_Column()
				.getColumnName();
		voBase.displayType = field.getAD_Reference_ID();
		voBase.AD_Reference_Value_ID = field.getAD_Reference_Value_ID();
		voBase.IsMandatory = field.isMandatory();
		voBase.IsAlwaysUpdateable = false;
		voBase.IsKey = field.isKey();
		voBase.DefaultValue = field.getDefaultValue();
		voBase.DefaultValue2 = field.getDefaultValue2();
		//voBase.InfoFactoryClass = field.getInfoFactoryClass();
		voBase.FieldLength = field.getFieldLength();
		voBase.ReadOnlyLogic = field.getReadOnlyLogic();
		voBase.DisplayLogic =  field.getDisplayLogic();
		voBase.VFormat = field.getVFormat();
		voBase.ValueMin = field.getValueMin();
		voBase.ValueMax = field.getValueMax();
		voBase.ValidationCode = field.getAD_Val_Rule().getCode();
		voBase.isRange = field.isRange();
		voBase.Description = field.getDescription();
		voBase.Help = uniqueName;
		voBase.Header = title;
		
		/**
		 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 07:02:36
		 * Set Callout
		 */
		voBase.Callout = field.getCallout();//"org.eevolution.form.BrowserCallOutExample.methodExample";
		voBase.initFinish();
		/**
		 * End Carlos Parada
		 */
		
		GridField gField = new GridField(GridFieldVO.createParameter(voBase));
		//  Set Default
		Object defaultObject = gField.getDefault();
		gField.setValue (defaultObject, true);
		gField.lookupLoadComplete();
		return voBase;
	}
	
	public void addBrowseField(int col , MBrowseField field)
	{
		
		if (field.isDisplayed()){
			indexes_display.put(col,viewColumns);
			display_indexes.put(viewColumns,col);
			viewColumns++;
		}
		
		browser_head.put(col, field);
	}

	public MBrowseField getBrowseField (int col)
	{
		return browser_head.get(col);
	}
	
	public void setRow (int id  , ArrayList<Object> row)
	{
		LinkedHashMap<Integer, Object> values = rows.get(id);
		if (values == null)
			values = new LinkedHashMap<Integer, Object>();
		
		for (Object o : row)
			values.put(id, o);
		
		rows.put(id, values);
	}
	
	public void setValue(int row , int col, Object value)
	{
		this.column = col;
		this.row = row; 
		
		LinkedHashMap<Integer, Object> values = rows.get(row);
		if (values == null)
			values = new LinkedHashMap<Integer, Object>();
		
		values.put(col , value);
		rows.put(row , values);
	}
	
	public Object getValue(int id , int col)
	{
		if (rows.size() > id)
		{
			LinkedHashMap<Integer, Object> values = rows.get(id);
			return values.get(col);
		}
		return null;
	}
	
	/**
	 * 
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 07:02:36
	 * @return
	 * @return LinkedHashMap<Integer,MBrowseField>
	 */
	public LinkedHashMap<Integer, MBrowseField> getBrowser_head() {
		return browser_head;
	}
	
	/**
	 * 
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 07:02:18
	 * @return
	 * @return LinkedHashMap<Integer,LinkedHashMap<Integer,Object>>
	 */
	public LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> getRows() {
		return rows;
	}
	
	/**
	 * 
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 07:02:22
	 * @return
	 * @return int
	 */
	public int size()
	{
		return rows.size();
	}
	
	/**
	 * 
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 02/09/2013, 07:02:27
	 * @return void
	 */
	public void clear()
	{
		rows.clear();
	}
	
	/**
	 * Get Number of Columns
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 15/10/2013, 09:44:11
	 * @return
	 * @return int
	 */
	public int getColumnCount()
	{
		return browser_head.size();
	}
	
	
	/**
	 * Returns Qty Columns Displayed
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 15/10/2013, 11:09:56
	 * @return
	 * @return Integer
	 */
	public Integer getViewColumns() {
		return viewColumns;
	}

	public int getIndex_display(int index) {
		
		return indexes_display.get(index);
	}
	
	public int getDisplay_index(int display) {
		return display_indexes.get(display);
	}
	
	/**
	 * Get Index on Browse
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 15/10/2013, 18:20:15
	 * @param columnName
	 * @param row
	 * @return
	 * @return int
	 */
	public int getIndex(String columnName,int row)
	{
		LinkedHashMap<Integer, Object> values = rows.get(row);
		for (int i=1;i<values.size();i++)
			if (columnName.equals(((GridField)values.get(i)).getColumnName() ))
				return i;
		
		return -1;
	}
	
	/**
	 * Get Index on Table
	 * @author <a href="mailto:carlosaparadam@gmail.com">Carlos Parada</a> 15/10/2013, 18:20:15
	 * @param columnName
	 * @param row
	 * @return
	 * @return int
	 */
	public int getDisplayIndex(String columnName,int row)
	{
		LinkedHashMap<Integer, Object> values = rows.get(row);
		for (int i=1;i<values.size();i++){
			GridField gField = (GridField)values.get(i);
			
			if (gField.isDisplayed())
				if(columnName.equals(gField.getColumnName()))
					return getIndex_display(i);
		}
		return -1;
	}
}
