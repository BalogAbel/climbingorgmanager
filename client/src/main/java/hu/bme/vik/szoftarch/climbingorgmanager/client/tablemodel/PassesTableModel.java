package hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Pass;

/**
 * Created by Dani on 2014.11.21..
 */
public class PassesTableModel extends AbstractTableModel {

	private String[] columnNames = new String[]{"Time left", "Use"};
	private List<Pass> passes;

	public PassesTableModel() {
		passes = new LinkedList<Pass>();
	}

	@Override
	public int getRowCount() {
		return passes.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return null;
	}

	public void setPasses(List<Pass> passes) {
		this.passes = passes;
		fireTableDataChanged();
	}
}
