import edu.innotech.Student;
import org.junit.Test;
import org.junit.Before;
import java.util.List;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    private Student student;

    @Before
    public void setUp() {
        student = new Student("Ivan");
    }

    @Test
    public void testAddValidGrade() {
        student.addGrade(4);
        assertEquals(1, student.getGrades().size());
        assertEquals(Integer.valueOf(4), student.getGrades().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalidGrade() {
        student.addGrade(6);
    }

    @Test
    public void testGetGradesImmutability() {
        student.addGrade(4);
        List<Integer> grades = student.getGrades();
        try {
            grades.add(3);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // expected, modification should throw exception
        }
        // check that grades did not change
        assertEquals(1, student.getGrades().size());
    }

    @Test
    public void testHashCodeAndEquals() {
        Student student1 = new Student("Ivan");
        Student student2 = new Student("Ivan");
        assertEquals(student1.hashCode(), student2.hashCode());
        assertTrue(student1.equals(student2));

        student1.addGrade(4);
        student2.addGrade(4);
        assertEquals(student1.hashCode(), student2.hashCode());
        assertTrue(student1.equals(student2));

        student2.addGrade(5);
        assertNotEquals(student1.hashCode(), student2.hashCode());
        assertFalse(student1.equals(student2));
    }

    @Test
    public void testSetName() {
        student.setName("Petr");
        assertEquals("Petr", student.getName());
    }

    @Test
    public void testToString() {
        student.addGrade(4);
        String expected = "Student{name=Ivan, marks=[4]}";
        assertEquals(expected, student.toString());
    }
}
