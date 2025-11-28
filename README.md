# ScholarWebSystem
A software web system to manage students and their grades, as well as scholar periods and it's subjects.

## Class Diagrams

<img width="411" height="316" alt="Scholar system website class diagram - User Heritage drawio" src="https://github.com/user-attachments/assets/c88dcae3-b594-4220-ada0-ec777c06e69c" /><br/><br/>
<img width="1031" height="783" alt="Scholar system website diagram - Student drawio" src="https://github.com/user-attachments/assets/a8a35592-abe1-45d9-bf1e-c2368365e34e" /><br/>
## EndPoints<br/>
### StudentController Endpoints<br/>
* GET
  * student/all: returns all students
  * student/id/{code}: returns a student with the specified id
  * student/name/{name}: returns all students with the matched name
* POST
  * student/new: creates a new record of student in DB
* PUT
  * student/update/{code}: updates a student from the DB

### ProfessorController Endpoints<br/>
 *GET
  * professor/all: returns all professors from DB
  * professor/id/{code}: returns a professor with the specified id
  * professor/name/{name}: returns all the professors with the matched name 
