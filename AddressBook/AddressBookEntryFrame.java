/**
 * David Monahan 24/11/2016
 * Client Server Programming Addressbook/sql assignment
 * This is a modified version of the example addressbook code which can store multiple addresses, 
 * phone numbers and emails for a given name entry. The search function can now return multiple 
 * results for a given name, the gui has been given a new look and feel and the overall program 
 * should be thread safe. * 
 */
// Fig. 8.37: AddressBookEntryFrame.java
// A subclass of JInternalFrame customized to display and 
// an AddressBookEntry or set an AddressBookEntry's properties
// based on the current data in the UI.

// Java core packages
import java.util.*;
import java.util.List;
import java.awt.*;

// Java extension packages
import javax.swing.*;

@SuppressWarnings("serial")
public class AddressBookEntryFrame extends JInternalFrame {

	// HashMap to store JTextField references for quick access
	@SuppressWarnings("rawtypes")
	private HashMap fields;

	// current AddressBookEntry set by AddressBook application
	private AddressBookEntry person;

	// panels to organize GUI
	private JPanel leftPanel, middlePanel;

	// static integers used to determine new window positions
	// for cascading windows
	private static int xOffset = 0, yOffset = 0;

	private int rowCount = 2;
	private int addressCount = 0;
	private int phoneCount = 0;
	private int emailCount = 0;
	// static Strings that represent name of each text field.
	// These are placed on JLabels and used as keys in
	// HashMap fields.
	private static final String FIRST_NAME = "First Name", LAST_NAME = "Last Name", ADDRESS1 = "Address Line 1",
			ADDRESS2 = "Address Line 2", CITY = "City", STATE = "State", EIRCODE = "Eircode", PHONE = "Phone",
			EMAIL = "Email";

