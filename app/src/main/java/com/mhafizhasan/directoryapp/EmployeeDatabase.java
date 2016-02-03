package com.mhafizhasan.directoryapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dermis on 1/9/16.
 */
public class EmployeeDatabase extends SQLiteOpenHelper {

    public static class Employee {
        public String ID;
        public String name;
        public String position;
        public String department;
        public String officePhone;
        public String address;
        public String email;
        public int age;
        public int gender;
        public int KPI;
        public String supervisorID;
        public String supervisorName;
    }

    public void addEmployee(Employee employee) {
        SQLiteDatabase db= getWritableDatabase();

        addEmployee(db, employee);

        db.close();
    }

    public void addEmployee(SQLiteDatabase db, Employee employee) {

        // Convert values
        ContentValues values = new ContentValues();
        values.put("ID", employee.ID);
        values.put("name", employee.name);
        values.put("position", employee.position);
        values.put("department", employee.department);
        values.put("officePhone", employee.officePhone);
        values.put("address", employee.address);
        values.put("email", employee.email);
        values.put("age", employee.age);
        values.put("gender", employee.gender);
        values.put("KPI", employee.KPI);
        values.put("supervisorID", employee.supervisorID);

        // Upload
        db.insert("employee", "name", values);

    }

    public Employee[] searchByName(String name) {
        // Access the database
        SQLiteDatabase db = getReadableDatabase();

        // Prepare SQL query
        String sql = "SELECT ID FROM employee WHERE name LIKE ?";

        // Select rows
        Cursor cursor = db.rawQuery(sql, new String[] { "%" + name + "%"});

        // Convert to array
        Employee[] employees = new Employee[cursor.getCount()];
        for (int c = 0; c < employees.length; c++) {
            cursor.moveToPosition(c);
            String id = cursor.getString(cursor.getColumnIndex("ID"));

            // Get full employee details
            employees[c] = getEmployee(id);
        }

        // Finish
        cursor.close();
        db.close();

        return employees;
    }

    public Employee getEmployee(String id) {
        // Access the database
        SQLiteDatabase db = getReadableDatabase();

        // Prepare SQL query
        String sql = "SELECT emp.name, emp.position, emp.department, emp.officePhone," +
                "emp.address, emp.email, emp.age, emp.gender, emp.KPI, emp.supervisorID," +
                "mgr.name AS 'supervisorName' " +
                "FROM employee emp LEFT OUTER JOIN employee mgr ON mgr.ID = emp.supervisorID " +
                "WHERE emp.ID = ?";

        // Go to row
        Cursor cursor = db.rawQuery(sql, new String[] { id });
        cursor.moveToPosition(0);

        // Convert to java values
        Employee employee = new Employee();
        employee.name = cursor.getString(cursor.getColumnIndex("name"));
        employee.position = cursor.getString(cursor.getColumnIndex("position"));
        employee.department = cursor.getString(cursor.getColumnIndex("department"));
        employee.officePhone = cursor.getString(cursor.getColumnIndex("officePhone"));
        employee.address = cursor.getString(cursor.getColumnIndex("address"));
        employee.email = cursor.getString(cursor.getColumnIndex("email"));
        employee.age = cursor.getInt(cursor.getColumnIndex("age"));
        employee.gender = cursor.getInt(cursor.getColumnIndex("gender"));
        employee.KPI = cursor.getInt(cursor.getColumnIndex("KPI"));
        employee.supervisorID = cursor.getString(cursor.getColumnIndex("supervisorID"));
        employee.supervisorName = cursor.getString(cursor.getColumnIndex("supervisorName"));
        employee.ID = id;

        // Finish
        cursor.close();
        db.close();

        return employee;

    }


