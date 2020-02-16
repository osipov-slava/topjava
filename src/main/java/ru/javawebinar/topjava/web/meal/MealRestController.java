package ru.javawebinar.topjava.web.meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import to.MealTo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    
    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public void methodPost (HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        save(meal);
        response.sendRedirect("meals");
    }

    public void methodGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
                log.info("getAll");
                request.setAttribute("meals", getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "filtered":
                log.info("getAllFiltered");
                request.setAttribute("meals",
                        getAllBetweenDates(request.getParameter("startDate"),
                                request.getParameter("endDate"),
                                request.getParameter("startTime"),
                                request.getParameter("endTime")));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
            default:
        }
    }

    public Meal save(Meal meal){
        return service.save(authUserId(), meal);
    }

    public boolean delete(int id){
        return service.delete(authUserId(),id);
    }

    public Meal get(int id){
        return service.get(authUserId(),id);
    }

    public Collection<MealTo> getAll(){
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public Collection<MealTo> getAllBetweenDates(String sd, String ed, String st, String et){
        LocalDate startDate;
        try {
            startDate = LocalDate.parse(sd);
        } catch (Exception e) {
            startDate = LocalDate.MIN;
        }
        LocalDate endDate;
        try {
            endDate = LocalDate.parse(ed);
        } catch (Exception e) {
            endDate = LocalDate.MAX;
        }
        LocalTime startTime;
        try {
            startTime = LocalTime.parse(st);
        } catch (Exception e) {
            startTime = LocalTime.MIN;
        }
        LocalTime endTime;
        try {
            endTime = LocalTime.parse(et);
        } catch (Exception e) {
            endTime = LocalTime.MAX;
        }

        return MealsUtil.getFilteredTos(service.getAllBetweenDates(authUserId(), startDate, endDate), authUserCaloriesPerDay(), startTime, endTime);
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}