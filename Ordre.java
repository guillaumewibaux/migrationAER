
import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe créant les requêtes nécessaires à transformer les données de l'ancienne table ordre
 * @author Fabien
 */
public class Ordre {
    
    public static void requetes(Connection con) throws SQLException{
        
        //On renomme la table en groupement_scientifique, puis on renomme les attributs
        
        String query = "ALTER TABLE ordre RENAME TO groupement_scientifique;";
        Statement stmt =con.createStatement();
        stmt.executeUpdate(query);
        
        query = "ALTER TABLE groupement_scientifique RENAME COLUMN ordre_id TO groupement_scientifique_id;";
        stmt.executeUpdate(query);
        
        query = "ALTER TABLE groupement_scientifique RENAME COLUMN ordre_nom TO groupement_scientifique_nom;";
        stmt.executeUpdate(query);
        
        //On ajoute les attributs spécifiques à groupement_scientifique
        
        query = "ALTER TABLE groupement_scientifique ADD(type_groupement_scientifique_intitule varchar(255) NOT NULL"
                + " FOREIGN KEY (`type_groupement_local_intitule`) "
                + "REFERENCES `type_groupement_scientifique` (`type_groupement_scientifique_intitule`));";
        stmt.executeUpdate(query);
        
        query= "ALTER TABLE groupement_scientifique ADD(groupement_scientifique_pere_id int(11)"
                + " FOREIGN KEY (`groupement_scientifique_pere_id`) REFERENCES `groupement_scientifique`"
                + " (`groupement_scientifique_id`));";
        stmt.executeUpdate(query);
        
        //On update le type de groupement (que des ordres pour l'instant)
        
        query= "UPDATE groupement_scientifique SET type_groupement_scientifique_intitule='ordre';";
        stmt.executeUpdate(query);
        
        //Création de la table groupement_scientifique_is_in_groupement_local
        
        query= "CREATE TABLE `groupement_scientifique_is_in_groupement_local` (\n" +
"  `groupement_scientifique_is_in_groupement_local_id` int(11) NOT NULL AUTO_INCREMENT,\n" +
"  `groupement_scientifique_id` int(11) NOT NULL,\n" +
"  `groupement_local_id` int(11) NOT NULL,\n" +
"  PRIMARY KEY (`groupement_scientifique_is_in_groupement_local_id`),\n" +
"  CONSTRAINT `fk_gp_gpl` FOREIGN KEY (`groupement_local_id`) REFERENCES `groupement_local` (`groupement_local_id`),\n" +
"  CONSTRAINT `fk_gp_gps` FOREIGN KEY (`groupement_scientifique_id`) REFERENCES `groupement_scientifique` (`groupement_scientifique_id`)\n" +
") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        stmt.executeUpdate(query);
        
        //On récupère l'auto-incrément de la table ordre_has_sous_groupe
        
        query = "SHOW TABLE STATUS WHERE name=ordre_has_sous_groupe;";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int aiohsg = rs.getInt("Auto_increment");
        
        //Pour chaque élément de ordre_has_sous_groupe
        
        for(int i=1; i<aiohsg; i++){
            query = "SELECT * FROM ordre_has_sous_groupe WHERE ordre_has_sous_groupe_id ="+i+";";
            rs = stmt.executeQuery(query);
            //S'il existe
            if (rs.next()){
                //On récupère l'id de l'ordre
                int ordreid = rs.getInt("ordre_ordre_id");
                /*Et l'id du sous-groupe (attention, on veut ici l'id du sous-groupe dans la table groupement_local
                D'où le fait qu'il faut passer par la HashMap précédemment construite
                */
                int sgroupeid = SousGroupe.hmsousgroupe.get(rs.getInt("sous_groupe_sous_groupe_id"));
                //On insère l'élément dans la table groupement_scientifique_is_in_groupement_local
                query = "INSERT INTO groupement_scientifique_is_in_groupement_local "
                        + "(groupement_scientifique_id, groupement_local_id) VALUES ("
                        + ordreid + ", "+ sgroupeid + ");";
                stmt.executeUpdate(query);
            }
        }
        stmt.close();
    }
}
