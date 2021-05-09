package com.bridgaLabz;
import com.bridgeLabz.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class NIOFileAPITest {
    private static String HOME = System.getProperty("user.home");
    private static String PLAY_WITH_NIO = "PlayGround";

    @Test
    public void givenPathWhenCheckedThenConfirm() throws IOException {
        //check file exists
        Path homePath = Paths.get(HOME);
        Assertions.assertTrue(Files.exists(homePath));

        //delete file and check file not exists
        Path playPath = Paths.get(HOME+"/"+PLAY_WITH_NIO);
        if(Files.exists(playPath)) FileUtils.deleteFiles(playPath.toFile());

        //create directory
        Files.createDirectory(playPath);
        Assertions.assertTrue(Files.exists(playPath));

        //create file
        IntStream.range(1,10).forEach(counter ->{
            Path tempfile = Paths.get(playPath+"/temp"+counter);
            Assertions.assertTrue(Files.notExists(tempfile));

            try {
                Files.createFile(tempfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Assertions.assertTrue(Files.exists(tempfile));

            //list files and directories as well as files wirh extensions
            try {
                Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
            } catch (IOException e) { }
            try {
                Files.newDirectoryStream(playPath).forEach(System.out::println);
            } catch (IOException e) { }
            try {
                Files.newDirectoryStream(playPath, path -> path.toFile().isFile() && path.toString().startsWith("temp"))
                        .forEach(System.out::println);
            } catch (IOException e) { }
        });
    }
}

