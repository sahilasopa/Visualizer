package com.example.visualizer;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonToCsv {
    private JSONObject jsonObject;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String jsonToCsv() throws IOException {
        File output = new File("output.csv");
        FileWriter writer = new FileWriter(output);
        List<String> keys = new ArrayList<>();
        List<String> objectKeys = new ArrayList<>();
        JSONObject object = getJsonObject();
        for (Iterator<String> it = object.keys(); it.hasNext(); ) {
            objectKeys.add(it.next());
        }
        for (Iterator<String> it = object.getJSONObject(objectKeys.get(0)).keys(); it.hasNext(); ) {
            keys.add(it.next());
        }
        String csv = "";
        csv = csv.concat("KEY").concat(", ");
        for (String key : keys) {
            csv = csv.concat(key.concat(", "));
        }
        csv = csv.concat("\n");
        for (String objectKey : objectKeys) {
            JSONObject jsonObject = object.getJSONObject(objectKey);
            csv = csv.concat(objectKey.concat(", "));
            for (String key : keys) {
                String var = String.valueOf(jsonObject.get(key));
                csv = csv.concat(var.concat(", "));
            }
            csv = csv.concat("\n");
        }
        writer.write(csv);
        return csv;
    }
}
