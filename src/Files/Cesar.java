package Files;

import java.io.*;

/**
 * Created by Kévin on 16/03/2016.
 */
public class Cesar {

    String readPath;

    File fileToRead; // Ficher à lire.
    FileReader fr;
    BufferedReader bfr;

    public Cesar(String readPath) {
        try {
            this.readPath = readPath;
            fileToRead = new File(readPath); // On crée la classe de l'objet à lire.
            fr = new FileReader(fileToRead);
            bfr = new BufferedReader(fr);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void encrypt(String writePath, int permutation) {
        BufferedWriter bfw = null;
        try {
            File fileToWrite = new File(writePath); // On crée la classe de l'objet à écrire.
            FileWriter fileWriter = new FileWriter(fileToWrite);
            bfw = new BufferedWriter(fileWriter);

            int c;

            permutation %= 26;
            if (permutation<0) {
                permutation+=26;
            }

            while ((c = bfr.read()) != -1) {
                if (c >= 65 && c <= 90) {
                    c = 65+(((c-65)+permutation)%26);
                } else if (c >= 97 && c <= 122) {
                    c = 97 + (((c-97)+permutation)%26);
                }
                bfw.write((char)c);
            }

            System.out.println("Fichier écrit avec succès.");

        } catch (IOException e) {
            e.printStackTrace();
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

    public void decrypt(int permutation) {
        permutation %= 26;
        if (permutation < 0) permutation += 26;
        this.encrypt(readPath+"_decrypted.txt", 26-permutation);
    }

    public static void main(String[] args) {

        String readPath = "rfc2549.txt";
        String writePath = "cesar.txt";

        int permutation = 2000000;

        Cesar cesar = new Cesar(readPath);

        cesar.encrypt(writePath, permutation);

        Cesar cesar2 = new Cesar(writePath);
        cesar2.decrypt(permutation);


    }

}
