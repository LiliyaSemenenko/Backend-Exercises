import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class JSONParser {

    public static Map<String, Object> parseJson(String json) {

        Map<String, Object> resultMap = new LinkedHashMap<>();

        int i = 0;
        int num_curl_open = 0;
        int num_curl_closed = 0;

        // List<String> list_of_strings = new ArrayList<>();

        boolean string_mode = false;
        boolean key_state = true;
        boolean value_state = false;
        boolean map_mode = false;
        boolean array_mode =  false;

        String str = "";
        String current_key = "";
        String array_string = "";
        String map_string = "";

        while (i < json.length()){

            // System.out.println(json.charAt(i));
            // System.out.println(json.length());
            // System.out.println(i);
            // System.out.println(resultMap);

            char currentChar = json.charAt(i);

            // Map section
            if (currentChar == '{' && i != 0) {
                map_mode = true;
                num_curl_open += 1;
                // System.out.println("here");
            }
            if (map_mode) {
                map_string += currentChar;
                // System.out.println("here1");
            }
            if (map_mode && currentChar == '}' && i != json.length()-1) {
                num_curl_closed += 1;
                if (num_curl_open != num_curl_closed){
                    i++;
                    continue;
                } else {

                    map_mode = false;
                    // System.out.println("here2");

                    Map<String, Object> map_output = JSONParser.parseJson(map_string);
                    resultMap.put(current_key, map_output);
                    key_state = true;
                    value_state = false;
                    i++;
                    continue;
                    }
            }
///////////////////////////////////////////////////////////////////////////////////////////////
            // Array section
            if (currentChar == '[') {
                array_mode = true;
                // num_brack_open += 1;
            }
            if (array_mode) {
                array_string += currentChar;
                // System.out.println("here1");
            }
            if (array_mode && currentChar == ']') {
                array_mode = false;
                // num_brack_closed += 1;
                for (int j = 1; j < array_string.length(); j++){
                    char array_char = array_string.charAt(j);
                    System.out.println(array_char);
                }
                i++;
                continue;
            }
///////////////////////////////////////////////////////////////////////////////////////////////
            // start of a String
            if (map_mode == false && currentChar == '"' && string_mode == false) {
                // System.out.println("here3");
                string_mode = true;
                i++;
                continue;
            }
            // end of a String
            if (map_mode == false && currentChar == '"' && string_mode) {
                string_mode = false;
                // System.out.println("here4");
                if (key_state){
                    current_key = str;
                    // switch states
                    key_state = false;
                    value_state = true;

                    str = "";
                    i++;
                    continue;
                }
                if (value_state){
                    resultMap.put(current_key, str);
                    // switch states
                    key_state = true;
                    value_state = false;

                    str = "";
                    i++;
                    continue;
                }
            }
            // conctenate a String
            if (map_mode == false && string_mode){
                // System.out.println("here5");
                str += currentChar;
                i++;
                continue;
            }

            // Map Section
            if (map_mode == false && string_mode == false && (currentChar == 't' || currentChar == 'f' || currentChar == 'n')){
                // System.out.println("here6");
                if (currentChar == 'n') {
                    Object null_type = null;
                    resultMap.put(current_key, null_type);
                    i++;
                    continue;
                }
                boolean bool = false;
                if (currentChar == 't'){
                    bool = true;
                }
                resultMap.put(current_key, bool);
                i++;
                continue;
            }
            if (map_mode == false && string_mode == false && Character.isDigit(currentChar)){
                // System.out.println("here7");
                boolean decimal = false;

                while (Character.isDigit(currentChar) || currentChar == '.'){
                    if (currentChar == '.'){
                        decimal = true;
                    }
                    str += currentChar;
                    i++;
                    currentChar = json.charAt(i);
                }
                if (decimal){
                    double num = Double.parseDouble(str);
                    resultMap.put(current_key, num);
                } else{
                    int num = Integer.parseInt(str);
                    resultMap.put(current_key, num);
                }
                str = "";
                i++;
                continue;
            }

            // if (Character.isWhitespace(currentChar)) {
            //     i++;
            //     continue;
            // }

            i++;
        }

        // System.out.println();
        // System.out.println(resultMap);
        // System.out.println(array_string);

        return resultMap;
    }


    public static void main(String[] args) {
        try {
            // String input = new String(Files.readAllBytes(Paths.get("data.json")));
            String input = "{\"debug\":\"on\",\"window\":{\"title\":\"sample\",\"size\":500}}";
            // String input = "{\"debug\" : \"on\",\"window\" : {\"title\" : {\"sample\": {\"height\":[2.019, 7]}}, \"size\": 500}, \"status\" : null}";

            Map<String, Object> output = JSONParser.parseJson(input);
            System.out.println(output);

            // java -ea JSONParser.java
            assert output.get("debug").equals("on") : "----------------- Assertion failed for debug -----------------";
            assert ((Map<String, Object>) output.get("window")).get("title").equals("sample") : "----------------- Assertion failed for title -----------------";
            assert ((Map<String, Object>) output.get("window")).get("size").equals(500) : "----------------- Assertion failed for size -----------------";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}