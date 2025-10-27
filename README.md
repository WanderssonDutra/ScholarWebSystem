# ScholarWebSystem
A software web system to manage students and their grades, as well as scholar periods and it's subjects.

## Class Diagrams

<img width="411" height="316" alt="Scholar system website class diagram - User Heritage drawio" src="https://github.com/user-attachments/assets/c88dcae3-b594-4220-ada0-ec777c06e69c" /><br/><br/>
<img width="1031" height="783" alt="Scholar system website diagram - Student drawio" src="https://github.com/user-attachments/assets/a8a35592-abe1-45d9-bf1e-c2368365e34e" /><br/>
## EndPoints<br/>
### Student Endpoints<br/>
* GET
  * student/all: returns all the students records
  * student/id/{id}: returns a student record by it's id
  * student/name/{name}: returns a list of student records searched by name
* POST
  * student/new: records a student object in the database
* PUT
  * student/update: updates a student record



