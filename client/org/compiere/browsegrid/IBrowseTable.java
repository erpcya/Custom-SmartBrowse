package org.compiere.browsegrid;

import java.sql.ResultSet;

import org.adempiere.model.MBrowse;
import org.adempiere.model.MBrowseField;
import org.compiere.minigrid.ColumnInfo;

import org.compiere.model.GridField;
import org.compiere.model.PO;

public interface IBrowseTable 
{
	public boolean isCellEditable(int row, int column);
	
	public Object getValueAt(int row, int column);
	
	public void setValueAt(Object value, int row, int column);
	
	public int convertColumnIndexToModel(int viewColumnIndex);
	
	public void setColumnReadOnly (int index, boolean readOnly);
	
	public String prepareTable(MBrowseField[] browse,boolean multiSelection);
	
	public void addColumn (String header);
	
	public void setColumnClass (int index, GridField gField,int displayType, boolean readOnly, String header);
	
	//public void setColumnClass (int index, Class classType, boolean readOnly);
	
	//public void loadTable(ResultSet rs);
	
	//public void loadTable(PO[] pos);
	
	public Integer getSelectedRowKey();
	
	public int getSelectedRow();
	
	public void setRowCount (int rowCount);
	
	public ColumnInfo[] getLayoutInfo();

	public int getColumnCount();
	
	public int getRowCount();
	
	public void setMultiSelection(boolean multiSelection);
	
	public boolean isMultiSelection();
	
	public int getColorCode (int row);
	
	public void setColorCompare (Object dataCompare);
	
	public void repaint();
	
	public void autoSize();
}
