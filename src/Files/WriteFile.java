import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    public static void main(String[] args) {
        BufferedWriter bw = null;
        try {
            String fileName = "myfile.txt"; // Le nom du fichier 
            String mycontent = "All work     and no play makes jack a dull boy.";
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }
            
            // Creation d'un FileWriter: flux de sortant vers un fichier    
            FileWriter fw = new FileWriter(file);

            // Creation d'un buffer d'ecriture sur le flux precedent
            bw = new BufferedWriter(fw);

            // Ecriture d'une chaine dans le fichier
            bw.write(mycontent);
            System.out.println("Succes de l'ecriture");
        } catch (IOException x) {
            System.err.println("Exception caught !");
            x.printStackTrace();
        } finally { 
            // Fermeture du buffer d'ecriture
            try{
                if(bw!=null)
                    bw.close();
            }catch(Exception ex){
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }
    }
}
