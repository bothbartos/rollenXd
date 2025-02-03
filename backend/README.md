# rollenXd

## Database Setup

To set up the database, follow these steps:

1. **Run the Python Scripts**:
    - Execute the `create_table.py` and `populate.py` files.

2. **Requirements**:
    - Python 3.12
    - `psycopg2` library

   You can install `psycopg2` using the following command:
   ```bash
   pip install psycopg2
   ```

3. **Configuration**:
    - Open the Python files (`create_table.py` and `populate.py`) in an IDE such as PyCharm or IntelliJ, ensuring that the Python interpreter is configured correctly.

4. **Database Connection**:
    - In both scripts, insert your:
        - **Database URL**
        - **Username**
        - **Password**
        - **Port**

5. **Populate Script**:
    - In `populate.py`, specify the absolute path to **three actual mp3 files** that should be added to the database.

6. **File Access Permissions**:
    - PostgreSQL needs permission to access the audio files. I recommend placing them in a temporary folder where PostgreSQL is installed to ensure it has access.

---

## Java Spring Boot Server Setup

To configure the Java Spring Boot server, you have two options:

### Option A: Set Environmental Variables
- Create the following environmental variables:
    - `DATABASE_URL`
    - `DATABASE_USERNAME`
    - `DATABASE_PASSWORD`

### Option B: Direct Configuration in `application.properties`
- Alternatively, you can specify the database details directly in the `src/main/resources/application.properties` file:
  ```properties
  spring.datasource.url=<your-database-url>
  spring.datasource.username=<your-database-username>
  spring.datasource.password=<your-database-password>
  ```

---

## Current Status

The project is currently very simple but secure. Its only functionality is to retrieve all the data from all users.

---
