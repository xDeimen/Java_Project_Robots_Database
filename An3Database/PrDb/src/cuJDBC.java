import java.io.IOException;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import net.ucanaccess.converters.TypesMap.AccessType;
import net.ucanaccess.ext.FunctionType;
import net.ucanaccess.jdbc.UcanaccessConnection;
import net.ucanaccess.jdbc.UcanaccessDriver;

public class cuJDBC {
    
    //aceasta cale trebuie "adaptata" la locul in care este baza de date MS Access
    //this path must be "adapted" to the location of your MS Access database
    private static final String filename = "S:\\III\\OOP\\Java Project Robots Database\\Java_Project_Robots_Database\\An3Database\\PrDb\\test.mdb";

    private static Connection getUcanaccessConnection(String filename) throws SQLException {
        filename = filename.trim();
        String url = UcanaccessDriver.URL_PREFIX + filename + ";newDatabaseVersion=V2003";
        return DriverManager.getConnection(url, "", "");
    }

    public static Connection getAccessDBConnection(String filename) throws SQLException {
        final String accessDBURLPrefix = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
        final String accessDBURLSuffix = ";DriverID=22;READONLY=false}";
        filename = filename.trim();
        String databaseURL = filename;
        //String databaseURL = accessDBURLPrefix + filename + accessDBURLSuffix;
        return DriverManager.getConnection(databaseURL, "", "");
    }

    public static void afisare(ResultSet resultSet) throws SQLException {
        String out = "";
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); ++i)
            out += resultSet.getMetaData().getColumnName(i) + " ";
        System.out.println(out);

        while (resultSet.next()) {
            out = "";
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); ++i)
                out += resultSet.getString(i) + " ";
            System.out.println(out);
        }
    }

    public static void main(String[] args) {
        try {
            //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            //Connection connection = getAccessDBConnection(filename);
            
            Connection connection = getUcanaccessConnection(filename);
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            

            DatabaseMetaData dbMetaData = connection.getMetaData();
            String productName = dbMetaData.getDatabaseProductName();
            System.out.println("Diver-ul bazei de date/The database driver is: " + productName);
            String productVersion = dbMetaData.getDatabaseProductVersion();
            System.out.println("Versiune/Version: " + productVersion);

            Statement statement = connection.createStatement();
            System.out.println("\nSelectie * linii/Select * linies:");
            String query = "SELECT * FROM Table1";
            ResultSet resultSet = statement.executeQuery(query);
            afisare(resultSet);

            java.sql.Date dataazi = new java.sql.Date(new java.util.Date().getTime());
            String ins =
                 "INSERT INTO Table1([program],[instructiune],[parametri],[data],[real]) VALUES('program3','arc','x1=1;x2=2',#" +
                dataazi + "#, 234)";
            ins ="INSERT INTO Table1([program],[instructiune],[parametri],[data],[real]) VALUES('qqq','aaa','aaa',#2022/12/05#,0.0)";

            System.out.println(ins);
            statement.executeUpdate(ins);

            System.out.println("\nSelectie numai linii program2/Select only linies with program2:");
            query = "SELECT * FROM Table1 WHERE [program]='program2'";
            resultSet = statement.executeQuery(query);
            afisare(resultSet);

            int rowsEffected =0;
            System.out.println("\nStergere linii din Table1/Delete lines from Table1:");
            query = "DELETE * FROM Table1 WHERE [real]=1211";
            //rowsEffected = statement.executeUpdate(query);
            System.out.println(rowsEffected + " rows effected");

            System.out.println("\nActualizare linii din Table1/Updates lines in Table1");
            query = "UPDATE Table1 SET [real] = 4321  WHERE [program] = 'program1'";
            rowsEffected = statement.executeUpdate(query);
            System.out.println(rowsEffected + " rows effected");

            statement.close();
            connection.close();

        } catch (ClassNotFoundException e) {
            System.err.println("Eroare la incarcarea de driver / Error loading the driver: " + e);
        } catch (SQLException e) {
            System.err.println("Exceptie SQL / SQL exception: " + e);
        }
    }
}
