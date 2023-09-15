package com.snail.sentinel.backend.commons;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    /*@Test
    void readClassCsvToJsonTest() throws IOException {
        String csvPath = System.getenv("TEST_CK_REPO_PATH") + Util.AST_ELEM_CLASS + ".csv";

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
    }*/

    private static boolean compareJSONLists(List<JSONObject> list1, List<JSONObject> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        JSONArray jsonArray1 = new JSONArray(list1);
        JSONArray jsonArray2 = new JSONArray(list2);

        return jsonArray1.similar(jsonArray2);
    }
}