    public EmployeeDatabase(Context context) {
        super(context, "EmployeeDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Prepare SQL query
        String sql = "CREATE TABLE IF NOT EXISTS employee (" +
                "ID TEXT(12) PRIMARY KEY," +
                "name TEXT," +
                "position TEXT," +
                "department TEXT," +
                "officePhone TEXT," +
                "address TEXT," +
                "email TEXT," +
                "age INT," +
                "gender INT," +
                "KPI INT," +
                "supervisorID TEXT(12))";

        // Execute query
        db.execSQL(sql);

        // Add default TODO

        // Add default entries
        Employee employee =  new Employee();

        employee.ID = "0";
        employee.name = "Sivaji";
        employee.position = "Big Boss";
        employee.department = "Magic Academy";
        employee.officePhone = "1-800-800";
        employee.address = "Magic, Cyberjaya";
        employee.email = "bigboss@magic.my";
        employee.age = 52;
        employee.gender = 1;
        employee.KPI = 99;
        employee.supervisorID = null;
        addEmployee(db, employee);

        employee.ID = "1";
        employee.name = "Babyrina";
        employee.position = "Practical";
        employee.department = "Magic Academy";
        employee.officePhone = "1-800-800";
        employee.address = "Magic, Cyberjaya";
        employee.email = "baby@magic.my";
        employee.age = 24;
        employee.gender = 0;
        employee.KPI = 90;
        employee.supervisorID = "0";
        addEmployee(db, employee);

        employee.ID = "2";
        employee.name = "John";
        employee.position = "Manager";
        employee.department = "Magic Academy";
        employee.officePhone = "1-800-800";
        employee.address = "Magic, Cyberjaya";
        employee.email = "john@magic.my";
        employee.age = 30;
        employee.gender = 1;
        employee.KPI = 80;
        employee.supervisorID = "0";
        addEmployee(db, employee);

        employee.ID = "3";
        employee.name = "Employee3";
        employee.position = "Designer";
        employee.department = "Magic Academy";
        employee.officePhone = "1-800-800";
        employee.address = "Magic, Cyberjaya";
        employee.email = "baby@magic.my";
        employee.age = 24;
        employee.gender = 0;
        employee.KPI = 49;
        employee.supervisorID = "0";
        addEmployee(db, employee);

        employee.ID = "4";
        employee.name = "Employee4";
        employee.position = "Designer";
        employee.department = "Magic Academy";
        employee.officePhone = "1-800-800";
        employee.address = "Magic, Cyberjaya";
        employee.email = "baby@magic.my";
        employee.age = 24;
        employee.gender = 0;
        employee.KPI = 49;
        employee.supervisorID = "0";
        addEmployee(db, employee);

        employee.ID = "5";
        employee.name = "Employee5";
        employee.position = "Programmer";
        employee.department = "Magic Academy";
        employee.officePhone = "1-800-800";
        employee.address = "Magic, Cyberjaya";
        employee.email = "baby@magic.my";
        employee.age = 24;
        employee.gender = 1;
        employee.KPI = 49;
        employee.supervisorID = "0";
        addEmployee(db, employee);

        employee.ID = "6";
        employee.name = "Employee6";
        employee.position = "Consultant";
        employee.department = "Magic Academy";
        employee.officePhone = "1-800-800";
        employee.address = "Magic, Cyberjaya";
        employee.email = "employee6@magic.my";
        employee.age = 24;
        employee.gender = 1;
        employee.KPI = 49;
        employee.supervisorID = "0";
        addEmployee(db, employee);

        employee.ID = "7";
        employee.name = "Employee7";
        employee.position = "Trainee";
        employee.department = "Magic Academy";
        employee.officePhone = "1-800-800";
        employee.address = "Magic, Cyberjaya";
        employee.email = "employee7@magic.my";
        employee.age = 24;
        employee.gender = 1;
        employee.KPI = 49;
        employee.supervisorID = "0";
        addEmployee(db, employee);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // When app is updated from previous version, be lazy and DELETE ALL ENTRIES
        db.execSQL("DROP TABLE IF EXISTS employee");
        // Populate with default values again
        onCreate(db);
    }
}
