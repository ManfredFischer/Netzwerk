/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netzwerkverwaltung;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;

import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFile {

    public void setZipFile(ArrayList<File> files, String Name) {


        // Create a buffer for reading the files
        byte[] buf = new byte[2048];

        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(Name));

            // Compress the files
            for (int i = 0; i < files.size(); i++) {
                FileInputStream in = new FileInputStream(files.get(i));

                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(files.get(i).getName()));

                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                // Complete the entry
                out.closeEntry();
                in.close();
            }

            // Complete the ZIP file
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ArrayList<File> getZipFiles(String quelle, String destinationname) {
        ArrayList<File> files = new ArrayList<File>();
        try {
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(new FileInputStream(quelle));

            zipentry = zipinputstream.getNextEntry();
            while (zipentry != null) {
                //for each entry to be extracted
                String[] name = zipentry.getName().split("\\\\");
                String entryName = name[name.length - 1];
                int n;
                FileOutputStream fileoutputstream;
                File newFile = new File(destinationname + entryName);
                String directory = newFile.getParent();

                if (directory == null) {
                    if (newFile.isDirectory()) {
                        break;
                    }
                }
                fileoutputstream = new FileOutputStream(
                        destinationname + entryName);

                while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                    fileoutputstream.write(buf, 0, n);
                }

                fileoutputstream.close();
                fileoutputstream.close();
                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();
                files.add(new File(destinationname + entryName));

            }//while

            zipinputstream.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return files;
    }
}