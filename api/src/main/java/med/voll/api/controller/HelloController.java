package med.voll.api.controller;


@RestController
@ResquetMapping("/hello")
public class HelloController  {

    @GetMapping
    public String olaMundo(){
        return"Hello World!";
    }

}
