# Dormitory Management Software for PTITHCM

## Project Description

The "Dormitory Management Software for PTITHCM" is a system developed for the Posts and Telecommunications Institute of Technology, Ho Chi Minh City. The platform simplifies student dormitory operations such as student registration, room allocation, and service management, enabling efficient administration of dormitory resources and billing.

### **Demo** : 
    You can check out the project demo at:
#### **Screenshots**

##### 1. Homepage
This is a dashboard about the status of the rooms, students, invoices, and room types.
![image](https://github.com/user-attachments/assets/501b95ec-3dcb-41d0-b155-24c8056ec94f)

##### 2. Rental records
This is the interface for creating a new rental receipt, consisting of 4 steps: selecting a room, selecting a student, choosing the rental period, and confirming.
![image](https://github.com/user-attachments/assets/3896c946-da39-40b6-8f28-cffed6e4d1f4)

##### 3. Invoices
This is the interface displaying the unpaid invoices.
![image](https://github.com/user-attachments/assets/c55d35d6-e42e-47db-906e-907af3ad3ae0)

### Key Features:
**For Students:**
- **Login**: Access with student account credentials.
- **Room Info**: View available rooms by gender and room ID. Check rental history.
- **Invoice Tracking**: View and filter invoices by paid/unpaid status.

**For Administrators:**
- **Admin Account**: One default admin, additional accounts can be requested.
- **User Management**: Add, delete, update users; reset passwords (Roles: Staff, Student).
- **Search & Filter**: Search by ID/name, filter by role/gender.
- **Full Access**: Unrestricted access to all features.

**For Staff:**
- **Login**: Access with staff credentials.
- **Room & Building Management**: View, add, update, delete rooms/buildings.
- **Rental & Invoice Management**: Search, create, update, and manage rentals and invoices.
- **Student Info**: View student records and search by ID/name.
- **Discounts**: Apply discounts during rental creation/update.
- **Room Type Management**: Manage room types.
- **Rental Period Management**: Manage rental timelines (create, edit, delete if not applied).

## Technologies Used
- **Frontend**: JavaScript, TypeScript, ReactJS
- **Backend**: Java, Spring Boot
- **Database**: SQL Server
- **Tools**:
  - **SQL Server**: Used for managing and storing data, with support for running multiple instances, maintaining separate production, development, and test environments, advanced security privileges, and backup features.
  - **IntelliJ IDEA**: Used for backend development, particularly for working with Java and Spring Boot, providing code editing, debugging, and project management features.
  - **Visual Studio Code**: Used for frontend development with JavaScript/TypeScript and ReactJS, offering a lightweight editor with support for extensions, debugging, and version control.

## Installation and Usage

Clone the project from GitHub:

```bash
https://github.com/levilee0342/Dormitory-Management-Software-For-PTITHCM.git
```
### BackEnd
 - **Configure the database** in: `application.properties`
 - **Run the application**:
    - Run `QLKTXApplication`.

### FrontEnd
- **Install required tools**:
    - Ensure that Node.js and npm (Node Package Manager) are installed.
    - Use `npm` to manage and install dependencies for ReactJS and related libraries.
- **Run the frontend application**:
    - Start the React development server by using the `npm run dev` command from the terminal in Visual Studio Code.

## Thank You!
Thank you for your interest in my project. Please feel free to reach out to me via email at [leetuan0342@gmail.com](mailto:leetuan0342@gmail.com) if you have any questions regarding this project.
