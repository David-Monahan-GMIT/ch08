import java.util.ArrayList;
import java.util.List;

// Fig. 8.33: AddressBookEntry.java
// JavaBean to represent one address book entry.

public class AddressBookEntry {
	private String firstName = "";
	private String lastName = "";
	private String address1 = "";
	private String address2 = "";
	private String city = "";
	private String state = "";
	private String eircode = "";
	private String phoneNumber = "";
	private String emailAddress = "";
	private int personID;
/*	private int addressID;
	private int phoneID;
	private int emailID;*/
	
	// Expanded data fields
	private List<List> addresses = new ArrayList<List>();
	private List<String> phoneNumbers = new ArrayList<String>();
	private List<String> emails = new ArrayList<String>();
	
	private List<Integer> addressIDS = new ArrayList<Integer>();
	private List<Integer> phoneIDS = new ArrayList<Integer>();
	private List<Integer> emailIDS = new ArrayList<Integer>();
	
	public void addAddressID(int id) {
		addressIDS.add(id);
	}
	public void removeAddressID(Integer id) {
		addressIDS.remove(id);
	}
	
	public void addPhoneID(int id) {
		phoneIDS.add(id);
	}
	
	public void removePhoneID(Integer id) {
		phoneIDS.remove(id);
	}
	
	public void addEmailID(int id) {
		emailIDS.add(id);
	}
	
	public void removeEmailID(Integer id) {
		emailIDS.remove(id);
	}
	public List<Integer> getAddressIDS() {
		return addressIDS;
	}

	public void setAddressIDS(List<Integer> addressIDS) {
		this.addressIDS = addressIDS;
	}

	public List<Integer> getPhoneIDS() {
		return phoneIDS;
	}

	public void setPhoneIDS(List<Integer> phoneIDS) {
		this.phoneIDS = phoneIDS;
	}

	public List<Integer> getEmailIDS() {
		return emailIDS;
	}

	public void setEmailIDS(List<Integer> emailIDS) {
		this.emailIDS = emailIDS;
	}

	public void addAddress(List<String> address) {
		addresses.add(address);
	}
	
	public void removeAddress(List<String> address) {
		this.addresses.remove(address);
	}
	public void addPhoneNumber(String phoneNumber) {
		phoneNumbers.add(phoneNumber);
	}
	
	public void addEmail(String email) {
		emails.add(email);
	}

	public List<List> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<List> addresses) {
		this.addresses = addresses;
	}

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
	}



	// empty constructor
	public AddressBookEntry() {
	}

	// set person's id
	public AddressBookEntry(int id) {
		personID = id;
	}

	// set person's first name
	public void setFirstName(String first) {
		firstName = first;
	}

	// get person's first name
	public String getFirstName() {
		return firstName;
	}

	// set person's last name
	public void setLastName(String last) {
		lastName = last;
	}

	// get person's last name
	public String getLastName() {
		return lastName;
	}

	// set first line of person's address
	public void setAddress1(String firstLine) {
		address1 = firstLine;
	}

	// get first line of person's address
	public String getAddress1() {
		return address1;
	}

	// set second line of person's address
	public void setAddress2(String secondLine) {
		address2 = secondLine;
	}

	// get second line of person's address
	public String getAddress2() {
		return address2;
	}

	// set city in which person lives
	public void setCity(String personCity) {
		city = personCity;
	}

	// get city in which person lives
	public String getCity() {
		return city;
	}

	// set state in which person lives
	public void setState(String personState) {
		state = personState;
	}

	// get state in which person lives
	public String getState() {
		return state;
	}

	// set person's eircode
	public void setEircode(String eir) {
		eircode = eir;
	}

	// get person's eircode
	public String getEircode() {
		return eircode;
	}

	// set person's phone number
	public void setPhoneNumber(String number) {
		phoneNumber = number;
	}

	// get person's phone number
	public String getPhoneNumber() {
		return phoneNumber;
	}

	// set person's email address
	public void setEmailAddress(String email) {
		emailAddress = email;
	}

	// get person's email address
	public String getEmailAddress() {
		return emailAddress;
	}

	// get person's ID
	public int getPersonID() {
		return personID;
	}

/*	// set person's addressID
	public void setAddressID(int id) {
		addressID = id;
	}

	// get person's addressID
	public int getAddressID() {
		return addressID;
	}

	// set person's phoneID
	public void setPhoneID(int id) {
		phoneID = id;
	}

	// get person's phoneID
	public int getPhoneID() {
		return phoneID;
	}

	// set person's emailID
	public void setEmailID(int id) {
		emailID = id;
	}

	// get person's emailID
	public int getEmailID() {
		return emailID;
	}*/
} // end class AddressBookEntry

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
