package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealsHard {
    private static final Logger log = LoggerFactory.getLogger(MealsHard.class);
    private static MealsHard instance;
    private static List<MealTo> mealsTo = new ArrayList<>();

    private MealsHard() {
        log.debug("MealsHardInit constructor");
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        mealsTo = filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
    }

    public static MealsHard getInstance() {
        log.debug("Get Instance MealsHard");
        if (instance == null) instance = new MealsHard();
        return instance;
    }

    public List<MealTo> getMealsTo() {
        log.debug("get MealsTo");
        return mealsTo;
    }
}
