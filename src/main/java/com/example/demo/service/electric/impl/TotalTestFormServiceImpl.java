package com.example.demo.service.electric.impl;

import com.example.demo.dao.electric.*;
import com.example.demo.dataobject.electric.*;
import com.example.demo.model.Result;
import com.example.demo.model.electric.*;
import com.example.demo.service.electric.TotalTestFormService;
import com.example.demo.utils.*;
import com.example.demo.utils.convert.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TotalTestFormServiceImpl implements TotalTestFormService {

    @Autowired
    private ElectricInspectDAO electricInspectDAO;
    @Autowired
    private EquipInspectDAO equipInspectDAO;
    @Autowired
    private LoopInspectDAO loopInspectDAO;
    @Autowired
    private InspectItemDAO inspectItemDAO;
    @Autowired
    private OtherNoRegularDAO otherNoRegularDAO;

    @Override
    // 事务注解
    @Transactional
    public Result<Void> insert(ElectricInspect electricInspect, EquipInspect equipInspect, InspectItem inspectItem, LoopInspect loopInspect, OtherNoRegular otherNoRegular, HttpServletRequest request) {
        Result<Void> result = Result.create();
        result.setSuccess(false);

        ElectricInspectDO electricInspectDO = ElectricInspectUtil.toDO(electricInspect);
        EquipInspectDO equipInspectDO = EquipInspectUtil.toDO(equipInspect);
        LoopInspectDO loopInspectDO = LoopInspectUtil.toDO(loopInspect);
        InspectItemDO inspectItemDO = InspectItemUtil.toDO(inspectItem);
        OtherNoRegularDO otherNoRegularDO = OtherNoRegularUtil.toDo(otherNoRegular);

        if (equipInspectDAO.insert(equipInspectDO) < 1 || equipInspectDO.getId() < 1) {
            result.setCode("501");
            result.setMessage("测试前装备部分入库异常");
            return result;
        }
        if (loopInspectDAO.insert(loopInspectDO) < 1 || loopInspectDO.getId() < 1) {
            result.setCode("502");
            result.setMessage("回路部分入库异常");
            return result;
        }
        if (inspectItemDAO.insert(inspectItemDO) < 1 || inspectItemDO.getId() < 1 ) {
            result.setCode("503");
            result.setMessage("检验项部分入库异常");
            return result;
        }
        if (otherNoRegularDAO.insert(otherNoRegularDO) < 1 || otherNoRegularDO.getId() < 1) {
            result.setMessage("504");
            result.setMessage("其他不合格项部分入库异常");
            return result;
        }

        electricInspectDO.setEquipInspectId(equipInspectDO.getId());
        electricInspectDO.setLoopInspectId(loopInspectDO.getId());
        electricInspectDO.setInspectItemId(inspectItemDO.getId());
        electricInspectDO.setOtherNoRegularId(otherNoRegularDO.getId());

        String token = request.getHeader("token");

        try {

            Integer userId = Integer.parseInt(JwtUtil.parseToken(token).getClaim("userId").asString());

            electricInspectDO.setUserId(userId);

        }catch (Exception e) {
            result.setCode("402");
            result.setMessage("未登录");
            return result;
        }

        if (electricInspectDAO.insert(electricInspectDO) < 1 || electricInspectDO.getId() < 1) {
            result.setCode("500");
            result.setMessage("总表入库异常");
            return result;
        }

        result.setSuccess(true);
        result.setCode("200");
        result.setMessage("检验单入库成功");

        return result;
    }
}
