package com.example.dataproc;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ResultWriter {
private final List<String> results = new ArrayList<>();


// synchronized to protect the shared list
public synchronized void addResult(String r) {
results.add(r);
}


public synchronized List<String> snapshot() {
return new ArrayList<>(results);
}


public void writeToFile(String filename) throws IOException {
// write using try-with-resources
try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
for (String r : snapshot()) {
bw.write(r);
bw.newLine();
}
}
}
}