package controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Controller
public class FileUploadController {
    public static String uploadDirectory = System.getProperty("user.dir")+"/uploads";

    @RequestMapping("/")
    public String UploadPage(Model model){
        return "uploadview";
    }

//    @RequestMapping("/upload")
//    public String upload(Model model, @RequestParam("file")MultipartFile file) {
//        //StringBuilder fileNames = new StringBuilder();
//        Path fileNamePath = null;
//        //for (MultipartFile file : file) {
//        fileNamePath = Paths.get(uploadDirectory, file.getOriginalFilename());
//        //fileNames.append(file.getOriginalFilename());
//        try {
//            Files.write(fileNamePath, file.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        //}
//        model.addAttribute("msg","successfully uploaded => "+file.getOriginalFilename());
//        //model.addAttribute("path", fileNamePath.toString());
//
//        return file.getOriginalFilename();
//    }

    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


    @PostMapping(value = "/upload")
    private String prediction(Model model, @RequestParam("file")MultipartFile filex) throws IOException {
        System.out.printf(filex.getOriginalFilename());
        File file=convertMultiPartToFile(filex);
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        HttpHeaders header = new HttpHeaders();
        MultiValueMap<String,Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file" , fileSystemResource);
        HttpEntity<File> request = new HttpEntity(bodyMap, header);
        HttpEntity<UploadDTO> result = new RestTemplate().exchange("http://127.0.0.1:8000/uploadfile/", HttpMethod.POST, request,UploadDTO.class);

        model.addAttribute("msg", new ResponseEntity<>(result.getBody(),HttpStatus.OK));

        return "uploadstatusview";
        //return new ResponseEntity<>(result.getBody(),HttpStatus.OK);
    }

}

class UploadDTO{
    String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}