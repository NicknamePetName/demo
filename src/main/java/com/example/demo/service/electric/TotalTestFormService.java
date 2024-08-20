package com.example.demo.service.electric;


import com.example.demo.model.Result;
import com.example.demo.model.electric.*;
import jakarta.servlet.http.HttpServletRequest;

public interface TotalTestFormService {
    Result<Void> insert(ElectricInspect electricInspect, EquipInspect equipInspect, InspectItem inspectItem, LoopInspect loopInspect, OtherNoRegular otherNoRegular, HttpServletRequest request);
}
