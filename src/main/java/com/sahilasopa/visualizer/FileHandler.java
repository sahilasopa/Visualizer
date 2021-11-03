package com.sahilasopa.visualizer;

import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.*;

public class FileHandler {
    private final Alert alert = new Alert(Alert.AlertType.WARNING, "Json File Is Invalid Or Poorly Formatted", ButtonType.OK);
    TableHandler handler = new TableHandler();
    private File file;
    private Event event;

    FileHandler(Event event) {
        this.event = event;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void open() throws IOException {
        if (getFile().isFile()) {
            String extension = "";

            int i = getFile().getName().lastIndexOf('.');
            if (i > 0) {
                extension = getFile().getName().substring(i + 1);
            }
            switch (extension) {
                case "json" -> jsonHandler("");
                case "xlsx" -> excelHandler();
                case "csv" -> handler.PlotTable(event, csvHandler());
            }
        }
    }

    private void jsonHandler(String data) throws IOException {
        JsonToCsv jsonToCsv = new JsonToCsv();
        if (data.isEmpty()) {
            data = new String(Files.readAllBytes(file.toPath()));
        } else {
            alert.setContentText("The API did not respond with valid JSON data");
        }
        try {
            JSONObject object = new JSONObject(data.trim());
            jsonToCsv.setJsonObject(object);
            setFile(new File(jsonToCsv.jsonObjectToCsv()));
            handler.PlotTable(event, csvHandler());
        } catch (JSONException e) {
            // the file is not json object so try Json Array
            try {
                JSONArray jsonArray = new JSONArray(data);
                jsonToCsv.setJsonArray(jsonArray);
                setFile(new File(jsonToCsv.jsonArrayToCsv()));
                handler.PlotTable(event, csvHandler());
            } catch (JSONException e1) {
                // the file is not json array too
                e.printStackTrace();
                alert.show();
            }
        }
    }

    private HashMap<String, List<String>> csvHandler() throws IOException {
        Scanner sc = new Scanner(new File(getFile().getPath()));
        List<String> data = new ArrayList<>();
        String line = sc.nextLine();
        String[] words = line.split(",");
        while (sc.hasNextLine()) {
            if (words.length <= 1) {
                words = sc.nextLine().split(",");
            }
            data.addAll(Arrays.asList(sc.nextLine().split(",")));
        }
        sc.close();  //closes the scanner
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("headers", List.of(words));
        map.put("data", data);
        return map;
    }

    public void excelHandler() {
        try {
            File file = new File(getFile().getPath());
            XSSFWorkbook wb = new XSSFWorkbook(file);
            File output = new File(wb.getSheetAt(0).getSheetName());
            XlsxToCSV csvConverter = new XlsxToCSV();
            String path = csvConverter.xlsx(getFile(), output);
            setFile(new File(path));
            handler.PlotTable(event, csvHandler());
        } catch (Exception e) {
            alert.setContentText("The Excel File Is Invalid");
            alert.show();
            e.printStackTrace();
        }
    }

    public void apiHandler(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        jsonHandler(response.body());
    }
}
