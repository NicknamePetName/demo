package com.example.demo.controller.electric;

import com.example.demo.model.Result;
import com.example.demo.model.electric.*;
import com.example.demo.service.electric.TotalTestFormService;
import com.example.demo.utils.convert.TotalTestFormUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excel")
public class TotalTestFormController {

    @Autowired
    private TotalTestFormService totalTestFormService;

    @PostMapping("/upload")
    public Result<Void> upload(@RequestBody TotalTestForm totalTestForm, HttpServletRequest request) {
        ElectricInspect electricInspect = TotalTestFormUtil.toModel(totalTestForm,new ElectricInspect());
        EquipInspect equipInspect = TotalTestFormUtil.toModel(totalTestForm, new EquipInspect());
        InspectItem inspectItem = TotalTestFormUtil.toModel(totalTestForm, new InspectItem());
        LoopInspect loopInspect = TotalTestFormUtil.toModel(totalTestForm, new LoopInspect());
        OtherNoRegular otherNoRegular = TotalTestFormUtil.toModel(totalTestForm, new OtherNoRegular());
        return totalTestFormService.insert(electricInspect, equipInspect, inspectItem, loopInspect, otherNoRegular, request);
    }
}
