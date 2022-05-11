import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    public static void main(String[] args) {

        String saveDirPath = "/Users/dns/Games/savegames/";
        String zipFileName = "zip.zip";
        String zipFilePath = saveDirPath + zipFileName;

        unzipSaves(zipFilePath, saveDirPath);

        List<String> saveFiles = getListOfSaveFiles(saveDirPath);

        if (saveFiles.size() != 0) {
            for (String saveFile : saveFiles) {
                System.out.println(openProgress(saveDirPath + saveFile));
            }
        } else {
            System.out.println("В папке \"" + saveDirPath + "\" не обнаружено файлов с сохранениями игры!");
        }
    }

    public static void unzipSaves(String zipFilePath, String saveDirPath) {

        try (ZipInputStream zin = new ZipInputStream(new
                FileInputStream(zipFilePath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(saveDirPath + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static List<String> getListOfSaveFiles(String saveDirPath) {
        List<String> saveFiles = new ArrayList<>();
        File dir = new File(saveDirPath);
        if (dir.isDirectory()) {
            try {
                for (File item : dir.listFiles()) {
                    String[] fileParts = item.getName().split("\\.");
                    if (fileParts[1].equals("dat")) {
                        saveFiles.add(item.getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return saveFiles;
    }

    public static GameProgress openProgress(String saveFileName) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(saveFileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage(
            ));
        }
        return gameProgress;
    }
}