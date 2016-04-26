package Files;

import java.io.*;


public class ReadFile {
    public static void main(String[] args) {

        String fileName = "rfc2549.txt"; // Nom du fichier a lire
        File file = new File(fileName);  // Pointeur vers ce fichier
        try  {
            // Creation d'un flux de lecture pour ce fichier
            FileReader in = new FileReader(file);

            // Creation d'un buffer de lecture pour ce flux
            BufferedReader reader = new BufferedReader(in); 

            int c;
            // Lecture du fichier ligne par ligne  tant que l'on est pas a la fin (null)
            while ((c = reader.read()) != -1) { 
                // Affichage de la ligne courante
                System.out.print((char)c); 
                if (c == '\n') {
                    System.out.println();
                }
            }
            reader.close();
        } catch (IOException x) {
            System.err.println("Exception caught !");
            x.printStackTrace();
        }
    }
}
