package com.xun;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;


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
            path = Main.class.getResource("Trend_Detection/trend_query.csv").toURI().toString();
            path = path.substring(6, path.length());
            System.out.println(path);
            FileWriter writer = new FileWriter(path);
            writer.write(date.toString());
            writer.close();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

    }
}
