package replace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-10
 * @modify
 * @copyright Navi Tsp
 */
public class ReplaceTest {

    public static void main(String[] args) throws IOException {

        Map<String, String> tokens = new HashMap<String, String>();
        tokens.put("token1", "<value1>");
        tokens.put("token2", "<JJ ROCKS!!!>");

        MapTokenResolver resolver = new MapTokenResolver(tokens);

        Reader source =
                new StringReader("1234567890${token1}abcdefg${token2}XYZ$000");

        Reader reader = new TokenReplacingReader(source, resolver);

        BufferedReader br = new BufferedReader(reader);
        System.out.println(br.readLine());
        br.close();
//        int data = reader.read();
//        while(data != -1){
//            System.out.print((char) data);
//            data = reader.read();
//        }
    }

}
