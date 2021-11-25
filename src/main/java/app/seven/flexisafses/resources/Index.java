package app.seven.flexisafses.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class Index {

    @GetMapping
    public ResponseEntity<String> landing(){
        return new ResponseEntity<>("Student enrollment api is up and running", HttpStatus.OK);
    }
}
