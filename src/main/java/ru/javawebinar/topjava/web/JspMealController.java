package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {

    @Autowired
    private MealRestController mealController;

    @GetMapping(params = "action=delete")
    public String delete(@RequestParam(value = "id", required = true) Integer mealId) {
        mealController.delete(mealId);
        return "redirect:meals";
    }

    @GetMapping(params = "action=create")
    public String create(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping(params = "action=update")
    public String update(Model model, @RequestParam(value = "id", required = true) Integer mealId) {
        final Meal meal = mealController.get(mealId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping(params = "action=filter")
    public String delete(Model model,
                         @RequestParam(value = "startDate", required = true) String sd,
                         @RequestParam(value = "endDate", required = true) String ed,
                         @RequestParam(value = "startTime", required = true) String st,
                         @RequestParam(value = "endTime", required = true) String et) {
        LocalDate startDate = parseLocalDate(sd);
        LocalDate endDate = parseLocalDate(ed);
        LocalTime startTime = parseLocalTime(st);
        LocalTime endTime = parseLocalTime(et);
        model.addAttribute("meals", mealController.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", mealController.getAll());
        return "meals";
    }

    @PostMapping
    public String post(HttpServletRequest request) throws IOException {
            request.setCharacterEncoding("UTF-8");
            Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            if (StringUtils.isEmpty(request.getParameter("id"))) {
                mealController.create(meal);
            } else {
                mealController.update(meal, getId(request));
            }
        return "redirect:meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
