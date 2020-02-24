package ru.javawebinar.topjava;


import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int MEAL_ID_START = START_SEQ + 2;
    // User
    public static final Meal MEAL1 = new Meal(MEAL_ID_START, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL2 = new Meal(MEAL_ID_START + 1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL3 = new Meal(MEAL_ID_START + 2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL4 = new Meal(MEAL_ID_START + 3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal MEAL5 = new Meal(MEAL_ID_START + 4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL6 = new Meal(MEAL_ID_START + 5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal MEAL7 = new Meal(MEAL_ID_START + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    // Admin
    public static final Meal MEAL8 = new Meal(MEAL_ID_START + 7, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL9 = new Meal(MEAL_ID_START + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal MEAL10 = new Meal(MEAL_ID_START + 9, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2020, Month.FEBRUARY, 20, 20, 0), "хачапури", 555);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL1);
        updated.setDescription("Шашлындос");
        updated.setCalories(777);
        return updated;
    }

    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "roles");
    }

    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}
