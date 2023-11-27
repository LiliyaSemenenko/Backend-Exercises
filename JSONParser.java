import java.util.HashMap;
import java.util.Map;

public class JSONParser {

    public static Map<String, Object> parseJson(String json) {

        Map<String, Object> resultMap = new HashMap<>();

        int i = 0;
        String str = "";
        boolean string_mode = false;
        // List<String> list_of_strings = new ArrayList<>();
        String current_key = "";
        boolean key_state = true;
        boolean value_state = false;
        boolean map_mode = false;
        String map_string = "";

        while (i < json.length()){

            // System.out.println(json.charAt(i));
            // System.out.println(json.length());
            // System.out.println(i);

            char currentChar = json.charAt(i);

            // Map section
            if (currentChar == '{' && i != 0) {
                map_mode = true;
            }
            if (map_mode) {
                map_string += currentChar;
            }
            if (map_mode && currentChar == '}' && i != json.length()-1) {
                map_mode = false;
                Map<String, Object> map_output = JSONParser.parseJson(map_string);
                resultMap.put(current_key, map_output);
                i++;
                continue;
            }

            // start of a String
            if (map_mode == false && currentChar == '"' && Character.isLetter(json.charAt(i+1))) {
                string_mode = true;
                i++;
                continue;
            }
            // conctenate a String
            if (map_mode == false && Character.isLetter(currentChar) && string_mode){
                str += currentChar;
                i++;
                continue;
            }
            // end of a String
            if (map_mode == false && currentChar == '"' && string_mode) {
                string_mode = false;

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

            if (Character.isWhitespace(currentChar)) {
                i++;
                continue;
            }

            i++;
        }

        // System.out.println("Value of i: " + i);
        // System.out.println();
        // System.out.println(resultMap);

        return resultMap;
    }


    public static void main(String[] args) {
        try {
            // String input = new String(Files.readAllBytes(Paths.get("data.json")));
            // String input = "{\"debug\":\"on\",\"window\":{\"title\":\"sample\",\"size\":500}}";
            String input = "{\"debug\" : \"on\",\"window\" : {\"title\" : \"sample\",\"size\": \"Medium\"}}";

            Map<String, Object> output = JSONParser.parseJson(input);
            System.out.println(output);

            // assert output.get("debug").equals("on");
            // assert ((Map<String, Object>) output.get("window")).get("title").equals("sample");
            // assert ((Map<String, Object>) output.get("window")).get("size").equals(500);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
