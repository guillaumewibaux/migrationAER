
import java.util.HashMap;
import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe créant les requêtes nécessaires à transformer les données de l'ancienne table sous_famille
 * @author Fabien
 */
public class SousFamille {
    public static HashMap<Integer, Integer> hmsousfamille;
    
    /*
    Ici la démarche est exactement la même que pour super_famille
    */
    
    public static void requetes(Connection con) throws SQLException{
        String query = "SHOW TABLE STATUS WHERE name=groupement_scientifique;";
        Statement stmt =con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int aigs = rs.getInt("Auto_increment");
        
        query = "SHOW TABLE STATUS WHERE name=sous_famille;";
        rs = stmt.executeQuery(query);
        rs.next();
        int aisousfamille = rs.getInt("Auto_increment");
        
        for (int i=1; i<aisousfamille; i++){
            query = "SELECT * FROM sous_famille WHERE sous_famille_id="+i+";";
            rs = stmt.executeQuery(query);
            if(rs.next()){
                if(rs.getInt("sous_famille_existe")!=0){
                    String nom= rs.getString("sous_famille_nom");
                    int idpere = Famille.hmfamille.get(rs.getInt("sous_famille_famille_famille_id"));
                    hmsousfamille.put(i, aigs);
                    aigs++;
                    query = "INSERT INTO groupement_scientifique (groupement_scientifique_nom, "
                            + "type_groupement_scientifique_intitule, groupement_scientifique_pere_id)"
                            + " VALUES ('"+nom+"', 'sous-famille', "+idpere+");";
                    stmt.executeUpdate(query);
                }
            }
        }
        
        query = "SHOW TABLE STATUS WHERE name=sous_famille_has_sous_groupe;";
        rs = stmt.executeQuery(query);
        rs.next();
        int aisfhsg = rs.getInt("Auto_increment");
        
        for(int i=1; i<aisfhsg; i++){
            query = "SELECT * FROM sous_famille_has_sous_groupe WHERE sous_famille_has_sous_groupe_id ="+i+";";
            rs = stmt.executeQuery(query);
            if (rs.next()){
                int sfid = rs.getInt("sous_famille_sous_famille_id");
                int sgroupeid = SousGroupe.hmsousgroupe.get(rs.getInt("sous_groupe_sous_groupe_id"));
                query = "SELECT * FROM sous_famille WHERE sous_famille_id = "+ sfid + ";";
                rs = stmt.executeQuery(query);
                if (rs.getInt("sous_famille_existe")!=0){
                    query = "INSERT INTO groupement_scientifique_is_in_groupement_local "
                        + "(groupement_scientifique_id, groupement_local_id) VALUES ("
                        + hmsousfamille.get(sfid) + ", "+ sgroupeid + ");";
                    stmt.executeUpdate(query);
                }
            }
        }
        stmt.close();
    }
}
