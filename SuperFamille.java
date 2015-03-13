
import java.util.HashMap;
import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 * Classe créant les requêtes nécessaires à transformer les données de l'ancienne table super_famille
 * @author Fabien
 */
public class SuperFamille {
    public static HashMap<Integer, Integer> hmsuperfamille;
    
    /*
    Ici la démarche est la même que pour les requêtes de l'ancienne table sous_groupe.
    Je ne vais donc pas tout recommenter, seulement un petit point de détail spécifique à super_famille (et sous_famille)
    */
    
    
    public static void requetes(Connection con) throws SQLException{
        String query = "SHOW TABLE STATUS WHERE name=groupement_scientifique;";
        Statement stmt =con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int aiordre = rs.getInt("Auto_increment");
        
        query = "SHOW TABLE STATUS WHERE name=super_famille;";
        rs = stmt.executeQuery(query);
        rs.next();
        int aisupfamille = rs.getInt("Auto_increment");
        
        for (int i=1; i<aisupfamille; i++){
            query = "SELECT * FROM super_famille WHERE super_famille_id="+i+";";
            rs = stmt.executeQuery(query);
            if(rs.next()){
                /*
                Le détail est ici : il faut s'assurer que la super_famille existe (attribut super_famille_existe de la table).
                Si elle n'existe pas, on ne l'insère pas.
                */  
                if(rs.getInt("super_famille_existe")!=0){
                    String nom= rs.getString("super_famille_nom");
                    int idpere = rs.getInt("super_famille_ordre_ordre_id");
                    hmsuperfamille.put(i, aiordre);
                    aiordre++;
                    query = "INSERT INTO groupement_scientifique (groupement_scientifique_nom, "
                            + "type_groupement_scientifique_intitule, groupement_scientifique_pere_id)"
                            + " VALUES ('"+nom+"', 'super-famille', "+idpere+");";
                    stmt.executeUpdate(query);
                }
            }
        }
        /*
        Pour cette partie, la démarche est la même que dans la classe Ordre :
        On prend chaque élément de super_famille_has_sous_groupe et on l'insère dans 
        groupement_scientifique_is_in_groupement_local
        */
        query = "SHOW TABLE STATUS WHERE name=super_famille_has_sous_groupe;";
        rs = stmt.executeQuery(query);
        rs.next();
        int aisfhsg = rs.getInt("Auto_increment");
        
        for(int i=1; i<aisfhsg; i++){
            query = "SELECT * FROM super_famille_has_sous_groupe WHERE super_famille_has_sous_groupe_id ="+i+";";
            rs = stmt.executeQuery(query);
            if (rs.next()){
                int sfid = rs.getInt("super_famille_super_famille_id");
                int sgroupeid = SousGroupe.hmsousgroupe.get(rs.getInt("sous_groupe_sous_groupe_id"));
                query = "SELECT * FROM super_famille WHERE super_famille_id = "+ sfid + ";";
                rs = stmt.executeQuery(query);
                if (rs.getInt("super_famille_existe")!=0){
                    query = "INSERT INTO groupement_scientifique_is_in_groupement_local "
                        + "(groupement_scientifique_id, groupement_local_id) VALUES ("
                        + hmsuperfamille.get(sfid) + ", "+ sgroupeid + ");";
                    stmt.executeUpdate(query);
                }
            }
        }
        stmt.close();
    }
}
