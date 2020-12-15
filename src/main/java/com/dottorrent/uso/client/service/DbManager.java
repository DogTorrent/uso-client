package com.dottorrent.uso.client.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/12/15
 */
public class DbManager {
    public Connection dbConnection;
    public static DbManager dbMgr;

    static {
        try {
            dbMgr = new DbManager(new File(GameConfig.getLocalDataDirPath().toString(),
                    GameConfig.getLocalSaveFilename().toString()));
        } catch (ClassNotFoundException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private DbManager(File localSaveFile)throws ClassNotFoundException, SQLException, IOException {

        //如果文件夹不存在，就创建该文件夹
        if(!localSaveFile.isFile()){
            Files.createDirectories(Path.of(localSaveFile.getParent()));
        }
        Class.forName("org.sqlite.JDBC");
        System.out.println(localSaveFile);
        dbConnection = DriverManager.getConnection("jdbc:sqlite:"+localSaveFile.toString());
        Statement statement= dbConnection.createStatement();
        //如果表user_0不存在，就创造该表
        statement.execute("CREATE TABLE IF NOT EXISTS \"user_0\" (" +
                " \"music_identifier\" TEXT NOT NULL," +
                " \"time_cn\" LONG NOT NULL," +
                " \"score\" INTEGER NOT NULL," +
                " \"great_num\" INTEGER NOT NULL," +
                " \"early_num\" INTEGER NOT NULL," +
                " \"late_num\" INTEGER NOT NULL," +
                " \"miss_num\" INTEGER NOT NULL" +
                ");");
    }

    public static String sqlWordPreprocessing(String keyWord){
        keyWord = keyWord.replace("\"", "\"\"");
        keyWord = keyWord.replace("'", "''");
        return keyWord;
    }
}
