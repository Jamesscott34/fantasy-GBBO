import javax.mail.MessagingException;
import java.io.*;
import java.util.NoSuchElementException;

public class ExceptionHandler {

    public static void handleIOException(IOException e) {
        System.err.println("IOException occurred at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Please check file I/O operations and ensure correct file paths.");
    }

    public static void handleFileNotFoundException(FileNotFoundException e) {
        System.err.println("File not found at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Ensure that the specified file exists and the path is correct.");
    }

    public static void handleNumberFormatException(NumberFormatException e) {
        System.err.println("Number format exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Validate input format. Expected a number but received something else.");

    }

    public static void handleIndexOutOfBoundsException(IndexOutOfBoundsException e) {
        System.err.println("Index out of bounds exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Check array or collection access to ensure it's within bounds.");
    }

    public static void handleNullPointerException(NullPointerException e) {
        System.err.println("Null Pointer Exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Check for null references before accessing methods or fields.");
    }

    public static void handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e) {
        System.err.println("Array Index out of bounds at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Review array access and ensure it's within the array size.");
    }

    public static void handleClassCastException(ClassCastException e) {
        System.err.println("Class Cast Exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Check casting operations between incompatible types.");
    }

    public static void handleIllegalStateException(IllegalStateException e) {
        System.err.println("Illegal State Exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Review the state of the application to ensure it's valid for the current operation.");
    }

    public static void handleNoSuchElementException(NoSuchElementException e) {
        System.err.println("No Such Element Exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Check for missing elements or objects in collections or iterators.");
    }

    public static void handleUnsupportedOperationException(UnsupportedOperationException e) {
        System.err.println("Unsupported Operation Exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Check and avoid unsupported operations on objects or collections.");
    }

    public static void handleSecurityException(SecurityException e) {
        System.err.println("Security Exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Check security manager permissions and access restrictions.");
    }

    public static void handleStringIndexOutOfBoundsException(StringIndexOutOfBoundsException e) {
        System.err.println("String Index out of bounds at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Verify string manipulation and ensure correct indices are used.");
    }

    public static void handleUnsupportedClassVersionError(UnsupportedClassVersionError e) {
        System.err.println("Unsupported Class Version Error at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Check the Java version compatibility of the class being used.");
    }

    public static void handleArrayStoreException(ArrayStoreException e) {
        System.err.println("Array Store Exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Verify the type of object being stored in the array.");
    }

    public static void handleTypeNotPresentException(TypeNotPresentException e) {
        System.err.println("Type Not Present Exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Check for missing types or classes specified in annotations or reflective operations.");
    }

    public static void handleMessagingException(MessagingException e) {
        System.err.println("Messaging Exception at line " + e.getStackTrace()[0].getLineNumber() + ": " + e.getMessage());
        System.err.println("Check for issues related to messaging and email operations.");
    }
    public static void handleGeneralException(Exception ex) {
        System.err.println("General Exception occurred at line " + ex.getStackTrace()[0].getLineNumber() + ": " + ex.getMessage());

    }

    // Add more specific exception handlers as needed...

}
