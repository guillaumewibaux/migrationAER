/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;

/**
 * Classe créant les requêtes nécessaires pour créer et remplir les tables type_groupement_local et 
 * type_groupement_scientifique.
 * @author Fabien
 */
public class TypesGroupements {
    
    public static void requetes(Connection con) throws SQLException{
        
        //création de la table type_groupement_local
        
        String query = 
       "CREATE TABLE `type_groupement_local` (\n" +
"	`type_groupement_local_intitule` varchar(255),\n" +
"	PRIMARY KEY(`type_groupement_local_intitule`)\n" +
") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
    
        //remplissage
        
        query = "INSERT INTO type_groupement_local VALUES ('groupe'),('sous-groupe');";
        stmt.executeUpdate(query);
        
        //création de la table type_groupement_scientifique
        
        query =
       "CREATE TABLE `type_groupement_scientifique` (\n" +
"	`type_groupement_scientifique_intitule` varchar(255),\n" +
"	PRIMARY KEY(`type_groupement_scientifique_intitule`)\n" +
") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        stmt.executeUpdate(query);
        
        //remplissage
        
        query = "INSERT INTO type_groupement_scientifique VALUES ('ordre'),('super-famille'),('famille'),('sous-famille');";
        stmt.executeUpdate(query);
        stmt.close();
    }
}
