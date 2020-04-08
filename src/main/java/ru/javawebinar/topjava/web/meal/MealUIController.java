package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = MealUIController.AJAX_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealUIController extends AbstractMealController {
    static final String AJAX_URL = "/ajax/meals";

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @PostMapping
    public ResponseEntity<Meal> createWithLocation(@RequestParam Integer id,
                                                   @RequestParam String dateTime,
                                                   @RequestParam String description,
                                                   @RequestParam Integer calories) {
        Meal meal = new Meal(id, LocalDateTime.parse(dateTime), description, calories);
        if (meal.isNew()) {
            meal = super.create(meal);
        } else {
            super.update(meal, id);
        }

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(AJAX_URL + "/{id}")
                .buildAndExpand(meal.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(meal);
    }

    @Override
    @GetMapping(value = "/filter")
    public List<MealTo> getBetween(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalTime startTime,
            @RequestParam @Nullable LocalDate endDate,
            @RequestParam @Nullable LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
