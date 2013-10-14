package org.eevolution.form;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;

import org.adempiere.model.MBrowseField;
import org.compiere.model.GridField;
import org.compiere.model.GridFieldVO;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MLookupInfo;

public class BrowserRows {

	MBrowseField m_browse_field = null;
	GridField m_grid_field = null;
	private Integer column = null;
	private Integer row = null;
	private LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> rows = new LinkedHashMap<Integer, LinkedHashMap<Integer, Object>>();
	private LinkedHashMap<Integer, MBrowseField> browser_head = new LinkedHashMap<Integer, MBrowseField>();
	
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
}
