package hu.bme.vik.szoftarch.climbingorgmanager.client.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.Equipment;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.EquipmentType;

/**
 * Created by Dani on 2014.11.20..
 */
public class EditEquipmentFrame extends JFrame {

	private Equipment equipment;
	private Controller controller;

	public EditEquipmentFrame(JFrame parent) {
		this.equipment = new Equipment();
		controller = Controller.getInstance();
		initGui(parent);
	}

	public EditEquipmentFrame(JFrame parent, Equipment equipment) {
		this.equipment = equipment;
		controller = Controller.getInstance();
		initGui(parent);
	}

	private void initGui(JFrame parent) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		final Container contentPane = getContentPane();

		JPanel inputPanel = new JPanel();
		contentPane.add(inputPanel, BorderLayout.CENTER);
		inputPanel.setLayout(new GridBagLayout());
		inputPanel.setBorder(new TitledBorder(equipment.getId() == null ? "Create new equipment" : "Edit equipment"));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 0, 5);
		constraints.anchor = GridBagConstraints.WEST;

		JLabel nameLabel = new JLabel("Name: ");
		constraints.gridx = 0;
		constraints.gridy = 0;
		inputPanel.add(nameLabel, constraints);

		final JTextField nameField = new JTextField(equipment.getName(), 20);
		constraints.gridx = 1;
		inputPanel.add(nameField, constraints);

		JLabel accessionNumberLabel = new JLabel("Inventory number");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(accessionNumberLabel, constraints);

		final JTextField accessionNumberField = new JTextField(equipment.getAccessionNumber(), 20);
		constraints.gridx = 1;
		inputPanel.add(accessionNumberField, constraints);

		JLabel descriptionLabel = new JLabel("Description");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(descriptionLabel, constraints);

//		final JTextField lastNameField = new JTextField(user.getUserData().getLastName(), 20);
		final JTextArea descriptionTextArea = new JTextArea(equipment.getDescription(), 5, 20);
		constraints.gridx = 1;
		inputPanel.add(descriptionTextArea, constraints);

		JLabel typeLabel = new JLabel("Type");
		constraints.gridx = 0;
		constraints.gridy++;
		inputPanel.add(typeLabel, constraints);

		final JComboBox<String> typeComboBox = new JComboBox<>();
		constraints.gridx = 1;
		inputPanel.add(typeComboBox, constraints);

		Vector<String> equipmentTypesVector = new Vector<>();
		for (EquipmentType equipmentType : controller.getEquipmentTypes()) {
			equipmentTypesVector.add(equipmentType.getName());
		}
		final DefaultComboBoxModel<String> comboBoxModel =
				new DefaultComboBoxModel<>(new Vector<>(equipmentTypesVector));
		typeComboBox.setModel(comboBoxModel);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton okButton = new JButton("Save");
		contentPane.add(buttonPanel, BorderLayout.PAGE_END);
		buttonPanel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				equipment.setName(nameField.getText());
				equipment.setAccessionNumber(accessionNumberField.getText());
				equipment.setDescription(descriptionTextArea.getText());
				List<EquipmentType> equipmentTypes = controller.getEquipmentTypes();
				equipment.setEquipmentType(equipmentTypes.get(typeComboBox.getSelectedIndex()));
				if (equipment.getId() == null) {
					controller.addEquipment(EditEquipmentFrame.this, equipment);
				} else {
					controller.editEquipment(EditEquipmentFrame.this, equipment);
				}
			}
		});
		pack();
		setLocationRelativeTo(parent);
	}
}
