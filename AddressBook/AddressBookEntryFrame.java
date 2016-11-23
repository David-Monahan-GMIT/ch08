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

public class AddressBookEntryFrame extends JInternalFrame {
   
   // HashMap to store JTextField references for quick access
   private HashMap fields; 
   
   // current AddressBookEntry set by AddressBook application
   private AddressBookEntry person;
   
   // panels to organize GUI
   private JPanel leftPanel, middlePanel, rightPanel;
   
   // static integers used to determine new window positions  
   // for cascading windows
   private static int xOffset = 0, yOffset = 0;
   
   // static Strings that represent name of each text field.
   // These are placed on JLabels and used as keys in 
   // HashMap fields.
   private static final String FIRST_NAME = "First Name", 
      LAST_NAME = "Last Name", ADDRESS1 = "Address 1", 
      ADDRESS2 = "Address 2", CITY = "City", STATE = "State", 
      EIRCODE = "Eircode", PHONE = "Phone", EMAIL = "Email";
  
   // construct GUI
   public AddressBookEntryFrame()
   {
      super( "Address Book Entry", true, true );
      
      fields = new HashMap();  

      leftPanel = new JPanel();
      leftPanel.setLayout( new GridLayout( 12, 1, 0, 5 ) );
      middlePanel = new JPanel();
      middlePanel.setLayout( new GridLayout( 12, 1, 0, 5 ) );
      rightPanel = new JPanel();
      rightPanel.setLayout( new GridLayout( 12, 1, 0, 5 ) );
      
      createRow( FIRST_NAME );
      createRow( LAST_NAME );
      createButton("Addresses: ", "Add Address", "Remove Address");
      createRow( ADDRESS1 );
      createRow( ADDRESS2 );
      createRow( CITY );
      createRow( STATE );
      createRow( EIRCODE );
      createButton("Phone Numbers: ", "Add Phone Number", "Remove Phone Number");
      createRow( PHONE );
      createButton("E-Mails: ", "Add E-Mails", "Remove E-Mails");
      createRow( EMAIL );
      
      Container container = getContentPane();
      container.add( leftPanel, BorderLayout.WEST );
      container.add( middlePanel, BorderLayout.CENTER );
      container.add(rightPanel, BorderLayout.EAST );
     
      setBounds( xOffset, yOffset, 700, 500 );
      xOffset = ( xOffset + 30 ) % 700;
      yOffset = ( yOffset + 30 ) % 500;
   }

   // set AddressBookEntry then use its properties to 
   // place data in each JTextField
   public void setAddressBookEntry( AddressBookEntry entry )
   {
      person = entry;
      
      setField( FIRST_NAME, person.getFirstName() );
      setField( LAST_NAME, person.getLastName() );
/*      setField( ADDRESS1, person.getAddress1() );
      setField( ADDRESS2, person.getAddress2() );
      setField( CITY, person.getCity() );
      setField( STATE, person.getState() );
      setField( EIRCODE, person.getEircode() );
      setField( PHONE, person.getPhoneNumber() );
      setField( EMAIL, person.getEmailAddress() );*/
      for( List<String> address : person.getAddresses()) {
          setField( ADDRESS1, address.get(0) );
          setField( ADDRESS2, address.get(1) );
          setField( CITY, address.get(2) );
          setField( STATE, address.get(3) );
          setField( EIRCODE, address.get(4) );
      }
      for (String phoneNumber: person.getPhoneNumbers()) {
    	  setField( PHONE, phoneNumber);
      }
      for (String email : person.getEmails()){
    	  setField( EMAIL, email);
      }
      

   }
   
   // store AddressBookEntry data from GUI and return 
   // AddressBookEntry
   public AddressBookEntry getAddressBookEntry()
   {
      person.setFirstName( getField( FIRST_NAME ) );
      person.setLastName( getField( LAST_NAME ) );
/*      person.setAddress1( getField( ADDRESS1 ) );
      person.setAddress2( getField( ADDRESS2 ) );
      person.setCity( getField( CITY ) );
      person.setState( getField( STATE ) );
      person.setEircode( getField( EIRCODE ) );
      person.setPhoneNumber( getField( PHONE ) );
      person.setEmailAddress( getField( EMAIL ) );*/
      
      List<String> address = new ArrayList<String>();
      address.add(getField( ADDRESS1 ));
      address.add(getField( ADDRESS2 ));
      address.add(getField( CITY ));
      address.add(getField( EIRCODE ));
      person.addAddress(address);
      person.addEmail(getField( EMAIL ));
      person.addPhoneNumber(getField( PHONE));
      
      return person;
   }

   // set text in JTextField by specifying field's
   // name and value
   private void setField( String fieldName, String value )
   {
      JTextField field = 
         ( JTextField ) fields.get( fieldName );
      
      field.setText( value );
   }
   
   // get text in JTextField by specifying field's name
   private String getField( String fieldName )
   {
      JTextField field = 
         ( JTextField ) fields.get( fieldName );
            
      return field.getText();  
   }
   
   // utility method used by constructor to create one row in
   // GUI containing JLabel and JTextField
   private void createRow( String name )
   {            
      JLabel label = new JLabel( name, SwingConstants.RIGHT );
      label.setBorder( 
         BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
      leftPanel.add( label );
          
      JTextField field = new JTextField( 30 );
      middlePanel.add( field );
      
      JButton hiddenButton = new JButton();
      hiddenButton.setVisible(false);
      rightPanel.add(hiddenButton);

      fields.put( name, field );
   }
   
   private void createButton( String sectionName, String buttonAddName , String buttonRemoveName)
   {            
      JLabel label = new JLabel( sectionName, SwingConstants.LEFT );
      label.setBorder( 
         BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
      leftPanel.add( label );
          
      JButton buttonAdd = new JButton(buttonAddName);
      middlePanel.add( buttonAdd );
      
      JButton buttonRemove = new JButton(buttonRemoveName);
      rightPanel.add( buttonRemove );

     // fields.put( name, field );
   }
}  // end class AddressBookEntryFrame


/**************************************************************************
 * (C) Copyright 2001 by Deitel & Associates, Inc. and Prentice Hall.     *
 * All Rights Reserved.                                                   *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
