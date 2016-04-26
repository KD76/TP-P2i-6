package Files;

import java.io.*;

/**
 * Created by Kévin on 16/03/2016.
 */
public class Main {

    public static void main(String[] args) {

        String readPath = "rfc2549.txt";
        String writePath = "stats.txt";

        BufferedWriter bfw = null; // Flux d'écriture avec mémoire tampon
        BufferedReader bfr = null; // Flux de lecture avec mémoire tampon

        try {
            File fileToRead = new File(readPath); // On crée la classe de l'objet à lire.
            File fileToWrite = new File(writePath); // On crée la classe de l'objet à écrire.

            if (!fileToRead.exists()) {
                fileToRead.createNewFile();
            }

            FileReader fr = new FileReader(fileToRead);
            bfr = new BufferedReader(fr);


            int c;
            int totalChars = 0;

            int[] occurrences = new int[127];

            while ((c = bfr.read()) != -1) {
                if (c < occurrences.length) {
                    occurrences[c]++;
                }
                totalChars++;
            }

            FileWriter fw = new FileWriter(fileToWrite);

            bfw = new BufferedWriter(fw);

            bfw.write("Nombre total de caractères (spéciaux compris) : "+totalChars);
            bfw.newLine();

            for (int i=32; i<occurrences.length; i++) {
                bfw.write((char)i+": "+occurrences[i]);
                bfw.newLine(); // OU RAJOUTER +'\r'+'\n'
            }

            System.out.println("Fichier écrit avec succès.");

        } catch (IOException e) {
            System.out.println("Erreur d'écriture !");

        } finally {
            if (bfw != null) {
                try {
                    bfw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
