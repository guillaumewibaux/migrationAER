/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;

/**
 * Classe modifiant les données associées aux espèces.
 * @author Fabien
 */
public class Espece {
    
    public static void requetes(Connection con) throws SQLException{
        
        //Création de la table espece_is_in_groupement_local
        
        String query = "CREATE TABLE `espece_is_in_groupement_local` (\n" +
                       "  `espece_is_in_groupement_local_id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                       "  `espece_id` int(11) NOT NULL,\n" +
                       "  `groupement_local_id` int(11) NOT NULL,\n" +
                       "  PRIMARY KEY (`espece_is_in_groupement_local_id`),\n" +
                       "  CONSTRAINT `fk_espgp_gpl` FOREIGN KEY (`groupement_local_id`) REFERENCES `groupement_local` (`groupement_local_id`),\n" +
                       "  CONSTRAINT `fk_espgp_esp` FOREIGN KEY (`espece_id`) REFERENCES `espece` (`espece_id`)\n" +
                       ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
        
        //On récupère l'auto-incrément de espece_has_sous_groupe
        
        query = "SHOW TABLE STATUS WHERE name=espece_has_sous_groupe;";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int aiehsg = rs.getInt("Auto_increment");
        
        //Pour chaque élément, on fait :
        
        for(int i=1; i<aiehsg; i++){
            query = "SELECT * FROM espece_has_sous_groupe WHERE espece_has_sous_groupe_id ="+i+";";
            rs = stmt.executeQuery(query);
            //S'il existe, on 
            if (rs.next()){
                //récupère l'id de l'espece
                int especeid = rs.getInt("espece_espece_id");
                //et celui du sous-groupe (toujours en utilisant la HashMap)
                int sgroupeid = SousGroupe.hmsousgroupe.get(rs.getInt("sous_groupe_sous_groupe_id"));
                query = "INSERT INTO espece_is_in_groupement_local "
                        + "(espece_id, groupement_local_id) VALUES ("
                        + especeid + ", "+ sgroupeid + ");";
                stmt.executeUpdate(query);
            }
        }
        
        query = "SHOW TABLE STATUS WHERE name=espece;";
        rs = stmt.executeQuery(query);
        rs.next();
        int aiespece = rs.getInt("Auto_increment");
        
        //Pour chaque élément de la table espèce
        for(int i=1; i<aiespece; i++){
            query = "SELECT espece_sous_groupe_sous_groupe_id FROM espece WHERE espece_id = " + i + ";";
            rs = stmt.executeQuery(query);
            //S'il existe
            if(rs.next()){
                //On récupère l'id du sous_groupe sur l'attribut espece_sous_groupe_sous_groupe_id
                int idsgroupe = rs.getInt("espece_sous_groupe_sous_groupe_id");
                query = "SELECT * FROM espece_has_sous_groupe WHERE espece_espece_id = " + i + 
                        ", sous_groupe_sous_groupe_id = " + idsgroupe + ";";
                rs = stmt.executeQuery(query);
                //On vérifie qu'il était bien dans espece_has_sous_groupe
                if(!(rs.next())){
                    //Si c'est pas le cas, on l'insère dans la nouvelle table espece_is_in_groupement_local
                    query = "INSERT INTO espece_is_in_groupement_local "
                        + "(espece_id, groupement_local_id) VALUES ("
                        + i + ", "+ SousGroupe.hmsousgroupe.get(idsgroupe) + ");";
                    stmt.executeUpdate(query);
                }
            }
        }
        
        //On ajoute l'attribut donnant le groupement scientifique père
        query= "ALTER TABLE espece ADD(groupement_scientifique_pere_id int(11)"
                + " FOREIGN KEY (`groupement_scientifique_pere_id`) REFERENCES `groupement_scientifique`"
                + " (`groupement_scientifique_id`));";
        stmt.executeUpdate(query);
        
        //On remplit pour chaque élément cet attribut
        for (int i=1; i<aiespece; i++){
            query = "SELECT espece_sous_famille_sous_famille_id FROM espece WHERE espece_id = "+ i +";";
            stmt.executeQuery(query);
            if (rs.next()){
                int sfid = rs.getInt("espece_sous_famille_sous_famille_id");
                //Ici on vérifie si c'est une sous-famille ou non
                if (SousFamille.hmsousfamille.containsKey(sfid)){
                    //Si oui on l'insère directement
                    query = "UPDATE espece SET groupement_scientifique_pere_id = " + SousFamille.hmsousfamille.get(sfid) +
                            " WHERE espece_id = " + i + ";";
                    stmt.executeUpdate(query);
                }else{
                    //Si non il faut aller récupérer l'id de la famille associée à l'espece
                    query = "SELECT sous_famille_famille_famille_id FROM sous_famille WHERE sous_famille_id = "
                            + sfid + ";";
                    rs=stmt.executeQuery(query);
                    int familleid = rs.getInt("sous_famille_famille_famille_id");
                    query = "UPDATE espece SET groupement_scientifique_pere_id = " + Famille.hmfamille.get(familleid) +
                            " WHERE espece_id = " + i + ";";
                    stmt.executeUpdate(query);
                }
            }
        }
        
        stmt.close();
    }
}
