/**
 * David Monahan 24/11/2016
 * Client Server Programming Addressbook/sql assignment
 * This is a modified version of the example addressbook code which can store multiple addresses, 
 * phone numbers and emails for a given name entry. The search function can now return multiple 
 * results for a given name, the gui has been given a new look and feel and the overall program 
 * should be thread safe. * 
 */
// Fig. 8.35 DataAccessException.java
// Class AddressBookDataAccess throws DataAccessExceptions
// when there is a problem accessing the data source.


@SuppressWarnings("serial")
public class DataAccessException extends Exception {

   private Exception exception;
 
   // constructor with String argument
   public DataAccessException( String message )
   {
     super( message );
   }

   // constructor with Exception argument
   public DataAccessException( Exception exception )
   {
      exception = this.exception;
   }

   // printStackTrace of exception from constructor
   public void printStackTrace()
   {
      exception.printStackTrace();
   }
}