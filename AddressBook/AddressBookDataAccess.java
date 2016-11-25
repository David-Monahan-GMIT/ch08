/**
 * David Monahan 24/11/2016
 * Client Server Programming Addressbook/sql assignment
 * This is a modified version of the example addressbook code which can store multiple addresses, 
 * phone numbers and emails for a given name entry. The search function can now return multiple 
 * results for a given name, the gui has been given a new look and feel and the overall program 
 * should be thread safe. * 
 */
// Fig. 8.34: AddressBookDataAccess.java
// Interface that specifies the methods for inserting,
// updating, deleting and finding records.


// Java core packages
import java.util.List;

public interface AddressBookDataAccess {
      
   // Locate specified person by last name. Return 
   // AddressBookEntry containing information.
   public List<AddressBookEntry> findPerson( String lastName );
   
   // Update information for specified person.
   // Return boolean indicating success or failure.
   public boolean savePerson( 
      AddressBookEntry person ) throws DataAccessException;

   // Insert a new person. Return boolean indicating 
   // success or failure.
   public boolean newPerson( AddressBookEntry person )
      throws DataAccessException;
      
   // Delete specified person. Return boolean indicating if 
   // success or failure.
   public boolean deletePerson( 
      AddressBookEntry person ) throws DataAccessException;
      
   // close data source connection
   public void close(); 
}  // end interface AddressBookDataAccess


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