	// construct GUI
	@SuppressWarnings("rawtypes")
	public AddressBookEntryFrame() {
		super("Address Book Entry", true, true);

		fields = new HashMap();

		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(rowCount, 1, 0, 5));
		middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout(rowCount, 1, 0, 5));

		createRow(FIRST_NAME);
		createRow(LAST_NAME);
		/*
		 * createRow(ADDRESS1); createRow(ADDRESS2); createRow(CITY);
		 * createRow(STATE); createRow(EIRCODE); createRow(PHONE);
		 * createRow(EMAIL);
		 */
		addAddress();
		addPhoneNumber();
		addEmail();

		Container container = getContentPane();
		container.add(leftPanel, BorderLayout.WEST);
		container.add(middlePanel, BorderLayout.CENTER);
		// container.add(rightPanel, BorderLayout.EAST );

		setBounds(xOffset, yOffset, 700, 500);
		xOffset = (xOffset + 30) % 700;
		yOffset = (yOffset + 30) % 500;
	}

	// set AddressBookEntry then use its properties to
	// place data in each JTextField
	/**
	 * Updated setAddressBookEntry Method. This method will iterate over the
	 * lists stored in the passes AddressBookEntry and fill in the fields on the
	 * Frame with the relevant information. If a field or fields do not exist
	 * the methods addAddress() addPhoneNumber and addEmail() are called to
	 * create the relevant fields.
	 * 
	 * @param entry
	 *            The AddressBookEntry to have its data displayed.
	 */
	public void setAddressBookEntry(AddressBookEntry entry) {
		person = entry;

		setField(FIRST_NAME, person.getFirstName());
		setField(LAST_NAME, person.getLastName());
		/*
		 * setField( ADDRESS1, person.getAddress1() ); setField( ADDRESS2,
		 * person.getAddress2() ); setField( CITY, person.getCity() ); setField(
		 * STATE, person.getState() ); setField( EIRCODE, person.getEircode() );
		 * setField( PHONE, person.getPhoneNumber() ); setField( EMAIL,
		 * person.getEmailAddress() );
		 */
		int i = 0;
		for (List<String> address : person.getAddresses()) {
			i++;
			if (i >= 2) {
				addAddress();
			}
			setField(ADDRESS1 + ": " + i, address.get(0));
			setField(ADDRESS2 + ": " + i, address.get(1));
			setField(CITY + ": " + i, address.get(2));
			setField(STATE + ": " + i, address.get(3));
			setField(EIRCODE + ": " + i, address.get(4));
		}
		i = 0;
		for (String phoneNumber : person.getPhoneNumbers()) {
			i++;
			if (i >= 2) {
				addPhoneNumber();
			}
			setField(PHONE + ": " + i, phoneNumber);
		}
		i = 0;
		for (String email : person.getEmails()) {
			i++;
			if (i >= 2) {
				addEmail();
			}
			setField(EMAIL + ": " + i, email);
		}

	}

	// store AddressBookEntry data from GUI and return
	// AddressBookEntry
	/**
	 * Updated method getAddressBookEntry(). This method will now iterate over
	 * several fields withing the frame and construct Lists for each. *
	 * 
	 * @return An updated AddressBookEntry with all fields stored as Lists
	 *         instead of individual entries
	 */
	public AddressBookEntry getAddressBookEntry() {

		person.setFirstName(getField(FIRST_NAME));
		person.setLastName(getField(LAST_NAME));

		List<List<String>> addresses = new ArrayList<List<String>>();
		for (int i = 1; i <= addressCount; i++) {
			List<String> address = new ArrayList<String>();
			address.add(getField(ADDRESS1 + ": " + i));
			address.add(getField(ADDRESS2 + ": " + i));
			address.add(getField(CITY + ": " + i));
			address.add(getField(STATE + ": " + i));
			address.add(getField(EIRCODE + ": " + i));
			addresses.add(address);
		}
		person.setAddresses(addresses);

		List<String> emails = new ArrayList<String>();
		for (int i = 1; i <= emailCount; i++) {
			emails.add(getField(EMAIL + ": " + i));
		}
		person.setEmails(emails);

		List<String> nums = new ArrayList<String>();
		for (int i = 1; i <= phoneCount; i++) {
			nums.add(getField(PHONE + ": " + i));
		}
		person.setPhoneNumbers(nums);

		return person;
	}

	// set text in JTextField by specifying field's
	// name and value
	private void setField(String fieldName, String value) {
		JTextField field = (JTextField) fields.get(fieldName);

		field.setText(value);
	}

	// get text in JTextField by specifying field's name
	private String getField(String fieldName) {
		JTextField field = (JTextField) fields.get(fieldName);

		return field.getText();
	}

	// utility method used by constructor to create one row in
	// GUI containing JLabel and JTextField
	@SuppressWarnings("unchecked")
	private void createRow(String name) {
		JLabel label = new JLabel(name, SwingConstants.RIGHT);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftPanel.add(label);

		JTextField field = new JTextField(30);
		middlePanel.add(field);

		fields.put(name, field);
	}

	/**
	 * New method to create the fields to add a new Address. This method
	 * will change the layout to account for the new field and then
	 * clears/refreshes the container.
	 */
	public void addAddress() {
		rowCount += 5;
		addressCount++;
		leftPanel.setLayout(new GridLayout(rowCount, 1, 0, 5));
		middlePanel.setLayout(new GridLayout(rowCount, 1, 0, 5));
		// createButton("Address: "+addressCount, "Remove Address");
		createRow(ADDRESS1 + ": " + addressCount);
		createRow(ADDRESS2 + ": " + addressCount);
		createRow(STATE + ": " + addressCount);
		createRow(CITY + ": " + addressCount);
		createRow(EIRCODE + ": " + addressCount);

		Container container = getContentPane();
		container.removeAll();
		container.add(leftPanel, BorderLayout.WEST);
		container.add(middlePanel, BorderLayout.CENTER);

	}

	/**
	 * New method to create the fields to add a new Phone Number. This method
	 * will change the layout to account for the new field and then
	 * clears/refreshes the container.
	 */
	public void addPhoneNumber() {
		rowCount += 1;
		phoneCount++;
		leftPanel.setLayout(new GridLayout(rowCount, 1, 0, 5));
		middlePanel.setLayout(new GridLayout(rowCount, 1, 0, 5));
		// createButton("Phone Number: " + phoneCount, "Remove Phone Number");
		createRow(PHONE + ": " + phoneCount);

		Container container = getContentPane();
		container.removeAll();
		container.add(leftPanel, BorderLayout.WEST);
		container.add(middlePanel, BorderLayout.CENTER);

	}

	/**
	 * New method to create the fields to add a new E-Mail address. This method
	 * will change the layout to account for the new field and then
	 * clears/refreshes the container.
	 */
	public void addEmail() {
		rowCount += 1;
		emailCount++;
		leftPanel.setLayout(new GridLayout(rowCount, 1, 0, 5));
		middlePanel.setLayout(new GridLayout(rowCount, 1, 0, 5));
		// createButton("E-Mail: "+emailCount, "Remove E-Mail");
		createRow(EMAIL + ": " + emailCount);

		Container container = getContentPane();
		container.removeAll();
		container.add(leftPanel, BorderLayout.WEST);
		container.add(middlePanel, BorderLayout.CENTER);

	}
} // end class AddressBookEntryFrame
