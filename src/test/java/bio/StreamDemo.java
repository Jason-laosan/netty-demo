package bio;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author: laosan
 * Date: 2021/6/23
 * Time: 6:07 PM
 * Describe:
 */
public class StreamDemo {
    @Test
    public void arrayTest(){
        forE(1,2,3,4,5,6);
    }
    public static void forE(Integer... args){
        Arrays.asList(args).stream().forEach(System.out::println);
    }
    @Test
    public void optionalTest(){
//        Optional<String> laosan = Optional.of("laosan");
//        System.out.println(laosan.isPresent());
//        laosan.ifPresent(System.out::println);
//        System.out.println(Optional.ofNullable(null).orElse("jinkai"));
//        System.out.println(Optional.ofNullable(null).orElseGet(()->"jinkai"));

//        String text = null;
//
//        String defaultText = Optional.ofNullable(text).orElseGet(this::getMyDefault);
//        assertEquals("Default Value", defaultText);
//
//        defaultText = Optional.ofNullable(text).orElse(getMyDefault());
//        assertEquals("Default Value", defaultText);



        String text = "Text present";

        System.out.println("Using orElseGet:");
        String defaultText
                = Optional.ofNullable(text).orElseGet(this::getMyDefault);
        System.out.println("orElseGet process");
        assertEquals("Text present", defaultText);

        System.out.println("Using orElse:");
        System.out.println("orElse process");
        defaultText = Optional.ofNullable(text).orElse(getMyDefault());
        assertEquals("Text present", defaultText);
    }
    public String getMyDefault() {
        System.out.println("Getting Default Value");
        return "Default Value";
    }
    public String hello(){
        System.out.println("hello");
        return null;
    }
    @Test
    public void numberTest() throws IOException {
//        Integer i = new Integer(-1);
//        System.out.println("str"+i.toString());
        File file = new File("/Users/laosan/file/dev");
        boolean newFile = file.mkdir();
        System.out.println(newFile);
    }
    @Test
    public void fileCreateTest(){
//        createDirectory("/Users/laosan/Documents/zy/data/aa");
        System.getProperties().forEach((k,v) -> {
//            if (k.toString().startsWith("user")) {
                System.out.println(k +"="+v);
//            }
        });
    }
    private static void createDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }
}
