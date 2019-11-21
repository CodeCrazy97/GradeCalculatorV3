/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gradecalculatorv3;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Ethan
 */
public class GCV4 extends javax.swing.JFrame {

    /**
     * Creates new form GCV4
     */
    public GCV4() {
        initComponents();

        ArrayList<String> semesters = getSemesters();
        if (semesters.size() == 0) {
            coursesjPanel1.setVisible(false);
            jButton3.setVisible(false);
        } else {  // There were semesters.
            for (int i = 0; i < semesters.size(); i++) {
                semestersComboBox.addItem(semesters.get(i));
            }
        }

        /*
        titleList.add("Assignment 5");
        weightList.add("10.5");
        scoreList.add("98.3");
        titleList.add("Test 1");
        weightList.add("25");
        scoreList.add("68");
         */
        calculateOverallGrade();
        calculateOverallPercentagePoints();
        calculatePercentageComplete();

        newClassjPanel2.setVisible(false);
        infojLabel1.setVisible(false);
        editGradejPanel1.setVisible(false);
        assignmentTitleTextField.setVisible(false);
        weightTextField.setVisible(false);
        scoreTextField.setVisible(false);
        submitChangesToDBjButton2.setVisible(false);

    }

    // Get all semesters that have assignment grades.
    public static void createTablesIfNotExist() {
        // Check that course table exists.
        String sql = "SELECT COUNT(*) FROM course;";
        try (Connection conn = connectToCollege();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("no such table: course")) {
                createCourseTable();
                // Since course table does not exist, must create assignment table.
                createAssignmentTable();
                // Exit this method, since would not want to attempt checking if assignment table exists twice.
                return;
            }
        }

