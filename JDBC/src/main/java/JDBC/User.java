package JDBC;

public class User {
    public User(int _id, String _fname, String _lname, String _address) {
        id = _id;
        firstName = _fname;
        LastName = _lname;
        Address = _address;
    }

    public int id;
    public String firstName;
    public String LastName;
    public String Address;

    public String getInsertQuery() {
        return String.format("insert into user(firstName,LastName,Address) Values('%s','%s','%s')",
                this.firstName,
                this.LastName,
                this.Address);
    }
}
