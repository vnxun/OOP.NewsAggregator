package com.xun;
// import java.io.BufferedReader;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.io.InputStreamReader;
import java.time.LocalDate;


public class test {
    public static void main(String[] args) {
        String s = "22-10-2022";
        StringBuffer buffer = new StringBuffer();
        buffer.append(s.substring(6, 10) + "-");
        buffer.append(s.substring(3, 5) + "-");
        buffer.append(s.substring(0, 2));
        LocalDate date = LocalDate.parse(buffer.toString());
        System.out.println(date);

        // try {
        //     BufferedReader bReader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("en-lemmatizer.csv")));
        //     String line;
        //     StringBuffer buffer = new StringBuffer();
        //     while ((line = bReader.readLine()) != null) {
        //         for (int i = 0; i < line.length(); i++) {
        //             char c = line.charAt(i);
        //             if (!Character.isWhitespace(c)) {
        //                 buffer.append(c);
        //             } else {
        //                 buffer.append(',');
        //             }
        //         }
        //         buffer.append("\n");
        //     }
        //     bReader.close();

        //     FileWriter writer;
        //     writer = new FileWriter("lemmatizer.csv");
        //     writer.write(buffer.toString());
        //     writer.close();
        // } catch (IOException | IndexOutOfBoundsException e) {
        //     e.printStackTrace();
        // }
    }
}
