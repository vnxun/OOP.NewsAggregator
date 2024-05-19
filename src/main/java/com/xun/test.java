package com.xun;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.io.File;


public class test {
    public static void main(String[] args) {
        String s = "22-10-2022";
        StringBuffer buffer = new StringBuffer();
        buffer.append(s.substring(6, 10) + "-");
        buffer.append(s.substring(3, 5) + "-");
        buffer.append(s.substring(0, 2));
        LocalDate date = LocalDate.parse(buffer.toString());

        String path;
        try {
            FileWriter writer2 = new FileWriter("trend_query2.csv");
            writer2.write(date.toString() + "writer2");
            writer2.flush();
            writer2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
