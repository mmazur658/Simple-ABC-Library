Simple ABC Library – Simple Java web application with Maven, Spring and Hibernate using to manage a library. 
This application contains three sections: Customer, Employee and Administrator. As Customer, you can check the 
books in the bookstore, reserve books, update your personal data, change password and use messaging service. 
As Employee you can do more than Customer: add, delete or update books; borrow and return books, manage reservations 
and other users data. As Administrator, you can increase or decrease  user access level and you have access to 
error and configuration section.  CSS style has been downloaded from https://codepen.io , own corrections and changes 
have been provided. 

To do:
- Spring AOP – after throwing - Logout user and show error message after throwing. Save the stack trace 
in a text file and save the file in the database. Show these files in admin/error section. 
- Spring AOP  - after – After each action on the book or user, save the details of this action in the 
database – who, when, what. Show the book/user history in the book/book details section.
- Put the messages text, the book reservation configuration and the book borrowing configuration in properties files.
Configuration details are available at admin/configuration section. 
- Clean and organize CSS files.
- Add new feature:  Closing user account by user or employee.