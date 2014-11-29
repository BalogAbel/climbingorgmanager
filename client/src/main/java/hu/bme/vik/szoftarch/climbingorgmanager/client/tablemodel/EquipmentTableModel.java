package hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.20..
 */
public class EquipmentTableModel extends AbstractTableModel {

	private String[] columnNames = new String[]{"Name", "Accession number", "Type", "Actual rental"};
	private List<Equipment> equipments;

	public EquipmentTableModel() {
		this.equipments = new LinkedList<Equipment>();
	}

	@Override
	public int getRowCount() {
		return equipments.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex != 3) {
			return String.class;
		} else {
			return JButton.class;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex != 3) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Equipment equipment = equipments.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return equipment.getName();
			case 1:
				return equipment.getAccessionNumber();
			case 2:
				return equipment.getEquipmentType().getName();
			case 3:
				return equipment.getActualRental() != null ? equipment.getActualRental().getUser().getId() :
						equipment.getId();
			default:
				return "NOT IMPLEMENTED YET...";
		}
	}

	public Equipment getEquipment(int rowIndex) {
		return equipments.get(rowIndex);
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
		fireTableDataChanged();
	}

	public static class EquipmentButtonRenderer extends JButton implements TableCellRenderer {

		public EquipmentButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			EquipmentTableModel tableModel = (EquipmentTableModel) table.getModel();
			final Equipment equipment = tableModel.getEquipment(row);

			if (equipment.getActualRental() == null) {
				setText("Rent this");
			} else {
				final User user = equipment.getActualRental().getUser();
				setText(user.getUserName());
			}
			return this;
		}
	}

	public static class EquipmentButtonEditor extends DefaultCellEditor {
		protected JButton button;
		private boolean rented;
		private long id;
		private boolean isPushed;

		public EquipmentButtonEditor() {
			super(new JCheckBox());
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			id = (Long) value;

			EquipmentTableModel tableModel = (EquipmentTableModel) table.getModel();
			final Equipment equipment = tableModel.getEquipment(row);
			rented = equipment.getActualRental() != null;

			if (rented) {
				final User user = equipment.getActualRental().getUser();
				button.setText(user.getUserName());
			} else {
				button.setText("Rent this");
			}
			isPushed = true;
			return button;
		}

		public Object getCellEditorValue() {
			if (isPushed) {
				Controller controller = Controller.getInstance();
				if (rented) {
					controller.setSelectedUser(id);
				} else {
					controller.rentEquipment(id);
				}
			}
			isPushed = false;
			return id;
		}

		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}
}
