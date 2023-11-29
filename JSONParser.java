import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class JSONParser {

    private static Object stringToObject(String str, char type) {
        switch (type) {
            case 'd':  // decimal
                return Double.parseDouble(str);
            case 'i':  // integer
                return Integer.parseInt(str);
            case 't':  // true
                return true;
            case 'f':  // false
                return false;
            default:
                return null;  // or throw an exception if needed
        }
    }

    public static Map<String, Object> parseJson(String json) {

        Map<String, Object> resultMap = new LinkedHashMap<>();

        int i = 0;
        int num_curl_open = 0;
        int num_curl_closed = 0;
        int num_brack_open = 0;
        int num_brack_closed = 0;

        boolean key_state = true;
        boolean string_mode = false;
        boolean map_mode = false;
        boolean list_mode = false;

        String str = "";
        String current_key = "";
        String map_string = "";
        String list_str = "";

        while (i < json.length()){

            char currentChar = json.charAt(i);
            // ------------------------------------------------------------------------------------------------------------------
            // Map Section
            // ------------------------------------------------------------------------------------------------------------------
            if (list_mode == false){
                // Start a Map
                if (currentChar == '{' && i != 0) {
                    map_mode = true;
                    num_curl_open += 1;
                }
                // Concatenate a Map string
                if (map_mode) {
                    map_string += currentChar;
                }
                // End a Map
                if (map_mode && currentChar == '}' && i != json.length()-1) {
                    num_curl_closed += 1;

                    if (num_curl_open == num_curl_closed){
                        map_mode = false;

                        Map<String, Object> map_output = JSONParser.parseJson(map_string);

                        resultMap.put(current_key, map_output);
                        map_string = "";
                        key_state = true;
                        i++;
                        continue;
                        }
                }
            }
            if (list_mode == false && map_mode == false){
                // ------------------------------------------------------------------------------------------------------------------
                // String Section
                // ------------------------------------------------------------------------------------------------------------------
                // Start of a String
                if (currentChar == '"' && string_mode == false) {
                    string_mode = true;
                    i++;
                    continue;
                }
                // End of a String
                if (list_mode == false && map_mode == false && currentChar == '"' && string_mode) {

                    string_mode = false;
                    if (key_state){
                        current_key = str;
                        // switch states
                        key_state = false;

                        str = "";
                        i++;
                        continue;
                    }
                    if (key_state == false){
                        resultMap.put(current_key, str);
                        // switch states
                        key_state = true;

                        str = "";
                        i++;
                        continue;
                    }
                }
                // Conctenate a String
                if (list_mode == false && map_mode == false && string_mode){
                    str += currentChar;
                    i++;
                    continue;
                }
                // ------------------------------------------------------------------------------------------------------------------
                // Boolean (T/F), Null Section
                // ------------------------------------------------------------------------------------------------------------------
                if (string_mode == false && (currentChar == 't' || currentChar == 'f' || currentChar == 'n')){
                    Object boolOrNull = JSONParser.stringToObject("", currentChar);
                    resultMap.put(current_key, boolOrNull);
                    key_state = true;
                    i++;
                    continue;
                }
                // ------------------------------------------------------------------------------------------------------------------
                // Numbers (int, double) Section
                // ------------------------------------------------------------------------------------------------------------------
                if (string_mode == false && Character.isDigit(currentChar)){
                    char numType = 'i';  // integer

                    while (Character.isDigit(currentChar) || currentChar == '.'){
                        if (currentChar == '.'){
                            numType = 'd';  // double
                        }
                        str += currentChar;
                        i++;
                        currentChar = json.charAt(i);
                    }
                    Object num = JSONParser.stringToObject(str, numType);
                    resultMap.put(current_key, num);
                    key_state = true;
                    str = "";
                    i++;
                    continue;
                }
            }
            // ------------------------------------------------------------------------------------------------------------------
            // List Section
            // ------------------------------------------------------------------------------------------------------------------
            if (map_mode == false){
                // Start a List
                if (currentChar == '['){
                        list_mode = true;
                        num_brack_open += 1;
                    }
                // Concatenate a List string
                if (list_mode){
                    list_str += currentChar;
                }
                // End a List
                if (list_mode && currentChar == ']') {
                    num_brack_closed += 1;
                    if (num_brack_open == num_brack_closed){
                        list_mode = false;
                        List<Object> list_output = JSONParser.convertStringToList(list_str);
                        resultMap.put(current_key, list_output);
                        list_str = "";
                        key_state = true;
                    }
                }
            }
            i++;
        }
        return resultMap;
    }

    private static List<Object> convertStringToList(String inputString) {

        List<Object> resultList = new ArrayList<>();

        int j = 0;
        int numCurlOpen = 0;
        int numCurlClosed = 0;
        int numBrackOpen = 0;
        int numBrackClosed = 0;

        boolean stringMode = false;
        boolean mapMode = false;
        boolean listMode = false;

        String elementString = "";
        String mapString = "";
        String listString = "";

        while (j < inputString.length()){

            char currentCharInList = inputString.charAt(j);

            // ------------------------------------------------------------------------------------------------------------------
            // String Section
            // ------------------------------------------------------------------------------------------------------------------
            if (listMode == false && mapMode == false){
                // Start a String
                if (currentCharInList == '"' && stringMode == false){
                    stringMode = true;
                    j++;
                    continue;
                }
                // End a String
                if (stringMode && currentCharInList == '"'){
                    stringMode = false;
                    resultList.add(elementString);
                    elementString = "";
                }
                // Concatenate a String
                if (stringMode){
                    elementString += currentCharInList;
                }
            }
            if (stringMode == false){
                if (listMode == false && mapMode == false){
                    // ------------------------------------------------------------------------------------------------------------------
                    // Numbers (int, double) Section
                    // ------------------------------------------------------------------------------------------------------------------
                    if (Character.isDigit(currentCharInList)){
                        char numType = 'i';

                        while (Character.isDigit(currentCharInList) || currentCharInList == '.'){
                            if (currentCharInList == '.'){
                                numType = 'd';
                            }
                            elementString += currentCharInList;
                            j++;
                            currentCharInList = inputString.charAt(j);
                        }
                        Object num = JSONParser.stringToObject(elementString, numType);
                        resultList.add(num);
                        elementString = "";
                        j++;
                        continue;
                    }
                    // ------------------------------------------------------------------------------------------------------------------
                    // Boolean (T/F), Null Section
                    // ------------------------------------------------------------------------------------------------------------------
                    if (currentCharInList == 't' || currentCharInList == 'f' || currentCharInList == 'n'){
                        Object boolOrNull = JSONParser.stringToObject("", currentCharInList);
                        resultList.add(boolOrNull);
                    }
                }
            }
            // ------------------------------------------------------------------------------------------------------------------
            // List Section
            // ------------------------------------------------------------------------------------------------------------------
            if (mapMode == false){
                // Start a List
                if (listMode == false && currentCharInList == '[' && j != 0){
                    listMode = true;
                    numBrackOpen += 1;
                }
                // Concatenate a List string
                if (listMode){
                    listString += currentCharInList;
                }
                // End a List
                if (listMode && currentCharInList == ']' && j != inputString.length()-1) {
                    numBrackClosed += 1;
                    if (numBrackOpen == numBrackClosed){
                        listMode = false;
                        List<Object> listOutput = JSONParser.convertStringToList(listString);
                        resultList.add(listOutput);
                        listString = "";
                        j++;
                        continue;
                    }
                }
            }
            // ------------------------------------------------------------------------------------------------------------------
            // Map Section
            // ------------------------------------------------------------------------------------------------------------------
            if (listMode == false){
                // Start a Map
                if (mapMode == false && currentCharInList == '{') {
                    mapMode = true;
                    numCurlOpen += 1;
                }
                // Concatenate a Map string
                if (mapMode) {
                    mapString += currentCharInList;
                }
                // End a Map
                if (mapMode && currentCharInList == '}') {

                    numCurlClosed += 1;
                    if (numCurlOpen == numCurlClosed){
                        mapMode = false;

                        Map<String, Object> mapOutput = JSONParser.parseJson(mapString);
                        resultList.add(mapOutput);

                        mapString = "";
                    }
                }
            }
            j++;
        }
        return resultList;
    }

    public static void main(String[] args) {
        try {
            // Input string from the exersice
            String input = "{\"debug\":\"on\",\"window\":{\"title\":\"sample\",\"size\":500}}";

            Map<String, Object> output = JSONParser.parseJson(input);
            System.out.println(output);

            // Test block
            // In the terminal: java -ea JSONParser.java
            assert output.get("debug").equals("on");
            assert ((Map<String, Object>) output.get("window")).get("title").equals("sample");
            assert ((Map<String, Object>) output.get("window")).get("size").equals(500);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}