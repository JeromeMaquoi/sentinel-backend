package com.snail.sentinel.backend.commons;

import com.snail.sentinel.backend.service.exceptions.AstElemNotKnownException;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositorySimpleDTO;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {
    public Util() {}

    private static final Logger log = LoggerFactory.getLogger(Util.class);

    public static final String OWNER = "owner";
    public static final String NAME = "name";
    public static final String SHA = "sha";

    public static final String AST_ELEM_METHOD = "method";
    public static final String AST_ELEM_CLASS = "class";
    public static final String AST_ELEM_VARIABLE = "variable";

    public static final String FILE = "file";
    public static final String TOOL_VERSION = "ck-0.7.0-jar-with-dependencies";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static List<JSONObject> readCsvToJson(String csvPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath, StandardCharsets.UTF_8))) {
            String csvAsString = reader.lines().collect(Collectors.joining("\n"));
            JSONArray json = CDL.toJSONArray(csvAsString);
            return IntStream.range(0, json.length())
                .mapToObj(index -> ((JSONObject)json.get(index)))
                .toList();
        }
    }

    public static List<JSONObject> readCsvWithoutHeaderToJson(String csvPath) throws IOException {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath, StandardCharsets.UTF_8))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                final String[] lineValues = line.split(",");
                // For now, the length is limited to 2 because it prevents from the kotlin source code to be inserted, because these stack trace elements contain more than one "," so the app is not designed to handle this, yet
                //TODO handle Kotlin source code
                if (lineValues.length == 2) {
                    JSONObject jsonValues = new JSONObject();
                    jsonValues.put(lineValues[0], lineValues[1]);
                    jsonObjectList.add(jsonValues);
                }
            }
        }
        return jsonObjectList;
    }

    public static String methodNameParser(String className, String methodName) {
        String method = "";
        if (methodName.equals("<init>")) {
            if (className.contains("$") && className.split("\\$")[1].length() > 1) {
                method = className.substring(className.lastIndexOf("$") + 1);
            } else {
                // The method is certainly a constructor of the class
                method = className.substring(className.lastIndexOf(".") + 1);
            }
        } else if (methodName.contains("lambda$")) {
            // Removing "lambda$" at the beginning of the string
            String tmpMethod = methodName.substring(methodName.lastIndexOf("lambda$") + 7);
            // Removing "$[0-9]" at the end of the string
            method = tmpMethod.split("\\$\\d+")[0];
            //log.warn("{} => {}", methodName, method);
        } else {
            method = methodName;
        }

        //log.info("Parsed method \"{}\" is returned", method);
        return method;
    }

    public static String classNameParser(String className) {
        String parsedClass = "";
        if (className.contains("$")) {
            parsedClass = className.substring(0, className.lastIndexOf("$"));
            //log.warn("{} => {}", className, parsedClass);
        } else {
            parsedClass = className;
        }
        return parsedClass;
    }

    public static MeasurableElementDTO getMeasurableElementForJoular(CkAggregateLineDTO line, String classMethodSignature) {
        MeasurableElementDTO measurableElementDTO = new MeasurableElementDTO();
        measurableElementDTO.setAstElem(AST_ELEM_METHOD);
        measurableElementDTO.setClassName(line.getClassName());
        measurableElementDTO.setMethodName(line.getMethodName());
        measurableElementDTO.setFilePath(line.getFilePath());
        measurableElementDTO.setClassMethodSignature(classMethodSignature);
        return measurableElementDTO;
    }

    public static MeasurableElementDTO getMeasurableElementForCk(String astElem, Object line) {
        try {
            MeasurableElementDTO measurableElementDTO = new MeasurableElementDTO();
            measurableElementDTO.setAstElem(astElem);
            measurableElementDTO.setFilePath(((JSONObject) line).getString(Util.FILE));
            measurableElementDTO.setClassName(((JSONObject) line).getString(Util.AST_ELEM_CLASS));
            switch (astElem) {
                case Util.AST_ELEM_CLASS -> {
                    measurableElementDTO.setClassType(((JSONObject) line).getString("type"));
                }
                case Util.AST_ELEM_METHOD -> {
                    measurableElementDTO.setMethodName(((JSONObject) line).getString(Util.AST_ELEM_METHOD));
                }
                case Util.AST_ELEM_VARIABLE -> {
                    measurableElementDTO.setMethodName(((JSONObject) line).getString(Util.AST_ELEM_METHOD));
                    measurableElementDTO.setVariableName(((JSONObject) line).getString(Util.AST_ELEM_VARIABLE));
                }
                default -> {
                    return null;
                }
            }
            return measurableElementDTO;
        } catch (JSONException e) {
            throw new AstElemNotKnownException(e);
        }
    }

    public static CommitSimpleDTO createCommitSimpleFromCommitCompleteDTO(CommitCompleteDTO commitCompleteDTO) {
        CommitSimpleDTO commitSimpleDTO = new CommitSimpleDTO();
        commitSimpleDTO.setSha(commitCompleteDTO.getSha());
        RepositorySimpleDTO repositorySimpleDTO = new RepositorySimpleDTO();
        repositorySimpleDTO.setName(commitCompleteDTO.getRepository().getName());
        repositorySimpleDTO.setOwner(commitCompleteDTO.getRepository().getOwner());
        commitSimpleDTO.setRepository(repositorySimpleDTO);
        return commitSimpleDTO;
    }

    public static List<File> searchDirectories(String name, File root) {
        List<File> result = new ArrayList<>();
        if (root.listFiles() != null) {
            for (File file : root.listFiles()) {
                if (file != null && file.isDirectory()) {
                    if (file.getName().equals(name)) {
                        result.add(file);
                    }
                    result.addAll(searchDirectories(name, file));
                }
            }
        }
        return result;
    }

    public static void writeTimeToFile(String lineToAdd) {
        String filePath = System.getenv("PLUGINS_DIRECTORY") + "/totalTime.txt";
        log.debug("filePath = {}", filePath);
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Brussels"));
            String formattedTime = now.format(formatter);
            fileWriter.write(formattedTime + " - " + lineToAdd + "\n");
            log.info("Line added to totalTime.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTimeToFileForIteration(String joularType, int iterationNumber, int numberOfCells, int numberOfUnhandledCells) {
        String filePath = System.getenv("PLUGINS_DIRECTORY") + "/totalTime.txt";
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Brussels"));
            String formattedTime = now.format(formatter);
            fileWriter.write("        => " + formattedTime + " - Data for iteration " + iterationNumber + " of " + joularType + " done (" + numberOfUnhandledCells + " unhandled cells out of " + numberOfCells + ")!\n");
            log.info("Iteration line added to totalTime.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTimeToFileWarningIterationTitle(int iterationId) {
        String filePath = System.getenv("PLUGINS_DIRECTORY") + "/unhandled-methods.txt";
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            fileWriter.write("\n============\n");
            fileWriter.write("Iteration " + iterationId + "\n");
            fileWriter.write("============\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTimeToFileForWarningIterationResult(int numberOfCell, String message) {
        String filePath = System.getenv("PLUGINS_DIRECTORY") + "/unhandled-methods.txt";
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Brussels"));
            String formattedTime = now.format(formatter);
            fileWriter.write(formattedTime + " - Cell number " + numberOfCell + " : " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTimeToFileForWarningEmptyLine() {
        String filePath = System.getenv("PLUGINS_DIRECTORY") + "/unhandled-methods.txt";
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            fileWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
