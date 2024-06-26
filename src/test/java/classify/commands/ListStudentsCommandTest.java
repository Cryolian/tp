package classify.commands;
//@@author tayponghee

import classify.student.Student;
import classify.student.StudentAttributes;
import classify.student.SubjectGrade;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListStudentsCommandTest {

    private static final String YI_XIN = "Yi Xin";
    private static final String GERARD = "Gerard";
    private static final String SCIENCE = "Science";
    private static final String MATH = "Math";

    /**
     * Test case to verify listing students with valid phone numbers.
     * This test checks if the method listStudentsWithPhoneNumbers(list)
     * correctly lists students with valid phone numbers.
     */
    @Test
    void testListStudentsWithValidNumbers() {
        ArrayList<Student> students = new ArrayList<>();
        Student student1 = new Student(YI_XIN);
        StudentAttributes attributes1 = new StudentAttributes(student1);
        attributes1.setPhoneNumber(87654321);
        student1.setAttributes(attributes1);
        students.add(student1);

        ArrayList<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("1." + YI_XIN + " - Phone Number: 87654321");

        assertEquals(expectedOutput, captureOutput(() -> ListStudentsCommand.listStudentsWithPhoneNumbers(students)));
    }

    /**
     * Test case to verify listing students with invalid phone numbers.
     * This test checks if the method listStudentsWithPhoneNumbers(List)
     * correctly handles students with invalid phone numbers.
     */
    @Test
    void testListStudentsWithInvalidNumbers() {
        ArrayList<Student> students = new ArrayList<>();
        Student student1 = new Student(GERARD);
        StudentAttributes attributes1 = new StudentAttributes(student1);
        attributes1.setPhoneNumber(1234567);
        student1.setAttributes(attributes1);
        students.add(student1);

        ArrayList<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("1." + GERARD + " - Invalid Phone Number! Save file may be corrupt.");

        assertEquals(expectedOutput, captureOutput(() -> ListStudentsCommand.listStudentsWithPhoneNumbers(students)));
    }

    /**
     * Test case to verify listing students with total classes attended.
     * This test ensures that the method listStudentsWithTotalClasses(list)
     * correctly lists students along with their total classes attended.
     */
    @Test
    void testListStudentsWithTotalClasses() {
        ArrayList<Student> students = new ArrayList<>();
        Student student1 = new Student(YI_XIN);
        StudentAttributes attributes1 = new StudentAttributes(student1);
        SubjectGrade mathGrade = new SubjectGrade(MATH, 10, 10);
        SubjectGrade scienceGrade = new SubjectGrade(SCIENCE, 12,-1);
        attributes1.getSubjectGrades().add(mathGrade);
        attributes1.getSubjectGrades().add(scienceGrade);
        student1.setAttributes(attributes1);
        students.add(student1);

        ArrayList<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("1." + YI_XIN + " - Total Classes Attended: 10");

        assertEquals(expectedOutput, captureOutput(() -> ListStudentsCommand.listStudentsWithTotalClasses(students)));
    }

    /**
     * Test case to verify listing students by subject.
     * This test ensures that the method listStudentsBySubject(list, subject)
     * correctly lists students with the specified subject.
     */
    @Test
    void testListStudentsBySubject() {
        ArrayList<Student> students = new ArrayList<>();
        Student student1 = new Student(YI_XIN);
        StudentAttributes attributes1 = new StudentAttributes(student1);
        SubjectGrade mathGrade = new SubjectGrade(MATH, 10,10);
        attributes1.getSubjectGrades().add(mathGrade);
        student1.setAttributes(attributes1);
        students.add(student1);

        ArrayList<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("Students with the subject " + '"' + MATH + '"' + ":");
        expectedOutput.add("- " + YI_XIN + " - " + "Classes Attended for Math: 10");
        expectedOutput.add("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        assertEquals(expectedOutput, captureOutput(() -> ListStudentsCommand.listStudentsBySubject(students, MATH)));
    }

    /**
     * Captures the output generated by a given action.
     *
     * @param action The action whose output needs to be captured.
     * @return A list containing the captured output lines.
     */
    private List<String> captureOutput(Runnable action) {
        List<String> output = new ArrayList<>();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        action.run();

        System.out.flush();
        System.setOut(originalOut);
        String[] lines = byteArrayOutputStream.toString().split(System.lineSeparator());
        for (String line : lines) {
            output.add(line.trim());
        }
        return output;
    }
}
