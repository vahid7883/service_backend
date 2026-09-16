package com.numjew.service_backend.demo;

import com.numjew.service_backend.demo.DemoDto;
import com.numjew.service_backend.demo.Demo;
import com.numjew.service_backend.mappers.DemoMapper;
import com.numjew.service_backend.demo.DemoRepository;
import com.numjew.service_backend.demo.DemoService;
import com.numjew.service_backend.user.User;
import com.numjew.service_backend.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

//@AllArgsConstructor
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final DemoRepository demoRepository;
    private final DemoService demoService;

    @Autowired
    private UserRepo userRepository;


    @Autowired
    public DemoController(DemoRepository demoRepository, DemoService demoService, DemoMapper demoMapper) {
        this.demoRepository = demoRepository;
        this.demoService = demoService;
    }

    public DemoDto mapToDto(Demo demo) {
        DemoDto dto = new DemoDto();
        dto.setId(demo.getId());
        dto.setTitle(demo.getTitle());
        dto.setLocation(demo.getLocation());
        dto.setUserId(demo.getUser().getId());
        return dto;
    }


//    @GetMapping
//    public List<DemoDto> getAllDemos(){
//        var demos =   demoRepository.findAll();
////        var demoDto = demos.stream().map(d -> new DemoDto(d.getId(),d.getTitle(),d.getLocation()));
//        return demos.stream().<DemoDto>map(demo->demoMapper.toDto(demo)).toList();
//
//    }
    @GetMapping
    public List<DemoDto> getAll(){
//        return demoRepository.findAll().stream().toList();
        var demos =   demoRepository.findAll();
        return demos.stream().<DemoDto>map(demo->mapToDto(demo)).toList();
    }


//    @GetMapping("/{demoId}")
//    public ResponseEntity<Demo> getDemoById(@PathVariable Long demoId){
//        var demo = demoRepository.findById(demoId).orElse(null);
//        if (demo == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////            return ResponseEntity.notFound().build();
//        }
////        return ResponseEntity.ok(demo);
//         return new ResponseEntity<>(demo, HttpStatus.OK);
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<DemoDto> getDemoById(@PathVariable Long id) {
//        Demo demo = demoRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Not found"));
//        var dto = mapToDto(demo);
////        DemoDTO dto = new DemoDTO(demo.getId(), demo.getName(),
////                demo.getUser().getName());
//        return ResponseEntity.ok(dto);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<DemoDto> getDemoById(@PathVariable Long id) {
        var dto = demoService.getDemoById(id);
        if (dto == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<DemoDto> create(
            @PathVariable Long userId,
            @RequestBody Demo demo,
            UriComponentsBuilder uriBuilder
    ) {
        // Find the user
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        // Optional: Prevent duplicate title for the same user
//        boolean exists = demoRepository.findByUserId(userId);
//        if (exists) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
//        }

        // Associate user and save
        demo.setUser(user);
        Demo savedDemo = demoRepository.save(demo);

        // Build URI
        URI uri = uriBuilder
                .path("/api/demo/{id}")
                .buildAndExpand(savedDemo.getId())
                .toUri();

        // Map to DTO
        DemoDto responseDto = mapToDto(savedDemo);

        return ResponseEntity.created(uri).body(responseDto);
    }
//    @GetMapping("/user/{userId}")
//    public List<DemoDto> getByUser(@PathVariable Long userId){}

//    @GetMapping(path = "/user/{userId}")
//    public ResponseEntity<List<DemoDto>> getDemoByUserId(@PathVariable(value = "userId")Long userId){
//        if (!userRepository.existsById(userId)){
//            throw new RuntimeException("not found customer with id = " + userId);
//        }
//        List<DemoDto> demoList = demoRepository.findByUserId(userId);
//        return new ResponseEntity<>(demoList , HttpStatus.OK);
//    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DemoDto>> getDemoByUserId(
            @PathVariable Long userId
    ) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found: " + userId);
        }

        List<DemoDto> demoList = demoRepository
                .findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(demoList);
    }


//    @PutMapping("/{id}")
//    public DemoDto update(@PathVariable Long id, @RequestBody DemoDto dto){}

//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id){}


