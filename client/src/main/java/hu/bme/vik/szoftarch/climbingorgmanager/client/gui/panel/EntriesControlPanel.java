package hu.bme.vik.szoftarch.climbingorgmanager.client.gui.panel;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.RowFilter;
import javax.swing.border.TitledBorder;

import hu.bme.vik.szoftarch.climbingorgmanager.client.controller.Controller;
import hu.bme.vik.szoftarch.climbingorgmanager.client.tablemodel.EntriesTableModel;
import hu.bme.vik.szoftarch.climbingorgmanager.core.entities.User;

/**
 * Created by Dani on 2014.11.28..
 */
public class EntriesControlPanel extends JPanel implements ActionListener {

	private Controller controller;

	private final JXTable entriesTable;
	private RowFilter<EntriesTableModel, Object> dateFilter;
	private final JRadioButton selectedUserRadioButton;
	private final JRadioButton guestRadioButton;
	private final JRadioButton anyUserRadioButton;
	private final JRadioButton ticketRadioButton;
	private final JRadioButton passRadioButton;
	private final JRadioButton anyTypeRadioButton;
	private final JXDatePicker toPicker;
	private final JXDatePicker fromPicker;

	public EntriesControlPanel(JXTable entriesTable) {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(200, 0));
		this.entriesTable = entriesTable;
		controller = Controller.getInstance();

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

		//------ DATA ------
		JPanel datePanel = new JPanel();
		add(datePanel);
		datePanel.setBorder(new TitledBorder("Date"));
		datePanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 0, 5);
		constraints.anchor = GridBagConstraints.WEST;

		JLabel fromLabel = new JLabel("From");
		constraints.gridx = 0;
		constraints.gridy = 0;
		datePanel.add(fromLabel, constraints);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		fromPicker = new JXDatePicker(new Date());
		constraints.gridx = 1;
		datePanel.add(fromPicker, constraints);
		fromPicker.getEditor().setColumns(8);
		fromPicker.setFormats(df);
		fromPicker.addActionListener(this);

		JLabel toLabel = new JLabel("To");
		constraints.gridx = 0;
		constraints.gridy++;
		datePanel.add(toLabel, constraints);

		toPicker = new JXDatePicker(new Date());
		constraints.gridx = 1;
		datePanel.add(toPicker, constraints);
		toPicker.getEditor().setColumns(8);
		toPicker.setFormats(df);
		toPicker.addActionListener(this);

		JButton todayButton = new JButton("Today");
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.gridwidth = 2;
		datePanel.add(todayButton, constraints);
		todayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				fromPicker.setDate(calendar.getTime());
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				toPicker.setDate(calendar.getTime());

				applyFilters();
			}
		});

		JButton monthButton = new JButton("This month");
		constraints.gridx = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.EAST;
		datePanel.add(monthButton, constraints);
		monthButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				fromPicker.setDate(calendar.getTime());
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				calendar.set(Calendar.HOUR, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				toPicker.setDate(calendar.getTime());

				applyFilters();
			}
		});

	}

	private void applyFilters() {
		String regex = ".*";
		User user = controller.getSelectedUser();
		if (selectedUserRadioButton.isSelected() && user != null) {
			regex = user.getUserName();
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
		RowFilter<EntriesTableModel, Object> fromFilter = RowFilter.dateFilter(RowFilter.ComparisonType.AFTER,
				fromPicker.getDate(), 0);
		RowFilter<EntriesTableModel, Object> toFilter = RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE,
				toPicker.getDate(), 0);

		List<RowFilter<EntriesTableModel, Object>> filters = new LinkedList<>();
		filters.add(userFilter);
		filters.add(typeFilter);
		filters.add(fromFilter);
		filters.add(toFilter);
		entriesTable.setRowFilter(RowFilter.andFilter(filters));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		applyFilters();
	}
}
