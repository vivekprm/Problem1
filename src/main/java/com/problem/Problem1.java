package com.problem;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vivek on 29/6/15.
 */
public class Problem1 {
    public static final int PAGE_SIZE = 1024 * 4;
    private static HashMap<String, Integer> result = new HashMap<>();

    public static void main(String [] args) throws IOException {
        if(args.length<2){
            System.out.println("Please provide args[0] as query file & args[1] as graph file.");
            return;
        }
        //long start = System.currentTimeMillis();
        fileProcessor(args[0],true);
        writeResult(args[1]);
        //long end = System.currentTimeMillis();
        //System.out.println("Time taken: "+(end-start)+" ms");
    }

    private static HashMap<String,Integer> writeResult(String sourceFilePath) throws IOException {
        fileProcessor(sourceFilePath, false);
        writeResultToFIle(result);
        return result;
    }

    private static void fileProcessor(String filePath, boolean init) throws IOException {
        FileChannel in = new RandomAccessFile(filePath, "rw").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(PAGE_SIZE);
        StringBuilder number = new StringBuilder();
        char c;
        while (-1 != (in.read(buffer))) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                c=(char) buffer.get();
                if (init ?(c!='\n'):(c!='\t' && c!='\n')){
                    number.append(c);
                }
                else{
                    if(init){
                        result.put(number.toString(), 0);
                    }
                    else{
                        if(result.get(number.toString())!=null){
                            int count = result.get(number.toString());
                            result.put(number.toString(), ++count);
                        }
                    }
                    number.setLength(0);
                }
            }
            buffer.clear();
        }
    }

    private static void writeResultToFIle(HashMap<String, Integer> result) throws IOException {
        File file = new File("src/main/resources/output.txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            output.append(entry.getKey()+" : "+entry.getValue()+"\n");
        }
        out.write(output.toString());
        out.close();
    }
}
