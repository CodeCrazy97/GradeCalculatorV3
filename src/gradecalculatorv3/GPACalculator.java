package gradecalculatorv3;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GPACalculator extends javax.swing.JFrame {

    public GPACalculator() {
        initComponents();
        this.getContentPane().setBackground(Color.yellow);
        letterGradeTextField1.requestFocusInWindow();  //Set cursor to blinking in this text field.

        // show completed credit hours
        try {
            double credits = new Querying().getTotalCreditHours();
            completedGPAHoursTextField2.setText("" + credits);

            double gpa = new Querying().getGPA();

            // format to 3 decimal places
            gpa = Math.round(gpa * 1000.0) / 1000.0;
            currentGPATextField1.setText("" + gpa);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);  // Show the exception message.
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

        letterGradeTextField1 = new java.awt.TextField();
        letterGradeTextField4 = new java.awt.TextField();
        letterGradeTextField3 = new java.awt.TextField();
        letterGradeTextField5 = new java.awt.TextField();
        letterGradeTextField6 = new java.awt.TextField();
        hoursTextField1 = new java.awt.TextField();
        hoursTextField3 = new java.awt.TextField();
        hoursTextField2 = new java.awt.TextField();
        hoursTextField4 = new java.awt.TextField();
        hoursTextField5 = new java.awt.TextField();
        label3 = new java.awt.Label();
        label4 = new java.awt.Label();
        label5 = new java.awt.Label();
        button1 = new java.awt.Button();
        hoursTextField6 = new java.awt.TextField();
        letterGradeTextField2 = new java.awt.TextField();
        button2 = new java.awt.Button();
        currentGPATextField1 = new java.awt.TextField();
        completedGPAHoursTextField2 = new java.awt.TextField();
        label6 = new java.awt.Label();
        label7 = new java.awt.Label();
        addClassButton = new java.awt.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(204, 255, 255));

        letterGradeTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        letterGradeTextField1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        letterGradeTextField4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        letterGradeTextField3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        letterGradeTextField5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        letterGradeTextField6.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        hoursTextField1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        hoursTextField1.setText("3.0");

        hoursTextField3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        hoursTextField3.setText("3.0");

        hoursTextField2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        hoursTextField2.setText("3.0");

        hoursTextField4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        hoursTextField4.setText("3.0");

        hoursTextField5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        hoursTextField5.setText("3.0");

        label3.setFont(new java.awt.Font("Dialog", 2, 14)); // NOI18N
        label3.setText("Letter Grade Received");

        label4.setFont(new java.awt.Font("Dialog", 2, 14)); // NOI18N
        label4.setText("Hours");

        label5.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        label5.setText("Below, enter grades for future classes.");

        button1.setBackground(new java.awt.Color(51, 255, 51));
        button1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        button1.setLabel("Calculate");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        hoursTextField6.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        hoursTextField6.setText("3.0");

        letterGradeTextField2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N

        button2.setBackground(new java.awt.Color(51, 255, 51));
        button2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        button2.setLabel("Quit");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });

        currentGPATextField1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        currentGPATextField1.setText("3.72");

        completedGPAHoursTextField2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        completedGPAHoursTextField2.setText("90");

        label6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label6.setText("Your Current GPA:");

        label7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        label7.setText("Your completed credit hours:");

        addClassButton.setBackground(new java.awt.Color(51, 255, 51));
        addClassButton.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        addClassButton.setLabel("Add/View Classes");
        addClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClassButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(completedGPAHoursTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(110, 110, 110)
                                .addComponent(currentGPATextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(letterGradeTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(letterGradeTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(letterGradeTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(letterGradeTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(letterGradeTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(letterGradeTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(hoursTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hoursTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(hoursTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(hoursTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(hoursTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(hoursTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(169, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(244, 244, 244))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(229, 229, 229))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(addClassButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(196, 196, 196))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(currentGPATextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(completedGPAHoursTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hoursTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(letterGradeTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(hoursTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hoursTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(letterGradeTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(letterGradeTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(letterGradeTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hoursTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(hoursTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(letterGradeTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(hoursTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(letterGradeTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addClassButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed

        try {
            double qualityPoints = Double.parseDouble(currentGPATextField1.getText()) * Double.parseDouble(completedGPAHoursTextField2.getText());
            double totalHours = Double.parseDouble(completedGPAHoursTextField2.getText());

            if (!letterGradeTextField1.getText().equals("") && !hoursTextField1.getText().equals("")) {
                totalHours += Double.parseDouble(hoursTextField1.getText());
                qualityPoints += calculateQualityPoints(letterGradeTextField1.getText().charAt(0), Double.parseDouble(hoursTextField1.getText()));
            }
            if (!letterGradeTextField2.getText().equals("") && !hoursTextField2.getText().equals("")) {
                totalHours += Double.parseDouble(hoursTextField2.getText());
                qualityPoints += calculateQualityPoints(letterGradeTextField2.getText().charAt(0), Double.parseDouble(hoursTextField2.getText()));
            }
            if (!letterGradeTextField3.getText().equals("") && !hoursTextField3.getText().equals("")) {
                totalHours += Double.parseDouble(hoursTextField3.getText());
                qualityPoints += calculateQualityPoints(letterGradeTextField3.getText().charAt(0), Double.parseDouble(hoursTextField3.getText()));
            }
            if (!letterGradeTextField4.getText().equals("") && !hoursTextField4.getText().equals("")) {
                totalHours += Double.parseDouble(hoursTextField4.getText());
                qualityPoints += calculateQualityPoints(letterGradeTextField4.getText().charAt(0), Double.parseDouble(hoursTextField4.getText()));
            }
            if (!letterGradeTextField5.getText().equals("") && !hoursTextField5.getText().equals("")) {
                totalHours += Double.parseDouble(hoursTextField5.getText());
                qualityPoints += calculateQualityPoints(letterGradeTextField5.getText().charAt(0), Double.parseDouble(hoursTextField5.getText()));
            }
            if (!letterGradeTextField6.getText().equals("") && !hoursTextField6.getText().equals("")) {
                totalHours += Double.parseDouble(hoursTextField6.getText());
                qualityPoints += calculateQualityPoints(letterGradeTextField6.getText().charAt(0), Double.parseDouble(hoursTextField6.getText()));
            }
            //Now, show the results.
            System.out.println("quality points = " + qualityPoints);
            System.out.println("total hours = " + totalHours);
            NumberFormat formatter = new DecimalFormat("#0.00");
            JOptionPane.showMessageDialog(this, "Given the grades you've entered, your GPA will be: " + formatter.format(qualityPoints / totalHours), "Results", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Something is wrong with your input.", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.out.println("NFE: " + nfe);
        }

    }//GEN-LAST:event_button1ActionPerformed

    private void button2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_button2ActionPerformed

    private void addClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClassButtonActionPerformed

        // Show the classes editor screen.
        JFrame frame = new JFrame("Classes");
        FinalGradesForm classAdder = new FinalGradesForm(frame);
        this.dispose();
    }//GEN-LAST:event_addClassButtonActionPerformed

    private double calculateQualityPoints(char letterGrade, double hours) {
        if (letterGrade == 'a' || letterGrade == 'A') {
            return hours * 4.0;
        } else if (letterGrade == 'b' || letterGrade == 'B') {
            return hours * 3.0;
        } else if (letterGrade == 'c' || letterGrade == 'C') {
            return hours * 2.0;
        } else if (letterGrade == 'd' || letterGrade == 'D') {
            return hours * 1.0;
        } else if (letterGrade == 'f' || letterGrade == 'F') {
            return hours * 0.0;
        } else { //Error.            
            throw new NumberFormatException();
        }
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
            java.util.logging.Logger.getLogger(GPACalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GPACalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GPACalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GPACalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GPACalculator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button addClassButton;
    private java.awt.Button button1;
    private java.awt.Button button2;
    private java.awt.TextField completedGPAHoursTextField2;
    private java.awt.TextField currentGPATextField1;
    private java.awt.TextField hoursTextField1;
    private java.awt.TextField hoursTextField2;
    private java.awt.TextField hoursTextField3;
    private java.awt.TextField hoursTextField4;
    private java.awt.TextField hoursTextField5;
    private java.awt.TextField hoursTextField6;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.TextField letterGradeTextField1;
    private java.awt.TextField letterGradeTextField2;
    private java.awt.TextField letterGradeTextField3;
    private java.awt.TextField letterGradeTextField4;
    private java.awt.TextField letterGradeTextField5;
    private java.awt.TextField letterGradeTextField6;
    // End of variables declaration//GEN-END:variables
}
