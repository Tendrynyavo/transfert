package fichier;

import java.io.File;
import java.sql.*;

public class Fichier extends File {
    
    String idFile;
    String nom;

    public void setIdFile(String idFile) {
        this.idFile = idFile;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIdFile() {
        return idFile;
    }

    public String getNom() {
        return nom;
    }

    public Fichier(String pathname) throws Exception {
        super(pathname);
        this.setIdFile(createPrimaryKey());
    }

    public Fichier(String idFile, String name) {
        super(name);
        setIdFile(idFile);
        setNom(name);
    }

    public String createPrimaryKey() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/file?user=postgres&password=postgres");
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT nextval('\"seqFile\"')");
        result.next();
        String sequence = result.getString(1);
        result.close();
        statement.close();
        connection.close();
        return "FILE" + sequence;
    }

    public void save() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/file?user=postgres&password=postgres");
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO file (idFile, nom) VALUES ('" + this.getIdFile() + "', '" + this.getName() + "')");
        connection.commit();
        statement.close();
        connection.close();
    }

    public static Fichier findByName(String name) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/file?user=postgres&password=postgres");
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM file WHERE nom='" + name + "'");
        result.next();
        Fichier file = new Fichier(result.getString(1), result.getString(2));
        result.close();
        statement.close();
        connection.close();
        return file;
    }
}