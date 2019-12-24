/*
Class to handle queries into the database (selecting, inserting, deleting, etc.).
 */
package gradecalculatorv3;

import java.sql.*;
import java.util.LinkedList;
import javax.swing.JOptionPane;

public class Querying {

    public String url = "jdbc:mysql://localhost:3306/college";

    public Querying() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
    }

    public LinkedList<String> getSemesters() {
        try {
            LinkedList<String> semesters = new LinkedList<>();

            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            String sqlString = "SELECT semester FROM grades GROUP BY semester ORDER BY SUBSTRING(semester, LENGTH(semester) - 4) DESC, SUBSTRING(semester, 1) ASC;";
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                semesters.add(rs.getString(1));
            }

            stmt.close();
            rs.close();
            conn.close();
            return semesters;
        } catch (Exception e) {
            System.err.println("No semesters returned. ");
            System.err.println(e.getMessage());
            return null;
        }
    }

    public LinkedList<String> getClasses(String semester) {
        try {
            LinkedList<String> classes = new LinkedList<>();

            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            String sqlString = "SELECT class FROM grades WHERE semester = '" + semester + "' GROUP BY class;";
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                classes.add(rs.getString(1));
            }

            stmt.close();
            rs.close();
            conn.close();
            return classes;
        } catch (Exception e) {
            System.err.println("No classes returned. ");
            System.err.println(e.getMessage());
            return null;
        }
    }

    //getGradesAndWeights returns a linked list of doubles. 
    //Every 3 values is a new grade.
    //EXAMPLE: 0 ~ description ('Test 1', 'Team Project', etc.), 1 ~ grade, 2 ~ weight.    
    public LinkedList<String> getGradesWeightsDescriptions(String className, String semester) {
        try {
            LinkedList<String> gradesWeightsDesc = new LinkedList<>();

            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            String sqlString = "SELECT description, grade, weight FROM grades WHERE semester = '" + semester + "' AND class = '" + className + "';";
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                gradesWeightsDesc.add(rs.getString(1));
                gradesWeightsDesc.add(rs.getString(2));
                gradesWeightsDesc.add(rs.getString(3));
            }

            stmt.close();
            rs.close();
            conn.close();
            return gradesWeightsDesc;
        } catch (Exception e) {
            System.err.println("No grades returned.");
            System.err.println(e.getMessage());
            return null;
        }
    }

    //createAndInitializeNewClass - creates a new class and inserts the first grade for it.
    public void createAndInitializeNewClass(String className, String initialGrade, String initialWeight, String initialDescription, String semester) {
        className = fixStringForMySQL(className);
        initialDescription = fixStringForMySQL(initialDescription);
        String sqlString = "INSERT INTO grades (class, description, grade, weight, semester) VALUES ('" + className + "', '" + initialDescription + "'," + initialGrade + ",'" + initialWeight + "','" + semester + "'); ";
        //Now, execute the insert statement.
        try {
            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            PreparedStatement stmt = conn.prepareStatement(sqlString);
            // execute the preparedstatement
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Successfully added the class.", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            JOptionPane.showMessageDialog(null, "Error inserting into MySQL. Try again.", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.err.println(e.getMessage());
        }
    }

    public double getTotalCreditHours() {
        try {
            double credits = 0.0;

            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            String sqlString = "SELECT sum(credits) FROM courses;";
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                credits += rs.getDouble(1);
            }

            stmt.close();
            rs.close();
            conn.close();
            return credits;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);  // Show the exception message.
            return 0.0;
        }
    }

    public double getGPA() {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            String sqlString = "SELECT finalGrade, credits FROM courses;";
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            ResultSet rs = stmt.executeQuery();

            double totalCredits = 0.0;
            double qualityPoints = 0.0;
            while (rs.next()) {
                String grade = rs.getString(1);
                double credits = rs.getDouble(2);
                totalCredits += credits;
                if (grade.equals("A")) {
                    qualityPoints += 4.0 * credits;
                } else if (grade.equals("B")) {
                    qualityPoints += 3.0 * credits;
                } else if (grade.equals("C")) {
                    qualityPoints += 2.0 * credits;
                } else if (grade.equals("D")) {
                    qualityPoints += 1.0 * credits;
                } else if (grade.equals("F")) {
                    // no quality points earned - failed the course
                } else {
                    // this is a grade that does not count against GPA (for example, "P" for pass, or "S" for satisfactory); don't add the credits to totalCredits
                    totalCredits -= credits;
                }
            }

            stmt.close();
            rs.close();
            conn.close();

            return qualityPoints / totalCredits;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);  // Show the exception message.
            return 0.0;
        }
    }

    public LinkedList<String> getCourses() {
        try {
            LinkedList<String> courses = new LinkedList<>();

            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            String sqlString = "SELECT className FROM courses ORDER BY className;";
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                courses.add(rs.getString(1));
            }

            stmt.close();
            rs.close();
            conn.close();
            return courses;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);  // Show the exception message.
            System.err.println("No courses returned.");
            System.err.println(e.getMessage());
            return null;
        }
    }

    public String[] getCourseInfo(String className) {
        try {
            String info[] = new String[4];

            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            String sqlString = "SELECT * FROM courses where className = '" + className + "';";

            PreparedStatement stmt = conn.prepareStatement(sqlString);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                info[0] = rs.getString(1);
                info[1] = rs.getString(2);
                info[2] = rs.getString(3);
                info[3] = rs.getString(4);
            }
            stmt.close();
            rs.close();
            conn.close();
            return info;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);  // Show the exception message.
            System.err.println("No courses returned.");
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void deleteClass(String className) {
        try {
            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            String sqlString = "DELETE FROM courses WHERE className = '" + className + "';";
            PreparedStatement stmt = conn.prepareStatement(sqlString);
            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void sendNewGradesToDatabase(LinkedList<String> newGrades, String className, String semester) throws SQLException {
        //First, remove any grades already present for the specified class (this avoids repeating grades).        
        try {
            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            String sqlString = "DELETE FROM grades WHERE class = '" + className + "' AND semester = '" + semester + "';";
            PreparedStatement stmt = conn.prepareStatement(sqlString);
            stmt.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }

        //Now, submit the new grades.
        for (int i = 0; i < newGrades.size() - 2; i += 3) {
            String desc = fixStringForMySQL(newGrades.get(i).toString());
            String sqlString = "INSERT INTO grades (description, grade, weight, class, semester) VALUES ('" + desc + "'," + newGrades.get(i + 1) + "," + newGrades.get(i + 2) + ",'" + className.toUpperCase() + "','" + semester + "'); ";
            //Now, execute the insert statement.
            try {
                Connection conn = DriverManager.getConnection(url, "root", "granted27");
                PreparedStatement stmt = conn.prepareStatement(sqlString);
                // execute the preparedstatement
                stmt.execute();

                stmt.close();
                conn.close();
            } catch (Exception e) {
                System.err.println("Got an exception!");
                System.err.println(e.getMessage());
            }
        }
    }

    public void submitNewClass(String className, String finalGrade, String credits, String semester) throws Exception {
        String sqlString = "INSERT INTO courses (className, finalGrade, credits, semester) VALUES ('" + className + "','" + finalGrade + "'," + credits + ",'" + semester + "');";
        //Now, execute the insert statement.
        try {
            Connection conn = DriverManager.getConnection(url, "root", "granted27");
            PreparedStatement stmt = conn.prepareStatement(sqlString);
            // execute the preparedstatement
            stmt.execute();

            stmt.close();
            conn.close();
        } catch (Exception e) {
            throw e;
        }
    }

    //Fix the apostrophe and newline so that they fit in MySQL properly.
    public String fixStringForMySQL(String s) {
        String s2 = s.replace("\'", "\\'");
        s2 = s2.replace("\n", "\\n");
        return s2;
    }
}
