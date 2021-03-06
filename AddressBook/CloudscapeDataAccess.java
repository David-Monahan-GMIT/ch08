/**
 * David Monahan 24/11/2016
 * Client Server Programming Addressbook/sql assignment
 * This is a modified version of the example addressbook code which can store multiple addresses, 
 * phone numbers and emails for a given name entry. The search function can now return multiple 
 * results for a given name, the gui has been given a new look and feel and the overall program 
 * should be thread safe. * 
 */
// Fig. 8.36: CloudscapeDataAccess.java
// An implementation of interface AddressBookDataAccess that 
// performs database operations with PreparedStatements.

// Java core packages
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CloudscapeDataAccess implements AddressBookDataAccess {

	// reference to database connection
	private Connection connection;

	// reference to prepared statement for locating entry
	private PreparedStatement sqlFind;

	// reference to prepared statement for determining personID
	private PreparedStatement sqlPersonID;

	// references to prepared statements for inserting entry
	private PreparedStatement sqlInsertName;
	private PreparedStatement sqlInsertAddress;
	private PreparedStatement sqlInsertPhone;
	private PreparedStatement sqlInsertEmail;

	// references to prepared statements for updating entry
	private PreparedStatement sqlUpdateName;
	private PreparedStatement sqlUpdateAddress;
	private PreparedStatement sqlUpdatePhone;
	private PreparedStatement sqlUpdateEmail;

	// references to prepared statements for updating entry
	private PreparedStatement sqlDeleteName;
	private PreparedStatement sqlDeleteAddress;
	private PreparedStatement sqlDeletePhone;
	private PreparedStatement sqlDeleteEmail;

	// New prepared statements for addition functionality
	private PreparedStatement sqlGetNames;
	private PreparedStatement sqlFindAddress;
	private PreparedStatement sqlGetAddresses;
	// private PreparedStatement sqlRemoveAddress;
	private PreparedStatement sqlFindPhoneNumber;
	private PreparedStatement sqlGetPhoneNumbers;
	// private PreparedStatement sqlRemovePhoneNumber;
	private PreparedStatement sqlFindEmail;
	private PreparedStatement sqlGetEmails;
	// private PreparedStatement sqlRemoveEmail;

	// set up PreparedStatements to access database
	public CloudscapeDataAccess() throws Exception {
		// connect to addressbook database
		connect();

		/**
		 * Prepared statements to get addresses phone numbers and e-mails by
		 * their id
		 */
		sqlFindAddress = connection
				.prepareStatement("SELECT address1, address2, city, state, eircode FROM addresses WHERE addressID = ?");
		sqlFindPhoneNumber = connection.prepareStatement("SELECT phoneNumber FROM phoneNumbers WHERE phoneID = ?");
		sqlFindEmail = connection.prepareStatement("SELECT emailAddress FROM emailAddresses WHERE emailID = ?");
		// Prepared statement to get names by person id
		sqlGetNames = connection.prepareStatement("SELECT firstName, lastName FROM names WHERE names.personID = ?");
		/**
		 * Prepared statements to get ids of all addresses phone numbers and
		 * emails using the peson id as a search parameter
		 */
		sqlGetAddresses = connection.prepareStatement("SELECT addressID FROM addresses WHERE addresses.personID = ?");
		sqlGetPhoneNumbers = connection
				.prepareStatement("SELECT phoneID FROM phoneNumbers WHERE phoneNumbers.personID = ?");
		sqlGetEmails = connection
				.prepareStatement("SELECT emailID FROM emailAddresses WHERE emailAddresses.personID = ?");

		// locate person
		/*
		 * sqlFind = connection.
		 * prepareStatement("SELECT names.personID, firstName, lastName, " +
		 * "addressID, address1, address2, city, state, " +
		 * "eircode, phoneID, phoneNumber, emailID, " + "emailAddress " +
		 * "FROM names, addresses, phoneNumbers, emailAddresses " +
		 * "WHERE lastName = ? AND " +
		 * "names.personID = addresses.personID AND " +
		 * "names.personID = phoneNumbers.personID AND " +
		 * "names.personID = emailAddresses.personID");
		 */
		/**
		 * Updated find person to just return the person id for each entry it
		 * finds with the given last name. Other prepared statements will be
		 * used to extract data using this id.
		 */
		sqlFind = connection.prepareStatement("SELECT names.personID FROM names WHERE lastName =?");

		// TODO change to use mysql instead of cloudscape
		// Obtain personID for last person inserted in database.
		// [This is a Cloudscape-specific database operation.]
		/*
		 * sqlPersonID = connection.prepareStatement(
		 * "VALUES ConnectionInfo.lastAutoincrementValue( " +
		 * "'APP', 'NAMES', 'PERSONID')" );
		 */

		/**
		 * Find the current biggest personID in the names table and use that
		 * instead of trying to find the last autoincrement value
		 */
		sqlPersonID = connection.prepareStatement("SELECT MAX(personID) FROM names");

		// Insert first and last names in table names.
		// For referential integrity, this must be performed
		// before sqlInsertAddress, sqlInsertPhone and
		// sqlInsertEmail.
		sqlInsertName = connection.prepareStatement("INSERT INTO names ( firstName, lastName ) " + "VALUES ( ? , ? )");

		// insert address in table addresses
		sqlInsertAddress = connection.prepareStatement("INSERT INTO addresses ( personID, address1, "
				+ "address2, city, state, eircode ) " + "VALUES ( ? , ? , ? , ? , ? , ? )");

		// insert phone number in table phoneNumbers
		sqlInsertPhone = connection
				.prepareStatement("INSERT INTO phoneNumbers " + "( personID, phoneNumber) " + "VALUES ( ? , ? )");

		// insert email in table emailAddresses
		sqlInsertEmail = connection
				.prepareStatement("INSERT INTO emailAddresses " + "( personID, emailAddress ) " + "VALUES ( ? , ? )");

		// update first and last names in table names
		sqlUpdateName = connection
				.prepareStatement("UPDATE names SET firstName = ?, lastName = ? " + "WHERE personID = ?");

		// update address in table addresses
		sqlUpdateAddress = connection.prepareStatement("UPDATE addresses SET address1 = ?, address2 = ?, "
				+ "city = ?, state = ?, eircode = ? " + "WHERE addressID = ?");

		// update phone number in table phoneNumbers
		sqlUpdatePhone = connection.prepareStatement("UPDATE phoneNumbers SET phoneNumber = ? " + "WHERE phoneID = ?");

		// update email in table emailAddresses
		sqlUpdateEmail = connection
				.prepareStatement("UPDATE emailAddresses SET emailAddress = ? " + "WHERE emailID = ?");

		// Delete row from table names. This must be executed
		// after sqlDeleteAddress, sqlDeletePhone and
		// sqlDeleteEmail, because of referential integrity.
		sqlDeleteName = connection.prepareStatement("DELETE FROM names WHERE personID = ?");

		// delete address from table addresses
		sqlDeleteAddress = connection.prepareStatement("DELETE FROM addresses WHERE personID = ?");

		// delete phone number from table phoneNumbers
		sqlDeletePhone = connection.prepareStatement("DELETE FROM phoneNumbers WHERE personID = ?");

		// delete email address from table emailAddresses
		sqlDeleteEmail = connection.prepareStatement("DELETE FROM emailAddresses WHERE personID = ?");
	} // end CloudscapeDataAccess constructor

	// Obtain a connection to addressbook database. Method may
	// may throw ClassNotFoundException or SQLException. If so,
	// exception is passed via this class's constructor back to
	// the AddressBook application so the application can display
	// an error message and terminate.
	private void connect() throws Exception {
		// Cloudscape database driver class name
		String driver = "com.mysql.jdbc.Driver";

		// URL to connect to addressbook database
		String url = "jdbc:mysql://localhost:3306/addressbook";

		// load database driver class
		Class.forName(driver);

		// connect to database
		connection = DriverManager.getConnection(url, "root", "root");

		// Require manual commit for transactions. This enables
		// the program to rollback transactions that do not
		// complete and commit transactions that complete properly.
		connection.setAutoCommit(false);
	}

	// Locate specified person. Method returns AddressBookEntry
	// containing information.

	/**
	 * Updated version of findPerson. This will now return a list of all people
	 * found with the specified last name. This allows the main thread to open a
	 * window for all that are found.
	 */
	public List<AddressBookEntry> findPerson(String lastName) {
		try {
			// set query parameter and execute query
			sqlFind.setString(1, lastName);
			ResultSet resultSet = sqlFind.executeQuery();

			List<AddressBookEntry> people = new ArrayList<AddressBookEntry>();
			int counter = 0;
			// if no records found, return immediately
			while (resultSet.next()) {
				people.add(new AddressBookEntry(resultSet.getInt(1)));

				sqlGetNames.setInt(1, resultSet.getInt(1));
				ResultSet names = sqlGetNames.executeQuery();
				while (names.next()) {
					people.get(counter).setFirstName(names.getString(1));
					people.get(counter).setLastName(names.getString(2));
				}

				sqlGetAddresses.setInt(1, resultSet.getInt(1));
				ResultSet addresses = sqlGetAddresses.executeQuery();
				while (addresses.next()) {
					sqlFindAddress.setInt(1, addresses.getInt(1));
					people.get(counter).addAddressID(addresses.getInt(1));
					ResultSet returnedAddresses = sqlFindAddress.executeQuery();
					while (returnedAddresses.next()) {
						List<String> address = new ArrayList<String>();
						address.add(returnedAddresses.getString(1));
						address.add(returnedAddresses.getString(2));
						address.add(returnedAddresses.getString(3));
						address.add(returnedAddresses.getString(4));
						address.add(returnedAddresses.getString(5));
						people.get(counter).addAddress(address);
					}
				}
				sqlGetPhoneNumbers.setInt(1, resultSet.getInt(1));
				ResultSet phoneNumbers = sqlGetPhoneNumbers.executeQuery();
				while (phoneNumbers.next()) {
					sqlFindPhoneNumber.setInt(1, phoneNumbers.getInt(1));
					people.get(counter).addPhoneID(phoneNumbers.getInt(1)); 
					ResultSet returnedPhoneNumbers = sqlFindPhoneNumber.executeQuery();
					while (returnedPhoneNumbers.next()) {
						people.get(counter).addPhoneNumber(returnedPhoneNumbers.getString(1));
					}
				}

				sqlGetEmails.setInt(1, resultSet.getInt(1));
				ResultSet eMails = sqlGetEmails.executeQuery();
				while (eMails.next()) {
					sqlFindEmail.setInt(1, eMails.getInt(1));
					people.get(counter).addEmailID(eMails.getInt(1));
					ResultSet returnedEmails = sqlFindEmail.executeQuery();
					while (returnedEmails.next()) {
						people.get(counter).addEmail(returnedEmails.getString(1));
					}
				}
				counter++;
			}
			// return AddressBookEntry
			return people;
		}

		// catch SQLException
		catch (SQLException sqlException) {
			return null;
		}
	} // end method findPerson

	// Update an entry. Method returns boolean indicating
	// success or failure.

	/**
	 * Updated version of savePerson. This method will now create new tables for
	 * addresses emails or phone numbers if they do not exist when updating an
	 * addressbook entry.
	 */
	public boolean savePerson(AddressBookEntry person) throws DataAccessException {
		// update person in database
		try {
			int result;

			// update names table
			sqlUpdateName.setString(1, person.getFirstName());
			sqlUpdateName.setString(2, person.getLastName());
			sqlUpdateName.setInt(3, person.getPersonID());
			result = sqlUpdateName.executeUpdate();

			// if update fails, rollback and discontinue
			if (result == 0) {
				connection.rollback(); // rollback update
				return false; // update unsuccessful
			}

			// update addresses table
			int idCount = 0;
			for (List<String> address : person.getAddresses()) {
				if (idCount >= person.getAddressIDS().size()) {
					sqlInsertAddress.setInt(1, person.getPersonID());
					for (int i = 0; i < 5; i++) {
						sqlInsertAddress.setString((i + 2), address.get(i));
					}
					result = sqlInsertAddress.executeUpdate();
				} else {
					for (int i = 0; i < 5; i++) {
						sqlUpdateAddress.setString((i + 1), address.get(i));
					}
					sqlUpdateAddress.setInt(6, person.getAddressIDS().get(idCount));
					result = sqlUpdateAddress.executeUpdate();
				}
				idCount++;

				// if update fails, rollback and discontinue
				if (result == 0) {
					connection.rollback(); // rollback update
					return false; // update unsuccessful
				}
			}
			/*
			 * // if update fails, rollback and discontinue if (result == 0) {
			 * connection.rollback(); // rollback update return false; // update
			 * unsuccessful }
			 */

			// update phoneNumbers table
			idCount = 0;
			for (String num : person.getPhoneNumbers()) {
				if (idCount >= person.getPhoneIDS().size()) {
					sqlInsertPhone.setInt(1, person.getPersonID());
					sqlInsertPhone.setString(2, num);

					result = sqlInsertPhone.executeUpdate();
				} else {
					sqlUpdatePhone.setString(1, num);
					sqlUpdatePhone.setInt(2, person.getPhoneIDS().get(idCount));
					result = sqlUpdatePhone.executeUpdate();
				}
				idCount++;

				// if update fails, rollback and discontinue
				if (result == 0) {
					connection.rollback(); // rollback update
					return false; // update unsuccessful
				}
			}

			/*
			 * // if update fails, rollback and discontinue if (result == 0) {
			 * connection.rollback(); // rollback update return false; // update
			 * unsuccessful }
			 */

			// update emailAddresses table
			idCount = 0;
			for (String email : person.getEmails()) {
				if (idCount >= person.getEmailIDS().size()) {
					sqlInsertEmail.setInt(1, person.getPersonID());
					sqlInsertEmail.setString(2, email);

					result = sqlInsertEmail.executeUpdate();
				} else {
					sqlUpdateEmail.setString(1, email);
					sqlUpdateEmail.setInt(2, person.getEmailIDS().get(idCount));
					result = sqlUpdateEmail.executeUpdate();
				}
				idCount++;

				// if update fails, rollback and discontinue
				if (result == 0) {
					connection.rollback(); // rollback update
					return false; // update unsuccessful
				}
			}

			connection.commit(); // commit update
			return true; // update successful
		} // end try

		// detect problems updating database
		catch (SQLException sqlException) {

			// rollback transaction
			try {
				connection.rollback(); // rollback update
				return false; // update unsuccessful
			}

			// handle exception rolling back transaction
			catch (SQLException exception) {
				throw new DataAccessException(exception);
			}
		}
	} // end method savePerson

	// Insert new entry. Method returns boolean indicating
	// success or failure.
	public boolean newPerson(AddressBookEntry person) throws DataAccessException {
		// insert person in database
		try {
			int result;

			// insert first and last name in names table
			sqlInsertName.setString(1, person.getFirstName());
			sqlInsertName.setString(2, person.getLastName());
			result = sqlInsertName.executeUpdate();

			// if insert fails, rollback and discontinue
			if (result == 0) {
				connection.rollback(); // rollback insert
				return false; // insert unsuccessful
			}

			// determine new personID
			ResultSet resultPersonID = sqlPersonID.executeQuery();

			if (resultPersonID.next()) {
				int personID = resultPersonID.getInt(1);

				// insert address in addresses table
				/*
				 * sqlInsertAddress.setInt( 1, personID );
				 * sqlInsertAddress.setString( 2, person.getAddress1() );
				 * sqlInsertAddress.setString( 3, person.getAddress2() );
				 * sqlInsertAddress.setString( 4, person.getCity() );
				 * sqlInsertAddress.setString( 5, person.getState() );
				 * sqlInsertAddress.setString( 6, person.getEircode() );
				 */

				for (List<String> address : person.getAddresses()) {
					sqlInsertAddress.setInt(1, personID);
					for (int i = 0; i < 5; i++) {
						sqlInsertAddress.setString((i + 2), address.get(i));
					}
					result = sqlInsertAddress.executeUpdate();
				}

				// if insert fails, rollback and discontinue
				if (result == 0) {
					connection.rollback(); // rollback insert
					return false; // insert unsuccessful
				}

				// insert phone number in phoneNumbers table
				for (String num : person.getPhoneNumbers()) {
					sqlInsertPhone.setInt(1, personID);
					sqlInsertPhone.setString(2, num);
					result = sqlInsertPhone.executeUpdate();
				}

				// if insert fails, rollback and discontinue
				if (result == 0) {
					connection.rollback(); // rollback insert
					return false; // insert unsuccessful
				}

				for (String email : person.getEmails()) {
					// insert email address in emailAddresses table
					sqlInsertEmail.setInt(1, personID);
					sqlInsertEmail.setString(2, email);
					result = sqlInsertEmail.executeUpdate();
				}

				// if insert fails, rollback and discontinue
				if (result == 0) {
					connection.rollback(); // rollback insert
					return false; // insert unsuccessful
				}

				connection.commit(); // commit insert
				return true; // insert successful
			}

			else
				return false;
		} // end try

		// detect problems updating database
		catch (SQLException sqlException) {
			// rollback transaction
			try {
				connection.rollback(); // rollback update
				return false; // update unsuccessful
			}

			// handle exception rolling back transaction
			catch (SQLException exception) {
				throw new DataAccessException(exception);
			}
		}
	} // end
		// method
		// newPerson

	// Delete an entry. Method returns boolean indicating
	// success or failure.
	public boolean deletePerson(AddressBookEntry person) throws DataAccessException {
		// delete a person from database
		try {
			int result;

			// delete address from addresses table
			sqlDeleteAddress.setInt(1, person.getPersonID());
			result = sqlDeleteAddress.executeUpdate();

			// if delete fails, rollback and discontinue
			if (result == 0) {
				connection.rollback(); // rollback delete
				return false; // delete unsuccessful
			}

			// delete phone number from phoneNumbers table
			sqlDeletePhone.setInt(1, person.getPersonID());
			result = sqlDeletePhone.executeUpdate();

			// if delete fails, rollback and discontinue
			if (result == 0) {
				connection.rollback(); // rollback delete
				return false; // delete unsuccessful
			}

			// delete email address from emailAddresses table
			sqlDeleteEmail.setInt(1, person.getPersonID());
			result = sqlDeleteEmail.executeUpdate();

			// if delete fails, rollback and discontinue
			if (result == 0) {
				connection.rollback(); // rollback delete
				return false; // delete unsuccessful
			}

			// delete name from names table
			sqlDeleteName.setInt(1, person.getPersonID());
			result = sqlDeleteName.executeUpdate();

			// if delete fails, rollback and discontinue
			if (result == 0) {
				connection.rollback(); // rollback delete
				return false; // delete unsuccessful
			}

			connection.commit(); // commit delete
			return true; // delete successful
		} // end try

		// detect problems updating database
		catch (SQLException sqlException) {
			// rollback transaction
			try {
				connection.rollback(); // rollback update
				return false; // update unsuccessful
			}

			// handle exception rolling back transaction
			catch (SQLException exception) {
				throw new DataAccessException(exception);
			}
		}
	} // end method deletePerson

	// method to close statements and database connection
	public void close() {
		// close database connection
		try {
			sqlFind.close();
			sqlPersonID.close();
			sqlInsertName.close();
			sqlInsertAddress.close();
			sqlInsertPhone.close();
			sqlInsertEmail.close();
			sqlUpdateName.close();
			sqlUpdateAddress.close();
			sqlUpdatePhone.close();
			sqlUpdateEmail.close();
			sqlDeleteName.close();
			sqlDeleteAddress.close();
			sqlDeletePhone.close();
			sqlDeleteEmail.close();
			// close new prepared statements

			sqlGetNames.close();
			sqlFindAddress.close();
			sqlGetAddresses.close();
			// sqlRemoveAddress.close();
			sqlFindPhoneNumber.close();
			sqlGetPhoneNumbers.close(); //
			// sqlRemovePhoneNumber.close();
			sqlFindEmail.close();
			sqlGetEmails.close();
			// sqlRemoveEmail.close();

			connection.close();

		} // end try

		// detect problems closing statements and connection
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	} // end method close

	// Method to clean up database connection. Provided in case
	// CloudscapeDataAccess object is garbage collected.
	protected void finalize() {
		close();
	}
} // end class CloudscapeDataAccess

/**************************************************************************
 * (C) Copyright 2001 by Deitel & Associates, Inc. and Prentice Hall. * All
 * Rights Reserved. * * DISCLAIMER: The authors and publisher of this book have
 * used their * best efforts in preparing the book. These efforts include the *
 * development, research, and testing of the theories and programs * to
 * determine their effectiveness. The authors and publisher make * no warranty
 * of any kind, expressed or implied, with regard to these * programs or to the
 * documentation contained in these books. The authors * and publisher shall not
 * be liable in any event for incidental or * consequential damages in
 * connection with, or arising out of, the * furnishing, performance, or use of
 * these programs. *
 *************************************************************************/
