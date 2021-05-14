package com.vekai.appframe.holiday.controller;

import com.vekai.appframe.holiday.model.Holiday;
import com.vekai.appframe.holiday.service.HolidayAdminService;
import cn.fisok.raw.kit.DateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/holiday")
public class HolidayAdminController {

    @Autowired
    private HolidayAdminService holidayAdminService;

    @PostMapping(value = {"/init/{date}"})
    public void initWeekend(@PathVariable("date")String date) {
        holidayAdminService.initWeekend(date);
    }

    @PostMapping(value = {"/workDay/{date}"})
    public void updateToWorkday(@PathVariable("date")String date) {
        holidayAdminService.updateToWorkday(DateKit.parse(date));
    }

    @PostMapping(value = {"/legal"})
    @ResponseBody
    public void addLegal(@RequestBody List<Holiday> holiday) {
        holidayAdminService.addLegal(holiday);
    }
    @GetMapping({"/holidays/{date}"})
    public List<Holiday> getAllHolidays(@PathVariable("date")String date){
        return holidayAdminService.getAllHolidays(date);
    }
    @GetMapping({"/holidays"})
    public List<Holiday> getAllHolidays(){
        return holidayAdminService.getAllHolidays(null);
    }
}
