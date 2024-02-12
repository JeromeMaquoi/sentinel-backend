package com.snail.sentinel.backend.commons;

import com.snail.sentinel.backend.service.exceptions.AstElemNotKnownException;
import com.snail.sentinel.backend.service.exceptions.NoCsvLineFoundException;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
                JSONObject jsonValues = new JSONObject();
                jsonValues.put(lineValues[0], lineValues[1]);
                jsonObjectList.add(jsonValues);
            }
        }
        return jsonObjectList;
    }

    public static Set<Path> getFileList(String path) {
        try (Stream<Path> stream = Files.list(Paths.get(path))) {
            return stream
                .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new NoCsvLineFoundException(e);
        }
    }

    public static String methodNameParser(String className, String methodName) {
        String method = "";
        if (methodName.equals("<init>")) {
            if (className.contains("$") && className.split("\\$")[1].length() > 1) {
                method = className.substring(className.lastIndexOf("$") + 1);
            } else {
                method = className.substring(className.lastIndexOf(".") + 1);
            }
        } else if (methodName.contains("lambda$")) {
            // Removing "lambda$" at the beginning of the string
            String tmpMethod = methodName.substring(methodName.lastIndexOf("lambda$") + 7);
            // Removing "$[0-9]" at the end of the string
            method = tmpMethod.split("\\$\\d+")[0];
            //log.warn("{} => {}", methodName, method);
        } else if (!methodName.equals("<clinit>")) {
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

    public static MeasurableElementDTO getMeasurableElement(String astElem, Object line) {
        try {
            MeasurableElementDTO measurableElementDTO = new MeasurableElementDTO();
            if (line instanceof JSONObject) {
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
            } else if (line instanceof CkAggregateLineDTO) {
                log.info("CkAggregateLineDTO");
                measurableElementDTO.setAstElem(astElem);
                measurableElementDTO.setClassName(((CkAggregateLineDTO) line).getClassName());
                measurableElementDTO.setMethodName(((CkAggregateLineDTO) line).getMethodName());
                measurableElementDTO.setFilePath(((CkAggregateLineDTO) line).getFilePath());
                return measurableElementDTO;
            }
            /*if (line instanceof JSONObject) {
                switch (astElem) {
                    case Util.AST_ELEM_CLASS -> {
                        ClassElementDTO element = new ClassElementDTO();
                        element.setAstElem(astElem);
                        element.setClassName(((JSONObject) line).getString(Util.AST_ELEM_CLASS));
                        element.setFilePath(((JSONObject) line).getString(Util.FILE));
                        element.setClassType(((JSONObject) line).getString("type"));
                        returnElement = element;
                    }
                    case Util.AST_ELEM_METHOD -> {
                        MethodElementDTO element = new MethodElementDTO();
                        element.setAstElem(astElem);
                        element.setClassName(((JSONObject) line).getString(Util.AST_ELEM_CLASS));
                        element.setFilePath(((JSONObject) line).getString(Util.FILE));
                        element.setMethodSignature(((JSONObject) line).getString(Util.AST_ELEM_METHOD));
                        returnElement = element;
                    }
                    case Util.AST_ELEM_VARIABLE -> {
                        VariableElementDTO element = new VariableElementDTO();
                        element.setAstElem(astElem);
                        element.setClassName(((JSONObject) line).getString(Util.AST_ELEM_CLASS));
                        element.setFilePath(((JSONObject) line).getString(Util.FILE));
                        element.setMethodSignature(((JSONObject) line).getString(Util.AST_ELEM_METHOD));
                        element.setVariableName(((JSONObject) line).getString(Util.AST_ELEM_VARIABLE));
                        returnElement = element;
                    }
                    default -> {
                        return null;
                    }
                }
                return returnElement;
            } else if (line instanceof CkAggregateLineDTO) {
                MethodElementDTO element = new MethodElementDTO();
                element.setAstElem(astElem);
                element.setClassName(((CkAggregateLineDTO) line).getClassName());
                element.setMethodSignature(((CkAggregateLineDTO) line).getMethodSignature());
                element.setFilePath(((CkAggregateLineDTO) line).getFilePath());
                return element;
            }*/
            return null;
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
        log.info("filePath = {}", filePath);
        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, true)) {
            fileWriter.write(lineToAdd);
            log.info("Line added to totalTime.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
