# GradeCalculatorV3
Java project that stores grades, calculates "what-ifs". GPA calculator embedded. Modeled after the Ben Eggleston grade calculator.


Grade Calculator, Version 3
By Ethan Vaughan
December 21, 2018

This calculator helps me keep track of my grades in classes. It stores information about past grades (a description of the grade, such as “Test 1” or “Term Project”; the grade I received, like 89.6%; and the assignment’s weight as percentage, such as 15%). The grade calculator calculates my overall grade in the course and allows me to run “what-if” calculations. (A what-if calculation tells me what grade I’ll need on remaining assignments in the course in order to finish with a specified grade; it also reports what grade I’ll have overall if I make a certain grade on remaining assignments.) Finally, there is section in the calculator for finding my GPA. This section shows me what GPA I’ll have overall, given certain grades on up to six classes. Another screen allows me to store the final (letter) grades of completed courses.

**Installation Instructions**

To install this product, you will need MySQL on your machine. Additionally, you’ll need to set the following line of text in the run_GradeCalculatorV3.bat 
C:\Users\Ethan_2\Documents\Programming Languages\Java\GradeCalculatorV3\dist\
Set this path to the location of the Jar file on your machine. 

Before running the program, you will need to create the database. Run the CreateDatabase.sql file. This file will create the database in which grades will be stored.
Built With
Netbeans 8.1.


