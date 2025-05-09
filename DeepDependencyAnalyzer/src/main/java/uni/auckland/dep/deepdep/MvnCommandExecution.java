package uni.auckland.dep.deepdep;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MvnCommandExecution {

     public ArrayList<String> runCommand(String cmd) throws IOException {

         // Create a list for storing output.

         // Execute a command and get its process handle
         Process proc = Runtime.getRuntime().exec(cmd);
         // Create a BufferedReader and specify it reads
         // from an input stream.

         return new ArrayList(Arrays.asList(Stream.of(proc.getErrorStream(), proc.getInputStream()).parallel().map((InputStream isForOutput) -> {
             BufferedReader br = new BufferedReader(new InputStreamReader(isForOutput));
             String str; // Temporary String variable
             StringBuilder output = new StringBuilder();
             try {
                 while ((str = br.readLine()) != null) {
                     output.append(str);
                     output.append("\n");
                 }
             } catch (IOException e) {
                 throw new RuntimeException(e);
             }
             // Wait for process to terminate and catch any Exceptions.
//             try {
//                 if (!proc.waitFor(5, TimeUnit.MINUTES)) {
//                     proc.destroyForcibly();
//                 }
//             } catch (InterruptedException e) {
//                 System.err.println("Process was interrupted");
//             }
             return output;
         }).collect(Collectors.joining()).split("\n")));
     }

}