//    @PostMapping("/{userId}/create")
//    public ResponseEntity<Demo> create(
//            @PathVariable Long userId,
//            @RequestBody Demo demo,
//            UriComponentsBuilder uriComponentsBuilder
//    ) {
//        // Find the user
//        User user = userRepository.findById(userId).orElse(null);
//        if (user == null) {
//            return ResponseEntity.badRequest().build(); // User not found
//        }
//
//
//        // Set the user in the demo object
//        demo.setUser(user);
//
//        // Save the demo
//        Demo savedDemo = demoRepository.save(demo);
//
//        // Build the URI for the created resource
//        URI uri = uriComponentsBuilder
//                .path("/api/demo/{id}")
//                .buildAndExpand(savedDemo.getId())
//                .toUri();
//
//        return ResponseEntity.created(uri).body(savedDemo);
//    }


    @DeleteMapping("/{demoId}")
    public ResponseEntity<Void> deleteDemoById(@PathVariable Long demoId){
        var demo = demoRepository.findById(demoId).orElse(null);
        if (demo == null){
            System.out.println("no demo");
            return ResponseEntity.notFound().build();
        }
        System.out.println(demo.getTitle());
        demoRepository.delete(demo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DemoDto> updateDemo(@PathVariable Long id, @RequestBody Demo demoDetails) {
        // Find the existing Demo by ID
        var optionalDemo = demoRepository.findById(id).orElse(null);

        if (optionalDemo == null) {
            // Return 404 Not Found if the Demo doesn't exist
            return ResponseEntity.notFound().build();
        }

        // Update the Demo object
        optionalDemo.setLocation(demoDetails.getLocation());
        optionalDemo.setTitle(demoDetails.getTitle());

        // Save the updated Demo object
        Demo updatedDemo = demoRepository.save(optionalDemo);
        DemoDto updatedDemoDto = mapToDto(updatedDemo);

        // Return the updated object with a 200 OK response
        return ResponseEntity.ok(updatedDemoDto);
    }

//    @PostMapping(path = "/{userId}/create")
//    public ResponseEntity<Customer> create(@PathVariable(value = "userId")Long userId, @RequestBody Customer customerReq) {
//        Customer customer = userRepository.findById(userId).map(user -> {
//            customerReq.setUser(user);
//            return customerRepository.save(customerReq);
//        }).orElseThrow(()-> new RuntimeException("not found user with id = " + userId));
//        return new ResponseEntity<>(customer, HttpStatus.CREATED);
//    }
//
//    @GetMapping(path = "/one/{userId}")
//    public ResponseEntity<List<Customer>> getCustomerByUserId(@PathVariable(value = "userId")Long userId){
//        if (!userRepository.existsById(userId)){
//            throw new RuntimeException("not found customer with id = " + userId);
//        }
//        List<Customer> customer = customerRepository.findByUserId(userId);
//        return new ResponseEntity<>(customer , HttpStatus.OK);
//    }

//    @PostMapping("/{userId}/create")
//    public ResponseEntity<Demo> create(@PathVariable(value = "userId")Long userId,@RequestBody Demo demo, UriComponentsBuilder uriComponentsBuilder){
////        var temp = demoRepo.save(demo);
////        return ResponseEntity.ok(temp);
//        Demo demo1Req = demoRepository.findByUserId(userId).stream().map(user -> demo.setUser())
//        var reqDemo = demoRepository.findById(demo.getId()).orElse(null);
//        if (reqDemo == null ){
//            return ResponseEntity.badRequest().build();
//        }
//        Demo savedDemo = demoRepository.save(demo);
//        System.out.println("demo: " + savedDemo.getTitle() + demo.getId());
//        var uri = uriComponentsBuilder.path("/api/demo/{id}").buildAndExpand(savedDemo.getId()).toUri();
//
//        return ResponseEntity.created(uri).body(savedDemo);
////        return ResponseEntity.ok(savedDemo);
////        return ResponseEntity.status(HttpStatus.CREATED).body(savedDemo);
//    }

//    @GetMapping("/all")
//    public List<Demo> getAllDemo(){
//    return demoRepository.findAll();
//
//    }
//
//    @PostMapping
//    public ResponseEntity<Demo> createDemo(@RequestBody Demo demo){
//        Demo req = demoRepository.save(demo);
//        return ResponseEntity.status(HttpStatus.CREATED).body(req);
//
//
//    }

}
