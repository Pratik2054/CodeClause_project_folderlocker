import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FolderLocker {

    private static final String KEY = "pratik97"; // encryption key
    
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path of the folder to lock: ");
        String folderPath = scanner.nextLine();
        System.out.print("Enter the password to lock the folder: ");
        String password = scanner.nextLine();
        
        if (password.equals(KEY)) {
            lockFolder(folderPath);
            System.out.println("Folder locked successfully!");
        } else {
            System.out.println("Invalid password.");
        }
    }
    
    private static void lockFolder(String folderPath) throws Exception {
        // get all files and subfolders in the folder
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        
        // encrypt and rename each file/folder
        for (File file : files) {
            if (file.isFile()) {
                encryptFile(file);
            } else if (file.isDirectory()) {
                lockFolder(file.getAbsolutePath());
            }
        }
        
        // add a marker file to indicate that the folder is locked
        Path markerFile = Paths.get(folderPath, ".locked");
        Files.createFile(markerFile);
    }
    
    private static void encryptFile(File file) throws Exception {
        // read file contents into a byte array
        byte[] data = Files.readAllBytes(file.toPath());
        
        // encrypt the byte array using XOR encryption
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] ^ KEY.charAt(i % KEY.length()));
        }
        
        // write the encrypted data back to the file
        Files.write(file.toPath(), data);
        
        // rename the file with a random name
        String newName = UUID.randomUUID().toString();
        file.renameTo(new File(file.getParent(), newName));
    }
}
