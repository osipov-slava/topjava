package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealsMemory implements MealDaoImpl {
    private static final Logger log = LoggerFactory.getLogger(MealsMemory.class);
    private static MealsMemory instance;

    private static AtomicInteger maxId = new AtomicInteger(0);
    private static Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static List<MealTo> mealsTo = new CopyOnWriteArrayList<>();

    private boolean isChanged = true;

    private MealsMemory() {
        log.debug("MealsMemory constructor");
        meals.put(1, new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.put(2, new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.put(3, new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.put(4, new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.put(5, new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.put(6, new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.put(7, new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        maxId.set(7);
        getMealsTo();
    }

    public static MealsMemory getInstance() {
        log.debug("Get Instance MealsMemory");
        if (instance == null) instance = new MealsMemory();
        return instance;
    }

    public List<MealTo> getMealsTo() {
        if (isChanged) {
            log.debug("getMealsTo");
            mealsTo = filteredByStreams(new ArrayList<>(meals.values()), LocalTime.MIN, LocalTime.MAX, 2000);
            isChanged = false;
        }
        return mealsTo;
    }

    @Override
    public void add(Meal meal) {
        log.debug("addMeal");
        meals.put(maxId.incrementAndGet(), new Meal(maxId.get(), meal.getDateTime(), meal.getDescription(), meal.getCalories()));
        isChanged = true;
    }

    @Override
    public void delete(int mealId) {
        log.debug("deleteMeal");
        meals.remove(mealId);
        isChanged = true;
    }

    @Override
    public void update(Meal meal) {
        log.debug("updateMeal");
        meals.put(meal.getId(), meal);
        isChanged = true;
    }

    @Override
    public List<MealTo> getAll() {
        getMealsTo();
        log.debug("getAllMeals");
        return mealsTo;
    }

    @Override
    public Meal getById(int mealId) {
        log.debug("getMealById");
        return meals.get(mealId);
    }
}
