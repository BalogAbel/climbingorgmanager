package hu.bme.vik.szoftarch.climbingorgmanager.client.gui;

import org.jdesktop.swingx.JXTable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.RowFilter;
import javax.swing.border.TitledBorder;

import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.EntriesTableModel;

/**
 * Created by Dani on 2014.11.28..
 */
public class EntriesControlPanel extends JPanel implements ActionListener {

	private final JXTable entriesTable;
	private RowFilter<EntriesTableModel, Object> dateFilter;
	private final JRadioButton selectedUserRadioButton;
	private final JRadioButton guestRadioButton;
	private final JRadioButton anyUserRadioButton;
	private final JRadioButton ticketRadioButton;
	private final JRadioButton passRadioButton;
	private final JRadioButton anyTypeRadioButton;

	public EntriesControlPanel(JXTable entriesTable) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.entriesTable = entriesTable;

		//------ USER ------
		JPanel usersPanel = new JPanel();
		add(usersPanel);
		usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.PAGE_AXIS));
		usersPanel.setBorder(new TitledBorder("Users"));

		selectedUserRadioButton = new JRadioButton("Show only for selected user");
		usersPanel.add(selectedUserRadioButton);
		selectedUserRadioButton.addActionListener(this);

		guestRadioButton = new JRadioButton("Only guests");
		usersPanel.add(guestRadioButton);
		guestRadioButton.addActionListener(this);

		anyUserRadioButton = new JRadioButton("Any user", true);
		usersPanel.add(anyUserRadioButton);
		anyUserRadioButton.addActionListener(this);

		ButtonGroup userGroup = new ButtonGroup();
		userGroup.add(selectedUserRadioButton);
		userGroup.add(guestRadioButton);
		userGroup.add(anyUserRadioButton);

		//------ TYPE ------
		JPanel typePanel = new JPanel();
		add(typePanel);
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.PAGE_AXIS));
		typePanel.setBorder(new TitledBorder("Type"));

		ticketRadioButton = new JRadioButton("Ticket");
		typePanel.add(ticketRadioButton);
		ticketRadioButton.addActionListener(this);

		passRadioButton = new JRadioButton("Pass");
		typePanel.add(passRadioButton);
		passRadioButton.addActionListener(this);

		anyTypeRadioButton = new JRadioButton("Any", true);
		typePanel.add(anyTypeRadioButton);
		anyTypeRadioButton.addActionListener(this);

		ButtonGroup typeGroup = new ButtonGroup();
		typeGroup.add(ticketRadioButton);
		typeGroup.add(passRadioButton);
		typeGroup.add(anyTypeRadioButton);
	}

	private void applyFilters() {
		String regex = ".*";
		if (selectedUserRadioButton.isSelected()) {
			regex = "teszt";
		} else if (guestRadioButton.isSelected()) {
			regex = "GUEST";
		}
		RowFilter<EntriesTableModel, Object> userFilter = RowFilter.regexFilter(regex, 1);

		String typeRegex = ".*";
		if (ticketRadioButton.isSelected()) {
			typeRegex = "TICKET";
		} else if (passRadioButton.isSelected()) {
			typeRegex = "PASS";
		}
		RowFilter<EntriesTableModel, Object> typeFilter = RowFilter.regexFilter(typeRegex, 2);

		List<RowFilter<EntriesTableModel, Object>> filters = new LinkedList<>();
		filters.add(userFilter);
		filters.add(typeFilter);
		entriesTable.setRowFilter(RowFilter.andFilter(filters));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		applyFilters();
	}
}
