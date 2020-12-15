package com.dottorrent.uso.client.service;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;

/**
 * Description here
 * //@TODO ID
 *
 * @author .torrent
 * @version 1.0.0 2020/11/21
 */
public class MusicLoader {
    public static Path songsDir = new File("songs").toPath();

    public static boolean loadSongsList(LinkedList<Music>                                                                                       songsList) {
        if(!songsDir.toFile().isDirectory()){
            if(!songsDir.toFile().mkdirs()){
                return false;
            }
        }
        for (File songDir : Objects.requireNonNull(songsDir.toFile().listFiles(File::isDirectory))) {
            for (File cfgFile : Objects.requireNonNull(songDir.listFiles(File::isFile))) {
                if (cfgFile.getName().endsWith(".uso")) {
                    Music music = new Music();
                    music.setCfgFilePath(cfgFile.toPath());
                    try {
                        loadSongBasicConfig(cfgFile.toPath(), music);
                        songsList.add(music);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    public static boolean transOsuToUso(Path osuFilePath) throws IOException{
        Path usoCfgFile =Paths.get(osuFilePath.getParent().toString()
                , Arrays.stream(osuFilePath.getFileName().toString().split("\\.osu$")).findFirst().get() +".uso");
        if(!usoCfgFile.toFile().createNewFile()) {
            throw new FileAlreadyExistsException(usoCfgFile.toString());
        }

        PrintWriter usoFilePrinter=new PrintWriter(new BufferedOutputStream(new FileOutputStream(usoCfgFile.toFile())));
        usoFilePrinter.println("uso file format v1");
        usoFilePrinter.println();
        usoFilePrinter.flush();

        // Basic configs
        loadOsuBasicCfgAndPrintToUso(usoFilePrinter,osuFilePath,"General","AudioFilename");
        loadOsuBasicCfgAndPrintToUso(usoFilePrinter,osuFilePath,"Difficulty","OverallDifficulty");
        loadOsuBasicCfgAndPrintToUso(usoFilePrinter,osuFilePath,"Metadata","(Title)|(Artist)|(Creator)|(Version)");

        // Background Image
        loadOsuBgCfgAndPrintToUso(usoFilePrinter,osuFilePath,"(jpg)|(JPG)|(png)|(PNG)");

        // HitObjects
        loadOsuHitObjAndPrintToUso(usoFilePrinter,osuFilePath);
        return true;
    }

    private static void loadOsuBasicCfgAndPrintToUso(PrintWriter usoFilePrinter, Path osuFilePath, String subTitle,
                                                     String valueRegex) throws IOException{
        String[] metaDataPairs= matchAndSplitBasicConfigs(osuFilePath,subTitle);
        usoFilePrinter.println("["+subTitle+"]");
        for(String metaDataPair:metaDataPairs){
            if(metaDataPair.matches("("+valueRegex+"):.*")){
                // Remove spaces before and after the value
                metaDataPair=metaDataPair.replaceAll("((?<=^("+valueRegex+"):)[\\s\\h]*)|([\\s\\h]*$)","");
                usoFilePrinter.println(metaDataPair);
            }
        }
        usoFilePrinter.println();
        usoFilePrinter.flush();
    }

    private static void loadOsuBgCfgAndPrintToUso(PrintWriter usoFilePrinter, Path osuFilePath,
                                                      String backgroundFileTypeRegex) throws IOException{
        Scanner osuFileScanner=new Scanner(new BufferedInputStream(new FileInputStream(osuFilePath.toFile())));
        Optional<MatchResult> backgroundStringMatch = osuFileScanner.findAll(
                "(?<=" +
                "\\[Events]\\R" +
                "//Background and Video events\\R" +
                "([0-9],){2}\"" +
                ")" +
                ".+\\.(" + backgroundFileTypeRegex + ")"+
                "(?=" +
                "\"(,[0-9]){2}\\R" +
                ")")
                .findFirst();
        if(backgroundStringMatch.isEmpty()){
            backgroundStringMatch = osuFileScanner.findAll(
                    "(?<=" +
                            "\\[Events]\\R" +
                            "//Background and Video events\\R" +
                            "(Video,([0-9]){0,5},\"(.){0,10}\"\\R)" +
                            "([0-9],){2}\"" +
                            ")" +
                            ".+\\.(" + backgroundFileTypeRegex + ")"+
                            "(?=" +
                            "\"(,[0-9]){2}\\R" +
                            ")")
                    .findFirst();
        }
        osuFileScanner.close();
        if(backgroundStringMatch.isPresent()){
            String background = backgroundStringMatch.get().group();
            if(!"".equals(background)){
                usoFilePrinter.println("[Events]");
                usoFilePrinter.println("BackgroundImage:"+background);
            }
        }
        usoFilePrinter.println();
        usoFilePrinter.flush();
    }

    private static void loadOsuHitObjAndPrintToUso(PrintWriter usoFilePrinter, Path osuFilePath) throws IOException{
        Scanner osuFileScanner=new Scanner(new BufferedInputStream(new FileInputStream(osuFilePath.toFile())));
        while(osuFileScanner.hasNextLine()){
            if(osuFileScanner.nextLine().matches("\\[HitObjects]")){
                usoFilePrinter.println("[HitObjects]");
                while (osuFileScanner.hasNextLine()){
                    String line=osuFileScanner.nextLine();
                    if(!line.matches("([0-9]+,){5}[0-9]+.*")){
                        break;
                    }
                    line=line.split(":",2)[0];
                    String[] lines=line.split(",",6);
                    int indexX= (Integer.parseInt(lines[0])-64)/128;
                    int startTime=Integer.parseInt(lines[2]);
                    int endTime=Integer.parseInt(lines[5]);
                    usoFilePrinter.println(indexX+","+startTime+","+endTime+";");
                    usoFilePrinter.flush();
                }
            }
        }
        osuFileScanner.close();
        usoFilePrinter.close();
    }

    public static void loadSongBasicConfig(Path configFile, Music music) throws IOException {
        String[] generalPairs= matchAndSplitBasicConfigs(configFile,"General");
        for(String generalPair:generalPairs){
            String[] s=generalPair.split(":",2);
            switch (s[0]){
                case "AudioFilename":{
                    music.setAudioPath(Paths.get(configFile.getParent().toString(),s[1]));
                    break;
                }
                default:
            }
        }

        String[] eventsPairs= matchAndSplitBasicConfigs(configFile,"Events");
        for(String eventsPair:eventsPairs){
            String[] s=eventsPair.split(":",2);
            switch (s[0]){
                case "BackgroundImage":{
                    music.setBgImagePath(Paths.get(configFile.getParent().toString(),s[1]));
                    break;
                }
                default:
            }
        }

        String[] difficultyPairs= matchAndSplitBasicConfigs(configFile,"Difficulty");
        for(String difficultyPair:difficultyPairs){
            String[] s=difficultyPair.split(":",2);
            switch (s[0]){
                case "OverallDifficulty":{
                    music.setDifficulty(Double.parseDouble(s[1]));
                    break;
                }
                default:
            }
        }

        String[] metaDataPairs= matchAndSplitBasicConfigs(configFile,"Metadata");
        for(String metaDataPair:metaDataPairs){
            String[] s=metaDataPair.split(":",2);
            switch (s[0]){
                case "Title": {
                    music.setTitle(s[1]);
                    break;
                }
                case "Artist":{
                    music.setArtist(s[1]);
                    break;
                }
                case "Creator":{
                    music.setCreator(s[1]);
                    break;
                }
                case "Version":{
                    music.setVersion(s[1]);
                }
                default:
            }
        }
        String audioFileSha1Hex=
                DigestUtils.sha1Hex(new BufferedInputStream(new FileInputStream(music.getAudioPath().toFile())));
        String cfgFileSha1Hex=
                DigestUtils.sha1Hex(new BufferedInputStream(new FileInputStream(configFile.toFile())));
        music.setIdentifier(audioFileSha1Hex+cfgFileSha1Hex);
    }

    private static String[] matchAndSplitBasicConfigs(Path configFile, String subTitle) throws FileNotFoundException{
        Scanner scanner=new Scanner(new BufferedInputStream(new FileInputStream(configFile.toFile())));
        String[] configs = scanner.findAll("(?<=\\["+subTitle+"]\\R)([a-zA-Z]+:.*\\R)*")
                .map(MatchResult::group)
                .collect(Collectors.joining())
                .split("\\R");
        scanner.close();
        return configs;
    }

    public static void loadSongHitObjects(Path configFile, Music music) throws FileNotFoundException{
        Scanner scanner=new Scanner(new BufferedInputStream(new FileInputStream(configFile.toFile())));
        LinkedList<HitObject> hitObjects=new LinkedList<>();
        while(scanner.hasNextLine()){
            if(scanner.nextLine().matches("\\[HitObjects]")){
                while (scanner.hasNext()){
                    String line= scanner.nextLine();
                    if(!line.matches("[0123],[0-9]+,[0-9]+;")){
                        break;
                    }
                    line=line.replaceAll(";$","");
                    String[] valuesInLine=line.split(",",3);
                    int indexX= Integer.parseInt(valuesInLine[0]);
                    int startTime=Integer.parseInt(valuesInLine[1]);
                    int endTime=Integer.parseInt(valuesInLine[2]);
                    hitObjects.add(new HitObject(indexX,startTime,endTime));
                }
            }
        }
        scanner.close();
        music.setHitObjects(hitObjects);
    }

    public static void main(String[] args) {
        Path songsDirPath=Path.of("songs");
        if(songsDirPath.toFile().isDirectory()){
            for(File songDir: Objects.requireNonNull(songsDirPath.toFile().listFiles(File::isDirectory))){
                for(File osuFile: Objects.requireNonNull(songDir.listFiles(File::isFile))){
                    if (osuFile.getName().endsWith(".osu")){
                        System.out.println(osuFile.getName());
                        try {
                            transOsuToUso(osuFile.toPath());}
                        catch (IOException e) {
                        }
                    }
                }
            }
        }



    }
}
