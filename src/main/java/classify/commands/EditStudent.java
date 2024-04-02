package classify.commands;

import java.util.ArrayList;
import java.util.Scanner;

import classify.student.Student;
import classify.student.StudentAttributes;
import classify.student.StudentList;
import classify.student.SubjectGrade;
import classify.user.InputParsing;
import classify.user.NameNumberMatchException;
import classify.ui.UI;

public class EditStudent {
    private static final String NUMBER = "number";
    private static final String PAYMENT = "payment";
    private static final String REMARKS = "remarks";
    private static final String ADD = "add";
    private static final String EDIT = "edit";
    private static final String DELETE = "delete";
    private static final String DEFAULT_STRING_VALUE = "Unknown";

    // @@author blackmirag3
    public static void editStudent(ArrayList<Student> list, Scanner in, String name) {
        if (list.isEmpty()) {
            UI.printEmptyListError();
            return;
        }

        Student student = null;

        if (name != null) {
            student = InputParsing.findStudentByName(list, name, in);
            if (student == null) {
                UI.printStudentNotFound();
            }
        }

        while (student == null) {

            System.out.println("Name of student to edit (enter blank to exit):");
            name = in.nextLine().trim();

            if (name.isBlank()) {
                System.out.println("Exiting edit.");
                return;
            }

            student = InputParsing.findStudentByName(list, name, in);

            if (student != null) {
                break;
            } else {
                UI.printStudentNotFound();
            }
        }

        editStudentAttributes(in, student);
    }

    private static void editStudentAttributes(Scanner in, Student student) {
        StudentAttributes attributes = student.getAttributes();
        ViewStudent.showAttributes(attributes);

        while (true) {
            UI.printEditPrompt();
            String command = in.nextLine().trim();
            if (command.isBlank()) {
                System.out.println("Exiting edit");
                UI.printDivider();
                return;
            }

            switch (command) {

            case ADD:
                AddStudent.addSubject(in, attributes);
                student.setAttributes(attributes);
                break;

            case EDIT:
                editAttribute(in, attributes);
                break;

            case DELETE:
                deleteAttribute(in, attributes);
                break;

            // @@author Cryolian
            case NUMBER:
                editNumber(in, attributes);
                break;

            case PAYMENT:
                editPaymentDate(in, attributes);
                break;

            case REMARKS:
                editRemarks(in, attributes);
                break;

            default:
                break;
            }

        }
    }

    // @@author Cryolian
    private static void editPaymentDate(Scanner in, StudentAttributes attributes) {
        UI.println("Current stored date: " + attributes.getLastPaymentDate());
        attributes.setLastPaymentDate(InputParsing.readInDate(in));
    }

    private static void editRemarks(Scanner in, StudentAttributes attributes) {
        UI.println(attributes.getRemarks());
        UI.printDivider();
        UI.println("Enter blank to stop editing.");

        String newRemarks = InputParsing.readInString(in);

        if (newRemarks.equals(DEFAULT_STRING_VALUE)) {
            return;
        }

        attributes.setRemarks(newRemarks);
        UI.printDivider();
    }

    private static void editNumber(Scanner in, StudentAttributes attributes) {
        try {
            openNumberEditInterface(in, attributes);
            UI.println("Number updated successfully: " + String.valueOf(attributes.getPhoneNumber()));
        } catch (NameNumberMatchException e) {
            UI.println("Please try again with a different number.");
            UI.println("Another student of the same name was found with the same number.");
        } catch (NumberFormatException e) {
            UI.println("Exiting number edit.");
        }
        UI.printDivider();
    }

    private static void openNumberEditInterface(Scanner in, StudentAttributes attributes)
            throws NameNumberMatchException, NumberFormatException {

        UI.printDivider();
        UI.println("Current Number: " + String.valueOf(attributes.getPhoneNumber()));
        UI.println("Type in and enter the letter a if you wish to stop editing.");
        int number = InputParsing.promptForPhoneNumber(in);
        StudentList.checkNameNumberPair(StudentList.masterStudentList, attributes.getName(), number);
        attributes.setPhoneNumber(number);
    }

    // @@author blackmirag3
    /**
     * Prompts the user to edit subject, grade, and classes attended for student.
     * Continues to prompt the user until user enters blank to exit.
     *
     * @param in         The scanner object to read user input.
     * @param attributes The StudentAttributes object to store the attributes of the
     *                   student.
     */
    private static void editAttribute(Scanner in, StudentAttributes attributes) {

        while (true) {

            System.out.print("Subject to edit (enter nothing to exit): ");
            String subjectToFind = in.nextLine().trim();

            if (subjectToFind.isBlank()) {
                System.out.println("No subject edited.");
                return;
            }

            SubjectGrade currentSubject = attributes.findSubject(subjectToFind);

            if (currentSubject != null) {
                System.out.print("New subject name (enter nothing to skip): ");
                String newName = in.nextLine().trim();

                if (!newName.isBlank()) {
                    currentSubject.setSubject(newName);
                }

                double newGrade = InputParsing.promptForGrade(in);

                if (newGrade != -1) {
                    currentSubject.setGrade(newGrade);
                }

                int newClassesAttended = AddStudent.promptForClassesAttended(in);
                currentSubject.setClassesAttended(newClassesAttended);
                System.out.println("Subject updated.");

            } else {
                System.out.println("Subject not found.");
            }

        }
    }

    private static void deleteAttribute(Scanner in, StudentAttributes attributes) {
        while (true) {

            System.out.println("Subject to delete (enter blank to exit)");
            String subjectToDelete = in.nextLine().trim();

            if (subjectToDelete.isBlank()) {
                System.out.println("no subject deleted");
                return;
            }

            if (attributes.findSubject(subjectToDelete) != null) {
                attributes.deleteSubject(subjectToDelete);
                System.out.println("Subject deleted");
            } else {
                System.out.println("subject not found");
            }
        }
    }

}
