package com.yflog.test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by root on 2/25/16.
 */
public class AnalyseLsof {
    public static void main(String[] args) throws IOException {
        String dataPath = "/Users/vincent/lmwork/yflog-crumbs/src/main/resources/data";
        File dataFile = new File(dataPath);
        String data = FileUtils.readFileToString(dataFile);

        String[] lines = data.split("\n");

        HashMap<String, AtomicInteger> destinationCounts = new HashMap<String, AtomicInteger>();
        for (String line : lines) {
            String prats [] = line.split(">");
            if (prats.length > 1) {
                String rightPart = prats[1];
                if (!rightPart.contains("27017")) {
                    continue;
                }
                String[] tmp= rightPart.split(":");
                String ip = tmp[0];
                if (!destinationCounts.containsKey(ip)) {
                    destinationCounts.put(ip, new AtomicInteger(0));
                }
                destinationCounts.get(ip).incrementAndGet();
            }
        }

        for (String ip : destinationCounts.keySet()) {
            System.out.println(ip + " :  "  + destinationCounts.get(ip).get());
        }
    }
}
