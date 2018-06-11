package com.resultant.task.controller;

import com.resultant.task.connector.CBRClient;
import com.resultant.task.entity.Course;
import com.resultant.task.service.CoursesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/v1/data")
@RequiredArgsConstructor
@PropertySource("classpath:appenv.properties")
@Slf4j
public class DataController {

    private final CBRClient client;

    private final CoursesService coursesService;

    @Value("${template.order}")
    private String order;

    private String[] orders;

    private static final String CHAR_CODE = "VcharCode";

    @PostConstruct
    private void load(){
        log.info(order);
        orders = order.split(",");
        Arrays.stream(orders).forEach(log::info);
    }

    @RequestMapping(value = "/currency", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List> getCurrent() {

        List<Map<String, String>> result = new ArrayList<>();

        List<Map<String, String>> enums = client.getEnum();

        for(String currency : orders){
            Optional<Map<String, String>> optional = enums.stream().filter(item ->  currency.equals(item.get(CHAR_CODE))).findFirst();
            optional.ifPresent(result::add);
        }

        enums.removeAll(result);
        enums.sort(Comparator.comparing(item -> item.get(CHAR_CODE) != null ? item.get(CHAR_CODE) : "ZZZ"));
        result.addAll(enums);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(value = "/courses", method = RequestMethod.GET)
    @ResponseBody
    public List<Course> getCourses(@RequestParam List<String> currencies,
                                   @RequestParam
                                   @DateTimeFormat(pattern = "dd.MM.yyyy")
                                           LocalDate date) {

        return coursesService.getByCharCodeAndDate(currencies, date);

    }

    @RequestMapping(value = "/courses/dynamic", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List> getDynamicCourses(@RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy")LocalDate from,
                                                  @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate to,
                                                  @RequestParam List<String> currencies) {
        return new ResponseEntity<>(coursesService.getDynamicCourses(from, to, currencies), HttpStatus.OK);
    }
}