        // Check that assignment table exists (only reached if course table does exist).
        sql = "SELECT COUNT(*) FROM assignment;";
        try (Connection conn = connectToCollege();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("no such table: assignment")) {
                createAssignmentTable();
            }
        }

    }

    // Get all semesters that have assignment grades.
    public ArrayList<String> getSemesters() {
        String sql = "SELECT semester_taken, year_taken FROM course GROUP BY semester_taken, year_taken ORDER BY year_taken DESC;";
        ArrayList<String> semesters = new ArrayList<String>();
        try (Connection conn = connectToCollege();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                String semester = rs.getString(1) + " " + rs.getString(2);
                semesters.add(semester);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return semesters;
    }

    public static String getAbsolutePath() {
        return (new File("").getAbsolutePath() + "/college.db").replace("\\", "/");
    }

    public static void addAssignment(String title, double weight, double grade, int courseID) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + getAbsolutePath();

        String sql = "INSERT INTO assignment(title, weight, grade, course_id) VALUES(?, ?, ?, ?)";

        try (Connection conn = connectToCollege(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
            // pstmt.setInt(1, id);
            pstmt.setString(1, title);
            pstmt.setDouble(2, weight);
            pstmt.setDouble(3, grade);
            pstmt.setInt(4, courseID);
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void addCourse(String title, double credits, String semesterTaken, int yearTaken, String finalGrade) throws SQLException {
        semesterTaken = semesterTaken.toUpperCase();
        title = title.toUpperCase();

        String url = "jdbc:sqlite:" + getAbsolutePath();

        String sql = "INSERT INTO course(title, credits, semester_taken, year_taken, final_grade) VALUES(?, ?, ?, ?, ?);";

        try (Connection conn = connectToCollege(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
            //pstmt.setInt(1, id);
            pstmt.setString(1, title);
            pstmt.setDouble(2, credits);
            pstmt.setString(3, semesterTaken);
            pstmt.setInt(4, yearTaken);
            pstmt.setString(5, finalGrade);
            System.out.println("\n\ntitle = " + title + ", semester taken = " + semesterTaken + ", year taken = " + yearTaken);
            pstmt.executeUpdate();
            conn.close();
        }
    }

    public static Connection connectToCollege() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + getAbsolutePath();
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void selectAll() {
        System.out.println("\n\nCourses:");
        String sql = "SELECT id, title, credits, semester_taken, year_taken FROM course;";

        try (Connection conn = connectToCollege();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", TITLE: " + rs.getString("title") + ", SEMESTER TAKEN: " + rs.getString(4) + ", YEAR TAKEN: " + rs.getString(5));
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        System.out.println("\n\nGrades:");
        sql = "SELECT id, title, course_id FROM assignment;";
        // select assignments
        try (Connection conn = connectToCollege();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println("ID : " + rs.getInt("id") + ", TITLE: " + rs.getString("title") + ", COURSE ID: " + rs.getInt("course_id"));
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void deleteFromTables() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + getAbsolutePath();

        // SQL statement for creating a new table
        String sql = "DELETE FROM assignment";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // SQL statement for creating a new table
        sql = "DELETE FROM course";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void dropTables() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + getAbsolutePath();

        // SQL statement for creating a new table
        String sql = "DROP TABLE assignment;";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // SQL statement for creating a new table
        sql = "DROP TABLE course;";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createCourseTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + getAbsolutePath();

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS course(\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	title CHAR(7) NOT NULL,\n"
                + "	credits DOUBLE NOT NULL,\n"
                + "	semester_taken CHAR(12) NOT NULL,\n"
                + "	year_taken INT(4) NOT NULL,\n"
                + "	final_grade CHAR(1),\n"
                + "	UNIQUE (title, semester_taken, year_taken)\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("created course table");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createAssignmentTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + getAbsolutePath();

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS assignment(\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	title CHAR(50),\n"
                + "	weight DOUBLE NOT NULL,\n"
                + "	grade DOUBLE NOT NULL,\n"
                + "	course_id INT NOT NULL, \n"
                + "	CONSTRAINT course_id_constraint FOREIGN KEY (course_id) REFERENCES course(id),\n"
                + "	UNIQUE (title, course_id)\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("created assignment table");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:" + getAbsolutePath();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
        }
        return conn;
    }

    public static void createNewDatabase() {
        String url = "jdbc:sqlite:" + getAbsolutePath();
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("null db");
                DatabaseMetaData meta = conn.getMetaData();
            } else {
                System.out.println("db was present");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gradesjPanel1 = new javax.swing.JPanel();
        titleList = new java.awt.List();
        currentGradeLabel = new javax.swing.JLabel();
        scoreList = new java.awt.List();
        percentagePointsLabel = new javax.swing.JLabel();
        weightList = new java.awt.List();
        percentCompleteLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        submitChangesToDBjButton2 = new javax.swing.JButton();
        editGradejPanel1 = new javax.swing.JPanel();
        assignmentTitlejTextField1 = new javax.swing.JTextField();
        weightjTextField2 = new javax.swing.JTextField();
        scoreReceivedjTextField3 = new javax.swing.JTextField();
        addChangesjButton5 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        desiredGradejTextField1 = new javax.swing.JTextField();
        assumedGradejTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        newClassjPanel2 = new javax.swing.JPanel();
        classNamejTextField1 = new javax.swing.JTextField();
        yearTakenjTextField1 = new javax.swing.JTextField();
        semesterTakenjTextField1 = new javax.swing.JTextField();
        coursesjPanel1 = new javax.swing.JPanel();
        coursesComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        semestersComboBox = new javax.swing.JComboBox<>();
        canceljButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        assignmentTitleTextField = new javax.swing.JTextField();
        weightTextField = new javax.swing.JTextField();
        scoreTextField = new javax.swing.JTextField();
        infojLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        titleList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                titleListMouseClicked(evt);
            }
        });
        titleList.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                titleListFocusGained(evt);
            }
        });
        titleList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleListActionPerformed(evt);
            }
        });

        currentGradeLabel.setText("Current grade:");

        scoreList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scoreListMouseClicked(evt);
            }
        });

        percentagePointsLabel.setText("Percentage points earned: ");

        weightList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                weightListMouseClicked(evt);
            }
        });

        percentCompleteLabel.setText("Percent complete: ");

        jButton1.setText("Delete Assignment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        submitChangesToDBjButton2.setText("Submit Changes to Database");
        submitChangesToDBjButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitChangesToDBjButton2ActionPerformed(evt);
            }
        });

        assignmentTitlejTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                assignmentTitlejTextField1KeyReleased(evt);
            }
        });

        addChangesjButton5.setText("Submit");
        addChangesjButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addChangesjButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editGradejPanel1Layout = new javax.swing.GroupLayout(editGradejPanel1);
        editGradejPanel1.setLayout(editGradejPanel1Layout);
        editGradejPanel1Layout.setHorizontalGroup(
            editGradejPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editGradejPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(assignmentTitlejTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(weightjTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scoreReceivedjTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(addChangesjButton5)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        editGradejPanel1Layout.setVerticalGroup(
            editGradejPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editGradejPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editGradejPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(assignmentTitlejTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weightjTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scoreReceivedjTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addChangesjButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton5.setText("Calculate");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel1.setText("Grade I would like in this course:");

        jLabel2.setText("What if I make this grade on remaining asignments?");

        javax.swing.GroupLayout gradesjPanel1Layout = new javax.swing.GroupLayout(gradesjPanel1);
        gradesjPanel1.setLayout(gradesjPanel1Layout);
        gradesjPanel1Layout.setHorizontalGroup(
            gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradesjPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gradesjPanel1Layout.createSequentialGroup()
                        .addGroup(gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(percentCompleteLabel)
                            .addComponent(percentagePointsLabel)
                            .addComponent(currentGradeLabel))
                        .addContainerGap())
                    .addGroup(gradesjPanel1Layout.createSequentialGroup()
                        .addComponent(titleList, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(weightList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(scoreList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(gradesjPanel1Layout.createSequentialGroup()
                                .addGroup(gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(submitChangesToDBjButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gradesjPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gradesjPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton5)
                                        .addGap(52, 52, 52))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gradesjPanel1Layout.createSequentialGroup()
                                        .addGroup(gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel2))
                                        .addGap(18, 18, 18)
                                        .addGroup(gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(assumedGradejTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                                            .addComponent(desiredGradejTextField1))
                                        .addGap(30, 30, 30))))))))
            .addGroup(gradesjPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(editGradejPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(165, 165, 165))
        );
        gradesjPanel1Layout.setVerticalGroup(
            gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradesjPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scoreList, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weightList, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleList, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(gradesjPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(submitChangesToDBjButton2)
                        .addGap(21, 21, 21)
                        .addGroup(gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(desiredGradejTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(9, 9, 9)
                        .addGroup(gradesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(assumedGradejTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editGradejPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(currentGradeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(percentagePointsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(percentCompleteLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton4.setText("Add a Class");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        classNamejTextField1.setText("Class Name (max. 7 characters)");
        classNamejTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                classNamejTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                classNamejTextField1FocusLost(evt);
            }
        });

        yearTakenjTextField1.setText("Year Taken (4 digits)");
        yearTakenjTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                yearTakenjTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                yearTakenjTextField1FocusLost(evt);
            }
        });

        semesterTakenjTextField1.setText("Semester Taken");
        semesterTakenjTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                semesterTakenjTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                semesterTakenjTextField1FocusLost(evt);
            }
        });

        javax.swing.GroupLayout newClassjPanel2Layout = new javax.swing.GroupLayout(newClassjPanel2);
        newClassjPanel2.setLayout(newClassjPanel2Layout);
        newClassjPanel2Layout.setHorizontalGroup(
            newClassjPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newClassjPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(newClassjPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(classNamejTextField1)
                    .addComponent(yearTakenjTextField1)
                    .addComponent(semesterTakenjTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        newClassjPanel2Layout.setVerticalGroup(
            newClassjPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newClassjPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(classNamejTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yearTakenjTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(semesterTakenjTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        coursesComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                coursesComboBoxItemStateChanged(evt);
            }
        });

        jLabel5.setText("Courses:");

        jLabel4.setText("Semesters:");

        semestersComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                semestersComboBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout coursesjPanel1Layout = new javax.swing.GroupLayout(coursesjPanel1);
        coursesjPanel1.setLayout(coursesjPanel1Layout);
        coursesjPanel1Layout.setHorizontalGroup(
            coursesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coursesjPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(coursesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(coursesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(semestersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        coursesjPanel1Layout.setVerticalGroup(
            coursesjPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, coursesjPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(semestersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(coursesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        canceljButton5.setText("CANCEL");
        canceljButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                canceljButton5ActionPerformed(evt);
            }
        });

        jButton3.setText("Add a New Grade");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        assignmentTitleTextField.setText("Assignment Title");
        assignmentTitleTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                assignmentTitleTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                assignmentTitleTextFieldFocusLost(evt);
            }
        });
        assignmentTitleTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                assignmentTitleTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                assignmentTitleTextFieldKeyReleased(evt);
            }
        });

        weightTextField.setText("Weight (%)");
        weightTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                weightTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                weightTextFieldFocusLost(evt);
            }
        });

        scoreTextField.setText("Score (%)");
        scoreTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                scoreTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                scoreTextFieldFocusLost(evt);
            }
        });

        infojLabel1.setFont(new java.awt.Font("Tahoma", 2, 16)); // NOI18N
        infojLabel1.setText("Enter information for a new grade in above text fields");

        jButton2.setText("GPA Calculator");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newClassjPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(coursesjPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(1, 1, 1)
                                .addComponent(assignmentTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scoreTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(348, 348, 348))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(infojLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gradesjPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(canceljButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 772, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(442, 442, 442)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(coursesjPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newClassjPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(gradesjPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(assignmentTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scoreTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infojLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(canceljButton5)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(34, 34, 34))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void titleListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_titleListActionPerformed

    }//GEN-LAST:event_titleListActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (jButton4.getText().equals("Add a Class")) {
            newClassjPanel2.setVisible(true);
            jButton4.setText("Add");
        } else {
            if (semesterTakenjTextField1.getText().contains(" ") || semesterTakenjTextField1.getText().contains("-")) {
                JOptionPane.showMessageDialog(null, "Incorrect input! Semester taken cannot contain spaces, dashes, or hyphens.\nAcceptable semester examples: summer, FALL, Winter, fall_term_a", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Integer.parseInt(yearTakenjTextField1.getText());

                // Now try inserting into the database.
                try {
                    addCourse(classNamejTextField1.getText(), 3.0, semesterTakenjTextField1.getText(), Integer.parseInt(yearTakenjTextField1.getText()), "");

                    newClassjPanel2.setVisible(false);
                    jButton4.setText("Add Class");

                    coursesjPanel1.setVisible(true);
                    jButton3.setVisible(true);

                    // Place the semester taken in the combo box, if it isn't already there.
                    semestersComboBox.removeAllItems();
                    titleList.removeAll();
                    weightList.removeAll();
                    scoreList.removeAll();

                    ArrayList<String> semesters = getSemesters();
                    for (int i = 0; i < semesters.size(); i++) {
                        semestersComboBox.addItem(semesters.get(i));
                    }

                    jButton4.setText("Add a Class");
                    JOptionPane.showMessageDialog(null, "Course added successfully!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error adding course: " + e, "Error!", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Incorrect input! Year taken must be an integer.", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void semesterTakenjTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_semesterTakenjTextField1FocusLost
        if (semesterTakenjTextField1.getText().equals("")) {
            semesterTakenjTextField1.setText("Semester Taken");
        }
    }//GEN-LAST:event_semesterTakenjTextField1FocusLost

    private void classNamejTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_classNamejTextField1FocusGained
        if (classNamejTextField1.getText().equals("Class Name (max. 7 characters)")) {
            classNamejTextField1.setText("");
        }
    }//GEN-LAST:event_classNamejTextField1FocusGained

    private void yearTakenjTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_yearTakenjTextField1FocusGained
        if (yearTakenjTextField1.getText().equals("Year Taken (4 digits)")) {
            yearTakenjTextField1.setText("");
        }
    }//GEN-LAST:event_yearTakenjTextField1FocusGained

    private void semesterTakenjTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_semesterTakenjTextField1FocusGained
        if (semesterTakenjTextField1.getText().equals("Semester Taken")) {
            semesterTakenjTextField1.setText("");
        }
    }//GEN-LAST:event_semesterTakenjTextField1FocusGained

    private void classNamejTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_classNamejTextField1FocusLost
        if (classNamejTextField1.getText().equals("")) {
            classNamejTextField1.setText("Class Name (max. 7 characters)");
        }
    }//GEN-LAST:event_classNamejTextField1FocusLost

    private void yearTakenjTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_yearTakenjTextField1FocusLost
        if (yearTakenjTextField1.getText().equals("")) {
            yearTakenjTextField1.setText("Year Taken (4 digits)");
        }
    }//GEN-LAST:event_yearTakenjTextField1FocusLost

    private void canceljButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_canceljButton5ActionPerformed
        newClassjPanel2.setVisible(false);
        classNamejTextField1.setText("Class Name (max. 7 characters)");
        yearTakenjTextField1.setText("Year Taken (4 digits)");
        semesterTakenjTextField1.setText("Semester Taken");
        jButton4.setText("Add a Class");
        assignmentTitleTextField.setVisible(false);
        weightTextField.setVisible(false);
        scoreTextField.setVisible(false);
        assignmentTitleTextField.setText("Assignment Title");
        weightTextField.setText("Weight (%)");
        scoreTextField.setText("Score (%)");
        infojLabel1.setVisible(false);
        jButton3.setText("Add a New Grade");

        assumedGradejTextField2.setText("");
        desiredGradejTextField1.setText("");

        assignmentTitlejTextField1.setText("");
        weightjTextField2.setText("");
        scoreReceivedjTextField3.setText("");
        editGradejPanel1.setVisible(false);

        weightList.select(-1);
        scoreList.select(-1);
        titleList.select(-1);
    }//GEN-LAST:event_canceljButton5ActionPerformed

    private void assignmentTitleTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_assignmentTitleTextFieldKeyPressed
    }//GEN-LAST:event_assignmentTitleTextFieldKeyPressed

    private void assignmentTitleTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_assignmentTitleTextFieldKeyReleased
        if (titleList.getSelectedIndex() == -1) {
            jButton3.setVisible(true);
        }
    }//GEN-LAST:event_assignmentTitleTextFieldKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jButton3.getText().equals("Add a New Grade")) {
            weightList.select(-1);
            scoreList.select(-1);
            titleList.select(-1);

            assignmentTitleTextField.setVisible(true);
            weightTextField.setVisible(true);
            scoreTextField.setVisible(true);

            jButton3.setText("Add");
            assignmentTitleTextField.setText("Assignment Title");
            weightTextField.setText("Weight (%)");
            scoreTextField.setText("Score (%)");
            infojLabel1.setVisible(true);

        } else if (gradeTitleDoesNotExist(assignmentTitleTextField.getText()) && stringIsANumber(weightTextField.getText()) && stringIsANumber(scoreTextField.getText())) {
            jButton3.setText("Add a New Grade");
            infojLabel1.setVisible(false);
            assignmentTitleTextField.setVisible(false);
            weightTextField.setVisible(false);
            scoreTextField.setVisible(false);

            titleList.add(assignmentTitleTextField.getText());
            weightList.add(weightTextField.getText());
            scoreList.add(scoreTextField.getText());

            // Recalculate percentage points, overall grade, and percent complete.
            calculateOverallGrade();
            calculateOverallPercentagePoints();
            calculatePercentageComplete();
            submitChangesToDBjButton2.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect input! Weight, grade must be doubles. Title cannot already exist for some other assignment.", "Error!", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void assignmentTitleTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_assignmentTitleTextFieldFocusGained
        if (assignmentTitleTextField.getText().equals("Assignment Title")) {
            assignmentTitleTextField.setText("");
        }
    }//GEN-LAST:event_assignmentTitleTextFieldFocusGained

    private void assignmentTitleTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_assignmentTitleTextFieldFocusLost
        if (assignmentTitleTextField.getText().equals("") && titleList.getSelectedIndex() == -1) {
            assignmentTitleTextField.setText("Assignment Title");
        }
    }//GEN-LAST:event_assignmentTitleTextFieldFocusLost

    private void weightTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weightTextFieldFocusGained
        if (weightTextField.getText().equals("Weight (%)")) {
            weightTextField.setText("");
        }
    }//GEN-LAST:event_weightTextFieldFocusGained

    private void weightTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weightTextFieldFocusLost
        if (weightTextField.getText().equals("") && weightList.getSelectedIndex() == -1) {
            weightTextField.setText("Weight (%)");
        }
    }//GEN-LAST:event_weightTextFieldFocusLost

    private void scoreTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_scoreTextFieldFocusGained
        if (scoreTextField.getText().equals("Score (%)")) {
            scoreTextField.setText("");
        }
    }//GEN-LAST:event_scoreTextFieldFocusGained

    private void scoreTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_scoreTextFieldFocusLost
        if (scoreTextField.getText().equals("") && scoreList.getSelectedIndex() == -1) {
            scoreTextField.setText("Score (%)");
        }
    }//GEN-LAST:event_scoreTextFieldFocusLost

    private void titleListFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_titleListFocusGained
    }//GEN-LAST:event_titleListFocusGained

    private void titleListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_titleListMouseClicked
        if (titleList.getSelectedIndex() != -1) {
            editGradejPanel1.setVisible(true);
            weightList.select(titleList.getSelectedIndex());
            scoreList.select(titleList.getSelectedIndex());
            weightjTextField2.setText(weightList.getSelectedItem());
            assignmentTitlejTextField1.setText(titleList.getSelectedItem());
            scoreReceivedjTextField3.setText(scoreList.getSelectedItem());
        }
    }//GEN-LAST:event_titleListMouseClicked

    private void weightListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_weightListMouseClicked
        if (weightList.getSelectedIndex() != -1) {
            editGradejPanel1.setVisible(true);
            titleList.select(weightList.getSelectedIndex());
            scoreList.select(weightList.getSelectedIndex());
            weightjTextField2.setText(weightList.getSelectedItem());
            assignmentTitlejTextField1.setText(titleList.getSelectedItem());
            scoreReceivedjTextField3.setText(scoreList.getSelectedItem());
        }
    }//GEN-LAST:event_weightListMouseClicked

    private void scoreListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scoreListMouseClicked
        if (scoreList.getSelectedIndex() != -1) {
            editGradejPanel1.setVisible(true);
            titleList.select(scoreList.getSelectedIndex());
            weightList.select(scoreList.getSelectedIndex());
            weightjTextField2.setText(weightList.getSelectedItem());
            assignmentTitlejTextField1.setText(titleList.getSelectedItem());
            scoreReceivedjTextField3.setText(scoreList.getSelectedItem());
        }
    }//GEN-LAST:event_scoreListMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (titleList.getSelectedIndex() != -1) {
            if (titleList.getItem(titleList.getSelectedIndex()).equals(assignmentTitlejTextField1.getText())) {
                editGradejPanel1.setVisible(false);
                weightjTextField2.setText("");
                assignmentTitlejTextField1.setText("");
                scoreReceivedjTextField3.setText("");
            }
            titleList.remove(titleList.getSelectedIndex());
            weightList.remove(weightList.getSelectedIndex());
            scoreList.remove(scoreList.getSelectedIndex());
            submitChangesToDBjButton2.setVisible(true);
        }

        // Recalculate percentage points, overall grade, and percent complete.
        calculateOverallGrade();
        calculateOverallPercentagePoints();
        calculatePercentageComplete();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void semestersComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_semestersComboBoxItemStateChanged
        try {
            coursesComboBox.removeAllItems();
            String wholeSemester = semestersComboBox.getSelectedItem().toString();
            String semester = wholeSemester.substring(0, wholeSemester.indexOf(" "));
            int year = Integer.parseInt(wholeSemester.substring(wholeSemester.indexOf(" ") + 1));

            ArrayList<String> courses = getCoursesBySemester(semester, year);
            for (int i = 0; i < courses.size(); i++) {
                coursesComboBox.addItem(courses.get(i));
            }
        } catch (NullPointerException npe) {
            // Do nothing - exception likely caused by nothing being in the combo box.            
        }
    }//GEN-LAST:event_semestersComboBoxItemStateChanged

    private static ArrayList<String> getCoursesBySemester(String semester, int year) {
        String sql = "SELECT title FROM course WHERE semester_taken = '" + semester + "' AND year_taken = " + year + ";";
        ArrayList<String> courses = new ArrayList<String>();
        try (Connection conn = connectToCollege();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(rs.getString(1));
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error trying to fetch semesters from the database.", "Error!", JOptionPane.ERROR_MESSAGE);
        }
        return courses;
    }

    private void assignmentTitlejTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_assignmentTitlejTextField1KeyReleased

    }//GEN-LAST:event_assignmentTitlejTextField1KeyReleased

    private void addChangesjButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addChangesjButton5ActionPerformed
        if (stringIsANumber(weightjTextField2.getText()) && stringIsANumber(scoreReceivedjTextField3.getText()) && gradeTitleDoesNotExist(assignmentTitlejTextField1.getText()) && !assignmentTitlejTextField1.getText().equals("") && !weightjTextField2.getText().equals("") && !scoreReceivedjTextField3.getText().equals("")) {
            // First, delete whatever is being edited.
            titleList.remove(titleList.getSelectedIndex());
            weightList.remove(weightList.getSelectedIndex());
            scoreList.remove(scoreList.getSelectedIndex());

            titleList.add(assignmentTitlejTextField1.getText());
            weightList.add(weightjTextField2.getText());
            scoreList.add(scoreReceivedjTextField3.getText());

            assignmentTitlejTextField1.setText("");
            weightjTextField2.setText("");
            scoreReceivedjTextField3.setText("");
            editGradejPanel1.setVisible(false);
            submitChangesToDBjButton2.setVisible(true);

            // Recalculate grade info.
            calculateOverallGrade();
            calculateOverallPercentagePoints();
            calculatePercentageComplete();
        }
    }//GEN-LAST:event_addChangesjButton5ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if ((stringIsANumber(desiredGradejTextField1.getText()) || desiredGradejTextField1.getText().equals("")) && (stringIsANumber(assumedGradejTextField2.getText()) || assumedGradejTextField2.getText().equals(""))) {
            String necessaryScoreMessage = "";
            if (!desiredGradejTextField1.getText().equals("")) {
                double necessaryScore = (Double.parseDouble(desiredGradejTextField1.getText()) / 100.0 - calculateOverallPercentagePoints() / 100.0) / (1.00 - calculatePercentageComplete() / 100.0);
                necessaryScore = ((int) (necessaryScore * 1000.0) / 10.0);
                necessaryScoreMessage = "In order to get " + desiredGradejTextField1.getText() + "% overall in the course, you will need " + necessaryScore + "% on remaining assignments.";
            }

            String finalGradeMessage = "";
            if (!assumedGradejTextField2.getText().equals("")) {
                // Now calculate what grade the user will have overall, assuming he/she makes a specified score on remaining assignments.
                double finalGrade = (Double.parseDouble(assumedGradejTextField2.getText())) / 100.0 * (1.0 - calculatePercentageComplete() / 100.0) + calculateOverallPercentagePoints() / 100.0;
                finalGrade = ((int) (finalGrade * 1000.0)) / 10.0;
                finalGradeMessage = "If you make " + assumedGradejTextField2.getText() + "% on remaining coursework, you will have " + finalGrade + "% overall.";
            }
            if (!necessaryScoreMessage.equals("") || !finalGradeMessage.equals("")) {
                JOptionPane.showMessageDialog(null, necessaryScoreMessage + "\n" + finalGradeMessage, "Results", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect input. Must be doubles.", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        GPACalculator gpacalc = new GPACalculator();
        String[] arguments = {};
        gpacalc.main(arguments);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void coursesComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_coursesComboBoxItemStateChanged
        // Fetch all the grades for the selected course.
        try {
            // reset any grade data that was in the editing textfields.
            assignmentTitlejTextField1.setText("");
            weightjTextField2.setText("");
            scoreReceivedjTextField3.setText("");
            editGradejPanel1.setVisible(false);
            submitChangesToDBjButton2.setVisible(true);

            // Every course is a unique combination of its title, semester_taken, and year_taken.
            String title = coursesComboBox.getSelectedItem().toString();
            String wholeSemester = semestersComboBox.getSelectedItem().toString();
            String semesterTaken = wholeSemester.substring(0, wholeSemester.indexOf(" "));
            int yearTaken = Integer.parseInt(wholeSemester.substring(wholeSemester.indexOf(" ") + 1));

            String sql = "SELECT title, weight, grade FROM assignment WHERE course_id = (SELECT id FROM course WHERE title = '" + title + "' AND semester_taken = '" + semesterTaken + "' AND year_taken = " + yearTaken + ");";
            ArrayList<String> courses = new ArrayList<String>();
            try (Connection conn = connectToCollege();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                // First, clear the lists from any previous grades in them.
                titleList.removeAll();
                weightList.removeAll();
                scoreList.removeAll();

                while (rs.next()) {
                    titleList.add(rs.getString(1));
                    weightList.add(rs.getString(2));
                    scoreList.add(rs.getString(3));
                }

                // Recalculate grade info.
                calculateOverallGrade();
                calculateOverallPercentagePoints();
                calculatePercentageComplete();

                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There was an error trying to fetch grades for the course: " + e, "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_coursesComboBoxItemStateChanged

    private void submitChangesToDBjButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitChangesToDBjButton2ActionPerformed
        try {
            // First, try deleting all info already in the database.
            String courseTitle = coursesComboBox.getSelectedItem().toString();
            String wholeSemester = semestersComboBox.getSelectedItem().toString();
            String semester = wholeSemester.substring(0, wholeSemester.indexOf(" "));
            int year = Integer.parseInt(wholeSemester.substring(wholeSemester.indexOf(" ") + 1));
            deleteFromAssignmentTableForClass(courseTitle, semester, year);

            // Then, try inserting everything that's already in the jlists.
            int courseID = getCourseID(courseTitle, semester, year);
            for (int i = 0; i < titleList.getItemCount(); i++) {
                addAssignment(titleList.getItem(i), Double.parseDouble(weightList.getItem(i)), Double.parseDouble(scoreList.getItem(i)), courseID);
            }
            JOptionPane.showMessageDialog(null, "Database was successfully updated.", "Success!", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There was an error trying to delete/add grades: " + e, "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_submitChangesToDBjButton2ActionPerformed

    private int getCourseID(String title, String semester, int year) {
        String sql = "SELECT id FROM course WHERE title = '" + title + "' AND semester_taken = '" + semester + "' AND year_taken = " + year + ";";
        try (Connection conn = connectToCollege();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                return rs.getInt(1);
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        JOptionPane.showMessageDialog(null, "There was an error trying to find the course id for course with title = " + title + ", semester taken = " + semester + ", and year taken = " + year, "Error!", JOptionPane.ERROR_MESSAGE);
        throw new Error("Could not find course id!");
    }

    private void deleteFromAssignmentTableForClass(String courseTitle, String semester, int year) {
        String url = "jdbc:sqlite:" + getAbsolutePath();

        String sql = "DELETE FROM assignment WHERE course_id = " + getCourseID(courseTitle, semester, year) + ";";

        try (Connection conn = connectToCollege(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private double calculateOverallGrade() {
        double sum = 0.0;
        for (int i = 0; i < weightList.getItemCount(); i++) {
            sum += Double.parseDouble(weightList.getItem(i));
        }

        double pPoints = 0.0;
        for (int i = 0; i < weightList.getItemCount(); i++) {
            pPoints += Double.parseDouble(weightList.getItem(i)) * (Double.parseDouble(scoreList.getItem(i)) / 100.0);
        }
        currentGradeLabel.setText("Current grade: " + ((int) ((pPoints / sum) * 1000)) / 10.0);
        return (pPoints / sum) * 100.0;
    }

    private double calculateOverallPercentagePoints() {
        double pPoints = 0.0;
        for (int i = 0; i < weightList.getItemCount(); i++) {
            pPoints += Double.parseDouble(weightList.getItem(i)) * (Double.parseDouble(scoreList.getItem(i)) / 100.0);
        }
        percentagePointsLabel.setText("Percentage points earned: " + (int) (pPoints * 10) / 10.0);
        return pPoints;
    }

    private double calculatePercentageComplete() {
        double sum = 0.0;
        for (int i = 0; i < weightList.getItemCount(); i++) {
            sum += Double.parseDouble(weightList.getItem(i));
        }
        percentCompleteLabel.setText("Percent complete: " + (int) (sum * 10) / 10.0);
        return sum;
    }

    // Check that the title does not already exist for the class.
    private boolean gradeTitleDoesNotExist(String title) {
        for (int i = 0; i < titleList.getItemCount(); i++) {
            if (i != titleList.getSelectedIndex() && titleList.getItem(i).equals(title)) {  // Title differs from all others in list (don't look at the title itself, obviously!)
                return false;
            }
        }
        return true;
    }

    private boolean stringIsANumber(String number) {
        try {
            Double.parseDouble(number);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GCV4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GCV4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GCV4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GCV4.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        createTablesIfNotExist();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GCV4().setVisible(true);
            }
        });

        /*
        dropTables();
        
        
        createCourseTable();
        createAssignmentTable();
        
      
        selectAll();
        
        addCourse("CSC360", 3.0, "Spring", 2018, "B");
        addCourse("CSC190", 3.0, "Fall", 2016, "B");
        addAssignment("Test 1", 15, 85.6, 0);
        addAssignment("Quiz 1", 5, 80, 1);
         
       
        createCourseTable();
        createAssignmentTable();

        dropTables();

        createCourseTable();
        createAssignmentTable();
        
        addCourse("CSC360", 3.0, "Spring", 2018, "B");
        addCourse("CSC190", 3.0, "Fall", 2016, "B");
        
        addAssignment("Test 1", 15, 85.6, 1);
        addAssignment("Quiz 1", 5, 80, 2);

        

        createNewDatabase();
        createCourseTable();
        createAssignmentTable();
        insert("CSC191", 3.0);
        selectAll();
         */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addChangesjButton5;
    private javax.swing.JTextField assignmentTitleTextField;
    private javax.swing.JTextField assignmentTitlejTextField1;
    private javax.swing.JTextField assumedGradejTextField2;
    private javax.swing.JButton canceljButton5;
    private javax.swing.JTextField classNamejTextField1;
    private javax.swing.JComboBox<String> coursesComboBox;
    private javax.swing.JPanel coursesjPanel1;
    private javax.swing.JLabel currentGradeLabel;
    private javax.swing.JTextField desiredGradejTextField1;
    private javax.swing.JPanel editGradejPanel1;
    private javax.swing.JPanel gradesjPanel1;
    private javax.swing.JLabel infojLabel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel newClassjPanel2;
    private javax.swing.JLabel percentCompleteLabel;
    private javax.swing.JLabel percentagePointsLabel;
    private java.awt.List scoreList;
    private javax.swing.JTextField scoreReceivedjTextField3;
    private javax.swing.JTextField scoreTextField;
    private javax.swing.JTextField semesterTakenjTextField1;
    private javax.swing.JComboBox<String> semestersComboBox;
    private javax.swing.JButton submitChangesToDBjButton2;
    private java.awt.List titleList;
    private java.awt.List weightList;
    private javax.swing.JTextField weightTextField;
    private javax.swing.JTextField weightjTextField2;
    private javax.swing.JTextField yearTakenjTextField1;
    // End of variables declaration//GEN-END:variables
}