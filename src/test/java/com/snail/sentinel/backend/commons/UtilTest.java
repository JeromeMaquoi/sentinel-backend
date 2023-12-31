package com.snail.sentinel.backend.commons;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void readClassCsvToJsonTest() throws IOException {
        String csvPath = "src/test/resources/ck-csv-test/" + Util.AST_ELEM_CLASS + ".csv";

        JSONObject line = new JSONObject();
        line.put(Util.FILE, "/home/jerome/Documents/Assistant/Recherche/open-source-repositories/commons-lang/src/test/java/org/apache/commons/lang3/concurrent/TimedSemaphoreTest.java");
        line.put("class", "org.apache.commons.lang3.concurrent.TimedSemaphoreTest$SemaphoreThread");
        line.put("type", "innerclass");
        line.put("cbo", "1");
        line.put("cboModified", "1");
        line.put("fanin", "0");
        line.put("fanout", "1");
        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(line);

        List<JSONObject> maybeTmpList = Util.readCsvToJson(csvPath);
        assertTrue(compareJSONLists(maybeTmpList, jsonObjectList));
    }

    @Test
    void readCsvWithoutHeaderToJsonTest() throws IOException {
        String csvPath = "src/test/resources/util-csv-test/joularJX-424172-filtered-methods-energy.csv";

        JSONObject line1 = new JSONObject();
        line1.put("org.springframework.boot.testsupport.classpath.ModifiedClassPathClassLoader.loadClass 98", "26.8692");
        JSONObject line2 = new JSONObject();
        line2.put("org.springframework.boot.web.servlet.server.StaticResourceJars.isResourcesJar 127", "0.9245");
        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(line1);
        jsonObjectList.add(line2);

        List<JSONObject> maybeTmpList = Util.readCsvWithoutHeaderToJson(csvPath);
        assertTrue(compareJSONLists(maybeTmpList, jsonObjectList));
    }

    private static boolean compareJSONLists(List<JSONObject> list1, List<JSONObject> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        JSONArray jsonArray1 = new JSONArray(list1);
        JSONArray jsonArray2 = new JSONArray(list2);

        return jsonArray1.similar(jsonArray2);
    }
}
